import java.util.Random;

public class Position implements Comparable<Position> {
	private int row;
	private int col;
	private int max;// Last index of each row and column
	private int ord;// Ordinal of the position on the board, counting left to right, top to bottom, from 1 to max

	public Position() {
		inValidate();// Since 0 is an actual position in the board's private array, initialize values as invalid array index of -1
	}

	public Position(int max) {
		inValidate();
		setMax(max);
	}

	public Position(int row, int col, int max) {
		setMax(max);
		set(row, col);
	}

	public Position(Position position) {
		setMax(position.max());
		set(position.row(), position.col());
	}

	public void set(int row, int col) {
		if (isValid(row, col)) {
			this.row = row;
			this.col = col;
			setOrdinal(row, col);
		}
		else {
			inValidate();
		}
	}
	public void setRow(int row) {
		this.row = row;
	}
	public void setCol(int col) {
		this.col = col;
	}
	public void setMax(int max) {
		this.max = max;
	}
	public void setAsCenter() {
		if (hasCenter()) {
			set(max / 2, max / 2);
		}
		// Invalidate if an even number of possible positions
		else {
			inValidate();
		}
	}
	// todo: maybe change names to setAsAnyCorner, setAsAnyAdjCorner
	// Sets as a random corner
	public void setAsCorner() {
		set(randomBit() * max, randomBit() * max);
	}
	// Set as an adjacent corner based on a position that is a side position // todo: may not need this method
	public void setAsAdjCorner(Position position) {
		if (position.isSide()) {
			int row = position.row();
			int col = position.col();

			setAsCorner();

			// If pos is in an outside row, chose that row
			if (row == 0 || row == max) {
				setRow(row);

			}
			// If pos is in an outside col, use that col
			else if (col == 0 || col == max) {
				setCol(col);
			}
			else {
				// This condition should never be reachable
				inValidate();
			}

		}
		else {
			inValidate();
		}
	}
	public void setAsXAxisFlip() {
		setRow(max - row);
	}
	public void setAsYAxisFlip() {
		setCol(max - col);
	}
	public void setAsSlashAxisFlip() {
		int temp = max - row;
		setRow(max - col);
		setCol(temp);
	}
	public void setAsBackslashAxisFlip() {
		int temp = row;
		setRow(col);
		setCol(temp);
	}
	public void setAsClockwiseRotation() {
		setAsSlashAxisFlip();
		setAsXAxisFlip();
	}
	public void setAsCounterclockwiseRotation() {
		setAsSlashAxisFlip();
		setAsYAxisFlip();
	}
	public void setAs180Rotation() {
		setAsXAxisFlip();
		setAsYAxisFlip();
	}
	public void setRandom() {
		set(random(), random());
	}
	public int row() {
		return row;
	}
	public int col() {
		return col;
	}
	public int max() {
		return max;
	}
	public int ord() {
		return ord;
	}
	public boolean equals(Position position) {
		return (this.row == position.row && this.col == position.col && this.max == position.max);
	}
	public boolean isCorner() {
		return (isEdgeRow() && isEdgeCol());
	}
	public boolean isCenter() {
		return (isDiagSlash() && isDiagBackslash());
	}
	public boolean isSide() {
		return (isEdgeRow() ^ isEdgeCol());// todo: make sure this is right
	}
	public boolean isEdgeRow() {
		return (row == 0 || row == max);
	}
	public boolean isEdgeCol() {
		return (col == 0 || col == max);
	}
	public boolean isDiagSlash() {
		return (row == max - col);
	}
	public boolean isDiagBackslash() {
		return (row == col);
	}

	private void setOrdinal(int row, int col) {
		ord = (row + 1) + max * (col + 1);
	}
	private boolean isValid(int row, int col) {
		return (row >= 0 && row <= max && col >= 0 && col <= max);
	}
	private boolean hasCenter() {
		// To have a center there must be an odd number of rows and cols, meaning the max index is an even number
		return max % 2 == 0;
	}
	private void inValidate() {
		row = -1;
		col = -1;
		max = -1;// todo: maybe not set this, for example setAsCenter doesn't really need the max invalidated
	}
	private int randomBit() {
		Random rand = new Random();
		return rand.nextInt(2);
	}
	private int random() {
		Random rand = new Random();
		return rand.nextInt(max + 1);
	}
	// todo: for debug purposes, remove this later
	public String show() {
		return row + ", " + col + ", " + max;
	}

	// todo: is this necessary in Position, or can it just be in Play?
	@Override
	public int compareTo(Position position) {
		return ord - position.ord();
	}
}
