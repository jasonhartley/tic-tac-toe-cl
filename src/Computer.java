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
		Position tactic(Board0 board);
	}

	Tactic[] tactics = new Tactic[] {
			new Tactic() { public Position tactic(Board0 b) { return buildTriple(b); } },
			new Tactic() { public Position tactic(Board0 b) { return blockTriple(b); } },
			new Tactic() { public Position tactic(Board0 b) { return buildSplit(b); } },
			new Tactic() { public Position tactic(Board0 b) { return buildDouble(b); } },
			new Tactic() { public Position tactic(Board0 b) { return blockSplit(b); } },
			new Tactic() { public Position tactic(Board0 b) { return blockDouble(b); } },
			new Tactic() { public Position tactic(Board0 b) { return random(b); } },
	};

	public int value() {
		return playerValue;
	}

	public String show() {
		return Value.show(playerValue);
	}

	public Play getPlay(Board0 board) {
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

		// If there are two or more moves
		else {
			position = generalStrategy(board);
		}

		return new Play(position, playerValue);
	}

	private Position generalStrategy(Board0 board) {
		Position position = null;

		// Try each tactic in order.  Return once one works.
		for (int i = 0; position == null; i++) {
			position = tactics[i].tactic(board);
		}
		
		return position;
	}

	private Position firstPlay(Board0 board) {
		Position position = new Position(board.maxIndex());
		position.setAsCorner();// Choose a corner
		//position.setRandom();// Jerk mode
		// todo: mix it up: corners half the time, sides and center a quarter each
		return position;
	}

	private Position secondPlay(Board0 board) {
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

	private Position buildTriple(Board0 board) {
		return board.findDouble(playerValue);
	}
	private Position blockTriple(Board0 board) {
		return board.findDouble(-playerValue);
	}
	private Position buildSplit(Board0 board) {
		return board.findSinglesIntersections(playerValue).get(0);// If more than one intersection, any one of them will do
	}
	// It is important that if this double is blocked, it will not complete the opponent's split.
	// todo: be sure that the open square of the the double is NOT the opponent's split position (this can happen with scenario B6)
	private Position buildDouble(Board0 board) {
		Set<Position> mySingles = board.findSingles(playerValue);
		Set<Position> hisSplits = board.findSinglesIntersections(-playerValue);
		return mySingles.removeAll(hisSplits);
	}
	private Position blockSplit(Board0 board) {
		return board.findSinglesIntersections(-playerValue).get(0);
	}
	private Position blockDouble(Board0 board) {
		return board.findSingles(-playerValue).get(0);
	}
	private Position random(Board0 board) {
		return board.findRandom();
	}

}
