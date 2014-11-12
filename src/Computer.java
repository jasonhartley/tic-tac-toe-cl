import java.util.Iterator;
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
		Position position = new Position(board.maxIndex());// Create a temp position
		Set<Play> plays = board.getPlays();
		Iterator<Play> iterator = plays.iterator();

		// If board is empty, take a corner
		if (plays.size() == 0) {
			position.setAsCorner();// Choose a random corner I guess
		}
		// If there is one move
		else if (plays.size() == 1) {
			Position opponentPos = iterator.next().position();
			//If corner, chose center
			if (opponentPos.isCorner()) {
				System.out.println("Choosing center");
				position.setAsCenter();
			}
			//Else if center, chose corner
			else if (opponentPos.isCenter()) {
				System.out.println("Choosing corner");
				position.setAsCorner();
			}

			//Else side, chose adjacent corner
			else if(opponentPos.isSide()) {
				System.out.println("Choosing adjacent corner");
				position.setAsAdjCorner(opponentPos);
			}

		}
		// If there are two or more moves
		else {
			// Choose at random temporarily
			do {
				System.out.println("Choosing random");
				position.setRandom();
			}
			while (!board.isValidPosition(position));

			// aligned plays: Two or more plays from the same player in the same row, col, or diagonal
			// open aligned: Aligned plays with at least 1 open position in that line

			// If an open aligned exists (there may be more than one, chose the first encountered), chose an open position (first encountered) in that line

			// Else build
				// If corners is doable, do it

				// Else do wedge

		}

		return new Play(position, value);
	}
}
