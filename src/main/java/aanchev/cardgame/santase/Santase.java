package aanchev.cardgame.santase;

import static java.util.Collections.addAll;
import static java.util.Optional.ofNullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.BiConsumer;

import aanchev.cardgame.CardGame;
import aanchev.cardgame.model.Card;
import aanchev.cardgame.model.Deck;
import aanchev.cardgame.model.Card.Rank;

public class Santase extends CardGame {
	
	/* Properties */
	
	private State state;

	/* Convenience Properties */
	
	private Player playerA;
	private Player playerB;

	
	/* Construction */
	
	public Santase() {
		this.deck = new Deck(true) {
			@Override
			public void refresh(boolean shuffled) {
				super.refresh(shuffled);
				cards.removeIf(c -> ordinal(c.rank) < 9);
			}
		};
		this.state = new State();
	}

	public static CardGame create() {
		return new Santase();
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
	
	public static int index(Card card) {
		int w = index(card.suit);
		int h = ordinal(card.rank);
		
		int i = w*W + h;
		return i;
	}
	
	public static int index(Card.Suit suit) {
		switch (suit) {
		case Clubs: return 0;
		case Diamonds: return 1;
		case Hearts: return 2;
		case Spades: return 3;
		default: return -1;
		}
	}
	
	private static int W = java.util.stream.Stream.of(Card.Rank.values()).mapToInt(Santase::ordinal).max().orElse(-1);
	
	
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
	
	
	public static int score(Card card) {
		return score(card.rank);
	}
	
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

	
	public static int strength(Card card) {
		return strength(card.rank);
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
		/* Core behaviour */
		
		public void react(Move move);

		
		/* Optional Accessors */
		
		default Player name(String name) {
			return this;
		}
		
		default String name() {
			return this.toString();
		}
		
		
		/* Static CONST instances */
		
		public static final Player NEITHER = new Player(){
			public void react(Move move) {}
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
		
		protected Map<Player, List<Card>> playerHands = new HashMap<>();
		protected Map<Player, List<Card>> playerWinPiles = new HashMap<>();
		protected Map<Player, Integer> playerCallPoints = new HashMap<>();

		
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

		public static class GameStart extends Move {
			public final State state;
			
			public GameStart(State state) {
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

		public static class TrumpRevealed implements GameEvent {
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
		
		public static class Played implements GameEvent {
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
		
		public static class Victory implements GameEvent {
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
		startGame(); //broadcast the start of the game
		
		draw(playerA, 3);
		draw(playerB, 3);
		draw(playerA, 2);
		draw(playerB, 2);
		revealTrump();
	}
	
	private void finish() {
		final int scoreA = countScore(playerA);
		final int scoreB = countScore(playerB);
		
//		System.out.println(scoreA + " vs " + scoreB);
		
		if (scoreA > 66 && scoreA > scoreB)
			crown(playerA, scoreB);
		else
		if (scoreB > 66 && scoreB > scoreA)
			crown(playerB, scoreA);
		else {
			crown(Player.NEITHER, 66);
		}
	}
	

	/* Game Moves performance */
	
	protected void startGame() {
		Move gameStart = new Move.GameStart(state);
		playerA.react(gameStart);
		playerB.react(gameStart);
	}

	protected void draw(Player player, int n) {
		if (deck.size() < n)
			return;
			//throw new NotEnoughDeckCardsException(n);
		
		Card[] cards = deck.draw(n);
		Move move = new Move.Drawn(player, cards);
		
		addAll(state.playerHands.computeIfAbsent(player, k -> new ArrayList<>(5)),
				cards);
		
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
		
		addAll(state.playerWinPiles.computeIfAbsent(player, k -> new LinkedList<>()),
			cards);
		
		player.react(move);
		fire(move);
	}
	
	protected void crown(Player player, int otherScore) {
		state.winner = player;
		state.victoryPoints = otherScore == 0 ? 3 : otherScore < 33 ? 2 : 1;
		
		fire(new Move.Victory(player, state.victoryPoints, state.turn));
	}
	
	
	private int countScore(Player player) {
		return ofNullable(state.playerCallPoints.get(player)).orElse(0) +
				ofNullable(state.playerWinPiles.get(player))
				.map(cards -> cards.stream()
						.mapToInt(Santase::strength)
						.sum())
				.orElse(0);
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
		
		if (!state.playerHands.get(player).remove(card))
			throw new IllegalStateException("The card "+card+" was not in possession of "+player+", but they tried to play it!");
		
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
		
		int points = card.suit==state.trumpCard.suit? 40 : 20;
		state.playerCallPoints.compute(player, (k, p) -> p==null? points : p+points);
		
		
		//#! THIS ONLY HANDLES THE CALLING
		//#! THE CARD MUST BE PLAYED THROUGH .playCard()
		
		return true;
	}
	
	protected boolean exchangeTrump(Player player) {
		//#trusting: For performance reasons, players are not double-checked
		//checkPlayer(player);
		
		//not allowed on the first turn
		if (state.turn == 0) 
			return false;
		
		//only allowed at "Requests"
		if (state.turn % 2 != 0)
			return false;
		
		//not allowed when deck is finished or almost finished
		if (deck.size() <= 2)
			return false;
		
		
		//not allowed when market is closed
		//#!

		
		final Card trumpCard = state.getTrumpCard(); //local store
		final Card trump9 = Card.of(trumpCard.suit, Rank.N9);
		
		if (!state.playerHands.get(player).remove(trump9))
			return false;
			//throw new IllegalStateException("The card "+trump9+" was not in possession of "+player+", but they tried to use it!");
		
		state.trumpCard = trump9;
		deck.takeFromBottom();
//		assert trumpCard.equals(bottomCard);
		deck.putOnBottom(trump9);
		
		state.playerHands.get(player).add(trumpCard);
		
		return true;
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
		finish();
		return true;
	}

	
	/* Game Sequencer */

	public static class Sequencer implements CardGame.Sequencer {
		//## needs to be an inner class so to have access to State
		
		/* Properties */
		
		private int setPoints;
		
		/* Strategy Components */
		
		private BiConsumer<Deck, Santase.State> recollector;

		
		/* Construction */
		
		public Sequencer() {
			this((deck, state) -> deck.refresh(true));
		}
		
		public Sequencer(BiConsumer<Deck, Santase.State> recollector) {
			this(11, recollector);
		}
		public Sequencer(int setPoints, BiConsumer<Deck, Santase.State> recollector) {
			this.setPoints = setPoints;
			this.recollector = recollector;
		}
		
		
		/* Game Sequencer Contract */
		
		@Override
		public void cue(CardGame game) {
			if (!(game instanceof Santase))
				throw new UnsupportedOperationException("This Game Sequencer can only handle Santase games!");
			
			Santase g = (Santase) game;
			
			// Recollect cards
			recollector.accept(g.deck, g.state);
			
			// Shift players so that the Winner starts
			if (g.state.winner == g.playerB)
				g.setPlayers(g.playerB, g.playerA);
			
			// Reset Game State
			g.state = g.new State(); //easiest clear
			g.state.cued = g.playerA;
			
			// Reset Player States
			//done through a Reaction to the GameStart Move
//			g.playerA.reset();
//			g.playerB.reset();
		}

		@SuppressWarnings("null")
		@Override
		public void playSet(CardGame game) {
			if (!(game instanceof Santase))
				throw new UnsupportedOperationException("This Game Sequencer can only handle Santase games!");
			
			Santase g = (Santase) game;
			Map<Player, Integer> points = new HashMap<>();
			Entry<Player, Integer> lead = null;

			do {
				g.play();
				
				g.players.forEach(player -> points.putIfAbsent((Player) player, 0));
				
				Player w = g.state.winner;
				
				if (w == Player.NEITHER)
					points.replaceAll((k, p) -> p + g.state.victoryPoints);
				else
					points.compute(w, (k, p) -> p + g.state.victoryPoints);
				
				lead = points.entrySet().stream().max((a,b) -> Integer.compare(a.getValue(), b.getValue())).get();
				if (lead.getValue() >= setPoints)
					break;
				
				cue(g);
			} while (true);
			

//			System.out.print("set:");
//			points.forEach((player, p) -> System.out.print(" " + player + "/" + p));
//			System.out.println();
			
			g.fire(new SetWon(lead.getKey()));
			cue(g);
		}
		
		
		public static class SetWon extends Move {
			public final Player winner;
			
			public SetWon(Player winner) {
				this.winner = winner;
			}
			
			@Override
			public String toString() {
				return winner + " won the game set.";
			}
		}
	}
}
