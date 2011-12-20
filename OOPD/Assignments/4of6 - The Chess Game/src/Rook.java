public class Rook extends Piece {

    public Rook(Color c) {
	super(c);
    }

    @Override
    public char key() {
	return 'R';
    }

    @Override
    public boolean isLegalMove(Position start, Position end, Board board) {
	if (!super.isLegalMove(start, end, board))
	    return false;
	if (start.getX() != end.getX() && start.getY() != end.getY()) return false;
	try {
	    if (!board.allPositionsAreEmpty(Position.path(start, end)))
		return false;
	} catch (IllegalArgumentException e) {
	    return false;
	}
	return true;

    }
}
