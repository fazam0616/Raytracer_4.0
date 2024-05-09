package ObjRender;

import Main.Main;
import MathP.*;

import java.util.LinkedList;

public class Ray {
    private Point pos;
    private Vector v;
    private Point rot;
    public static double clippingDistance=10;

    public Ray(Point pos, Point rot){
        this.pos = pos;
        this.rot = rot;

        this.v = new Vector(this.pos, rot.getX(), rot.getY());
    }

    public Ray(Point pos, Vector v) {
        this.pos = pos;
        this.v = v;
        this.v.setO(pos);
    }

    public Face run(double renderDistance, LinkedList<Face> faces) {
        for(Face f:faces){
            Plane plane = f.getPlane();
            Point intersect = plane.vectorIntersec(this.v);
            if (f.isCollision(intersect)){
                Vector path = Vector.getVector(pos,intersect);
                double dist = path.getRealNormal();
                if (dist <= renderDistance  && dist >= clippingDistance){
                    //double yaw = Math.toDegrees(Math.atan2(intersect.getX()-Main.cameraPos.getX(),intersect.getY()-Main.cameraPos.getY()));
                    //System.out.println(yaw);
                    //System.out.println(pitch);
                    //double yaw = Math.toDegrees(Math.acos(intersect.getY()-Main.cameraPos.getY()))
                    //if (angle < 0)
                    {
                        this.pos = intersect;
                        return f;
                    }
                }
            }
        }

        return null;
    }

    public Face run(double renderDistance) {
//        for (Entity e:Main.objects){
//            for(Face f:e.getFaces()){
//                Plane plane = f.getPlane();
//                Point intersect = plane.vectorIntersec(this.v);
//                if (f.isCollision(intersect)){
//                    Vector path = Vector.getVector(v.getO(),intersect);
//                    if (path.getRealNormal() <= renderDistance){
//                        return f;
//                    }
//                }
//            }
//        }
//
//        return null;

        double dist = 0;
        while (dist < renderDistance) {
            if (Octree.base != null) {
                Octree t = Octree.base.getCube(this.pos);
                if (t.hasPoint()) {
                    for (Face tri : t.getPoint().getTriangle()) {
                        Plane plane = tri.getPlane();
                        Point intersect = plane.vectorIntersec(this.v);
                        if (tri.isCollision(intersect)) {
                            //                        System.out.println("\n");
                            this.pos = intersect;
                            return tri;
                        }
                    }
                }
                double xVal, yVal, zVal, l;

                if (v.getT().getX() < 0)
                    xVal = (t.getOrigin().getX() - pos.getX())/v.getT().getX();
                else
                    xVal = ((t.getOrigin().getX() + t.getHalfLen()*2) - pos.getX())/v.getT().getX();

                if (v.getT().getY() < 0)
                    yVal = (t.getOrigin().getY() - pos.getY())/v.getT().getY();
                else
                    yVal = ((t.getOrigin().getY() + t.getHalfLen()*2) - pos.getY())/v.getT().getY();

                if (v.getT().getZ() < 0)
                    zVal = (t.getOrigin().getZ() - pos.getZ())/v.getT().getZ();
                else
                    zVal = ((t.getOrigin().getZ() + t.getHalfLen()*2) - pos.getZ())/v.getT().getZ();

                l = Math.max(Math.min(xVal, Math.min(yVal, zVal)), t.getHalfLen()/10000);

                pos = v.getPoint(l);
//                System.out.print(", "+pos);
                v.setO(pos);
                dist += l*v.getRealNormal();
            }else {
//                System.out.println("");
                return null;
            }
        }
//        System.out.println("HIT DEPTH");
        return null;
    }

    public Point getPos() {
        return this.pos;
    }
}
