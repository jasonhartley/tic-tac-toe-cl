import java.util.Set;
import java.util.TreeSet;

public class Board {
	public static final int MIN_SIZE = 3;
	public static final int MAX_SIZE = 30;

	private int[][] board;
	private int[] rowPlays, colPlays;
	private int size, diagSlashPlays, diagBackslashPlays;

	public Board(int size) {
		this.size = size > MAX_SIZE ? MAX_SIZE : size;
		board = new int[size][size];
		rowPlays = new int[size];
		colPlays = new int[size];
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
		System.out.println("Board.get() pos: " + position.show());
		return board[position.row()][position.col()];
	}

	// todo: time complexity is O(n^2), perhaps find a faster way
	public Set<Play> getPlays() {
		Set<Play> plays = new TreeSet<Play>();
		for (int row = 0; row < size; row++) {
			for(int col = 0; col < size; col++) {
				if (isOccupied(row, col)) {
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
			rowPlays[row] += value;
			colPlays[col] += value;
			if (pos.isDiagBackslash()) diagBackslashPlays += value;
			if (pos.isDiagSlash()) diagSlashPlays += value;

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

		if (rowPlays[row] == player * size ||
				colPlays[col] == player * size ||
				diagBackslashPlays == player * size ||
				diagSlashPlays == player * size) {
			return play.playerVal();
		}
		else {
			return Value.NONE;
		}
	}

	private boolean isValidValue(int row, int col) {
		return (row >= 0 && row < size && col >= 0 && col < size);
	}

	private boolean isOccupied(int row, int col) {
		return board[row][col] != Value.NONE;// todo: maybe combine isOccupied and isValidPosition
	}

	public boolean isValidPosition(Position position) {
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
}
