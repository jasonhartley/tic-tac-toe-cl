public class Main {

	public static void main(String[] args) {
		// Size of the board, default to 3
		final int size = args.length < 1 ? Board.MIN_SIZE : Integer.parseInt(args[0]);
		// Player count, default to 2
		final int playerCount = args.length < 2 ? 2 : Integer.parseInt(args[1]);

		Board board = new Board(size);
		Player[] players = new Player[playerCount];// In the future, the number of players may scale
		players[0] = new Computer(1);// todo: fix redundant player value, make ArrayList
		players[1] = new Human(2);// todo: menu for which player is which

		int playerValue = 0;// Player value of whose turn it is
		int play, state = 0;

		while (state == 0) {
			board.show();
			playerValue = playerValue % playerCount + 1;
			play = players[playerValue - 1].getPlay(board);
			state = board.put(play, playerValue);
		}

		board.show();
		if (state > 0) {
			System.out.println("Winner: " + board.getPlayerString(state));
			System.out.println();
		}
		else {
			System.out.println("Draw");
			System.out.println();
		}
	}
}
