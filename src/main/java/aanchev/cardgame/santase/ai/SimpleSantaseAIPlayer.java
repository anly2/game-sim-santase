package aanchev.cardgame.santase.ai;

import static aanchev.cardgame.santase.Santase.strength;

import aanchev.cardgame.model.Card;
import aanchev.cardgame.model.Card.Suit;
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
		final Card played = gameState.getPlayedCard();
		
		Card card = hand.stream()
			.sorted((b, a) -> byPivottedStrength(played.rank, a, b))
			.sorted((a, b) -> byFittingSuit(played.suit, a, b))
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
		return Integer.compare(strength(a.rank), strength(b.rank));
	}


	private int byFittingSuit(Card.Suit suit, Card a, Card b) {
		if (a.suit == b.suit)
			return 0;
		
		if (a.suit == suit)
			return -1;

		if (b.suit == suit)
			return 1;
		
		return 0;
	}
	
	private int byPivottedStrength(Card.Rank rank, Card a, Card b) {
		final int pivot = strength(rank);
		
		if (strength(a.rank) > pivot && strength(b.rank) > pivot)
			return byStrength(b, a);
		
		return byStrength(a, b);
	}
}