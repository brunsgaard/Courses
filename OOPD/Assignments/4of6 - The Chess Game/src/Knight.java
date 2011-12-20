public class Knight extends Piece {

    public Knight(Color c) {
	super(c);
    }

    @Override
    public char key() {
	return 'N';
    }

    @Override
    public boolean isLegalMove(Position start, Position end, Board board) {
	if (!super.isLegalMove(start, end, board))
	    return false;
	int diffX = Math.abs(start.getX() - end.getX());
	int diffY = Math.abs(start.getY() - end.getY());
	if ((diffX == 2 && diffY == 1) || (diffX == 1 && diffY == 2))
	    return true;
	return false;
    }
}
