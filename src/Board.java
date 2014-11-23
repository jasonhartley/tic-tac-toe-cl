import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class Board {
	public static final int MIN_SIZE = 3;
	public static final int MAX_SIZE = 30;

	private int[][] board;
	private int[] laneSum;// rows: 0-2, cols: 3-5, diagSlash: 6, diagBackslash: 7
	private int[] rowSum, colSum, rowCount, colCount;
	private int size, diagSlashSum, diagBackslashSum, diagSlashCount, diagBackslashCount;

	public Board(int size) {
		this.size = size > MAX_SIZE ? MAX_SIZE : size;
		board = new int[size][size];
		rowSum = new int[size];
		colSum = new int[size];

		// laneSum points to the other sums, but is now easy to iterate over
		for (int i = 0; i < size; i++) {
			laneSum[i] = rowSum[i];
			laneSum[size + i] = colSum[i];
		}
		laneSum[size * 2 + 1] = diagSlashSum;
		laneSum[size * 2 + 2] = diagBackslashSum;
	}

	private int get(int row, int col) {
		if (isValidValue(row, col)) {
			return board[row][col];
		}
		else {
			return Value.INVALID;
		}
	}

	public int get(Position position) {
		return board[position.row()][position.col()];
	}

	public int getCenter() {
		if (size % 2 == 0) {
			return Value.INVALID;// Board has an even number of rows/cols with no center
		}
		else {
			return board[size / 2][size /2];
		}
	}

	// todo: time complexity is O(n^2), perhaps find a faster way
	public Set<Play> getPlays() {
		Set<Play> plays = new TreeSet<Play>();
		for (int row = 0; row < size; row++) {
			for(int col = 0; col < size; col++) {
				if (!isOpenPosition(row, col)) {
					plays.add(new Play(new Position(row, col, size - 1), board[row][col]));
				}
			}
		}
		return plays;
	}

	public int put(Play play) {
		if (isValidPlay(play)) {
			int row, col, value;
			row = play.position().row();
			col = play.position().col();
			value = play.playerVal();
			Position pos = play.position();
			board[row][col] = value;
			rowSum[row] += value;
			colSum[col] += value;
			rowCount[row]++;
			colCount[col]++;
			if (pos.isDiagSlash()) {
				diagSlashSum += value;
				diagSlashCount++;
			}
			if (pos.isDiagBackslash()) {
				diagBackslashSum += value;
				diagBackslashCount++;
			}


			return outcome(play);
		}
		else {
			return Value.INVALID;
		}
	}

	// Output a position
	public String show(Position position) {
		return show(position.row(), position.col());
	}
	public String show(int row, int col) {
		return Value.show(board[row][col]);
	}

	// Output the board
	public void show() {
		for (int j = 0; j < size; j++) {
			System.out.print("----");
		}
		System.out.print("-\n");
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				System.out.print("| " + show(i, j) + " ");
			}
			System.out.print("|\n");
			for (int j = 0; j < size; j++) {
				System.out.print("----");
			}
			System.out.print("-\n");
		}
	}

	private int outcome(Play play) {
		int row = play.position().row();
		int col = play.position().col();
		int player = play.playerVal();

		if (rowSum[row] == player * size ||
				colSum[col] == player * size ||
				diagBackslashSum == player * size ||
				diagSlashSum == player * size) {
			return play.playerVal();
		}
		else {
			return Value.NONE;
		}
	}

	private boolean isValidValue(int row, int col) {
		return (row >= 0 && row < size && col >= 0 && col < size);
	}

	private boolean isOpenPosition(int row, int col) {
		return board[row][col] == Value.NONE;
	}

	public boolean isOpenPosition(Position position) {
		return get(position) == Value.NONE;
	}

	private boolean isValidPlay(Play play) {
		return get(play.position()) == Value.NONE;
		// todo: maybe ensure that play.player() has 1 less play on the board than the other player
	}

	private boolean isValidPlayer(int player) {
		return (player == Value.X || player == Value.O);
	}

	public int size() {
		return size;
	}

	public int maxIndex() {
		return size - 1;
	}

	public Position anyOpenAdjacent(Position position) {
		int row = position.row();
		int col = position.col();
		int max = position.max();

		// Try above
		if (row > 0 && isOpenPosition(row - 1, col)) return new Position(row - 1, col, max);
		// Try to right
		else if (col < max && isOpenPosition(row, col + 1)) return new Position(row, col + 1, max);
		// Try below
		else if (row < max && isOpenPosition(row + 1, col)) return new Position(row + 1, col, max);
		// Try to left
		else if (col > 0 && isOpenPosition(row, col - 1)) return new Position(row, col - 1, max);
		else return new Position();// Nothing adjacent, so an invalidated position is returned
	}

	// Returns the open position corresponding to the first double found
	public Position findDouble(int playerValue) {
		for (int i = 0; i < size; i++) {
			if (rowSum[i] == size * playerValue - 1) {
				// Now find which square in that row
				for (int j = 0; j < size; j++) {
					if (isOpenPosition(i, j)) {
						return new Position(i, j, size - 1);
					}
				}
			}
			if (colSum[i] == size * playerValue -1 ) {
				// Now find which square in that col
				for (int j = 0; j < size; j++) {
					if (isOpenPosition(j, i)) {
						return new Position(j, i, size - 1);
					}
				}
			}
		}
		if (diagSlashSum == size * playerValue - 1) {
			for (int i = 0; i < size; i++) {
				int j = size - 1 - 1;
				if (isOpenPosition(i, j)) {
					return new Position(i, j, size - 1);
				}
			}
		}
		if (diagBackslashSum == size * playerValue - 1) {
			for (int i = 0; i < size; i++) {
				if (isOpenPosition(i, i)) {
					return new Position(i, i, size - 1);
				}
			}
		}
		return null;
	}

	public List<Integer> findSingles(int playerValue) {
		List<Integer> lanes = new ArrayList<Integer>();
		// Find all the lane sums that equal the player value and have only one entry
		for (int i = 0; i < size; i++) {
			if (rowSum[i] == playerValue && rowCount[i] == 1) lanes.add(i);
			if (colSum[i] == playerValue && colCount[i] == 1) lanes.add(i + size);
		}
		if (diagSlashSum == playerValue && diagSlashCount == 1) lanes.add(size * 2 + 1);
		if (diagBackslashSum == playerValue && diagBackslashCount == 1) lanes.add(size * 2 + 2);

		return lanes;
	}

	// Returns the position where two singles intersect
	// note: It is possible to have more than one singles intersection, but since occupying one will guarantee a win on
	//       the next play, choosing the first one we find will do.
	public List<Position> findSinglesIntersections(int playerValue) {
		List<Position> intersections = new ArrayList<Position>();
		List<Integer> lanes = findSingles(playerValue);
		int slashIndex = size * 2;
		int backslashIndex = size * 2 + 1;
		int count = lanes.size();
		int last = count - 1;
		int next, row, col;

		for (int i = 0; i < count; i ++) {
			row = Value.INVALID;
			col = Value.INVALID;

			// If we are looking at the last index, then 'next' loops back to the beginning and is 0
			next = i < last ? i + 1 : 0;
			int j = lanes.get(next);

			// If it's a row
			if (i < size) {
				row = lanes.get(i);
				// If next is a column
				if (j < size * 2) {
					col = j;
				}
				// If next is a diagonal
				else {
					// If next is slash
					if (j == slashIndex) {
						col = size - row - 1;
					}
					// If next is backslash
					else if (j == backslashIndex) {
						col = row;
					}
					else {
						System.out.println("Error.  Index out of expected bounds.");
					}
				}
			}
			// If it's a column
			else if (i < size * 2) {
				col = lanes.get(i);
				// If next is a row
				if (j < size) {
					row = j;
				}
				// If next is a diagonal
				else {
					// If next is slash
					if (j == slashIndex) {
						row = size - col - 1;
					}
					// If next is backslash
					else if (j == backslashIndex) {
						row = col;
					}
					else {
						System.out.println("Error.  Index out of expected bounds.");
					}
				}
			}
			// If it's a diagonal
			else if (i <= backslashIndex) {
				// If next is a row
				if (j < size) {
					row = j;
					// If i is slash
					if (i == slashIndex) {
						col = size - row - 1;
					}
					// If i is backslash
					else if (j == backslashIndex) {
						col = row;
					}
				}
				// If next is a column
				else if (j < size * 2) {
					col = j;
					// If i is slash
					if (j == slashIndex) {
						row = size - col - 1;
					}
					// If i is backslash
					else if (j == backslashIndex) {
						row = col;
					}
				}
			}
			else {
				System.out.println("Error.  Index out of expected bounds.");
			}

			// Just to double check
			if (row != Value.INVALID && col != Value.INVALID && isOpenPosition(row, col)) {
				intersections.add(new Position(row, col, size - 1));
			} else {
				System.out.println("Error: Board.findSinglesIntersection()");
			}
		}

		return intersections;
	}
}
