import java.util.*;

public class Lane {
	private List<Set<Integer>> ordinalSetList;
	private final int OPEN = 0;// 0 means the position is open. Player values start with 1.
	private int size;
	private int playerCount;

	public Lane(int laneIndex, int size, int playerCount) {
		ordinalSetList = new ArrayList<Set<Integer>>();
		this.size = size;
		this.playerCount = playerCount;

		TreeSet<Integer> openSet = new TreeSet<Integer>();
		// Rows
		if (laneIndex < size) {
			for (int i = 0; i < size; i++) {
				openSet.add(laneIndex * size + i);
			}
		}
		// Columns
		else if (laneIndex < size * 2) {
			for (int i = 0; i < size; i++) {
				openSet.add(i * size + laneIndex - size);
			}
		}
		// Main diagonal
		else if (laneIndex < size * 2 + 1) {
			for (int i = 0; i < size; i++) {
				openSet.add(i * size + i);
			}
		}
		// Anti-diagonal
		else if (laneIndex < size * 2 + 2) {
			for (int i = 0; i < size; i++) {
				openSet.add((size - 1) * (i + 1));
			}
		}
		else {
			System.out.println("Error.  Lane index out of bounds.");
		}

		ordinalSetList.add(OPEN, openSet);

		// Initialize the "taken" entries with empty sets
		for (int playerValue = 1; playerValue <= playerCount; playerValue++) {
			ordinalSetList.add(playerValue, new TreeSet<Integer>());
		}

	}

	// Returns true if a winning play
	boolean put(int playerValue, int ordinal) {
		ordinalSetList.get(OPEN).remove(ordinal);
		playerSet(playerValue).add(ordinal);
		// If the number of plays by this player equals the size of the lane, this player wins
		return playerSet(playerValue).size() == size;
	}

	// A union across all player sets - returns the ordinals used in this lane
	Set<Integer> getAll() {
		Set<Integer> all = new TreeSet<Integer>();
		for (Set<Integer> ordinalSet : ordinalSetList) {
			all.addAll(ordinalSet);
		}
		return all;
	}

	// Returns all taken ordinals
	Set<Integer> getTaken() {
		Set<Integer> taken = new TreeSet<Integer>();
		for (int playerValue = 1; playerValue < ordinalSetList.size(); playerValue++) {
			taken.addAll(playerSet(playerValue));
		}
		return taken;
	}

	// Returns a set of one element containing the player value (including 0 for empty) who occupies the given ordinal
	Set<Integer> getPlayerValue(int ordinal) {
		Set<Integer> set = new TreeSet<Integer>();
		for (int playerValue = 0; playerValue < size; playerValue++) {
			if (playerSet(playerValue).contains(ordinal)) {
				set.add(playerValue);
				return set;
			}
		}
		return set;// This should never get called
	}
	
	// If a lane is empty except for one playerValue, return the open ordinals as a set.
	// If playerValue is negative, check all player values OTHER than playerValue.
	Set<Integer> getSingle(int playerValue) {
		Set<Integer> playerValueSet = playerValueSet(playerValue);
		Set<Integer> openSet = getOpen();

		for (int playerValue1 : playerValueSet) {
			if (playerSet(playerValue1).size() == 1 && openSet.size() == size - 1) {
				return getOpen();
			}
		}

		return new TreeSet<Integer>();// Empty set
	}

	// If lane is occupied entirely by playerValue except for one open ordinal, return that open ordinal as a set,
	// otherwise return an empty set.
	// If playerValue is negative, check all player values OTHER than playerValue.
	Set<Integer> getDouble(int playerValue) {
		Set<Integer> playerValueSet = playerValueSet(playerValue);
		Set<Integer> openSet = getOpen();

		for (int playerValue1 : playerValueSet) {
			if (playerSet(playerValue1).size() == size - 1 && openSet.size() == 1) {
				return openSet;// Should only have 1 element
			}
		}

		return new TreeSet<Integer>();// Empty set
	}

	Set<Integer> getOpen() {
		return ordinalSetList.get(OPEN);
	}

	private Set<Integer> playerSet(int playerValue) {
		return ordinalSetList.get(playerValue);
	}

	private Set<Integer> playerValueSet(int playerValue) {
		Set<Integer> playerValueSet = new TreeSet<Integer>();

		if (playerValue < 0) {
			for (int i = 1; i <= playerCount; i++) {
				playerValueSet.add(i);
			}
			playerValueSet.remove(-playerValue);// playerValue is the only element NOT in the set
		}
		else {
			playerValueSet.add(playerValue);// playerValue is the only element in the set
		}

		return playerValueSet;
	}
}
