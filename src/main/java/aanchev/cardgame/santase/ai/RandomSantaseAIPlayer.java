package aanchev.cardgame.santase.ai;

import static aanchev.cardgame.Randomness.pickRandom;

public class RandomSantaseAIPlayer extends SantaseAIPlayer {
	
	@Override
	protected void playRequest() {
		play(pickRandom(hand));
	}

	@Override
	protected void playResponse() {
		play(pickRandom(hand));
	}
}
