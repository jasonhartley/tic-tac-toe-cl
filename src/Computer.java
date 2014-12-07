import java.util.Set;

public class Computer implements Player {
	private int playerValue;

	public Computer(int playerValue) {
		this.playerValue = playerValue;
	}

	interface Tactic {
		int tactic(Board board);
	}

	Tactic[] tactics = new Tactic[] {
			new Tactic() { public int tactic(Board b) { return buildTriple(b); } },
			new Tactic() { public int tactic(Board b) { return blockTriple(b); } },
			new Tactic() { public int tactic(Board b) { return buildSplit(b); } },
			new Tactic() { public int tactic(Board b) { return buildDouble(b); } },
			new Tactic() { public int tactic(Board b) { return blockSplit(b); } },
			new Tactic() { public int tactic(Board b) { return blockDouble(b); } },
			new Tactic() { public int tactic(Board b) { return random(b); } },
	};

	public int value() {
		return playerValue;//todo: maybe unnecessary
	}

	public String show() {
		return String.valueOf(playerValue);// todo: get this working
	}

	public int getPlay(Board board) {
		// If board is empty (I am X, first move)
		if (board.playCount() == 0) {
			return firstPlay(board);
		}
		// If there is one move (I am O, second move)
		else if (board.size() == 1) {
			return secondPlay(board);
		}
		// If there are two moves (I am X, third movie)

		// If there are two or more moves
		else {
			return generalStrategy(board);
		}
	}

	private int generalStrategy(Board board) {
		int ordinal = -1;// If -1 is returned, something went wrong because a suitable tactic should have been found

		// Try each tactic in order.  Return once one works.
		for (int i = 0; i < tactics.length && ordinal == -1; i++) {
			ordinal = tactics[i].tactic(board);
		}
		
		return ordinal;
	}

	private int firstPlay(Board board) {
		return board.anOpenCorner();
		//return board.findOpen();// Jerk mode
		// todo: mix it up: corners half the time, sides and center a quarter each
	}

	private int secondPlay(Board board) {
		int taken = board.findTaken().iterator().next();// There should only be one

		if (isCorner(board, taken)) {
			return board.center().iterator().next();
		}
		else if (isCenter(board, taken)) {
			return board.anOpenCorner();// This is slower than taking the first open corner, but is more interesting
		}
		else if (isSide(board, taken)) {
			// Far side and far corner can result in a loss <-- proven, so don't use
			// Adjacent and center can win <-- todo: prove that there is no possible loss
			// Note: If O's first move is a corner, O's second move should be the center, and vice versa
			return board.anOpenAdjacentCorner(taken);
		}
		else {
			// The board size is >3 and the taken position is neither corner, center, nor side
			return -1;// todo: develop a strategy for larger boards
		}

	}

	private int buildTriple(Board board) {
		return board.findADouble(playerValue);
	}
	private int blockTriple(Board board) {
		return board.findADouble(-playerValue);
	}
	private int buildSplit(Board board) {
		return board.findASinglesIntersection(playerValue);
	}
	// It is important that if this double is blocked, it will not complete the opponent's split.
	// Returns an open ordinal that is a single for the computer, esp. if it is a split position for any opponents
	private int buildDouble(Board board) {
		Set<Integer> mySingles = board.findSingles(playerValue);
		Set<Integer> theirSplits = board.findSinglesIntersections(-playerValue);
		theirSplits.retainAll(mySingles);
		if (theirSplits.size() > 0) {
			return theirSplits.iterator().next();
		}
		else {
			return mySingles.iterator().next();
		}
	}
	private int blockSplit(Board board) {
		return board.findASinglesIntersection(-playerValue);
	}
	private int blockDouble(Board board) {
		return board.findASingle(-playerValue);
	}
	private int random(Board board) {
		return board.findAnOpen(playerValue);
	}

	private boolean isCorner(Board board, int ordinal) {
		return board.corners().contains(ordinal);
	}
	private boolean isCenter(Board board, int ordinal) {
		return board.center().contains(ordinal);
	}
	private boolean isSide(Board board, int ordinal) {
		return board.sides().contains(ordinal);
	}
}
