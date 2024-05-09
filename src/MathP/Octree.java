package MathP;

import ObjRender.Face;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.BinaryOperator;
import java.util.stream.Collectors;

public class Octree {
    private final static int NE_UP = 0;
    private final static int SE_UP = 1;
    private final static int SW_UP = 2;
    private final static int NW_UP = 3;
    private final static int NE_DOWN = 4;
    private final static int SE_DOWN = 5;
    private final static int SW_DOWN = 6;
    private final static int NW_DOWN = 7;
    public static Octree base = null;
    private List<Octree> children;
    private Octree parent;
    private Point contained;
    private Point origin;
    private double half_length;
    public int spot = -1;

    public Octree(Point base, double half_length,int spot, Octree parent){
        this.origin = base;
        this.half_length = half_length;
        this.parent = parent;
        this.contained = null;
        this.children = null;
        this.spot = spot;
    }

    public Octree(Point base, double length){
        this.origin = base;
        this.half_length = length;
        this.parent = null;
        this.contained = null;
        this.children = null;
    }
    public int count(){
        int num = 1;
        if (this.children != null){
            num += this.children.stream().map(t -> t.count()).reduce(new BinaryOperator<Integer>() {
                @Override
                public Integer apply(Integer integer, Integer integer2) {
                    return integer + integer2;
                }
            }).orElse(0);
        }
        return num;
    }

    private String stringRec(String base){
        StringBuilder s = new StringBuilder(base+"Base: " + origin + "\tSide Length: " + half_length * 2 + (hasPoint() ? "\t*\n": "\n"));
        if (children != null){
            for (Octree t: children)
                s.append(base+t.stringRec(base+" "));
        }
        return s.toString();
    }

    public String toString(){
        return stringRec("");
    }

    public void split(){
        if (children == null){
            this.children=  new ArrayList<>(8);
            for (int i = 0; i < 8; i++) {
                Point newBase = new Point(0,0,0);
                if (i >= 4)
                    newBase.setZ(origin.getZ());
                else
                    newBase.setZ(origin.getZ()+ half_length);

                switch (i){
                    case NE_UP, NE_DOWN -> {
                        newBase.setX(origin.getX()+ half_length);
                        newBase.setY(origin.getY()+ half_length);
                    }
                    case SE_UP, SE_DOWN -> {
                        newBase.setX(origin.getX()+ half_length);
                        newBase.setY(origin.getY());
                    }
                    case SW_UP, SW_DOWN -> {
                        newBase.setX(origin.getX());
                        newBase.setY(origin.getY());
                    }
                    case NW_UP, NW_DOWN -> {
                        newBase.setX(origin.getX());
                        newBase.setY(origin.getY()+ half_length);
                    }
                }
                this.children.add(new Octree(newBase, half_length /2, i, this));
                if (contained != null){
                    if (this.children.getLast().contains(contained)){
                        children.getLast().contain(contained);
                        this.contained = null;
                    }
                }
            }
        }
    }

    public void contain(Point contained) {
        if (this.contained != null) {
            this.split();
        }
        if (this.children == null) {
            this.contained = contained;
        } else {
            children.stream().map(tree -> {
                if (tree.contains(contained))
                    tree.contain(contained);
                return null;
            });
        }

    }

    public boolean contains(Point p){
        double boundX = origin.getX() + half_length*2;
        double boundY = origin.getY() + half_length*2;
        double boundZ = origin.getZ() + half_length*2;
        if (p.getX() < boundX && p.getX() >= origin.getX())
            if (p.getY() < boundY && p.getY() >= origin.getY())
                if (p.getZ() < boundZ && p.getZ() >= origin.getZ())
                    return true;
        return false;
    }

    public Octree getCube(Point p){
        if (this.contains(p)){
            if (this.children == null)
                return this;
            else {
                for (Octree t:children){
                    if (t.contains(p)){
                        return t.getCube(p);
                    }
                }
                this.contains(p);
                for (Octree t:children){
                    if (t.contains(p)){
                        t.getCube(p);
                    }
                }
                throw new RuntimeException();
            }
        }
        Octree curr = this;
        int n = 0;
        while (true) {
            if (curr.parent == null)
                curr.expand(p);
            for (Octree t : curr.parent.children) {
                if (t != curr)
                    if (t.contains(p))
                        return t.getCube(p);

            }
            n += 1;
            curr = curr.parent;
        }
//        throw new RuntimeException("This shouldn't be possible...");
    }

    public Point getOrigin(){
        return origin;
    }

    private void expand(Point p){
        Point dir = new Point(p.getX() - origin.getX(), p.getY()-origin.getY(),p.getZ()-origin.getZ());
        Point base = new Point(
                dir.getX() <= 0 ? origin.getX() - 2*half_length: origin.getX(),
                dir.getY() <= 0 ? origin.getY() - 2*half_length: origin.getY(),
                dir.getZ() <= 0 ? origin.getZ() - 2*half_length: origin.getZ()
                );
        this.parent = new Octree(base, 2*half_length);
        this.parent.split();
        if (dir.getX() > 0)
            if (dir.getY() > 0)
                if (dir.getZ() > 0)
                    this.parent.children.set(SW_DOWN, this);
                else
                    this.parent.children.set(SW_UP, this);
            else
                if (dir.getZ() > 0)
                    this.parent.children.set(NW_DOWN, this);
                else
                    this.parent.children.set(NW_UP, this);
        else
            if (dir.getY() > 0)
                if (dir.getZ() > 0)
                    this.parent.children.set(SE_DOWN, this);
                else
                    this.parent.children.set(SE_UP, this);
            else
                if (dir.getZ() > 0)
                    this.parent.children.set(NE_DOWN, this);
                else
                    this.parent.children.set(NE_UP, this);
        Octree.base = this.parent;
        System.out.println("Expanding! New Size: "+Octree.base.count()+"\n");
    }


    public boolean hasPoint() {
        return this.contained != null;
    }

    public Point getPoint() {
        return contained;
    }

    public Point getRelPos(Point p){
        return new Point(p.getX() - origin.getX(), p.getY() - origin.getY(), p.getZ() - origin.getZ());
    }

    public double getHalfLen() {
        return half_length;
    }
}
