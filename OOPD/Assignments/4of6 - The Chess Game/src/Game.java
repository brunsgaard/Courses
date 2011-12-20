public class Game {

    private Board board;

    private int currentPlayer;

    public Game() {
	this.board = new Board();
	this.currentPlayer = 1;
    }

    public void restart() {
	this.board = new Board();
	this.currentPlayer = 1;
    }

    public boolean tryMovePiece(Position start, Position end) {
	boolean success = board
		.tryMovePiece(this.currentPlayer == 1 ? Piece.Color.WHITE
			: Piece.Color.BLACK, start, end);
	if (success) {
	    this.currentPlayer = this.currentPlayer == 1 ? 2 : 1;
	}
	return success;
    }

    public String getBoardAsString() {
	return this.board.humanReadableState();
    }

    public boolean gameOver() {
	return this.board.gameOver();
    }

    public int getCurrentPlayer() {
	return this.currentPlayer;
    }

}
