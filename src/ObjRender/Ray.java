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
        for (Entity e:Main.objects){
            for(Face f:e.getFaces()){
                Plane plane = f.getPlane();
                Point intersect = plane.vectorIntersec(this.v);
                if (f.isCollision(intersect)){
                    Vector path = Vector.getVector(v.getO(),intersect);
                    if (path.getRealNormal() <= renderDistance){
                        return f;
                    }
                }
            }
        }

        return null;
    }

    public Point getPos() {
        return this.pos;
    }
}
