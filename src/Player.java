public abstract class Player {
	protected int playerValue;

	public int value() {
		return playerValue;
	}

	// Returns a capital letter of the alphabet corresponding to playerValue, except with X for 1 and O for 2
	public String show() {
		return show(playerValue);
	}

	public static String show(int playerValue) {
		final int O = 15;
		final int X = 24;
		char playerChar = (char) ('@' + playerValue);// '@' is ascii 64, 'A' is 65

		if (playerValue == 1) {
			playerChar = 'X';
		}
		else if (playerValue == 2) {
			playerChar = 'O';
		}
		else if (playerValue >= O) {
			playerChar -= 1;
		}
		else if (playerValue >= X) {
			playerChar -= 2;
		}

		return String.valueOf(playerChar);
	}

	public abstract int getPlay(Board board);
}
