package aanchev.cardgame;

import aanchev.cardgame.model.Deck;
import aanchev.eventful.EventStream;

public abstract class CardGame implements EventStream.Default<CardGame.Event<? extends CardGame.GameState>> {
	
	private Deck deck;

	
	/* Accessors */
	
	public Deck getDeck() {
		return this.deck;
	}
	
	protected void setDeck(Deck deck) {
		this.deck = deck;
	}
	
	
	/* Inner Types */
	
	public interface GameState {};
	
	public interface Event<S> {
		public S getGameState();
	};
	
	
	/* Game progression */
	
	public abstract void progress();
	public abstract boolean isOver();
	
	public void play() {
		while (!isOver())
			progress();
	}
	
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public <E> void useUI(UI<E> ui) {
		ui.bind((EventStream<E>) (EventStream) this);
	}
}
