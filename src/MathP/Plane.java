package MathP;

public class Plane {
    private Vector normal;
    private double d;

    public Plane(Point A, Point B, Point C){
        Vector v1 = Vector.getVector(B,A);
        Vector v2 = Vector.getVector(B,C);
        this.normal = Vector.getCrossProduct(v1,v2);
        normal.setO(B);
        d = normal.getT().getX()*A.getX() + normal.getT().getY()*A.getY() + normal.getT().getZ()*A.getZ();
    }

    public Plane(Vector v){
        this.normal = v;
        Point p = this.normal.getO();
        d = normal.getT().getX()*p.getX() +
            normal.getT().getY()*p.getY() +
            normal.getT().getZ()*p.getZ();
    }

    public Point vectorIntersec(Vector v){
        double t =
                normal.getT().getX() * v.getT().getX() +
                normal.getT().getY() * v.getT().getY() +
                normal.getT().getZ() * v.getT().getZ();
        double D = this.d - (
                normal.getT().getX() * v.getO().getX() +
                normal.getT().getY() * v.getO().getY() +
                normal.getT().getZ() * v.getO().getZ());
        double T = D/t;
        if(T>=0)return v.getPoint(T);
        else
            return null;
    }

    public Vector getNormal() {
        return normal;
    }

    public double getD() {
        return d;
    }
}
