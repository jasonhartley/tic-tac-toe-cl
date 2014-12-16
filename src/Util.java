import java.util.Random;
import java.util.Set;

public class Util {

	public static int randomElement(Set<Integer> set) {
		if (!set.iterator().hasNext()) {
			throw new IllegalArgumentException();
		}

		int random = new Random().nextInt(set.size());
		int chosen = 0;
		for (int ordinal : set) {
			if (random-- == 0) chosen = ordinal;
		}
		return chosen;

	}

	public static boolean isPositiveInt(String input) {
		String pattern ="\\d+";// regex for positive int
		return input.matches(pattern);
	}
}
