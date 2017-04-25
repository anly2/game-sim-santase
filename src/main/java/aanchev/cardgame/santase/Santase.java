package aanchev.cardgame.santase;

import java.util.List;

import aanchev.cardgame.CardGame;

public class Santase extends CardGame {
	

	public static CardGame create() {
		return new Santase(); //TODO: include setup here
	}
	
	
	private static abstract class SantaseState extends GameState {};

	
	
	@Override
	public void setPlayers(List<Player> players) {
		if (players.size() != 2)
			throw new IllegalArgumentException("A Santase game has exactly 2 players!");
		
		super.setPlayers(players);
	}
	
	
	
	//TODO actually use distinct events
	Event<SantaseState> staticEvent = new Event<SantaseState>() {
		@Override
		public SantaseState getGameState() {
			return null;
		}
		
		char L = 'A';
		@Override
		public String toString() {
			return String.valueOf(L++);
		}
	};
	
	@Override
	public void progress() {
		fire(staticEvent);
	}

	
	//FIXME!!!
	int i = 0;
	@Override
	public boolean isOver() {
		return (i++ > 10);
	}
	
	
	

}
