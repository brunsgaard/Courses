import java.util.ArrayList;

public class Position {
    // Final variables cannot later be changed
    private final byte x, y;

    // Constructor
    private Position(byte x, byte y) {
	this.x = x;
	this.y = y;
    }

    // Getter for x coordinate
    public byte getX() {
	return x;
    }

    // Getter for y coordinate
    public byte getY() {
	return y;
    }

    /*
     * Generates an ArrayList of the positions a piece has to pass through in a
     * move from one position to another. This method is used together with the
     * method "allPositionsAreEmpty" in the Board Class. For a "path is clear"
     * check.
     */
    public static Position[] path(Position start, Position end) {
	// An arrayList is constructed to hold the positions temporary.
	ArrayList<Position> path = new ArrayList<Position>();

	byte currentX = start.getX();
	byte currentY = start.getY();

	// stepX and stepY figures out the direction the piece
	// want to move.
	int stepX = (end.getX() > start.getX()) ? 1 : -1;
	int stepY = (end.getY() > start.getY()) ? 1 : -1;
	currentX += stepX;
	currentY += stepY;

	// Moving horizontal
	if (end.getY() == start.getY()) {
	    for (byte i = currentX; i < end.getX(); i++) {
		path.add(new Position(i, end.getY()));
	    }
	    // Moving vertical
	} else if (end.getX() == start.getX()) {
	    for (byte i = currentY; i < end.getY(); i++) {
		path.add(new Position(end.getX(), i));
	    }
	    // Moving diagonal
	} else {
	    while (currentX != end.getX() || currentY != end.getY()) {
		// If given an invalid diagonal move, the while loop will never
		// end
		if (currentX > 7 || currentY > 7) {
		    throw new IllegalArgumentException(
			    "Pathfinder off the board");
		}
		path.add(new Position(currentX, currentY));
		currentX += stepX;
		currentY += stepY;
	    }
	}

	// Return the ArrayList 'path' as a Position Array.
	return path.toArray(new Position[0]);
    }

    /**
     * @null if the representation is invalid.
     */
    public static Position fromRepresentation(String representation) {
	if (representation.length() != 2) {
	    return null;
	}

	char[] elements = representation.toCharArray();

	boolean letterIsOutOfBounds = elements[0] < 'A' || elements[0] > 'H';
	boolean numberIsOutOfBounds = elements[1] < '1' || elements[1] > '8';

	if (letterIsOutOfBounds || numberIsOutOfBounds) {
	    return null;
	}

	byte x = (byte) ((elements[0]) - 65);
	byte y = (byte) ((elements[1]) - 49);

	return new Position(x, y);
    }

    /**
     * @true if the x and y coordinates are equal.
     */
    @Override
    public boolean equals(Object other) {
	if (!(other instanceof Position)) {
	    return false;
	}

	Position position = (Position) other;

	return position.x == this.x && position.y == this.y;
    }

    /**
     * x * 8 + y
     */
    @Override
    public int hashCode() {
	return (this.x << 3) + this.y;
    }

}
