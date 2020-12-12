package ObjRender;

import Main.Main;

import java.util.Arrays;
import java.util.LinkedList;
import MathP.*;

public class Entity implements Comparable<Entity>{
    private LinkedList<Face> faces;
    private LinkedList<Face> OGfaces;
    private double scaleFactor = 1;
    private Point pos = new Point(0,0,0);
    private Point rot = new Point(0,0,0);

    public Entity(LinkedList<Face> faces){
        this.faces = faces;
        this.OGfaces = faces;
        Main.objects.add(this);
        this.pos = this.getCenter();
    }

    public Entity() {
        Main.objects.add(this);
    }

    public String toString(){
        String s = "";
        for(Face f:faces){
            for (Point p:f.getPoints()){
                s += p.getX()+", "+p.getY()+", "+p.getZ()+"\n";
            }
        }
        return s;
    }

    public Entity(Face[] faces){
        this.OGfaces = new LinkedList<Face>(Arrays.asList(faces));
        this.faces = (LinkedList<Face>) this.OGfaces.clone();
        Main.objects.add(this);
    }

    public boolean isCollision(Point p){
        Vector v = new Vector(p, this.getCenter());
        if (v.getNormal()<this.getMinDistance()){
            return true;
        }
        else
            return false;
    }

    public void scale(double scaleFactor){
        this.scaleFactor *= scaleFactor;
        LinkedList<Point> points = new LinkedList<>();
        this.faces = (LinkedList<Face>) OGfaces.clone();

        for (Face f:faces){
            for (Point p:f.getPoints()){
                if (points.indexOf(p) == -1){
                    points.add(p);
                    p.setX(p.getX()*scaleFactor);
                    p.setY(p.getY()*scaleFactor);
                    p.setZ(p.getZ()*scaleFactor);
                }
            }
        }

        this.move(this.getPos());
    }

    public void move(Point delta){
        Point d;
        Point OGCenter = this.getOGCenter();
        this.pos.setX(this.pos.getX()+delta.getX());
        this.pos.setY(this.pos.getY()+delta.getY());
        this.pos.setZ(this.pos.getZ()+delta.getZ());
        LinkedList<Point> points = new LinkedList<>();
        this.faces = (LinkedList<Face>) OGfaces.clone();

        d = new Point(
                this.pos.getX() - OGCenter.getX(),
                this.pos.getY() - OGCenter.getY(),
                this.pos.getZ() - OGCenter.getZ()
        );

        for (Face f:faces){
            for (Point p:f.getPoints()){
                if (points.indexOf(p) == -1){
                    points.add(p);
                    p.setX(p.getX()+d.getX());
                    p.setY(p.getY()+d.getY());
                    p.setZ(p.getZ()+d.getZ());
                }
            }
        }
    }

    public void rotate(Point delta){
        this.faces = (LinkedList<Face>) OGfaces.clone();
        LinkedList<Point> points = new LinkedList<>();
        rot.setX(rot.getX()+delta.getX());
        rot.setY(rot.getY()+delta.getY());
        rot.setZ(rot.getZ()+delta.getZ());
        Point centre = this.getCenter();
        for (Face f:faces){
            for (Point p:f.getPoints()){
                if (!points.contains(p)){
                    Vector v = Vector.getVector(centre,p);
                    double d = v.getRealNormal();
                    double pitch = Math.toDegrees(
                            Math.asin((v.getT().getZ())/d)
                    );
                    Point r = new Point(
                            Math.toDegrees(
                                    Math.acos( (v.getT().getX()/d)/Math.cos(Math.toRadians(pitch)) )
                            ),
                            pitch,
                            0
                    );
                    System.out.print(v.getT().getX()/d+", ");
                    System.out.print(v.getT().getY()/d+", ");
                    System.out.println(v.getT().getZ()/d);
                    System.out.println(r);
                    System.out.println();
                    double cosZ = Math.cos(Math.toRadians(r.getZ()));
                    double sinZ = Math.sin(Math.toRadians(r.getZ()));
                    double cosY = Math.cos(Math.toRadians(r.getY()));
                    double sinY = Math.sin(Math.toRadians(r.getY()));
                    double cosX = Math.cos(Math.toRadians(r.getX()));
                    double sinX = Math.sin(Math.toRadians(r.getX()));

                    double[][] rows = new double[][]{
                            new double[]{cosX,  sinX,  0},
                            new double[]{-sinX,  cosX,   0},
                            new double[]{0,     0,      1}
                    };
                    Matrix m = new Matrix(rows);

                    rows = new double[][]{
                            new double[]{cosY,  0,  -sinY   },
                            new double[]{   0,  1,  0       },
                            new double[]{sinY,  0,  cosY    }
                    };

                    m = Matrix.multiply(m,new Matrix(rows));
                    points.add(p);
                    p.setX(p.getX()-centre.getX());
                    p.setY(p.getY()-centre.getY());
                    p.setZ(p.getZ()-centre.getZ());

                    Point newP = Matrix.multiply(m,p);

                    p.setX(newP.getX()+centre.getX());
                    p.setY(newP.getY()+centre.getY());
                    p.setZ(newP.getZ()+centre.getZ());
                }
            }
        }
    }

    public double getMinDistance(){
        Point center = this.getCenter();
        LinkedList<Double> distances = new LinkedList<Double>();
        Vector v;
        for(Face f:faces){
            for(Point p:f.getPoints()){
                v = new Vector(center,p);
                distances.add(v.getNormal());
            }
        }

        return getMax(distances);
    }
    public Point getCenter(){
        double x = 0;
        double y = 0;
        double z = 0;

        LinkedList<Point> centers = new LinkedList<Point>();
        for(Face face : faces){
            centers.add(face.getCenter());
        }

        for(Point p: centers){
            x += p.getX();
            y += p.getY();
            z += p.getZ();
        }

        x= x / centers.size();
        y= y / centers.size();
        z= z / centers.size();
        return new Point(x,y,z);
    }

    public Point getOGCenter(){
        double x = 0;
        double y = 0;
        double z = 0;

        LinkedList<Point> centers = new LinkedList<Point>();
        for(Face face : OGfaces){
            centers.add(face.getCenter());
        }

        for(Point p: centers){
            x += p.getX();
            y += p.getY();
            z += p.getZ();
        }

        x/=centers.size();
        y/=centers.size();
        z/=centers.size();
        return new Point(x,y,z);
    }

    public double getMax(LinkedList<Double> list){
        double currentMax = list.get(0);

        for (Double d : list){
            if (d>currentMax)
                currentMax = d;
        }

        return currentMax;
    }

    public LinkedList<Face> getFaces() {
        return faces;
    }

    public double getScaleFactor() {
        return scaleFactor;
    }

    public Point getPos() {
        return pos;
    }

    public Point getRot() {
        return rot;
    }

    @Override
    public int compareTo(Entity that) {
        double thisD;
        double thatD;
        Point A = this.getCenter();
        Point B = that.getCenter();

        thisD = Math.pow(Main.cameraPos.getX() - A.getX(),2);
        thisD += Math.pow(Main.cameraPos.getY() - A.getY(),2);
        thisD += Math.pow(Main.cameraPos.getZ() - A.getZ(),2);

        thatD = Math.pow(Main.cameraPos.getX() - B.getX(),2);
        thatD += Math.pow(Main.cameraPos.getY() - B.getY(),2);
        thatD += Math.pow(Main.cameraPos.getZ() - B.getZ(),2);

        return Double.compare(thisD,thatD);
    }
}