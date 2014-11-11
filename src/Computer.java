import java.util.Set;

public class Computer implements Player {
	private int value;

	public Computer(int value) {
		if (Value.isValidPlayer(value)) {
			this.value = value;
		}
	}

	public int value() {
		return value;
	}

	public String show() {
		return Value.show(value);
	}

	public Play getPlay(Board board) {
		Position position = new Position(board.maxIndex());
		Set<Play> plays;

		// Logic goes here
		plays = board.getPlays();

		// If board is empty, take a corner

		// If there is one move
			//If corner, chose center

			//Else if center, chose corner

			//Else side, chose adjacent corner

		// If two or more moves
			// If is an aligned play with an open third, chose the third (maybe be two "open aligned", hence will lose)

			// Else build
				// If corners is doable, do it

				// Else do wedge


		return new Play(position, value);
	}
}
