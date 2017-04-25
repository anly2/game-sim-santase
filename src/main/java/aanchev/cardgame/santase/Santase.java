package aanchev.cardgame.santase;

import aanchev.cardgame.CardGame;
import aanchev.cardgame.model.Card;
import aanchev.cardgame.model.Deck;

public class Santase extends CardGame {
	
	private Player<Move, State> playerA;
	private Player<Move, State> playerB;
	
	private Deck deck;
	private State state;
	
	public Santase() {
		this.deck = new Deck(true);
		deck.getCards().removeIf(c -> ordinal(c.rank) < 9);
		this.state = new State();
	}
	

	public static CardGame create() {
		return new Santase(); //TODO: include setup here
	}

	
	
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
	
	
	//TODO: merge into Move
	public class Event implements CardGame.Event<GameState> {
		@Override
		public GameState getGameState() {
			return state;
		}

		
		public class Drawn extends Event {
			public Drawn(Player<Move, State> player, Card[] cards) {
			}
		}
		
		public class TrumpReveal extends Event {

			public TrumpReveal(Card trumpCard) {
			}
			
		}
	}
	
	//TODO: remove generic params
	public interface Player<MOVE, STATE> {
		public void react(MOVE move, STATE state);
	}
	
	//TODO: merge with Event
	public class Move {
		public class Drawn extends Move {
			public final Card[] cards;
			public Drawn(Card... drawn) {
				this.cards = drawn;
			}
		}

		public class TrumpRevealed extends Move {
			public final Card trumpCard;
			public TrumpRevealed(Card trumpCard) {
				this.trumpCard = trumpCard;
			}
		}
	}
	
	public class State implements GameState {
	}


	
	private int turn = 0;
	
	public boolean isOver() {
		return (turn > 10);
	}
	
	@Override
	public void progress() {
		if (turn == 0)
			setup();
		
		
		turn++;
	}
	
	private void setup() {
		draw(playerA, 3);
		draw(playerB, 3);
		draw(playerA, 2);
		draw(playerB, 2);
		revealTrump();
	}
	
	


	//TODO: removes these
	private Santase.Move moves = this.new Move();
	private Santase.Event events = this.new Event();
	
	private void draw(Player<Move, State> player, int n) {
		Card[] cards = deck.draw(n);
		player.react(moves.new Drawn(cards), state);
		fire(events.new Drawn(player, cards));
	}

	private void revealTrump() {
		Card trumpCard = deck.draw();
		Move trumpRevealed = moves.new TrumpRevealed(trumpCard);
		
		playerA.react(trumpRevealed, state);
		playerB.react(trumpRevealed, state);
		
		deck.putOnBottom(trumpCard);
		
		fire(events.new TrumpReveal(trumpCard));
	}
	
}
