package aanchev.cardgame.santase;

import java.util.Arrays;
import java.util.List;

import aanchev.cardgame.CardGame;
import aanchev.cardgame.model.Card;
import aanchev.cardgame.model.Deck;

public class Santase extends CardGame {
	
	/* Properties */
	
	private State state;

	/* Convenience Properties */
	
	private Player playerA;
	private Player playerB;

	
	/* Construction */
	
	public Santase() {
		this.deck = new Deck(true);
		deck.getCards().removeIf(c -> ordinal(c.rank) < 9);
		this.state = new State();
	}

	public static CardGame create() {
		return new Santase(); //TODO: include setup here
	}

	
	/* Accessors */
	
	@Override
	public void setPlayers(List<GamePlayer> players) {
		if (players.size() != 2)
			throw new IllegalArgumentException("The Santase game has exactly two players!");
		
		super.setPlayers(players);
		playerA = (Player) players.get(0);
		playerB = (Player) players.get(1);
	}
	
	
	/* Game-Specific Values */
	
	public int ordinal(Card.Rank rank) {
		switch (rank) {
		case N2: return 2;
		case N3: return 3;
		case N4: return 4;
		case N5: return 5;
		case N6: return 6;
		case N7: return 7;
		case N8: return 8;
		case N9: return 9;
		case N10: return 10;
		case Jack: return 11;
		case Queen: return 12;
		case King: return 13;
		case Ace: return 14;
		default: return -1;
		}
	}
	
	
	/* Inner Types */
	
	public interface Player extends GamePlayer {
		public void react(Move move, State state);
	}

	public class State implements GameState {
		protected int turn = 0;
		public int getTurn() {
			return this.turn;
		}
	}
	
	public static class Move implements GameEvent {
		public static class Drawn extends Move {
			public final Player player;
			public final Card[] cards;
			
			public Drawn(Player player, Card... drawn) {
				this.player = player;
				this.cards = drawn;
			}
		
			@Override
			public String toString() {
				return player + " draws the card(s): " + Arrays.toString(cards);
			}
		}

		public static class TrumpRevealed extends Move {
			public final Card trumpCard;
			
			public TrumpRevealed(Card trumpCard) {
				this.trumpCard = trumpCard;
			}
			
			@Override
			public String toString() {
				return "The '"+trumpCard+"' was revealed as Trump";
			}
		}
	}
	

	/* Game Interface */
	
	public boolean isOver() {
		return (state.turn > 10);
	}
	
	@Override
	public void progress() {
		if (state.turn == 0)
			setup();
		
		
		state.turn++;
	}
	
	
	/* Game Phases */
	
	private void setup() {
		draw(playerA, 3);
		draw(playerB, 3);
		draw(playerA, 2);
		draw(playerB, 2);
		revealTrump();
	}
	

	/* Game Moves performance */

	protected void draw(Player player, int n) {
		Card[] cards = deck.draw(n);
		Move move = new Move.Drawn(player, cards);
		
		player.react(move, state);
		fire(move);
	}

	protected void revealTrump() {
		Card trumpCard = deck.draw();
		Move trumpRevealed = new Move.TrumpRevealed(trumpCard);
		
		playerA.react(trumpRevealed, state);
		playerB.react(trumpRevealed, state);
		fire(trumpRevealed);
		
		deck.putOnBottom(trumpCard);
	}
	
}
