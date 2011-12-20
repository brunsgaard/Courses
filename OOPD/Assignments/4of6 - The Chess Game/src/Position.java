import java.util.ArrayList;

public class Position {
    private final byte x, y;

    private Position(byte x, byte y) {
	this.x = x;
	this.y = y;
    }

    public byte getX() {
	return x;
    }

    public byte getY() {
	return y;
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

    public static Position[] path(Position start, Position end) {
	ArrayList<Position> path = new ArrayList<Position>();

	byte currentX = start.getX();
	byte currentY = start.getY();
	int stepX = (end.getX() > start.getX()) ? 1 : -1;
	int stepY = (end.getY() > start.getY()) ? 1 : -1;
	currentX += stepX;
	currentY += stepY;
	if (end.getY() == start.getY()) {
	    for (byte i = currentX; i < end.getX(); i++) {
		path.add(new Position(i, end.getY()));
	    }
	} else if (end.getX() == start.getX()) {
	    for (byte i = currentY; i < end.getY(); i++) {
		path.add(new Position(end.getX(), i));
	    }
	} else {
	    while (currentX != end.getX() || currentY != end.getY()) {
		// If given an invalid diagonal move, we might never find end
		if (currentX > 7 || currentY > 7) {
		    throw new IllegalArgumentException(
			    "Pathfinder off the board");
		}
		path.add(new Position(currentX, currentY));
		currentX += stepX;
		currentY += stepY;
	    }
	}
	return path.toArray(new Position[0]);
    }
}
