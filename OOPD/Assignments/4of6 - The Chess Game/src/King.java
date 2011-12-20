
public class King extends Piece {
    
    public King (Color c){
	super(c);
    }

    @Override
    public char key() {

	return 'K';
    }
    
    @Override
    public boolean isLegalMove(Position start,Position end, Board board){
	if (!super.isLegalMove(start, end, board))
	    return false;
	if (Math.abs(start.getX()-end.getX()) > 1 ) return false;
	if (Math.abs(start.getY()-end.getY()) > 1 ) return false;
	return true;
    }
}
