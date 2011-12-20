public abstract class Piece {

    public enum Color {
	BLACK, WHITE
    }

    protected Color blackOrWhite;

    protected Piece(Color blackOrWhite) {
	this.blackOrWhite = blackOrWhite;
    }

    public String toString() {
	return new String(blackOrWhite == Color.WHITE ? "W" : "B") + key();
    }

    /**
     * Either P, R, N, B, Q, or K.
     */

    public abstract char key();

    public Color getColor() {
	return blackOrWhite;
    }

    public boolean isLegalMove(Position start, Position end, Board board) {
	if (board.getPiece(end) == null)
	    return true;
	if (board.getPiece(start).getColor() == board.getPiece(end).getColor())
	    return false;
	return true;
    }


}
