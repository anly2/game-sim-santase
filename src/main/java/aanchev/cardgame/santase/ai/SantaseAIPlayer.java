package aanchev.cardgame.santase.ai;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import aanchev.cardgame.santase.Santase;
import aanchev.cardgame.santase.Santase.Move;
import aanchev.cardgame.santase.Santase.State;
import aanchev.eventful.EventStream;
import aanchev.eventful.Handler;

public class SantaseAIPlayer implements Santase.Player, EventStream<Santase.Move> {
	
	private static char L = 'A';
	
	private String name;
	
	
	/* Construction */
	
	public SantaseAIPlayer() {
		this.name = "player" + (L++);
		
		on((Move.Drawn move) -> {
			System.out.println(this+": Drew cards: "+Arrays.toString(move.cards));
		});
	}
	
	
	/* EventStream implementation */
	private Set<Handler<Move>> handlers = new HashSet<>();

	@Override
	public Set<Handler<Move>> getHandlers() {
		return this.handlers;
	}

	
	/* Santase.Player implementation */
	
	@Override
	public void react(Move move, State state) {
		fire(move);
	}
	
	
	/* General Object Properties */
	
	@Override
	public String toString() {
		return this.name;
	}
}
