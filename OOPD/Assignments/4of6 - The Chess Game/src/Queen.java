public class Queen extends Piece {

    public Queen(Color c) {
	super(c);
    }

    @Override
    public char key() {
	return 'Q';
    }

    @Override
    public boolean isLegalMove(Position start, Position end, Board board) {
	if (!super.isLegalMove(start, end, board))
	    return false;
	try {
	    if (!board.allPositionsAreEmpty(Position.path(start, end)))
		return false;
	} catch (IllegalArgumentException e) {
	    return false;
	}
	return true;

    }
}
