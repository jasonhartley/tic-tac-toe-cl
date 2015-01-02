import java.util.Map;

public class Main {

	public static void main(String[] args) {
		int size = Options.getSize(args);
		int playerCount = Options.getPlayerCount(args);
		Board board = new Board(size, playerCount);
		Map<Integer, Player> players = Options.getPlayers(playerCount);
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
