public class Play implements Comparable<Play> {
	private Position position;
	private int playerVal;

	public Play(Position position, int playerVal) {
		this.position = position;
		this.playerVal = playerVal;
	}

	public Play(int playerVal) {
		this.playerVal = playerVal;
	}

	public void setPosition(Position position) {
		this.position = position;
	}

	public Position position() {
		return position;
	}

	public int playerVal() {
		return playerVal;
	}

	@Override
	public int compareTo(Play play) {
		return play.position().ord() - position.ord();
	}
}
