import java.util.Iterator;
import java.util.Set;

public class Computer implements Player {
	private int playerValue;

	public Computer(int playerValue) {
		if (Value.isValidPlayer(playerValue)) {
			this.playerValue = playerValue;
		}
	}

	interface Tactic {
		Position tactic(Board board);
	}

	Tactic[] tactics = new Tactic[] {
			new Tactic() { public Position tactic(Board b) { return buildTriple(b); } },
			new Tactic() { public Position tactic(Board b) { return blockTriple(b); } },
			new Tactic() { public Position tactic(Board b) { return buildSplit(b); } },
			new Tactic() { public Position tactic(Board b) { return buildDouble(b); } },
			new Tactic() { public Position tactic(Board b) { return blockSplit(b); } },
			new Tactic() { public Position tactic(Board b) { return blockDouble(b); } },
			new Tactic() { public Position tactic(Board b) { return random(b); } },
	};

	public int value() {
		return playerValue;
	}

	public String show() {
		return Value.show(playerValue);
	}

	public Play getPlay(Board board) {
		Position position = new Position(board.maxIndex());// Create a temp position
		Set<Play> plays = board.getPlays();
		Iterator<Play> iterator = plays.iterator();

		// If board is empty (I am X, first move)
		if (plays.size() == 0) {
			position = firstPlay(board);
		}
		// If there is one move (I am O, second move)
		else if (plays.size() == 1) {
			position = secondPlay(board);
		}
		// If there are two moves (I am X, third movie)
/*
		else if (plays.size() == 2) {
			position = iterator.next().position();// Get the first play because it is my play
			// If I have a corner (which I should since I went first)
			if (position.isCorner()) {
				// If center is open, take opposite corner
				if (board.getCenter() == Value.NONE) {
					position.setAs180Rotation();// todo: make sure it's okay to get a position, then reuse that same object when returning the play
				}
				// Else take an adjacent corner with an open side
				else {
					position = board.anyOpenAdjacent(position);// position is now the nearest open adjacent square (non-diagonal)
					// todo: position = adjacentCorner, or maybe just position = board.openAdjCornerWithOpenPath

				}
			}
			// If I have the center (which I should not because I had first move, therefore I took a corner)
			// If I have a side (then something is fucked up because I should never have a side after two moves)
		}
*/
		// If there are two or more moves
		else {
			position = generalStrategy(board);
/*
			// Choose at random temporarily
			do {
				System.out.println("Choosing random");
				position.setRandom();
			}
			while (!board.isOpenPosition(position));
*/

			// aligned plays: Two or more plays from the same player in the same row, col, or diagonal
			// open aligned: Aligned plays with at least 1 open position in that line

			// If an open aligned exists (there may be more than one, chose the first encountered), chose an open position (first encountered) in that line

			// Else build
				// If corners is doable, do it

				// Else do wedge

		}

		return new Play(position, playerValue);
	}

	private Position generalStrategy(Board board) {
		Position position = null;

		// Try each tactic in order.  Return once one works.
		for (int i = 0; position == null; i++) {
			position = tactics[i].tactic(board);
		}
		
		return position;
	}

	private Position firstPlay(Board board) {
		Position position = new Position(board.maxIndex());
		position.setAsCorner();// Choose a corner
		//position.setRandom();// Jerk mode
		// todo: mix it up: corners half the time, sides and center a quarter each
		return position;
	}

	private Position secondPlay(Board board) {
		Position position = new Position(board.maxIndex());
		Iterator<Play> iterator = board.getPlays().iterator();
		Position opponentPos = iterator.next().position();
		// If corner, chose center
		if (opponentPos.isCorner()) {
			System.out.println("Choosing center");
			position.setAsCenter();
		}
		// Else if center, chose corner
		else if (opponentPos.isCenter()) {
			System.out.println("Choosing corner");
			position.setAsCorner();
		}
		// Else side, chose adjacent corner
		else if(opponentPos.isSide()) {
			System.out.println("Choosing adjacent corner");
			position.setAsAdjCorner(opponentPos);// todo: finish evaluating scenarios, make sure this is best move
		}
		return position;
	}

	private Position buildTriple(Board board) {
		return board.findDouble(playerValue);
	}
	private Position blockTriple(Board board) {
		return board.findDouble(-playerValue);
	}
	private Position buildSplit(Board board) {
		return board.findSinglesIntersections(playerValue).get(0);// If more than one intersection, any one of them will do
	}
	// It is important that if this double is blocked, it will not complete the opponent's split.
	// todo: be sure that the open square of the the double is NOT the opponent's split position (this can happen with scenario B6)
	private Position buildDouble(Board board) {
		Set<Position> mySingles = board.findSingles(playerValue);
		Set<Position> hisSplits = board.findSinglesIntersections(-playerValue);
		return mySingles.removeAll(hisSplits);
		Position position = null;
		return position;
	}
	private Position blockSplit(Board board) {
		Position position = null;
		return position;
	}
	private Position blockDouble(Board board) {
		Position position = null;
		return position;
	}
	private Position random(Board board) {
		Position position = null;
		return position;
	}

}
