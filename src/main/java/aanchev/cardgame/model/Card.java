package aanchev.cardgame.model;

import java.util.List;
import static java.util.stream.Collectors.toList;
import java.util.stream.Stream;

public enum Card {
	S2(Suit.Spades, Rank.N2),
	S3(Suit.Spades, Rank.N3),
	S4(Suit.Spades, Rank.N4),
	S5(Suit.Spades, Rank.N5),
	S6(Suit.Spades, Rank.N6),
	S7(Suit.Spades, Rank.N7),
	S8(Suit.Spades, Rank.N8),
	S9(Suit.Spades, Rank.N9),
	S10(Suit.Spades, Rank.N10),
	SJ(Suit.Spades, Rank.Jack),
	SQ(Suit.Spades, Rank.Queen),
	SK(Suit.Spades, Rank.King),
	SA(Suit.Spades, Rank.Ace),
	
	D2(Suit.Diamonds, Rank.N2),
	D3(Suit.Diamonds, Rank.N3),
	D4(Suit.Diamonds, Rank.N4),
	D5(Suit.Diamonds, Rank.N5),
	D6(Suit.Diamonds, Rank.N6),
	D7(Suit.Diamonds, Rank.N7),
	D8(Suit.Diamonds, Rank.N8),
	D9(Suit.Diamonds, Rank.N9),
	D10(Suit.Diamonds, Rank.N10),
	DJ(Suit.Diamonds, Rank.Jack),
	DQ(Suit.Diamonds, Rank.Queen),
	DK(Suit.Diamonds, Rank.King),
	DA(Suit.Diamonds, Rank.Ace),

	H2(Suit.Hearts, Rank.N2),
	H3(Suit.Hearts, Rank.N3),
	H4(Suit.Hearts, Rank.N4),
	H5(Suit.Hearts, Rank.N5),
	H6(Suit.Hearts, Rank.N6),
	H7(Suit.Hearts, Rank.N7),
	H8(Suit.Hearts, Rank.N8),
	H9(Suit.Hearts, Rank.N9),
	H10(Suit.Hearts, Rank.N10),
	HJ(Suit.Hearts, Rank.Jack),
	HQ(Suit.Hearts, Rank.Queen),
	HK(Suit.Hearts, Rank.King),
	HA(Suit.Hearts, Rank.Ace),

	C2(Suit.Clubs, Rank.N2),
	C3(Suit.Clubs, Rank.N3),
	C4(Suit.Clubs, Rank.N4),
	C5(Suit.Clubs, Rank.N5),
	C6(Suit.Clubs, Rank.N6),
	C7(Suit.Clubs, Rank.N7),
	C8(Suit.Clubs, Rank.N8),
	C9(Suit.Clubs, Rank.N9),
	C10(Suit.Clubs, Rank.N10),
	CJ(Suit.Clubs, Rank.Jack),
	CQ(Suit.Clubs, Rank.Queen),
	CK(Suit.Clubs, Rank.King),
	CA(Suit.Clubs, Rank.Ace),
	;

	
	public enum Dye { Red, Black };
	
	public enum Suit {
		Clubs("\u2663", Dye.Black),
		Diamonds("\u2666", Dye.Red),
		Hearts("\u2665", Dye.Red),
		Spades("\u2660", Dye.Black);
		
		
		public final String pip;
		public final Dye dye;
		
		Suit(String pip, Dye dye) {
			this.pip = pip;
			this.dye = dye;
		}
	};

	
	public enum Rank {
		N2("2"), N3("3"), N4("4"), N5("5"), N6("6"),
		N7("7"), N8("8"), N9("9"), N10("10"),
		Jack("J", "Jack"), Queen("Q", "Queen"), King("K", "King"), Ace("A", "Ace");
		
		
		public final String shortName;
		public final String longName;
		
		Rank(String name) {
			this(name, name);
		}
		
		Rank(String shortName, String longName) {
			this.shortName = shortName;
			this.longName = longName;
		}
	}
	
	
	public final Suit suit;
	public final Rank rank;
	
	Card(Suit suit, Rank rank) {
		this.suit = suit;
		this.rank = rank;
	}

	
	public String getName() {
		return rank.longName + " of " + suit.name();
	}
	
	public String toString() {
		return suit.pip + rank.shortName;
	}
	

	public static List<Card> of(Suit suit) {
		return Stream.of(Card.values()).filter(c -> c.suit == suit).collect(toList());
	}
	
	public static List<Card> of(Rank rank) {
		return Stream.of(Card.values()).filter(c -> c.rank == rank).collect(toList());
	}

	public static Card of(Suit suit, Rank rank) {
		return Stream.of(Card.values()).filter(c -> c.suit == suit && c.rank == rank).findFirst().orElse(null);
	}
}
