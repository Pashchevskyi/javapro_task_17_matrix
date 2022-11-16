package ua.ithillel.lms;

import ua.ithillel.lms.matrix.Matrix;
import ua.ithillel.lms.matrix.MatrixLogger;
import ua.ithillel.lms.matrix.exception.MatrixException;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) {
        double[][] a = {{3, 2, 8, 1}, {1, -4, 0, 3}};
        Matrix A = new Matrix(2, 4);
        A.setMatrix(a);
        double[][] b = {{2, -1}, {1, -3}, {0, 1}, {3, 1}};
        double[][] c = {{3, 2, 8, 1}, {1, -4, 0, 3}};
        Matrix B = new Matrix(4, 2);
        B.setMatrix(b);
        Matrix C = new Matrix(2, 4);
        C.setMatrix(c);
        System.out.println("Is C equal to A? " + C.equals(A));
        System.out.println("Left operand");
        System.out.println(A);
        System.out.println("Right operand:");
        System.out.println(B);
        double number = 3.0;
        System.out.println("Multiplication by number " + number);
        System.out.println(Matrix.mulNumberByMatrix(number, C));
        System.out.println("Division matrix by number of " + number);
        System.out.println(C.divByNumber(number));
        try {
            Matrix AB = A.mul(B);
            System.out.println("Multiplication of matrices:");
            System.out.println(AB);
            System.out.println(AB.det());
            double[][] plain = {{1, 2, 3}, {-2, -4, -5}, {3, 5, 6}};
            Matrix mPlain = new Matrix(3, 3);
            mPlain.setMatrix(plain);
            System.out.println("Plain matrix");
            System.out.println(mPlain);
            System.out.println("det = " + mPlain.det());
            int i = 0;
            int j = 1;
            System.out.println("After clause del in row#" + (i + 1) + " and column#" + (j + 1) + ": " + mPlain.deleteRowColumn(i, j));
            System.out.println("adjoined matrix: " + mPlain.adj());
            System.out.println("transposed matrix:");
            System.out.println(mPlain.transpose());
            System.out.println("Is transpose correct? " + mPlain.isTransposeCorrect());
            System.out.println("Invert matrix:");
            System.out.println(mPlain.inv());
            System.out.println("Is inversion correct? " + mPlain.isInvCorrect());
            double[][] matrixToPow = {{0, 1, 0, 0}, {0, 0, 1, 0}, {0, 0, 0, 1}, {0, 0, 0, 0}};
            Matrix D = new Matrix(4, 4);
            D.setMatrix(matrixToPow);
            System.out.println("Square of 0-1-matrix: " + D.pow(2));
            double[][] e = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
            Matrix E = new Matrix(3, 3);
            E.setMatrix(e);
            System.out.println("Norms of matrix " + E + "\n m: " + E.mNorm() + " \n l: " + E.lNorm() + " \n k:" +
                    E.kNorm());

            System.out.println("System of linear algebraic equations (SLAE)#1:");
            double[][] slae = {{3, -1, 0, 5}, {-2, 1, 1, 0}, {2, -1, 4, 15}};
            Matrix SLAE = new Matrix(3, 4, slae);
            Matrix X = Matrix.resolveSLAE(SLAE);
            System.out.println(X);

            System.out.println("System of linear algebraic equations (SLAE)#2");
            double[][] slae2 = {{2, 1, -5, 1, 8}, {1, -3, 0, -6, 9}, {0, 2, -1, 2, -5}, {1, 4, -7, 6, 0}};
            Matrix X2 = Matrix.resolveSLAE(slae2);
            System.out.println(X2);

            double[][] f = {{1, 2, 3}, {4, 5, 6}};
            double[][] g = {{1, 2, 3}, {4, 5, 6}};
            double[][] h = {{1, 2}, {3, 4}, {5, 6}};
            Matrix F = new Matrix(f);
            Matrix G = new Matrix(g);
            Matrix H = new Matrix(h);
            System.out.println("Sum F and G matrices: " + F.add(G));
            System.out.println("Diff F and G matrices: " + F.sub(G));
            System.out.println("Div G by H matrices: " + G.div(H));
            System.out.println("Abs: of SLAE#2 solution matrix: " + X2.abs());
            System.out.println("Mul SLAE2 solution matrix by 1.5: " + X2.mulByNumber(1.5));
        } catch (MatrixException e) {
            System.out.println(e.getMessage());
            MatrixLogger.get().debug(e.getMessage());
        }


    }
}
