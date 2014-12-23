import java.util.Set;

public class Computer extends Player {

	public Computer(int playerValue) {
		this.playerValue = playerValue;
	}

	interface Tactic {
		int tactic(Board board);
	}

	// Todo: add different levels of difficulty
	// Expert: Perfect player, tries to win
	// Interesting: Perfect player, random openings (this implementation)
	// Normal: Sometimes uses strategy, sometimes chooses randomly
	// Easy: Chooses randomly
	// Idiot: Will try to lose.  Can you force him to win?

	Tactic[] tactics = new Tactic[] {
			new Tactic() { public int tactic(Board b) { return firstPlay(b); } },
			new Tactic() { public int tactic(Board b) { return secondPlay(b); } },
			new Tactic() { public int tactic(Board b) { return buildTriple(b); } },
			new Tactic() { public int tactic(Board b) { return blockTriple(b); } },
			new Tactic() { public int tactic(Board b) { return buildSplit(b); } },
			new Tactic() { public int tactic(Board b) { return buildDouble(b); } },
			new Tactic() { public int tactic(Board b) { return blockSplit(b); } },
			new Tactic() { public int tactic(Board b) { return blockDouble(b); } },
			new Tactic() { public int tactic(Board b) { return random(b); } },
	};

	/**
	 * Gets a play from a computer player in the form of an ordinal position on the board
	 *
	 * @param   board    the board
	 * @return  ordinal  the ordinal position on the board chosen my the computer as its play
	 */
	@Override
	public int getPlay(Board board) {
		int ordinal = -1;// If -1 is returned, something went wrong because a suitable tactic should have been found

		// Try each tactic in order.  Return once one works.
		for (int i = 0; i < tactics.length && ordinal == -1; i++) {
			ordinal = tactics[i].tactic(board);
		}

		return ordinal;
	}

	private int firstPlay(Board board) {
		if (board.playCount() == 0) {

			// Interesting mode - less likely to win, but still certain to draw
			int random = Util.randomInt(3);
			if (random == 0) {
				return board.center().iterator().next();
			}
			else if (random == 1) {
				return Util.randomElement(board.corners());
			}
			else if (random == 2) {
				return Util.randomElement(board.sides());
			}
			// This is the default case but should never be called
			else {
				return board.findAnOpen();
			}

			// Expert mode
			//return board.anOpenCorner();
		}
		else {
			return -1;
		}
	}

	private int secondPlay(Board board) {
		System.out.println("secondPlay");
		if (board.playCount() == 1) {
			return buildSingle(board);
		}
		else {
			return -1;
		}
	}

	private int buildTriple(Board board) {
		System.out.println("buildTriple");
		return board.findADouble(playerValue);
	}

	private int blockTriple(Board board) {
		System.out.println("blockTriple");
		return board.findADouble(-playerValue);
	}

	private int buildSplit(Board board) {
		System.out.println("buildSplit");
		return board.findASinglesIntersection(playerValue);
	}

	// Returns an open ordinal from a lane that is a single for the computer.
	// Prefers ordinals that will block an opponent's split,
	// or ordinals that will block an opponent's single (in that order).
	private int buildDouble(Board board) {
		System.out.println("buildDouble");
		Set<Integer> mySingles = board.findSingles(playerValue);
		Set<Integer> theirSplits = board.findSinglesIntersections(-playerValue);
		Set<Integer> theirSingles = board.findSingles(-playerValue);

		theirSplits.retainAll(mySingles);
		theirSingles.retainAll(mySingles);

		if (theirSplits.size() > 0) {
			return theirSplits.iterator().next();
		}
		else if (theirSingles.size() > 0) {
			return theirSingles.iterator().next();
		}
		else if (mySingles.size() > 0) {
			return Util.randomElement(mySingles);
		}
		else {
			return -1;
		}
	}
	private int blockSplit(Board board) {
		System.out.println("blockSplit");
		return board.findASinglesIntersection(-playerValue);
	}

	private int blockDouble(Board board) {
		System.out.println("blockDouble");
		return board.findASingle(-playerValue);
	}

	private int buildSingle(Board board) {
		System.out.println("buildSingle playCount: " + board.playCount());

		int taken = board.findTaken().iterator().next();// There should only be one

		if (isCorner(board, taken)) {
			return board.center().iterator().next();
		}
		else if (isCenter(board, taken)) {
			return board.anOpenCorner();
		}
		else if (isSide(board, taken)) {
			// Far side and far corner can result in a loss <-- proven, so don't use
			// Adjacent and center can win <-- todo: prove that there is no possible loss
			return board.anOpenAdjacentCorner(taken);
		}
		else {
			// The board size is >3 and the taken position is neither corner, center, nor side
			return -1;// todo: develop a strategy for larger boards
		}
	}

	private int random(Board board) {
		System.out.println("random");
		return board.findAnOpen();
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
