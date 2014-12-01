import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class Board {
	private int size, ordinalCount, laneCount, playerCount;

	List<Lane> lanes = new ArrayList<Lane>();
	List<Set<Integer>> laneIndexesByOrdinal = new ArrayList<Set<Integer>>();

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
			System.out.println(lanes.get(laneIndex).openSet());

			// Add this lane index to the list of lanes by ordinal
			for (int ordinal : lanes.get(laneIndex).openSet()) {
				laneIndexesByOrdinal.get(ordinal).add(laneIndex);
			}
		}
	}

	public boolean put(int ordinal, int playerValue) {
		Set<Integer> lanesToUpdate = laneIndexesByOrdinal.get(ordinal);
		for (int laneIndex : lanesToUpdate) {
			if (lanes.get(laneIndex).put(playerValue, ordinal)) {
				return true;
			}
		}
		return false;
	}

	public void show() {
		List<Integer> serial = new ArrayList<Integer>();
		// Create a serialized version of the board using the row lanes
		for (int laneIndex = 0; laneIndex < size; laneIndex++) {
			serial.addAll(lanes.get(laneIndex).getList());
		}
		// Print the board  todo: make this prettier
		for (int ordinal = 0; ordinal < ordinalCount; ordinal++) {
			if (ordinal % size == 0) System.out.println();
			System.out.println(" " + serial.get(ordinal) + " ");
		}
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
		return findDoubles(playerValue).iterator().next();// todo: not sure what happens if the set is empty
	}

	// Returns a set of all open ordinals where a lane contains the given player value once with the rest open
	private Set<Integer> findSingles(int playerValue) {
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

		return singlesIntersections;
	}

	// A test for inside the constructor
/*
		for (int ordinal = 0; ordinal < ordinalCount; ordinal++) {
			System.out.println("ord " + ordinal + ": " +laneIndexesByOrdinal.get(ordinal));
		}

		int playerValue = 1;
		for (int ordinal = 0; ordinal < ordinalCount; ordinal++) {
			// Method: boolean put(int ordinal)
			Set<Integer> lanesToUpdate = laneIndexesByOrdinal.get(ordinal);
			for (int laneIndex : lanesToUpdate) {
				lanes.get(laneIndex).put(playerValue, ordinal);
			}
			playerValue %= 2;
			playerValue++;
		}
		for (int j = 1; j <= 2; j++) {
			for (int i = 0; i < size; i++) {
				System.out.println(lanes.get(i).getTaken(j));
			}
		}
*/

}
