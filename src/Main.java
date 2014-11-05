import java.util.*;

public class Main {
	private enum BoardType { RANDOM, WIN_HORZ, WIN_VERT, WIN_DIAG1, WIN_DIAG2, UNFINISHED, DRAW }
	private static Random rand = new Random();

    public static void main(String[] args) {

	    // The size of the board, default to 3
		final int n = args.length == 0 ? 3 : Integer.parseInt(args[0]);

	    int[][] board = CreateBoard(n);

	    ShowBoard(board);

	    System.out.println(GameState(board));

    }

	private static String GameState(int[][] board) {
		int n = board.length;

		// Check the rows
		System.out.println("Checking rows...");
		int k;
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n - 1; j++) {
				k = j + 1;
				System.out.print("Looking at ["+i+"]["+j+"]&["+k+"]: ");
				// If not equal, remove that row of interest and move on to the next row
				if (board[i][j] == 0 && board[i][j] != board[i][k]) {
					System.out.print(board[i][j] + " & " + board[i][k] + "Nope, skipping to next row.\n");
					break;
				}
				// We made it to the end of the row and their all still equal
				else if (k == n - 1) {
					System.out.println();
					return "The winner is: " + board[i][j];
				}
				else {
					System.out.print(board[i][j] + ", ");
				}
			}
		}

		// Check the columns
		System.out.println("Checking columns...");
		for (int i = 0; i < n - 1; i++) {
			for (int j = 0; j < n; j++) {
				k = j + 1;
				System.out.print("Looking at ["+j+"]&["+k+"]["+i+"]: ");
				// If not equal, remove that row of interest and move on to the next row
				if (board[j][i] == 0 && board[j][i] != board[k][i]) {
					System.out.print(board[j][i] + " & " + board[k][i] + "Nope, skipping to next column.\n");
					break;
				}
				// We made it to the end of the row and their all still equal
				else if (k == n - 1) {
					System.out.println();
					return "The winner is: " + board[i][j];
				}
				else {
					System.out.print(board[j][i] + ", ");
				}
			}
		}


		return "Hi, mom!";
	}

	private static int[][] CreateBoard(int n, BoardType type) {
		int[][] board = new int[n][n];

		if (type == BoardType.RANDOM) {
			int tileCount = n * n;

			// All the tile positions numbered
			List<Integer> tiles = new ArrayList<Integer>();
			for (int i = 0; i < tileCount; i++) {
				tiles.add(i);
			}

			// Number of plays on the board
			int plays = randomInt(tileCount);
			System.out.println("Plays: " + plays);

			// Put plays on the board
			for (int i = 0; i < plays; i++) {
				int randomIndex = randomInt(tiles.size() - 1);
				int randomTile = tiles.get(randomIndex);
				int row = randomTile / n;
				int col = randomTile % n;
				System.out.println("Random Number: " + randomIndex + ", Random Tile: " + randomTile + ", row: " + row + ", col: " + col);
				board[row][col] = (i % 2) + 1;
				tiles.remove(randomIndex);
			}

		}

		return board;

	}

	private static void ShowBoard(int[][] board) {
		int n = board.length;
		for (int j = 0; j < n; j++) {
			System.out.print("----");
		}
		System.out.print("-\n");
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				System.out.print("| " + board[i][j] + " ");
			}
			System.out.print("|\n");
			for (int j = 0; j < n; j++) {
				System.out.print("----");
			}
			System.out.print("-\n");
		}
	}

	private static int[][] CreateBoard(int n) {
		return CreateBoard(n, BoardType.RANDOM);
	}

	private static int randomInt(int max) {
		return rand.nextInt((max) + 1);
	}
}
