import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Human implements Player {
	private int value;

	public Human(int value) {
		if (Value.isValidPlayer(value)) {
			this.value = value;
		}
	}

	public int value() {
		return value;
	}

	public String show() {
		return Value.show(value);
	}

	public Play getPlay(Board board) {
		int size = board.size();
		String inputRow, inputCol;
		Position position = new Position(board.maxIndex());
		int row, col;

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

				position.set(row, col);
				System.out.println(row + ":" + col + ": " + board.get(position));
			} while (board.get(position) != Value.NONE);

		} catch (IOException io) {
			io.printStackTrace();
		}

		return new Play(position, this);
	}
}
