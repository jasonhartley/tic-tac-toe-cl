import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Main {

	public static void main(String[] args) {
		final int size = args.length < 1 ? Board.MIN_SIZE : Integer.parseInt(args[0]);// Size of the board, default to 3
		final int playerCount = args.length < 2 ? 2 : Integer.parseInt(args[1]);// Player count, default to 2

		Board board = new Board(size, playerCount);
		List<Player> players = new ArrayList<Player>();// todo: more than 2 players
		String input;

		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			System.out.println();
			System.out.println("Press [1] for Computer, [2] for Human");

			for (int playerOrder = 0; playerOrder < playerCount; playerOrder++) {
				do {
					System.out.print("Player " + (playerOrder + 1) + ": ");
					input = br.readLine();
				}
				while (!Util.isPositiveInt(input) || Integer.valueOf(input) > playerCount);

				if (Integer.valueOf(input) == 1) {
					players.add(new Computer(playerOrder + 1));
				}
				else {
					players.add(new Human(playerOrder + 1));
				}
			}
			System.out.println();

		} catch (IOException io) {
			io.printStackTrace();
		}

		int play, state = 0, counter = 0;
		Player currentPlayer = null;

		while (state == 0) {
			board.show();
			counter = counter % playerCount;
			currentPlayer = players.get(counter);
			play = currentPlayer.getPlay(board);
			state = board.put(play, currentPlayer.value());
			counter++;
		}

		board.show();
		if (state > 0) {
			System.out.println("Winner: " + currentPlayer.show());
			System.out.println();
		}
		else {
			System.out.println("Draw");
			System.out.println();
		}
	}
}
