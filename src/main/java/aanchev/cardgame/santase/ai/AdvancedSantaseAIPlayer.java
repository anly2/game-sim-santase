package aanchev.cardgame.santase.ai;

import static aanchev.cardgame.santase.Santase.score;

import aanchev.cardgame.model.Card;
import aanchev.cardgame.santase.Santase.Move;

public class AdvancedSantaseAIPlayer extends AverageSantaseAIPlayer {
	
	private static final int WIN_THRESHOLD = 66;
	
	private int score = 0;
	
	
	@Override
	protected void initReactions() {
		super.initReactions();
		
		on((Move.GameStart m) -> {
			score = 0;
		});
		
		on((Move.Taken m) -> {
			for (Card card : m.cards)
				score += score(card);
		});
	}
	
	
	@Override
	protected void playRequest() {
		if (score >= WIN_THRESHOLD)
			earlyWin();
		
		super.playRequest();
	}
	
	@Override
	protected void call(Card card) {
		int points = card.suit == gameState.getTrumpCard().suit? 40 : 20;
		
		if (score + points >= WIN_THRESHOLD) {
			gameState.callPair(this, card);
			earlyWin();
			return;
		}
		
		super.call(card);
	}
	
	
	protected void earlyWin() {
		gameState.doWinCount(this);
	}
}
