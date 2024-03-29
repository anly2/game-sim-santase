package aanchev.cardgame.santase.ai;

import static aanchev.cardgame.santase.Santase.index;

import static java.util.stream.Collectors.toList;

import java.util.List;

import aanchev.cardgame.model.Card;
import aanchev.cardgame.model.Card.Rank;

public class AverageSantaseAIPlayer extends SimpleSantaseAIPlayer {
	
	@Override
	protected void playRequest() {
		
		// If we have the Trump 9, exchange it
		tryExchange();
		
		// If we have a King+Queen combo, call it		
		if (callCombo()) //also makes the call and plays a card
			return;
		
		
		// Play normally
		super.playRequest();
	}
	

	protected boolean tryExchange() {
		final Card trumpCard = gameState.getTrumpCard();
		if (!gameState.exchangeTrump(this)) //does the check for possession anyhow
			return false;

		//Update hand
		hand.remove(Card.of(trumpCard.suit, Rank.N9));
		hand.add(trumpCard);
		
		return true;
	}
	

	protected boolean callCombo() {
		List<Card> special = hand.stream()
			.filter(c -> c.rank == Rank.King || c.rank == Rank.Queen)
			.sorted((a, b) -> Integer.compare(index(a), index(b)))
			.collect(toList());
		
		if (special.size() > 1 && special.get(0).suit == special.get(1).suit) {
			call(special.get(0));
			return true;
		}
		
		return false;
	}
	
	protected void call(Card card) {
		gameState.callPair(this, card);
		play(card);
	}
}
