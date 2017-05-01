package aanchev.cardgame.santase.ai;

import java.util.List;
import java.util.Random;

public class RandomSantaseAIPlayer extends SantaseAIPlayer {
	
	@Override
	protected void playRequest() {
		play(pickRandom(hand));
	}

	@Override
	protected void playResponse() {
		play(pickRandom(hand));
	}


	private static Random rand = new Random(); //potentially seed and/or share
	private <E> E pickRandom(List<E> arr) {
		if (arr.isEmpty())
			return null;
		return arr.get(rand.nextInt(arr.size()));
	}
}
