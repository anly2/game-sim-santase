package aanchev.cardgame.santase.ai;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import aanchev.cardgame.model.Card;
import aanchev.cardgame.santase.Santase;
import aanchev.cardgame.santase.Santase.Move;
import aanchev.cardgame.santase.Santase.OutOfCardsException;
import aanchev.cardgame.santase.Santase.Player;
import aanchev.eventful.EventStream;
import aanchev.eventful.Handler;

public abstract class SantaseAIPlayer implements Santase.Player, EventStream<Santase.Move> {
	
	private static char L = 'A';
	
	protected String name;
	
	protected Santase.State gameState;
	protected List<Card> hand;
	
	
	/* Construction */
	
	public SantaseAIPlayer() {
		this("Player " + (L++));
	}
	
	public SantaseAIPlayer(String name) {
		name(name);
		
		this.hand = new ArrayList<>(5);
		
		initReactions();
	}
	
	
	/* Accessors */
	
	@Override
	public String name() {
		return this.name;
	}
	
	@Override
	public Player name(String name) {
		this.name = name;
		return this;
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
		on((Move.GameStart move) -> {
			// We are notified of a Game's Start
			gameState = move.state;
			hand.clear();
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
		if (hand.isEmpty())
			throw new OutOfCardsException();
		
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