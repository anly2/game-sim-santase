package aanchev.cardgame.santase;

import java.util.function.BiConsumer;

import aanchev.cardgame.CardGame;
import aanchev.cardgame.CardGame.Sequencer;
import aanchev.cardgame.model.Deck;

public class SantaseSequencer implements Sequencer {
	
	/* Strategy Components */
	
	private BiConsumer<Deck, Santase.State> recollector;

	
	/* Construction */
	
	public SantaseSequencer() {
		this((deck, state) -> deck.refresh());
	}
	
	public SantaseSequencer(BiConsumer<Deck, Santase.State> recollector) {
		this.recollector = recollector;
	}
	
	
	/* Game Sequencer Contract */
	
	@Override
	public void cue(CardGame game) {
		//recollect
		//shift players
		//reset state
		//reset players
	}

	@Override
	public void playSet(CardGame game) {
		Sequencer.super.playSet(game);
	}
}
