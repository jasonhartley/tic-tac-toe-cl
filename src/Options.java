import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class Options {

	public static Map<Integer, Player> getPlayers(int playerCount) {

		Map<Integer, Player> players = new HashMap<Integer, Player>();
		String input;

		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			System.out.println("\nPress [1] for Computer, [2] for Human");

			for (int playerValue = 1; playerValue <= playerCount; playerValue++) {
				do {
					System.out.print("Player " + playerValue + ": ");
					input = br.readLine();
				}
				while (!Util.isValidPlayerType(input));// two types: human or computer

				if (Integer.valueOf(input) == 1) {
					players.put(playerValue, new Computer(playerValue));
				}
				else {
					players.put(playerValue, new Human(playerValue));
				}
			}
			System.out.println();

		} catch (IOException io) {
			io.printStackTrace();
		}

		return players;
	}

	public static int getSize(String args[]) {
		int size = Board.MIN_SIZE;
		if (args.length > 0 && Util.isPositiveInt(args[0])) {
			int sizeArg = Integer.parseInt(args[0]);
			if (sizeArg > Board.MIN_SIZE) {
				size = sizeArg;
			}
			if (sizeArg > Board.MAX_SIZE) {
				size = Board.MAX_SIZE;
			}
		}
		return size;
	}

	public static int getPlayerCount(String args[]) {
		int playerCount = Board.MIN_PLAYER_COUNT;
		if (args.length > 1 && Util.isPositiveInt(args[1])) {
			int playerCountArg = Integer.parseInt(args[1]);
			if (playerCountArg > Board.MIN_PLAYER_COUNT) {
				playerCount = playerCountArg;
			}
			if (playerCountArg > Board.MAX_PLAYER_COUNT) {
				playerCount = Board.MAX_PLAYER_COUNT;
			}
		}
		return playerCount;
	}
}
