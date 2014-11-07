import java.io.*;

public class Ttt {

	public static class Player {
		public static int X = 1;
		public static int O = -1;
		public static int NONE = 0;
		public static int INVALID = -2;
		public static int DRAW = 2;
		public static String HUH = "?";

		public static String show(int p) {
			if (p == X) return "X";
			else if (p == O) return "O";
			else if (p == NONE) return " ";
			else return "?";
		}
	}

	public static class Pos { // todo: I think it might be time to break the classes up into files
		public static int row;
		public static int col;
	}

	public static class Board {
		private int[][] board;
		private int[] rowPlays, colPlays;
		private int size, diagSlash, diagBackslash;
		private static final int MIN_SIZE = 3;
		private final int MAX_SIZE = 30;
		private static final int INVALID = -2;

		public Board() {
			this(MIN_SIZE);
		}

		public Board(int n) {
			size = n > MAX_SIZE ? MAX_SIZE : n;
			board = new int[size][size];
			rowPlays = new int[size];
			colPlays = new int[size];
		}

		public int get(int row, int col) {
			if (isValidValue(row, col)) {
				return board[row][col];
			}
			else {
				return INVALID;
			}
		}

		public String show(int row, int col) {
			if (isValidValue(row, col)) {
				return Player.show(board[row][col]);
			}
			else {
				return Player.HUH;
			}
		}

		public int put(int row, int col, int player) {
			if (isValidPlay(row, col) && isValidPlayer(player)) {
				board[row][col] = player;
				if (isWinnerWinnerChickenDinner(row, col, player)) {
					return player;
				}
				else {
					return Player.NONE;
				}
			}
			else {
				return Player.INVALID;
			}

		}

		private boolean isWinnerWinnerChickenDinner(int row, int col, int player) {
			rowPlays[row] += player;
			colPlays[col] += player;
			if (row == col) diagBackslash += player;
			if (row == size - 1 - col) diagSlash += player;
			// todo: Handle draw
			return (rowPlays[row] == player * size ||
					colPlays[col] == player * size ||
					diagBackslash == player * size ||
					diagSlash == player * size);
		}

		private boolean isValidValue(int row, int col) {
			return (row >= 0 && row < size && col >= 0 && col < size);
		}

		private boolean isValidPlay(int row, int col) {
			return (get(row, col) == Player.NONE);
		}

		private boolean isValidPlayer(int player) {
			return (player == Player.X || player == Player.O);
		}

		public int size() {
			return size;
		}

	}

	public static void main(String[] args) {
		// The size of the board, default to 3
		final int n = args.length < 1 ? Board.MIN_SIZE : Integer.parseInt(args[0]);
		Board board = new Board(n);// Board state
		int winner = Player.NONE;// Player # of winner
		int currentPlayer = Player.X;// Player # of whose turn it is

		while (winner == Player.NONE) {
			showBoard(board);
			winner = getTurn(board, currentPlayer);
			currentPlayer *= -1;
		}

		showBoard(board);
		System.out.println("Winner: " + Player.show(winner));
	}

	private static int getTurn(Board board, int player) {
		int size = board.size();
		String inputRow, inputCol;
		int row = -1;
		int col = -1;// Todo: do I really need to initialize these?

		// If human player
		if (player == 1) {

		}
		// If computer
		else {

		}

		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			do {
				do {
					System.out.print("Enter row: ");
					inputRow = br.readLine();
					row = Integer.valueOf(inputRow);
				} while (row < 0 || row >= size);

				do {
					System.out.print("Enter column: ");
					inputCol = br.readLine();
					col = Integer.valueOf(inputCol);
				} while (col < 0 || col >= size);
				System.out.println(row + ":" + col + ": " + board.get(row, col));
			} while (board.get(row, col) != Player.NONE);

		} catch (IOException io) {
			io.printStackTrace();
		}

		// Put the play on the board
		return board.put(row, col, player);
	}

	private static void showBoard(Board board) {
		int n = board.size();
		for (int j = 0; j < n; j++) {
			System.out.print("----");
		}
		System.out.print("-\n");
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				System.out.print("| " + board.show(i, j) + " ");
			}
			System.out.print("|\n");
			for (int j = 0; j < n; j++) {
				System.out.print("----");
			}
			System.out.print("-\n");
		}
	}
}
