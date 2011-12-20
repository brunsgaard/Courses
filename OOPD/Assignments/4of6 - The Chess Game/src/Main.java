import java.util.Scanner;

public class Main {

    private static Scanner in;
    private static Game game;

    public static void main(String[] args) {
	game = new Game();
	System.out.println(game.getBoardAsString());
	in = new Scanner(System.in);
	while (true) {
	    System.out.println("Player " + game.getCurrentPlayer() + ":");
	    String token = in.next();
	    if (token.equals("handshake")) {
		game.restart();
		System.out.println(game.getBoardAsString());
	    } else if (token.equals("board")) {
		System.out.println(game.getBoardAsString());
	    } else {
		String secondToken = in.next();
		Position start = Position.fromRepresentation(token);
		Position end = Position.fromRepresentation(secondToken);
		if (start == null || end == null) {
		    System.out.println("ILLEGAL!");
		} else {
		    if (!game.tryMovePiece(start, end)) System.out.println("ILLEGAL!");
		    if (game.gameOver()) {
			System.out.println("GAME OVER !!!");
			break;
		    }
		}
	    }
	}

    }
}
