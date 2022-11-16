package ua.ithillel.lms.matrix;

import ua.ithillel.lms.logger.Logger;
import ua.ithillel.lms.matrix.exception.MatrixException;

import java.util.Arrays;
import java.util.Objects;

public class Matrix {
    private final int rowsQuantity;
    private final int columnsQuantity;
    private final double[][] matrix;

    private final Logger logger;

    static final double accuracy = 0.00000001;
    static final String loggerConfigPath = "./config/log.properties";

    public Matrix(int rowsQuantity, int columnsQuantity) {
        logger = MatrixLogger.create(loggerConfigPath);
        if (rowsQuantity <= 0) {
            logger.warn("Invalid rows quantity of matrix. Matrix will have 1 row (default value)");
            rowsQuantity = 1;
        }
        if (columnsQuantity <= 0) {
            logger.warn("Invalid columns quantity of matrix. Matrix will have 1 row (default value)");
            columnsQuantity = 1;
        }
        this.rowsQuantity = rowsQuantity;
        this.columnsQuantity = columnsQuantity;
        this.matrix = new double[rowsQuantity][columnsQuantity];
        for (int i = 0; i < this.rowsQuantity; i++) {
            for (int j = 0; j < this.columnsQuantity; j++) {
                this.matrix[i][j] = 0;
            }
        }
    }

    public Matrix(int rowsQuantity, int columnsQuantity, double[][] matrix) {
        logger = MatrixLogger.create(loggerConfigPath);
        if (rowsQuantity <= 0) {
            logger.warn("Invalid rows quantity of matrix. Matrix will have 1 row (default value)");
            rowsQuantity = 1;
        }
        if (columnsQuantity <= 0) {
            logger.warn("Invalid columns quantity of matrix. Matrix will have 1 row (default value)");
            columnsQuantity = 1;
        }
        if (matrix.length < rowsQuantity) {
            logger.warn("Real rows quantity of matrix does not match parameter. Real rows quantity will be used");
            rowsQuantity = matrix.length;
        }
        for (int i = 0; i < matrix[0].length; i++) {
            if (matrix[0].length < columnsQuantity) {
                logger.warn("Real columns quantity of matrix does not match parameter. Real columns quantity will be used");
                rowsQuantity = matrix.length;
            }
        }
        this.rowsQuantity = rowsQuantity;
        this.columnsQuantity = columnsQuantity;
        this.matrix = new double[rowsQuantity][columnsQuantity];
        for (int i = 0; i < this.rowsQuantity; i++) {
            System.arraycopy(matrix[i], 0, this.matrix[i], 0, this.columnsQuantity);
        }
    }

    public Matrix(double[][] matrix) {
        logger = MatrixLogger.create(loggerConfigPath);
        this.rowsQuantity = matrix.length;
        if (Matrix.isGear(matrix)) {
            logger.warn("The library is unable to work with gear matrix. Matrix will get the 1st raw columns count.");
        }
        this.columnsQuantity = (matrix.length > 0) ? matrix[0].length : 0;
        this.matrix = new double[this.rowsQuantity][this.columnsQuantity];
        for (int i = 0; i < this.rowsQuantity; i++) {
            System.arraycopy(matrix[i], 0, this.matrix[i], 0, this.columnsQuantity);
        }
    }

    /**
     * Sets matrix to memory reserved by Constructor
     *
     * @param matrix matrix you are going to set as a property of this object
     */
    public void setMatrix(double[][] matrix) {
        logger.info("If matrix you transferred has more rows (columns) than you define, redundant will be dropped.");
        if (matrix.length < rowsQuantity) {
            logger.error("Rows quantity of matrix you transferred is " + matrix.length + ", but before you defined " +
                    rowsQuantity + " . Matrix will not be changed.");
            return;
        }
        if (matrix[0].length < columnsQuantity) {
            logger.error("Columns quantity of matrix you transferred is " + matrix.length + ", but before you defined " +
                    columnsQuantity + " . Matrix will not be changed.");
            return;
        }
        if (Matrix.isGear(matrix)) {
            logger.error("The library is unable to work with gear matrix. Matrix will not be changed.");
            return;
        }
        for (int i = 0; i < rowsQuantity; i++) {
            if (columnsQuantity >= 0) System.arraycopy(matrix[i], 0, this.matrix[i], 0, columnsQuantity);
        }
    }

    /**
     * Returns Matrix, which is sum of matrices (this Matrix, which is left, and the right one)
     *
     * @param right right operand of addition
     * @return Matrix sum of matrices (this and right operand)
     * @throws MatrixException when matrices have different rows and/or columns quantity
     */
    public Matrix add(Matrix right) throws MatrixException {
        if (this.rowsQuantity != right.rowsQuantity || this.columnsQuantity != right.columnsQuantity) {
            String message = "Unable to add matrices which have different rows and/or columns quantity.";
            logger.error(message);
            throw new MatrixException(message);
        }
        Matrix c = new Matrix(this.rowsQuantity, this.columnsQuantity);
        for (int i = 0; i < this.rowsQuantity; i++) {
            for (int j = 0; j < this.columnsQuantity; j++) {
                c.matrix[i][j] = this.matrix[i][j] + right.matrix[i][j];
            }
        }
        return c;
    }

    /**
     * Returns Matrix, which is difference between this Matrix, which is left, and transferred
     *
     * @param right right operand of subtraction
     * @return Matrix difference between matrices (this and right operand)
     * @throws MatrixException when matrices have different rows and/or columns quantity
     */
    public Matrix sub(Matrix right) throws MatrixException {
        if (this.rowsQuantity != right.rowsQuantity || this.columnsQuantity != right.columnsQuantity) {
            String message = "Unable to subtract matrices which have different rows and/or columns quantity.";
            logger.error(message);
            throw new MatrixException(message);
        }
        Matrix c = new Matrix(this.rowsQuantity, this.columnsQuantity);
        for (int i = 0; i < this.rowsQuantity; i++) {
            for (int j = 0; j < this.columnsQuantity; j++) {
                c.matrix[i][j] = this.matrix[i][j] - right.matrix[i][j];
            }
        }
        return c;
    }

    /**
     * Multiplies Matrix by number. Returns Matrix
     *
     * @param number number, which you would like to multiply by
     * @return Matrix this multiplied by number
     */
    public Matrix mulByNumber(double number) {
        Matrix c = new Matrix(this.rowsQuantity, this.columnsQuantity);
        for (int i = 0; i < this.rowsQuantity; i++) {
            for (int j = 0; j < this.columnsQuantity; j++) {
                c.matrix[i][j] = this.matrix[i][j] * number;
            }
        }
        return c;
    }

    /**
     * Multiplies Matrix by number. Returns Matrix
     *
     * @param number number, which you would like to multiply by
     * @param right  Matrix, which you would like to multiply by number
     * @return Matrix this multiplied by number
     */
    public static Matrix mulNumberByMatrix(double number, Matrix right) {
        Matrix c = new Matrix(right.rowsQuantity, right.columnsQuantity);
        for (int i = 0; i < right.rowsQuantity; i++) {
            for (int j = 0; j < right.columnsQuantity; j++) {
                c.matrix[i][j] = number * right.matrix[i][j];
            }
        }
        return c;
    }

    /**
     * Divides Matrix by number. Returns Matrix
     *
     * @param number number, which you would like to divide by
     * @return Matrix this divided by number
     */
    public Matrix divByNumber(double number) {
        Matrix c = new Matrix(this.rowsQuantity, this.columnsQuantity);
        for (int i = 0; i < this.rowsQuantity; i++) {
            for (int j = 0; j < this.columnsQuantity; j++) {
                if (Math.abs(number) < accuracy) {
                    logger.warn("Possible divide by 0 . Matrix will be divided by " + accuracy +
                            " to avoid abnormal termination.");
                }
                c.matrix[i][j] = (Math.abs(number) < accuracy) ? this.matrix[i][j] / accuracy :
                        this.matrix[i][j] / number;
            }
        }
        return c;
    }

    /**
     * Multiplies this matrix, which is left, to the right one. Returns Matrix
     *
     * @param right right operand of multiplication operation
     * @return Matrix this multiplied by right operand
     * @throws MatrixException when tha columns number in left matrix does not equal to the rows number in right matrix
     */
    public Matrix mul(Matrix right) throws MatrixException {
        if (this.columnsQuantity != right.rowsQuantity) {
            String message = "Unable to multiply matrices, because of count of left matrix columns is " +
                    this.columnsQuantity + " and count of right matrix rows is " + right.rowsQuantity +
                    ". They are non-equal.";
            logger.error(message);
            throw new MatrixException(message);
        }
        Matrix c = new Matrix(this.rowsQuantity, right.columnsQuantity);
        for (int i = 0; i < this.rowsQuantity; i++) {
            for (int j = 0; j < right.columnsQuantity; j++) {
                for (int k = 0; k < right.rowsQuantity; k++) {
                    c.matrix[i][j] += (this.matrix[i][k] * right.matrix[k][j]);
                }
            }
        }
        return c;
    }

    /**
     * Divides this matrix, which is left, to the right one. Returns Matrix
     *
     * @param right right operand in division operation
     * @return Matrix this devided by right operand
     * @throws MatrixException when tha columns number in left matrix does not equal to the rows number in right matrix
     */
    public Matrix div(Matrix right) throws MatrixException {
        if (this.columnsQuantity != right.rowsQuantity) {
            String message = "Unable to divide matrices, because of count of left matrix columns is " +
                    this.columnsQuantity + " and count of right matrix rows is " + right.rowsQuantity +
                    ". They are non-equal.";
            logger.error(message);
            throw new MatrixException(message);
        }
        Matrix c = new Matrix(this.rowsQuantity, right.columnsQuantity);
        for (int i = 0; i < this.rowsQuantity; i++) {
            for (int j = 0; j < right.columnsQuantity; j++) {
                for (int k = 0; k < right.rowsQuantity; k++) {
                    if (Math.abs(right.matrix[k][j]) < accuracy) {
                        logger.warn("Possible divide by 0 . Matrix will be divided by " + accuracy +
                                " to avoid abnormal termination.");
                    }
                    c.matrix[i][j] += ((Math.abs(right.matrix[k][j]) < accuracy) ? this.matrix[i][k] / accuracy :
                            this.matrix[i][k] / right.matrix[k][j]);
                }
            }
        }
        return c;
    }

    /**
     * Exponentiates matrix to the power of n. Returns Matrix.
     *
     * @param n power, to which you would like to exponentiate
     * @return Matrix exponentiated to the power of n
     * @throws MatrixException when matrix is not square
     */
    public Matrix pow(int n) throws MatrixException {
        if (this.rowsQuantity != this.columnsQuantity) {
            String message = "Unable to raise a non-square matrix to a power";
            logger.error(message);
            throw new MatrixException(message);
        }
        if (Math.abs(this.det()) < accuracy && n < 0) {
            String message = "Unable to raise matrix, which determinant is equal to 0 to a negative power";
            logger.error(message);
            throw new MatrixException(message);
        }
        Matrix right = (n < 0) ? this.inv() : this;
        int p = Math.abs(n);
        Matrix c = identity();
        for (int i = 0; i < p; i++) {
            c = c.mul(right);
        }
        return c;
    }

    /**
     * Calculates absolute value of Matrix
     *
     * @return Matrix, each element of which is equal to itself if it was >=0 and (-itself) if it was < 0
     */
    public Matrix abs() {
        Matrix c = new Matrix(this.rowsQuantity, this.columnsQuantity);
        for (int i = 0; i < this.rowsQuantity; i++) {
            for (int j = 0; j < this.columnsQuantity; j++) {
                c.matrix[i][j] = Math.abs(this.matrix[i][j]);
            }
        }
        return c;
    }

    /**
     * Calculates m-norm of Matrix
     *
     * @return double m-norm of matrix
     */
    public double mNorm() {
        double result = 0, S;
        for (int i = 0; i < this.rowsQuantity; i++) {
            S = 0;
            for (int j = 0; j < this.columnsQuantity; j++) {
                S = S + Math.abs(this.matrix[i][j]);
            }
            if (S > result) {
                result = S;
            }
        }
        return result;
    }

    /**
     * Transposes Matrix
     *
     * @return Matrix transposed (rows in this matrix are columns in returned one)
     */
    public Matrix transpose() {
        Matrix c = new Matrix(this.columnsQuantity, this.rowsQuantity);
        for (int i = 0; i < this.rowsQuantity; i++) {
            for (int j = 0; j < this.columnsQuantity; j++) {
                c.matrix[j][i] = this.matrix[i][j];
            }
        }
        return c;
    }

    /**
     * Calculates l-norm of Matrix
     *
     * @return double l-norm of matrix
     */
    public double lNorm() {
        Matrix c = transpose();
        return c.mNorm();
    }

    /**
     * Calculates k-norm of Matrix
     *
     * @return double k-norm of matrix
     */
    public double kNorm() {
        double S = 0;
        for (int i = 0; i < this.rowsQuantity; i++) {
            for (int j = 0; j < columnsQuantity; j++) {
                S = S + Math.pow(Math.abs(this.matrix[i][j]), 2);
            }
        }
        return Math.sqrt(S);
    }

    /**
     * Deletes row, which number is rn (from 0 to (rows quantity - 1)) from Matrix
     *
     * @param rn number of row you are going to delete
     * @return Matrix with deleted row
     * @throws MatrixException when the row number is less than 0 or greater than (quantity - 1)
     */
    public Matrix deleteRow(int rn) throws MatrixException {
        if (rn < 0 || rn > this.rowsQuantity - 1) {
            String message = "Row#" + (rn + 1) + " is absent in matrix. It will not be deleted.";
            logger.error(message);
            throw new MatrixException(message);
        }
        Matrix c = new Matrix(this.rowsQuantity - 1, this.columnsQuantity);
        for (int i = 0; i < rn; i++) {
            if (this.columnsQuantity >= 0) {
                System.arraycopy(this.matrix[i], 0, c.matrix[i], 0, this.columnsQuantity);
            }
        }
        for (int i = rn + 1; i < this.rowsQuantity; i++) {
            if (this.columnsQuantity >= 0)
                System.arraycopy(this.matrix[i], 0, c.matrix[i - 1], 0, this.columnsQuantity);
        }
        return c;
    }

    /**
     * Deletes column, which number is cn (from 0 to (columns quantity - 1)) from Matrix
     *
     * @param cn number of column you are going to delete
     * @return Matrix with deleted column
     * @throws MatrixException when the column number is less than 0 or greater than (quantity - 1)
     */
    public Matrix deleteColumn(int cn) throws MatrixException {
        if (cn < 0 || cn > this.columnsQuantity - 1) {
            String message = "Column#" + (cn + 1) + " is absent in matrix. It will not be deleted.";
            logger.error(message);
            throw new MatrixException(message);
        }
        Matrix c = new Matrix(this.rowsQuantity, this.columnsQuantity - 1);
        for (int i = 0; i < this.rowsQuantity; i++) {
            System.arraycopy(this.matrix[i], 0, c.matrix[i], 0, cn);
            if (this.columnsQuantity - (cn + 1) >= 0)
                System.arraycopy(this.matrix[i], cn + 1, c.matrix[i], cn + 1 - 1,
                        this.columnsQuantity - (cn + 1));
        }
        return c;
    }

    /**
     * Deletes row number rn and column number cn. Row and column numeration starts with 0
     *
     * @param rn number of row (from 0)
     * @param cn number of column (from 0)
     * @return Matrix with deleted row and column
     * @throws MatrixException when the row and/or column numbers is less than 0 or greater than (quantity - 1)
     */
    public Matrix deleteRowColumn(int rn, int cn) throws MatrixException {
        if (rn < 0 || rn > this.rowsQuantity - 1) {
            String message = "Row#" + (rn + 1) + " is absent in matrix. It will not be deleted.";
            logger.error(message);
            throw new MatrixException(message);
        }
        if (cn < 0 || cn > this.columnsQuantity - 1) {
            String message = "Column#" + (cn + 1) + " is absent in matrix. It will not be deleted.";
            logger.error(message);
            throw new MatrixException(message);
        }
        return this.deleteRow(rn).deleteColumn(cn);
    }

    /**
     * Calculates determinant of the square matrix
     *
     * @return double determinant of square matrix
     * @throws MatrixException when the matrix is not square
     */
    public double det() throws MatrixException {
        if (this.rowsQuantity != this.columnsQuantity) {
            String message = "Unable to calculate matrix determinant. It is not square: rows quantity is " +
                    this.rowsQuantity + " and columns quantity is " + this.columnsQuantity;
            logger.error(message);
            throw new MatrixException(message);
        }

        int size = this.rowsQuantity;
        double result = 0;
        if (size == 1) {
            result = this.matrix[0][0];
        } else if (size == 2) {
            result = this.matrix[0][0] * this.matrix[1][1] - this.matrix[0][1] * this.matrix[1][0];
        } else if (size > 2) {
            int i = 0;
            for (int j = 0; j < size; j++) {
                Matrix temp = this.deleteRowColumn(i, j);
                result = result + (this.matrix[i][j] * Math.pow(-1, i + j + 2) * temp.det());
            }
        }
        return result;
    }

    /**
     * Calculates minor of element, which row is rn (from 0) and column is cn (from 0)
     *
     * @param rn number of row (from 0)
     * @param cn number of column (from 0)
     * @return double minor (determinant of matrix with deleted row and column)
     * @throws MatrixException when the Matrix is not square
     */
    public double minor(int rn, int cn) throws MatrixException {
        if (rn < 0 || rn > this.rowsQuantity - 1) {
            String message = "Row#" + (rn + 1) + " is absent in matrix. Minor will not be calculated.";
            logger.error(message);
            throw new MatrixException(message);
        }
        if (cn < 0 || cn > this.columnsQuantity - 1) {
            String message = "Column#" + (cn + 1) + " is absent in matrix. Minor will not be calculated.";
            logger.error(message);
            throw new MatrixException(message);
        }
        Matrix matrixToCalculateMinor = this.deleteRowColumn(rn, cn);
        return matrixToCalculateMinor.det();
    }

    /**
     * Calculates algebraic addition to element, which row is rn (from 0) and column is cn (from 0)
     *
     * @param rn number of row (from 0)
     * @param cn number of column (from 0)
     * @return double (determinant of matrix with deleted row and column, multiplied by -1 in power of (rn + cn + 2))
     * @throws MatrixException when the matrix is not square
     */
    public double algAdd(int rn, int cn) throws MatrixException {
        return this.minor(rn, cn) * Math.pow(-1, rn + cn + 2);
    }

    /**
     * Calculates an adjoint Matrix
     *
     * @return Matrix adjoint
     * @throws MatrixException when the Matrix is not square
     */
    public Matrix adj() throws MatrixException {
        Matrix c = new Matrix(this.columnsQuantity, this.rowsQuantity);
        for (int i = 0; i < this.rowsQuantity; i++) {
            for (int j = 0; j < this.columnsQuantity; j++) {
                c.matrix[j][i] = this.algAdd(i, j);
            }
        }
        return c;
    }

    /**
     * Calculates an invert Matrix
     *
     * @return Matrix invert
     * @throws MatrixException when matrix is not square
     */
    public Matrix inv() throws MatrixException {
        if (Math.abs(this.det()) < accuracy) {
            String message = "The matrix does not have invert one, because its determinant is equal to 0";
            logger.error(message);
            throw new MatrixException(message);
        }
        return Matrix.mulNumberByMatrix(1 / this.det(), this.adj());
    }

    /**
     * Returns an identity Matrix
     *
     * @return Matrix identity
     */
    public Matrix identity() {
        Matrix c = new Matrix(this.rowsQuantity, this.rowsQuantity);
        for (int i = 0; i < this.rowsQuantity; i++) {
            for (int j = 0; j < this.rowsQuantity; j++) {
                c.matrix[i][j] = (i == j) ? 1 : 0;
            }
        }
        return c;
    }

    /**
     * Checks if left matrix (this) equals to right
     *
     * @param right Matrix to compare to
     * @return boolean true, when equals and false otherwise
     */
    public boolean isLeftEqual(Matrix right) {
        if (this.rowsQuantity != right.rowsQuantity || this.columnsQuantity != right.columnsQuantity) {
            return false;
        }
        for (int i = 0; i < this.rowsQuantity; i++) {
            for (int j = 0; j < columnsQuantity; j++) {
                if (Math.abs(this.matrix[i][j] - right.matrix[i][j]) >= accuracy) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Matrix matrix1 = (Matrix) o;
        return rowsQuantity == matrix1.rowsQuantity && columnsQuantity == matrix1.columnsQuantity && isLeftEqual(matrix1);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(rowsQuantity, columnsQuantity);
        result = 31 * result + Arrays.deepHashCode(matrix);
        return result;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("[\n");
        for (double[] rows : this.matrix) {
            for (int j = 0; j < this.matrix[0].length; j++) {
                sb.append(rows[j]);
                sb.append(" ");
            }
            sb.append("\n");
        }
        sb.append("]");
        return sb.toString();
    }

    /**
     * Checks if transpose of matrix has been executed correctly
     *
     * @return boolean true if transposed correctly and false otherwise
     */
    public boolean isTransposeCorrect() {
        return this.transpose().transpose().equals(this);
    }

    /**
     * Checks if invert Matrix has been calculated correctly
     *
     * @return boolean true if transposed correctly and false otherwise
     * @throws MatrixException when matrix is not square
     */
    public boolean isInvCorrect() throws MatrixException {
        return (this.mul(this.inv()).equals(identity())) && (this.inv().mul(this).equals(identity()));
    }

    /**
     * Resolves the system of linear algebraic equation (SLAE)
     *
     * @param a Matrix, which size must be (m x m+1)
     * @return Matrix , which is column of X values
     * @throws MatrixException when quantity of variables is greater, than quantity of equations
     */
    public static Matrix resolveSLAE(double[][] a) throws MatrixException {
        Matrix A = new Matrix(a);
        if (A.columnsQuantity > A.rowsQuantity + 1) {
            String message = "Unable to resolve system, where quantity of variables is greater than equations quantity";
            MatrixLogger.get().error(message);
            throw new MatrixException(message);
        }
        int m = A.rowsQuantity;
        Matrix B = new Matrix(m, 1);
        for (int i = 0; i < m; i++) {
            B.matrix[i][0] = A.matrix[i][m];
        }
        return A.deleteColumn(m).inv().mul(B);
    }

    /**
     * @param a Matrix, which size must be rowsQuantity x (rowsQuantity+1)
     * @return Matrix , which is column of X values
     * @throws MatrixException when quantity of variables is greater, than quantity of equations
     */
    public static Matrix resolveSLAE(Matrix a) throws MatrixException {
        if (a.columnsQuantity > a.rowsQuantity + 1) {
            String message = "Unable to resolve system, where quantity of variables is greater than equations quantity";
            MatrixLogger.get().error(message);
            throw new MatrixException(message);
        }
        int m = a.rowsQuantity;
        Matrix b = new Matrix(m, 1);
        for (int i = 0; i < m; i++) {
            b.matrix[i][0] = a.matrix[i][m];
        }
        return a.deleteColumn(m).inv().mul(b);
    }

    private static boolean isGear(double[][] a) {
        int theFirstColumnsQuantity = (a.length > 0) ? a[0].length : 0;
        for (int i = 1; i < a.length; i++) {
            if (a[i].length != theFirstColumnsQuantity) {
                return true;
            }
        }
        return false;
    }
}
