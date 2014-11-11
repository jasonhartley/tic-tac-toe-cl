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
		set(row, col);
		setMax(max);
	}

	public void setMax(int max) {
		this.max = max;
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
	private void setOrdinal(int row, int col) {
		ord = (row + 1) + max * (col + 1);
	}
	public int row() {
		return row;
	}
	public int col() {
		return col;
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
		return (row == -col);
	}
	public boolean isDiagBackslash() {
		return (row == col);
	}
	private boolean isValid(int row, int col) {
		return (row >= 0 && row <= max && col >= 0 && col <= max);
	}
	private void inValidate() {
		row = -1;
		col = -1;
		max = -1;
	}
	@Override
	public int compareTo(Position position) {
		return ord - position.ord();
	}
}
