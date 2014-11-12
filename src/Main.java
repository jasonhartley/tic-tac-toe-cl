public class Main {

	public static void main(String[] args) {
		// The size of the board, default to 3
		final int size = args.length < 1 ? Board.MIN_SIZE : Integer.parseInt(args[0]);
		Board board = new Board(size);
		Player[] players = new Player[2];// In the future, the number of players may scale
		players[0] = new Human(Value.X);
		players[1] = new Computer(Value.O);// todo: offer the choice to play against the computer
		int winner = Value.NONE;// Player # of winner
		int counter = 0;// Player array position of whose turn it is
		Play play;

		while (winner == Value.NONE) {
			board.show();
			play = players[counter].getPlay(board);
			winner = board.put(play);
			counter = ++counter % 2;
		}

		board.show();
		System.out.println("Winner: " + Value.show(winner));
	}
}
