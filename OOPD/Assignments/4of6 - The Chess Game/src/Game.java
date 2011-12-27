public class Game {

    // Class variable of type Board
    private Board board;

    // Current player represented by an int 1 or 2
    private int currentPlayer;

    // Constructor
    public Game() {
	this.board = new Board();
	this.currentPlayer = 1;
    }

    // Restarts the game
    public void restart() {
	this.board = new Board();
	this.currentPlayer = 1;
    }

    // Try to move a piece and shifts turn if success
    public boolean tryMovePiece(Position start, Position end) {
	Piece.Color color;
	if (this.currentPlayer == 1) {
	    color = Piece.Color.WHITE;
	} else {
	    color = Piece.Color.BLACK;
	}

	boolean success = board.tryMovePiece(color, start, end);

	if (success) {
	    this.currentPlayer = this.currentPlayer == 1 ? 2 : 1;
	}
	return success;
    }

    // Calls the method to print the board to console
    // Necessary because the board variable is private and thus
    // Main can't call game.board.humanReadableState()
    public String getBoardAsString() {
	return this.board.humanReadableState();
    }

    // Checks if the game is over
    // Necessary because the board variable is private
    public boolean gameOver() {
	return this.board.gameOver();
    }

    // Getter for the current player
    public int getCurrentPlayer() {
	return this.currentPlayer;
    }

}
