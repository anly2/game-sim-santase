package aanchev.cardgame.model;

import static java.util.Collections.shuffle;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class Deck {
	
	protected LinkedList<Card> cards;
	
	
	public Deck() {
		this(false);
	}
	
	public Deck(boolean shuffled) {
		this.cards = new LinkedList<>(Arrays.asList(Card.values()));
		
		if (shuffled)
			shuffle(this.cards);
	}
	
	public Deck(Card... cards) {
		this(Arrays.asList(cards));
	}
	
	public Deck(List<Card> cards) {
		this.cards = new LinkedList<>(cards);
	}
	
	
	public List<Card> getCards() {
		return this.cards;
	}
	
	
	public Card draw() {
		return this.cards.removeFirst();
	}
	
	public Card[] draw(int n) {
		Card[] cards = new Card[n];
		
		for (int i=0; i<n; i++)
			cards[i] = draw();
		
		return cards;
	}
	
	
	public void putOnTop(Card card) {
		this.cards.addFirst(card);
	}
	
	public void putOnTop(Card... cards) {
		for (Card card : cards)
			putOnTop(card);
	}
	
	
	public void putOnBottom(Card card) {
		this.cards.addLast(card);
	}
	
	public void putOnBottom(Card... cards) {
		for (Card card : cards)
			putOnBottom(card);
	}
}
