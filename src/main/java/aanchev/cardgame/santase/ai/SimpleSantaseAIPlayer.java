package aanchev.cardgame.santase.ai;

import aanchev.cardgame.model.Card;
import aanchev.cardgame.model.Card.Suit;
import aanchev.cardgame.santase.Santase;
import aanchev.cardgame.santase.Santase.OutOfCardsException;

public class SimpleSantaseAIPlayer extends SantaseAIPlayer {

	@Override
	protected void playRequest() {
		Card card = hand.stream()
			.sorted((a, b) -> byStrength(a, b))
			.sorted((a, b) -> byTrump(a, b))
			.findFirst()
			.orElseGet(() -> { throw new OutOfCardsException(); });

		play(card);
	}

	@Override
	protected void playResponse() {
		Card card = hand.stream()
			.sorted((b, a) -> byStrength(a, b))
			.sorted((a, b) -> byTrump(a, b))
			.findFirst()
			.orElseGet(() -> { throw new OutOfCardsException(); });
		
		play(card);
	}

	
	private int byTrump(Card a, Card b) {
		if (a.suit != b.suit) {
			final Suit trump = gameState.getTrumpCard().suit;
			
			if (a.suit == trump)
				return 1;
			
			if (b.suit == trump)
				return -1;
		}
		return 0;
	}
	
	private int byStrength(Card a, Card b) {
		return Integer.compare(Santase.strength(a.rank), Santase.strength(b.rank));
	}
}