package aanchev.cardgame;

import java.util.Arrays;
import java.util.List;

import aanchev.eventful.EventStream;

public abstract class CardGame implements EventStream.Default<CardGame.Event<? extends CardGame.GameState>> {
	
	/* Inner Types */
	
	public static class GameState {};
	
	public interface Event<S> {
		public S getGameState();
	};
	
	public interface Player {
		public void play(GameState state);
	};

	
	/* Player setup */
	
	private List<Player> players;
	
	public void setPlayers(Player... players) {
		setPlayers(Arrays.asList(players));
	}
	public void setPlayers(List<Player> players) {
		this.players = players;
	}
	
	public List<Player> getPlayers() {
		return this.players;
	}
	
	
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
