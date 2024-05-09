package MathP;

import ObjRender.Face;

import java.util.ArrayList;
import java.util.List;

public class Point {
    private double x,y,z;
    private List<Face> triangle;

    public Point(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.triangle = new ArrayList<>();
    }

    public void addTriangle(Face f){
        this.triangle.add(f);
    }

    public List<Face> getTriangle() {
        return triangle;
    }

    public static int maxI(Point p){
        if (p.getX() >= p.getY() && p.getX() >= p.getZ())
            return 0;
        else if (p.getY() >= p.getX() && p.getY() >= p.getZ())
            return 1;
        else
            return 2;
    }

    public static double max(Point p){
        if (Math.abs(p.getX()) >= Math.abs(p.getY()) && Math.abs(p.getX()) >= Math.abs(p.getZ()))
            return Math.abs(p.getX());
        else if (Math.abs(p.getY()) >= Math.abs(p.getX()) && Math.abs(p.getY()) >= Math.abs(p.getZ()))
            return Math.abs(p.getY());
        else
            return Math.abs(p.getZ());
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setZ(double z) {
        this.z = z;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return this.y;
    }

    public double getZ() {
        return z;
    }

    public String toString(){

        return this.x+" "+this.y+" "+this.z;
    }

    public Point clone(){
        return new Point(x,y,z);
    }
}
