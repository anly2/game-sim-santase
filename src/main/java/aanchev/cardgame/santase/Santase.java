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
		
		state.cued = playerA;
	}
	
	public Player other(Player player) {
		return (player == playerA)? playerB : playerA;
	}
	
	
	/* Game-Specific Values */
	
	public static int score(Card.Rank rank) {
		switch (rank) {
		case N9: return 0;
		case N10: return 10;
		case Jack: return 2;
		case Queen: return 3;
		case King: return 4;
		case Ace: return 11;
		default: return -1;
		}
	}
	
	public static int ordinal(Card.Rank rank) {
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
	
	public static int strength(Card.Rank rank) {
		return score(rank);
	}
	
	public boolean stronger(Card c0, Card c1) {
		if (c0.suit == c1.suit)
			return (strength(c0.rank) > strength(c1.rank));
		
		if (c1.suit == state.trumpCard.suit)
			return false;
		
		// if c1.suit == trump.suit
		// if different and neither is trump
		
		return true;
	}
	
	
	/* Inner Types */
	
	public interface Player extends GamePlayer {
		public void react(Move move);
		
		//#! ideally this would not be a player's responsibility
		//#trusting: For performance reasons, score counting is entrusted to players
		public int countPoints();
		
		public static final Player NEITHER = new Player(){
			public void react(Move move) {}
			public int countPoints() { return 0; }
			public String toString() { return "Neither Player"; }
		};
	}

	public class State implements GameState {
		
		/* Actual State properties */
		
		protected int turn = 0;
		protected Card trumpCard = null;
		protected Card playedCard = null;
		protected Player cued = null;
		protected Player winner = null;
		protected int victoryPoints = 0;
		
		
		//#trusting: For performance reasons, players on turn are not tracked
		//protected Player playerOnTurn = null;
		
		//#trusting: For performance reasons, player hands are not tracked
		//protected Map<Player, List<Card>> playerHands;

		//#trusting: For performance reasons, player win-piles are not tracked
		//protected Map<Player, List<Card>> playerWinPiles;

		
		/* State accessors */
		
		public int getTurn() {
			return this.turn;
		}
		
		public Card getTrumpCard() {
			return this.trumpCard;
		}
		
		public Card getPlayedCard() {
			return playedCard;
		}
	
		public Player getWinner() {
			return this.winner;
		}
	
		public int getVictoryPoints() {
			return this.victoryPoints;
		}
		
	
		/* Proxy Game Actions performable by players */
		
		public boolean playCard(Player player, Card card) {
			return Santase.this.playCard(player, card);
		}
		
		public boolean callPair(Player player, Card card) {
			return Santase.this.callPair(player, card);
		}
		
		public boolean exchangeTrump(Player player) {
			return Santase.this.exchangeTrump(player);
		}
		
		public boolean closeMarket(Player player) {
			return Santase.this.closeMarket(player);
		}
		
		public boolean doWinCount(Player player) {
			return Santase.this.doWinCount(player);
		}
	}
	
	public static class Move implements GameEvent {

		public static class StateUsed extends Move {
			public final State state;
			
			public StateUsed(State state) {
				this.state = state;
			}
		}
		
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

		private static class TrumpRevealed extends Move {
			public final Card trumpCard;
			
			public TrumpRevealed(Card trumpCard) {
				this.trumpCard = trumpCard;
			}
			
			@Override
			public String toString() {
				return "The '"+trumpCard+"' was revealed as Trump";
			}
		}

		public static class PlayExpected extends Move {
			private static PlayExpected instance = new PlayExpected();
		}
		
		private static class Played extends Move {
			public final Player player;
			public final Card card;
			
			public Played(Player player, Card card) {
				this.player = player;
				this.card = card;
			}
		
			@Override
			public String toString() {
				return player + " played " + card;
			}
		}
		
		public static class Taken extends Move {
			public final Player player;
			public final Card[] cards;
			
			public Taken(Player player, Card... won) {
				this.player = player;
				this.cards = won;
			}
		
			@Override
			public String toString() {
				return player + " won the card(s): " + Arrays.toString(cards);
			}
		}
		
		private static class Victory extends Move {
			public final Player winner;
			public final int victoryPoints;
			public final int turn;
			
			public Victory(Player winner, int victoryPoints, int turn) {
				this.winner = winner;
				this.victoryPoints = victoryPoints;
				this.turn = turn;
			}
			
			@Override
			public String toString() {
				return winner +" won "+victoryPoints+" points on turn "+turn;
			}
		}
	}
	
	
	/* Inner Exceptions */
	
	public static class OutOfCardsException extends IllegalStateException {
		public OutOfCardsException() {
			super("Out of cards in hand");
		}
	}
	

	/* Game Interface */
	
	public boolean isOver() {
		if (state.winner != null)
			return true;
		
		return (state.turn > 100);
	}
	
	@Override
	public void progress() {
		if (state.turn == 0)
			setup();
		
		askToPlay(state.playedCard == null ? state.cued : other(state.cued));
		
		state.turn++;
	}
	
	
	/* Game Phases */
	
	private void setup() {
		broadcastState();
		
		draw(playerA, 3);
		draw(playerB, 3);
		draw(playerA, 2);
		draw(playerB, 2);
		revealTrump();
	}
	
	private void finish() {
		final int scoreA = playerA.countPoints();
		final int scoreB = playerB.countPoints();
		
		if (scoreA > 66 && scoreA > scoreB)
			crown(playerA, scoreB);
		else
		if (scoreB > 66 && scoreB > scoreA)
			crown(playerB, scoreA);
		else {
			System.err.println(scoreA + " vs " + scoreB);
			crown(Player.NEITHER, 66);
		}
	}
	

	/* Game Moves performance */
	
	protected void broadcastState() {
		Move broadcast = new Move.StateUsed(state);
		playerA.react(broadcast);
		playerB.react(broadcast);
	}

	protected void draw(Player player, int n) {
		if (deck.size() < n)
			return;
			//throw new NotEnoughDeckCardsException(n);
		
		Card[] cards = deck.draw(n);
		Move move = new Move.Drawn(player, cards);
		
		//#trusting: For performance reasons, hands are not tracked
		//state.playerHands.computeIfAbsent(player, k -> new HashMap<>()).addAll(Arrays.asList(cards));
		
		player.react(move);
		fire(move);
	}

	protected void revealTrump() {
		state.trumpCard = deck.draw();
		
		fire(new Move.TrumpRevealed(state.trumpCard));
		
		deck.putOnBottom(state.trumpCard);
	}
	
	protected void askToPlay(Player player) {
		try {
			player.react(Move.PlayExpected.instance);
		}
		catch (OutOfCardsException e) {
			finish();
		}
	}
	
	protected void take(Player player, Card... cards) {
		Move move = new Move.Taken(player, cards);
		
		//#trusting: For performance reasons, win-piles are not tracked
		//state.playerWinPiles.computeIfAbsent(player, k -> new HashMap<>()).addAll(Arrays.asList(cards));
		
		player.react(move);
		fire(move);
	}
	
	protected void crown(Player player, int otherScore) {
		state.winner = player;
		state.victoryPoints = otherScore == 0 ? 3 : otherScore < 33 ? 2 : 1;
		
		fire(new Move.Victory(player, state.victoryPoints, state.turn));
	}
	
	
	/* Game Actions performable by players */

	//#trusting: For performance reasons, players are not double-checked
	/*
	private void checkPlayer(Player player) {
		if (player != state.playerOnTurn)
			throw new IllegalStateException("It is not the player's turn!");
	}
	*/
	
	
	protected boolean playCard(Player player, Card card) {
		//#trusting: For performance reasons, players are not double-checked
		//checkPlayer(player);
		
		if (state.playedCard != null) {
			final Card c0 = state.playedCard; //shorthand
			final Card c1 = card; //shorthand
			
			fire(new Move.Played(player, card));
			
			boolean lost = stronger(c0, c1);
			Player otherPlayer = other(player);
			
			take((lost? otherPlayer : player), c0, c1);
			
			draw((lost? otherPlayer : player), 1);
			draw((lost? player : otherPlayer), 1);
			
			state.playedCard = null;
			state.cued = lost? otherPlayer : player;
		}
		else {
			state.playedCard = card;
			
			fire(new Move.Played(player, card));
		}
		
		return true;
	}
	
	protected boolean callPair(Player player, Card card) {
		//#trusting: For performance reasons, players are not double-checked
		//checkPlayer(player);
		
		return false;
	}
	
	protected boolean exchangeTrump(Player player) {
		//#trusting: For performance reasons, players are not double-checked
		//checkPlayer(player);
		
		return false;
	}
	
	protected boolean closeMarket(Player player) {
		//#trusting: For performance reasons, players are not double-checked
		//checkPlayer(player);
		
		return false;
	}
	
	protected boolean doWinCount(Player player) {
		//#trusting: For performance reasons, players are not double-checked
		//checkPlayer(player);
		
		//#trusting: For performance reasons, score counting is entrusted to players
		// ... do count scores, returning false upon insufficient points
		
		state.winner = player;
		return true;
	}
}
