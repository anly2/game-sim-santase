package aanchev.cardgame;

import java.util.Arrays;
import java.util.List;

import aanchev.cardgame.model.Deck;
import aanchev.eventful.EventStream;

public abstract class CardGame implements EventStream.Default<CardGame.GameEvent> {
	
	protected List<GamePlayer> players;
	protected Deck deck;

	
	/* Accessors */
	
	public Deck getDeck() {
		return this.deck;
	}
	
	protected void setDeck(Deck deck) {
		this.deck = deck;
	}
	
	
	public List<GamePlayer> getPlayers() {
		return this.players;
	}
	
	public void setPlayers(GamePlayer... players) {
		setPlayers(Arrays.asList(players));
	}
	
	public void setPlayers(List<GamePlayer> players) {
		this.players = players;
	}
	
	
	/* Inner Tagging Interfaces */
	
	public interface GamePlayer {};
	
	public interface GameState {};
	
	public interface GameEvent {};
	
	
	/* Game progression */
	
	public abstract boolean isOver();
	public abstract void progress();

	
	public void play() {
		while (!isOver())
			progress();
	}
	
	public abstract Deck recollectCards();
	public abstract void reset();
	
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public <E> void useUI(UI<E> ui) {
		ui.bind((EventStream<E>) (EventStream) this);
	}
}
