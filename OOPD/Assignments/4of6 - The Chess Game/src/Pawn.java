public class Pawn extends Piece {

    public Pawn(Color c) {
	super(c);
    }

    @Override
    public char key() {

	return 'P';
    }

    @Override
    public boolean isLegalMove(Position start, Position end, Board board) {
	if (!super.isLegalMove(start, end, board))
	    return false;
	// Forward one square
	if (start.getX() == end.getX()
		&& end.getY() - start.getY() == (board.getPiece(start)
			.getColor() == Piece.Color.BLACK ? -1 : 1)
		&& board.getPiece(end) == null)
	    return true;
	// attackmove
	int stepY = (end.getY() > start.getY()) ? 1 : -1;
	if (end.getY() - start.getY() == stepY
		&& Math.abs(end.getX() - start.getX()) == 1
		&& board.getPiece(end) != null)
	    return true;
	return false;
    }
}
