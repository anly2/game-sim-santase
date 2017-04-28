package aanchev.cardgame.santase.ai;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import aanchev.cardgame.model.Card;
import aanchev.cardgame.santase.Santase;
import aanchev.cardgame.santase.Santase.Move;
import aanchev.eventful.EventStream;
import aanchev.eventful.Handler;

public abstract class SantaseAIPlayer implements Santase.Player, EventStream<Santase.Move> {
	
	private static char L = 'A';
	
	protected String name;
	
	protected Santase.State gameState;
	protected List<Card> hand;
	
	
	/* Construction */
	
	public SantaseAIPlayer() {
		this.name = "Player " + (L++);
		
		this.hand = new ArrayList<>(5);
		
		initReactions();
	}
	
	
	/* EventStream implementation */
	private Set<Handler<Move>> handlers = new HashSet<>();

	@Override
	public Set<Handler<Move>> getHandlers() {
		return this.handlers;
	}

	
	/* Santase.Player implementation */
	
	public void reset() {
		gameState = null;
		hand.clear();
	}
	
	@Override
	public void react(Move move) {
		fire(move);
	}
	
	
	/* Reactions */
	
	protected void initReactions() {
		on((Move.StateUsed move) -> {
			// We are sent the GameState object
			gameState = move.state;
		});
		
		on((Move.Drawn move) -> {
			// We drew some cards
			for (Card card : move.cards)
				hand.add(card);
		});
		
		on((Move.PlayExpected move) -> {
			doPlay();
		});
	}
	
	protected void doPlay() {
		if (gameState.getPlayedCard() == null)
			playRequest();
		else
			playResponse();
	}
	
	
	protected abstract void playRequest();
	
	protected abstract void playResponse();
	
	
	protected void play(Card card) {
		hand.remove(card);
		gameState.playCard(this, card);
	}
	
	
	/* General Object Properties */
	
	@Override
	public String toString() {
		return this.name;
	}
}