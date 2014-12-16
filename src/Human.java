import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Human extends Player {

	public Human(int playerValue) {
		this.playerValue = playerValue;
	}

	public int getPlay(Board board) {
		String input;
		int ordinal = -1;// If -1 is returned, something went wrong getting the input
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			do {
				do {
					System.out.print("Enter a position number: ");
					input = br.readLine();
				}
				while (!Util.isPositiveInt(input));
				ordinal = Integer.valueOf(input) - 1;// The displayed ordinals start at 1 rather than 0
			}
			while (!board.isOpen(ordinal));

		} catch (IOException io) {
			io.printStackTrace();
		}

		System.out.println();

		return ordinal;
	}
}
