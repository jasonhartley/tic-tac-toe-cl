import java.util.*;

public class Board {
	public static final int MIN_SIZE = 3;
	public static final int MAX_SIZE = 30;

	private int size, ordinalCount, laneCount, playerCount;

	private List<Lane> lanes = new ArrayList<Lane>();
	private List<Set<Integer>> laneIndexesByOrdinal = new ArrayList<Set<Integer>>();
	private Set<Integer> sideIndexes = new TreeSet<Integer>();

	public Board(int size) {
		this.size = size;
		ordinalCount = size * size;
		laneCount = size * 2 + 2;// rows, columns, and 2 diagonals
		playerCount = 2;// todo: make this a parameter

		// Fill the list with open sets
		while (laneIndexesByOrdinal.size() < ordinalCount) {
			laneIndexesByOrdinal.add(new TreeSet<Integer>());
		}

		// Create all the lanes
		for (int laneIndex = 0; laneIndex < laneCount; laneIndex++ ) {
			// Create the lane
			lanes.add(new Lane(laneIndex, size, playerCount));
			System.out.println(lanes.get(laneIndex).getOpen());

			// Add this lane index to the list of lanes by ordinal
			for (int ordinal : lanes.get(laneIndex).getOpen()) {
				laneIndexesByOrdinal.get(ordinal).add(laneIndex);
			}
		}

		sideIndexes.add(0);
		sideIndexes.add(size - 1);
		sideIndexes.add(size);
		sideIndexes.add(size * 2 - 1);
	}

	// Returns true if is a winning play
	public boolean put(int ordinal, int playerValue) {
		Set<Integer> lanesToUpdate = laneIndexesByOrdinal.get(ordinal);
		for (int laneIndex : lanesToUpdate) {
			// If a winning play, immediately return true
			if (lanes.get(laneIndex).put(playerValue, ordinal)) {
				//playCount++;// todo: where to put playCount increment?
				return true;
			}
		}
		return false;
	}

	public void show() {
		int ordinal, playerValue;
		String playerString;
		Set<Integer> set = new TreeSet<Integer>();

		for (int row = 0; row < 3; row++) {
			for (int col = 0; col < 3; col++) {
				ordinal = row * size + col;
				playerValue = lanes.get(row).getPlayerValue(ordinal).iterator().next();
				if (playerValue == 0) {
					playerString = String.valueOf(ordinal + 1);// ordinal displayed starts at 1, not 0
				}
				else {
					playerString = getPlayerString(playerValue);
				}
				System.out.print(" " + playerString + " ");
			}
			System.out.println();
		}
		System.out.println();
	}

	// Return a set of ordinals that represent the four corners of the board
	public Set<Integer> corners() {
		Set<Integer> corners = new TreeSet<Integer>();
		corners.add(0);
		corners.add(size - 1);
		corners.add(size * size - size);
		corners.add(size * size - 1);
		return corners;
	}

	public Set<Integer> center() {
		Set<Integer> center = new TreeSet<Integer>();
		// Only odd size boards have a center position
		if (size % 2 == 1) {
			center.add(size * size / 2);
		}
		return center;
	}

	public Set<Integer> sides() {
		Set<Integer> sides = new TreeSet<Integer>();
		for (int sideIndex : sideIndexes) {
			sides.addAll(lanes.get(sideIndex).getAll());
		}
		sides.removeAll(corners());

		return sides;
	}

	public Set<Integer> openCorners() {
		Set<Integer> openCorners = new TreeSet<Integer>(corners());
		openCorners.retainAll(findOpen());
		return openCorners;
	}

	public int anOpenCorner() {
		return randomElement(openCorners());
	}

	public int anOpenAdjacentCorner(int ordinal) {
		Set<Integer> laneIndex = laneIndexesByOrdinal.get(ordinal);// Should return a set of 2 or 3
		laneIndex.retainAll(sideIndexes);// Should now be a set of 1 if ordinal was a side, 0 if not a side
		Set<Integer> ordinals = lanes.get(laneIndex.iterator().next()).getAll();// The ordinals in that side lane  todo: what if laneIndex is empty?
		ordinals.retainAll(corners());// Should now be two ordinals

		return randomElement(ordinals);
	}

	// Returns a set of ordinal where all other ordinals in at least one of its lanes are occupied by the given player value.
	// Example: If a lane has ordinals {0, 1, 2} and player 1 has {0, 2}, and {1} is open, the method returns {1}.
	public Set<Integer> findDoubles(int playerValue) {
		Set<Integer> ordinals = new TreeSet<Integer>();
		for (int i = 0; i < laneCount; i++) {
			ordinals.addAll(lanes.get(i).getDouble(playerValue));
		}
		return ordinals;
	}

	public int findADouble(int playerValue) {
		Set<Integer> set = findDoubles(playerValue);
		if (set.iterator().hasNext()) {
			return set.iterator().next();
		}
		else {
			return -1;
		}
	}

	// Returns a set of all open ordinals where a lane contains the given player value once with the rest open
	public Set<Integer> findSingles(int playerValue) {
		Set<Integer> singles = new TreeSet<Integer>();
		for (int i = 0; i < laneCount; i++) {
			singles.addAll(lanes.get(i).getSingle(playerValue));
		}
		return singles;
	}

	public int findASingle(int playerValue) {
		return findSingles(playerValue).iterator().next();// todo: what if set is empty?
	}

	// Returns a set of open ordinals where singles lanes intersect for a given player value
	// Example 1: Given the following board:
	//   X | 1 | O
	//   3 | 4 | 5
	//   6 | 7 | X
	//   Player X has two singles: row 2 and column 3, which intersect at ordinal 6, returning {6}
	// Example 2:
	//   X | 1 | 2
	//   3 | O | 5
	//   6 | 7 | X
	//   Player X has four singles: rows 0 & 2, and columns 3 & 5, which intersect at ordinals 2 and 6, returning {2,6}
	public Set<Integer> findSinglesIntersections(int playerValue) {
		Set<Integer> singlesIntersections = new TreeSet<Integer>();
		Set<Integer> tempIntersection;

		// Prep work: Union of open ordinals of rows that are singles
		Set<Integer> rows = new TreeSet<Integer>();
		for (int i = 0; i < size; i++) {
			rows.addAll(lanes.get(i).getSingle(playerValue));
		}
		// Prep work: Union of open ordinals of columns that are singles
		Set<Integer> cols = new TreeSet<Integer>();
		for (int i = size; i < size * 2; i++) {
			cols.addAll(lanes.get(i).getSingle(playerValue));
		}
		// Prep work: Get the open ordinals of the diagonals that are singles
		Set<Integer> diag = lanes.get(size * 2).getSingle(playerValue);
		Set<Integer> anti = lanes.get(size * 2 + 1).getSingle(playerValue);
		// Add intersection of rows and columns
		tempIntersection = new TreeSet<Integer>(rows);
		tempIntersection.retainAll(cols);
		singlesIntersections.addAll(tempIntersection);
		// Add intersection of rows and diagonal
		tempIntersection = new TreeSet<Integer>(rows);
		tempIntersection.retainAll(diag);
		singlesIntersections.addAll(tempIntersection);
		// Add intersection of rows and anti-diagonal
		tempIntersection = new TreeSet<Integer>(rows);
		tempIntersection.retainAll(anti);
		singlesIntersections.addAll(tempIntersection);
		// Add intersection of columns and diagonal
		tempIntersection = new TreeSet<Integer>(cols);
		tempIntersection.retainAll(diag);
		singlesIntersections.addAll(tempIntersection);
		// Add intersection of columns and anti-diagonal
		tempIntersection = new TreeSet<Integer>(cols);
		tempIntersection.retainAll(anti);
		singlesIntersections.addAll(tempIntersection);
		// Add intersection of diagonal and anti-diagonal
		tempIntersection = new TreeSet<Integer>(diag);
		tempIntersection.retainAll(anti);
		singlesIntersections.addAll(tempIntersection);

		// todo: when playerValue is negative, loop through all the other players and return a singlesIntersections
		// set as soon as a playerValue produces a non-empty set.  Reason: This is used for blocking a possible
		// split and since a player can only block one at a time, we'll just return the first one we find.

		return singlesIntersections;
	}

	public int findASinglesIntersection(int playerValue) {
		Set<Integer> set = findSinglesIntersections(playerValue);
		if (set.iterator().hasNext()) {
			return set.iterator().next();
		}
		else {
			return -1;
		}
	}

	public Set<Integer> findOpen() {
		Set<Integer> open = new TreeSet<Integer>();
		for (int i = 0; i < size; i++) {
			open.addAll(lanes.get(i).getOpen());
		}
		return open;
	}

	public int findAnOpen(int playerValue) {
		return randomElement(findOpen());
	}

	public boolean isOpen(int ordinal) {
		return findOpen().contains(ordinal);
	}

	public Set<Integer> findTaken() {
		Set<Integer> taken = new TreeSet<Integer>();
		for (int i = 0; i < size; i++) {
			taken.addAll(lanes.get(i).getTaken());
		}
		return taken;
	}

	public int size() {
		return size;
	}

	public int playCount() {
		int count = 0;
		// Only loop through the rows
		for (int i = 0; i < size; i++) {
			count += lanes.get(i).getTaken().size();
		}
		return count;
	}

	private int randomElement(Set<Integer> set) {
		Random r = new Random();
		int random = r.nextInt(size);
		while (random-- > 0) set.iterator().next();
		return set.iterator().next();
	}

	public String getPlayerString(int playerValue) {
		if (playerValue == 1) return "X";
		if (playerValue == 2) return "O";
		return "A";// todo: make this actually work using the other letters of the alphabet
	}
}
