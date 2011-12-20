public class Matrix {

    private int[][] matrix;
    private int size;

    public Matrix(int size, int[][] rowArray) {
	this.matrix = rowArray;
	this.size = size;
    }

    public Matrix(int size, int diagonalNumber) {
	this.matrix = new int[size][size];
	for (int i = 0; i < size; i++) {
	    this.matrix[i][i] = diagonalNumber;
	}
	this.size = size;
    }

    public int getSize() {
	return this.size;
    }

    public int[][] getMatrix() {
	return this.matrix;
    }

    public void print() {
	String s = "";
	for (int i = 0; i < this.size; i++) {
	    s += "\n";
	    for (int j = 0; j < this.size; j++) {
		s += this.matrix[i][j] + " ";
	    }
	}
	System.out.println(s.trim());
    }

    public void transpose() {
	int[][] transposedMatrix = new int[size][size];
	for (int i = 0; i < size; ++i) {
	    for (int j = 0; j < size; ++j) {
		transposedMatrix[j][i] = this.matrix[i][j];
	    }
	}
	this.matrix = transposedMatrix;

    }

    public void multiply(Matrix bMatrix) {
	if (this.size != bMatrix.getSize()) throw new IllegalArgumentException("The matrices have to be same dimensions");
	int[][] result = new int[size][size];
	Matrix bMatrixTransposed = new Matrix(size,bMatrix.getMatrix());
	bMatrixTransposed.transpose();
	int[][] a = this.matrix;
	int[][] bt = bMatrixTransposed.getMatrix();
	for (int i = 0; i < this.size; i++) {
	    for (int j = 0; j < size; j++) {
		for (int k = 0; k < size; k++) {
		    result[i][j] += a[i][k] * bt[k][j];
		}
	    }
	}
	this.matrix = result;
    }

}