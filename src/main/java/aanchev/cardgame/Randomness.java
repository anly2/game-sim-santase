package aanchev.cardgame;

import java.util.List;

public class Randomness {
	
	private static java.util.Random rng = new java.util.Random();
	
	public static void seed(long seed) {
		rng = new java.util.Random(seed);
	}

	public static java.util.Random getRNG() {
		return rng;
	}
	

	/* Static exposure */
	
	public static boolean chance(double probability) {
		return (rng.nextDouble() < probability);
	}

	public static <E> E pickRandom(List<E> list) {
		if (list.isEmpty())
			return null;
		
		return list.get(rng.nextInt(list.size()));
	}
}
