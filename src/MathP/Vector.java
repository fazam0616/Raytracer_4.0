package MathP;

public class Vector {
    private Point o, t;

    public Vector(Point o, double yaw, double pitch){
        this.o = o;
        double sinYaw = Math.sin(Math.toRadians(yaw));
        double cosYaw = Math.cos(Math.toRadians(yaw));
        double sinPitch = Math.sin(Math.toRadians(pitch));
        double cosPitch = Math.cos(Math.toRadians(pitch));

        this.t = new Point(
                cosYaw*cosPitch,
                sinYaw*cosPitch,
                0+sinPitch
        );
    }

    public Vector(Point o, Point t) {
        this.o = o;
        this.t = t;
    }

    public static Vector getVector(Point A, Point B){
        Point t = new Point(
                B.getX() - A.getX(),
                B.getY() - A.getY(),
                B.getZ() - A.getZ()
        );

        return new Vector(A, t);
    }

    public static Vector getCrossProduct(Vector a, Vector b){
        Point A = a.getT();
        Point B = b.getT();
        Point o = new Point(0,0,0);
        Point t = new Point(
                A.getY()*B.getZ() - A.getZ()*B.getY(),
                A.getZ()*B.getX() - A.getX()*B.getZ(),
                A.getX()*B.getY() - A.getY()*B.getX()
        );

        return new Vector(o,t);
    }

    public Point getPoint(double T){
        return new Point(
                o.getX() + t.getX()*T,
                o.getY() + t.getY()*T,
                o.getZ() + t.getZ()*T
        );
    }

    public void setO(Point o) {
        this.o = o;
    }

    public void setT(Point t) {
        this.t = t;
    }

    public Point getO() {
        return o;
    }

    public Point getT() {
        return t;
    }

    /*
    Optimized by not square rooting everything, as comparatively square rooting does nothing
    Will instead just add the components squared
    Method getRealNormal() returns un-optimized real normal
     */
    public double getNormal(){
        double x = Math.pow(this.getT().getX(),2);
        double y = Math.pow(this.getT().getY(),2);
        double z = Math.pow(this.getT().getZ(),2);

        return x+y+z;
    }

    public double getRealNormal(){
        double x = Math.pow(this.getT().getX(),2);
        double y = Math.pow(this.getT().getY(),2);
        double z = Math.pow(this.getT().getZ(),2);

        return Math.sqrt(x+y+z);
    }

    public static double dotProduct(Vector A, Vector B){
        double x,y,z;
        x = A.getT().getX()*B.getT().getX();
        y = A.getT().getY()*B.getT().getY();
        z = A.getT().getZ()*B.getT().getZ();

        return x+y+z;
    }

    public Vector getInverse(){
        return new Vector(o, new Point(
                -t.getX(),-t.getY(),-t.getZ()));
    }
}
