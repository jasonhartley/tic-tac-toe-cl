import java.util.Map;

public class Main {

	public static void main(String[] args) {
		int size = Board.MIN_SIZE;
		int playerCount = Board.MIN_PLAYER_COUNT;

		// Sanitize the arguments
		if (args.length > 0 && Util.isPositiveInt(args[0])) {
			int sizeArg = Integer.parseInt(args[0]);
			if (sizeArg > Board.MIN_SIZE) {
				size = sizeArg;
			}
			if (sizeArg > Board.MAX_SIZE) {
				size = Board.MAX_SIZE;
			}
		}
		if (args.length > 1 && Util.isPositiveInt(args[1])) {
			int playerCountArg = Integer.parseInt(args[1]);
			if (playerCountArg > Board.MIN_PLAYER_COUNT) {
				playerCount = playerCountArg;
			}
			if (playerCountArg > Board.MAX_PLAYER_COUNT) {
				playerCount = Board.MAX_PLAYER_COUNT;
			}
		}

		Board board = new Board(size, playerCount);
		Map<Integer, Player> players = Menu.getPlayers(playerCount);
		int play, state = 0, counter = 0;
		Player currentPlayer = null;

		// The main loop
		while (state == 0) {
			board.show();
			counter = counter % playerCount;
			currentPlayer = players.get(counter + 1);
			play = currentPlayer.getPlay(board);
			state = board.put(play, currentPlayer.value());
			counter++;
		}

		board.show();
		if (state > 0) {
			System.out.println("Winner: " + currentPlayer.show() + "\n");
		}
		else {
			System.out.println("Draw\n");
		}
	}
}
