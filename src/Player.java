public abstract class Player {
	protected int playerValue;

	public int value() {
		return playerValue;
	}

	/**
	 * Show the player's character, e.g. X or O
	 *
	 * @return Returns a capital letter of the alphabet corresponding to playerValue, with 'X' for 1 and 'O' for 2,
	 *         'A' for 3... 'N' for 16, 'P' for 17... 'W' for 24, 'Y' for 25, and 'Z' for 26
	 */
	public String show() {
		return show(playerValue);
	}

	public static String show(int playerValue) {
		char playerChar = (char) ('@' + playerValue);// '@' is ascii 64, 'A' is 65

		if (playerValue == 1) {
			playerChar = 'X';
		}
		else if (playerValue == 2) {
			playerChar = 'O';
		}
		// P
		else if (playerValue >= 17) {
			playerChar -= 1;
		}// A
		else if (playerValue >= 3) {
			playerChar -= 2;
		}

		return String.valueOf(playerChar);
	}

	public abstract int getPlay(Board board);
}
