import java.util.*;

public class Lane {
	private List<Set<Integer>> ordinalSetsList;
	private final int OPEN = 0;// 0 means the position is open. Player values start with 1.
	private int size;

	Lane(int laneIndex, int size, int playerCount) {
		ordinalSetsList = new ArrayList<Set<Integer>>();
		this.size = size;

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

		ordinalSetsList.add(OPEN, openSet);

		// Initialize the "taken" entries with empty sets
		for (int playerValue = 1; playerValue <= playerCount; playerValue++) {
			ordinalSetsList.add(playerValue, new TreeSet<Integer>());
		}

	}

	Set<Integer> getTaken(int playerValue) {
		return playerSet(playerValue);
	}
	
	Set<Integer> getSingle(int playerValue) {
		if (playerSet(playerValue).size() == 1 && openSet().size() == size - 1) {
			return openSet();
		}
		return new TreeSet<Integer>();
	}

	// If lane is occupied entirely by the given player save for one ordinal, return that open ordinal as a set.
	// Otherwise return an empty set.
	Set<Integer> getDouble(int playerValue) {
		Set<Integer> openSet = openSet();
		if (playerSet(playerValue).size() == size - 1 && openSet.size() == 1) {
			return openSet;
		}
		return new TreeSet<Integer>();// Empty set
	}

	// A union across all sets returned as a list: open set and all player sets
	List<Integer> getList() {
		Integer[] all = new Integer[size];
		// Example: if 0 (open) has { 3 } and 1 (player X) has { 4, 5 } then we return [0, 1, 1]
		for (int playerValue = 0; playerValue < ordinalSetsList.size(); playerValue++) {
			Integer[] ordinals = (Integer[]) playerSet(playerValue).toArray();
			for (int i : ordinals) {
				all[i % size] = playerValue;
			}
		}
		return Arrays.asList(all);
	}

	public Set<Integer> openSet() {
		return ordinalSetsList.get(OPEN);
	}

	boolean put(int playerValue, int ordinal) {
		if (ordinalSetsList.get(OPEN).remove(ordinal)) {
			playerSet(playerValue).add(ordinal);
			// If there are <size> plays in this lane, the player wins
			if (playerSet(playerValue).size() == size) {
				return true;
			}
		}
		return false;
	}

	private Set<Integer> playerSet(int playerValue) {
		return ordinalSetsList.get(playerValue);
	}
}
