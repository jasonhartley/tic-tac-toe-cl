import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Human implements Player {
	private int value;

	public Human(int value) {
		this.value = value;
	}

	public int value() {
		return value;
	}

	public String show() {
		return String.valueOf(value);// todo: fix this
	}

	public int getPlay(Board board) {
		String input;
		int ordinal = -1;// If -1 is returned, something went wrong getting the input
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			do {
				do {
					System.out.print("Enter a position number: ");
					input = br.readLine();
				}
				while (!isPostiveInt(input));
				ordinal = Integer.valueOf(input) - 1;// The displayed ordinals start at 1 rather than 0
			}
			while (!board.isOpen(ordinal));

		} catch (IOException io) {
			io.printStackTrace();
		}

		return ordinal;

		/*int size = board.size();
		String inputRow, inputCol;
		//Position position = new Position(board.maxIndex());
		int row, col;

		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			do {
				do {
					do {
						System.out.print("Enter row: ");
						inputRow = br.readLine();
					} while (!isPostiveInt(inputRow));
					row = Integer.valueOf(inputRow);
				} while (row < 0 || row >= size);

				do {
					do {
						System.out.print("Enter column: ");
						inputCol = br.readLine();
					} while (!isPostiveInt(inputCol));
					col = Integer.valueOf(inputCol);
				} while (col < 0 || col >= size);

				position.set(row, col);

			} while (!board.isOpenPosition(position));

		} catch (IOException io) {
			io.printStackTrace();
		}

		return new Play(position, value);*/
	}

	private boolean isPostiveInt(String input) {
		String pattern ="\\d+";// regex for positive int
		return input.matches(pattern);
	}
}
