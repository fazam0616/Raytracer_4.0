package MathP;

public class Point {
    private double x,y,z;

    public Point(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
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
