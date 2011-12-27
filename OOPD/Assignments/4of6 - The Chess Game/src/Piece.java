public abstract class Piece {

    // "Datatype" describing something to be either black og white
    protected enum Color {
	BLACK, WHITE
    }

    // A Class variable describing the color of the piece.
    protected Color blackOrWhite;

    // Constructor
    protected Piece(Color blackOrWhite) {
	this.blackOrWhite = blackOrWhite;
    }

    // A getter for the variable Color
    public Color getColor() {
	return blackOrWhite;
    }

    // This method will be defined in the extended classes.
    public abstract char key();

    // A getter describing the color and key of the piece.
    public String toString() {
	return new String(blackOrWhite == Color.WHITE ? "W" : "B") + key();
    }

    // Basic tests for movement on the board, this method is extended in
    // the subclass for each piece.
    public boolean isLegalMove(Position start, Position end, Board board) {
	if (board.getPiece(end) == null)
	    return true;
	if (board.getPiece(start).getColor() == board.getPiece(end).getColor())
	    return false;
	return true;
    }

}
