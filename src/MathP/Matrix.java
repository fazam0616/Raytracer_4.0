package MathP;

public class Matrix {
    private double[][] rows;
    public Matrix(int row, int column) {
        rows = new double[row][column];
    }

    public Matrix(double[][] table) {
        rows = table;
    }

    public static Point multiply(Matrix A, Point B){
        double x = 0;
        double y = 0;
        double z = 0;
        for(int i = 0; i<A.rows[0].length; i++)
            x+=A.rows[0][i]*B.getX();

        for(int i = 0; i<A.rows[1].length; i++)
            y+=A.rows[1][i]*B.getY();

        for(int i = 0; i<A.rows[2].length; i++)
            z+=A.rows[2][i]*B.getZ();

        return new Point(x,y,z);
    }

    public static Matrix multiply(Matrix A, Matrix B){
        double[] col;
        double[] row;
        double[][] m = new double[3][3];
        for (int rowNum = 0; rowNum < A.rows.length; rowNum++) {
            row = new double[] {
                    A.rows[rowNum][0],A.rows[rowNum][1],A.rows[rowNum][2]
            };
            for (int column = 0; column < B.rows[rowNum].length; column++) {
                col = new double[] {
                        B.rows[0][column],B.rows[1][column],B.rows[2][column]
                };
                m[rowNum][column] = getDotProduct(row,col);
            }
        }

        return new Matrix(m);
    }

    public static double getDotProduct(double[] A, double[] B){
        double n = 0;
        for (int i = 0; i < A.length; i++) {
            n += A[i]*B[i];
        }

        return n;
    }

    public double[][] getRows() {
        return rows;
    }
}
