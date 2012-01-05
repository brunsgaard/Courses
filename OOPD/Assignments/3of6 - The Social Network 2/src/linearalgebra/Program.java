import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Program {

    private enum Commands {
	bye, define, manual, diagonal, transpose, multiply, print
    }

    private enum State {
	START, DEFINE, MANUAL, DIAGONAL, FINISHED
    }

    private HashMap<String, Matrix> matrixList;
    private Scanner in;
    private State state;
    private String inputName;

    public Program() {
	this.matrixList = new HashMap<String, Matrix>();
	this.in = new Scanner(System.in);
	this.state = State.START;
	this.inputName = null;
    }
    
    public static void main(String[] args) {
	Program p = new Program();
	while (p.getState() != State.FINISHED) {
	    p.readToken();
	}
    }

    public State getState() {
	return this.state;
    }

    public void readToken() {
	if (this.state == State.START)
	    System.out.println("What would you like to do?");
	String token = in.next();
	try {
	    switch (Commands.valueOf(token)) {
	    case bye:
		this.state = State.FINISHED;
		return;
	    case define:
		this.state = State.DEFINE;
		this.define();
		return;
	    case manual:
		if (this.state != State.DEFINE) {
		    System.out.println("You must first define the matrix");
		    return;
		} else {
		    this.state = State.MANUAL;
		    this.manual();
		    this.state = State.START;
		    return;
		}
	    case diagonal:
		if (this.state != State.DEFINE) {
		    System.out.println("You must first define the matrix");
		    return;
		} else {
		    this.state = State.DIAGONAL;
		    this.diagonal();
		    this.state = State.START;
		    return;
		}
	    case transpose:
		this.transpose();
		return;
	    case multiply:
		this.multiply();
		return;
	    case print:
		this.print();
		return;
	    }
	} catch (IllegalArgumentException e) {
	    in.nextLine();
	    System.out
		    .println("I do not understand command: '"
			    + token
			    + "', try one of: bye, define, manual, diagonal, transpose, multiply, print");
	} catch (InputMismatchException e) {
	    System.out.println("I can break rules too");
	    this.state = State.FINISHED;
	    return;
	}
    }

    public void define() {
	this.inputName = this.in.next().trim();
	System.out.println("How do you want to define the matrix?");
    }

    public void manual() {
	int inputSize;
	System.out.println("What size matrix would you like to define?");
	inputSize = in.nextInt();
	int[][] inputMatrix = new int[inputSize][inputSize];

	for (int i = 0; i < inputSize; i++) {
	    int[] inputRow = new int[inputSize];
	    System.out.println("Please enter row " + (i + 1) + ":");
	    for (int j = 0; j < inputSize; j++) {
		inputRow[j] = in.nextInt();
	    }
	    inputMatrix[i] = inputRow;
	}
	this.matrixList.put(inputName, new Matrix(inputSize, inputMatrix));
	System.out.println("Done.\n");
    }

    public void diagonal() {
	int inputSize, inputValue;
	System.out.println("Of what size?");
	inputSize = in.nextInt();
	System.out.println("Which value?");
	inputValue = in.nextInt();
	this.matrixList.put(this.inputName, new Matrix(inputSize, inputValue));
	System.out.println("Done.\n");
    }

    public void transpose() {
	this.inputName = this.in.next().trim();
	Matrix a = this.matrixList.get(this.inputName);
	if (a == null) {
	    System.out.println("undefined matrix!");
	    return;
	}
	a.transpose();
	System.out.println("Done.\n");
    }

    public void multiply() {
	String aName = this.in.next().trim();
	String bName = this.in.next().trim();
	Matrix a = this.matrixList.get(aName);
	Matrix b = this.matrixList.get(bName);
	if (a == null || b == null) {
	    System.out.println("undefined matrix!");
	    return;
	}
	try {
	    a.multiply(b);
	    System.out.println("Done.\n");
	} catch (IllegalArgumentException e) {
	    System.out.println(e.getMessage());
	}

    }

    public void print() {
	this.inputName = this.in.next().trim();
	Matrix a = this.matrixList.get(this.inputName);
	if (a == null) {
	    System.out.println("undefined matrix!");
	    return;
	}
	a.print();
	System.out.println("");
    }
}
