import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class Menu {

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
}
