public class Main {

	public static void main(String[] args) {
		// Size of the board, default to 3
		final int size = args.length < 1 ? Board.MIN_SIZE : Integer.parseInt(args[0]);
		// Player count, default to 2
		final int playerCount = args.length < 2 ? 2 : Integer.parseInt(args[1]);

		Board board = new Board(size);
		Player[] players = new Player[playerCount];// In the future, the number of players may scale
		players[0] = new Computer(1);
		players[1] = new Human(2);// todo: menu for which player is which

		int playerValue = 0;// Player value of whose turn it is
		int play;

		do {
			board.show();
			playerValue = playerValue % playerCount + 1;
			play = players[playerValue - 1].getPlay(board);
		}
		while (!board.put(play, playerValue));

		board.show();
		System.out.println("Winner: " + board.getPlayerString(playerValue));// todo: show the player letter, e.g. X or O, etc.
	}
}
