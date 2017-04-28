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
		this.cards = new LinkedList<>();
		refresh(shuffled);
	}
	
	public Deck(Card... cards) {
		this(Arrays.asList(cards));
	}
	
	public Deck(List<Card> cards) {
		this.cards = new LinkedList<>(cards);
	}
	

	public void refresh() {
		refresh(false);
	}
	
	public void refresh(boolean shuffled) {
		this.cards.clear();
		
		for (Card card : Card.values())
			this.cards.add(card);
		
		if (shuffled)
			shuffle(this.cards);
	}

	
	
	public List<Card> getCards() {
		return this.cards;
	}
	
	public int size() {
		return this.cards.size();
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

	public void putOnTop(Iterable<Card> cards) {
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

	public void putOnBottom(Iterable<Card> cards) {
		for (Card card : cards)
			putOnBottom(card);
	}
}
