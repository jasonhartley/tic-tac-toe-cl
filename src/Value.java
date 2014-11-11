public class Value {
	public static int X = 1;
	public static int O = -1;
	public static int NONE = 0;
	public static int INVALID = -2;
	public static int DRAW = 2;

	public static String show(int player) {
		if (player == X) return "X";
		else if (player == O) return "O";
		else if (player == NONE) return " ";
		else if (player == DRAW) return "Draw";
		else return "?";
	}

	public static boolean isValidPlayer(int player) {
		return (player <= X && player >= O);
	}
}

// Because of type safety, it is difficult to use an enum for this
/*public enum Value {
	X(1),
	O(-1),
	NONE(0),
	INVALID(-2),
	DRAW(2);

	Value(int unused) { }

	// I want that if an integer is the argument, it returns something that can be output as a String like 'X'
	public Value show(Value v) {
		return v;
	}
	public String show(int v) {
		return ((Value) v).toString();
	}

}*/

