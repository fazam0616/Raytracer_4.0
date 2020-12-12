package ObjRender;

import java.awt.Color;

import Main.Main;
import MathP.*;

public class Face implements Comparable<Face> {
    private Point[] points;
    private Material color;

    public Face(Point a,Point b, Point c) {
        points = new Point[]{a,b,c};

    }

    public Face(Point a,Point b, Point c, Color color) {
        points = new Point[]{a,b,c};
        this.color = (Material) color;
    }

    public Plane getPlane(){
        return new Plane(points[0],points[1],points[2]);
    }

    public boolean isCollision(Point p) {
        if (p!=null){
            Vector v = Vector.getVector(p,this.getCenter());
            if (v.getNormal() < getMinDistance()){
                Face A = new Face(points[0],points[1],p);
                Face B = new Face(points[0],points[2],p);
                Face C = new Face(points[1],points[2],p);
                return (Math.abs(A.getArea() + B.getArea() + C.getArea() -this.getArea()) <= 0.001*this.getArea());
            }
        }
        return false;
    }

    public double getMinDistance(){
        Point center = this.getCenter();
        Vector A = Vector.getVector(points[0],center);
        Vector B = Vector.getVector(points[1],center);
        Vector C = Vector.getVector(points[2],center);

        return Math.max(Math.max(A.getNormal(),B.getNormal()),C.getNormal());
    }

    public double getArea(){
        Vector A = Vector.getVector(points[0],points[1]);
        Vector B = Vector.getVector(points[0],points[2]);
        double normal = A.getRealNormal() * B.getRealNormal();
        double theta = Math.acos(Vector.dotProduct(A,B) / normal); //This is stored in radians to reduce load

        return 0.5*(normal*Math.sin(theta));
    }

    public Vector getVector(){
        return null;
    }

    public Point getCenter(){
        double x = 0;
        double y = 0;
        double z = 0;
        for (int i = 0; i < 3; i++) {
            x += points[i].getX();
            y += points[i].getY();
            z += points[i].getZ();
        }
        x /= 3;
        y /= 3;
        z /= 3;
        return new Point(x,y,z);
    }

    public Point[] getPoints() {
        return points;
    }

    public void setPoints(Point a, Point b, Point c) {
        points = new Point[]{a,b,c};
    }

    public void setMaterial(Material color) {
        this.color = color;
    }

    public Material getMaterial() {
        return color;
    }

    public int compareTo(Face that) {
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

    private static double round(double value, double target) {
        return Math.ceil(value / target) * target;
    }

    public Face clone() throws CloneNotSupportedException {
        return new Face(points[0].clone(), points[1].clone(), points[2].clone(), color.clone());
    }
}
