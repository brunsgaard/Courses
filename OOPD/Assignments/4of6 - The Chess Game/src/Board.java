public class Board {

    private Piece[][] board;
    private boolean gameOver;

    public Board() {
	this.board = new Piece[8][8];
	this.initialize();
    }

    public Piece getPiece(Position position) {
	return board[position.getY()][position.getX()];
    }

    public boolean allPositionsAreEmpty(Position[] positions) {
	for (Position p : positions) {
	    if (p == null)
		return false;
	}
	return true;
    }

    private void initialize() {
	this.gameOver = false;

	// Pawn
	for (int i = 0; i < 8; i++) {
	    board[1][i] = new Pawn(Piece.Color.WHITE);
	    board[6][i] = new Pawn(Piece.Color.BLACK);
	}
	// Rook
	board[0][0] = new Rook(Piece.Color.WHITE);
	board[0][7] = new Rook(Piece.Color.WHITE);
	board[7][0] = new Rook(Piece.Color.BLACK);
	board[7][7] = new Rook(Piece.Color.BLACK);
	// Knight
	board[0][1] = new Knight(Piece.Color.WHITE);
	board[0][6] = new Knight(Piece.Color.WHITE);
	board[7][1] = new Knight(Piece.Color.BLACK);
	board[7][6] = new Knight(Piece.Color.BLACK);
	// Bishop
	board[0][2] = new Bishop(Piece.Color.WHITE);
	board[0][5] = new Bishop(Piece.Color.WHITE);
	board[7][2] = new Bishop(Piece.Color.BLACK);
	board[7][5] = new Bishop(Piece.Color.BLACK);
	// Queen
	board[0][3] = new Queen(Piece.Color.WHITE);
	board[7][3] = new Queen(Piece.Color.BLACK);
	// King
	board[0][4] = new King(Piece.Color.WHITE);
	board[7][4] = new King(Piece.Color.BLACK);
    }

    public String humanReadableState() {
	StringBuilder builder = new StringBuilder(192);
	for (int y = 7; y >= 0; y--) {
	    for (int x = 0; x < 8; x++) {
		Piece p = this.board[y][x];
		builder.append(p == null ? "00" : p);
		if (x < 7)
		    builder.append(" ");
	    }
	    builder.append("\n");
	}
	return builder.toString();
    }

    public boolean gameOver() {
	return this.gameOver;
    }

    public boolean tryMovePiece(Piece.Color playerColor, Position start,
	    Position end) {
	Piece p = getPiece(start);
	if (this.gameOver)
	    return false;
	if (p == null)
	    return false;
	if (p.getColor() != playerColor)
	    return false;
	if (!p.isLegalMove(start, end, this))
	    return false;
	if (getPiece(end) instanceof King)
	    this.gameOver = true;
	this.board[end.getY()][end.getX()] = p;
	this.board[start.getY()][start.getX()] = null;
	
	return true;
    }
}
