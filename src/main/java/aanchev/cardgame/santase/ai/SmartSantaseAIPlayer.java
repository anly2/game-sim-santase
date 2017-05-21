package aanchev.cardgame.santase.ai;

import java.util.function.Function;

import aanchev.cardgame.model.Card;

public class SmartSantaseAIPlayer extends SantaseAIPlayer {
	
	protected Function<Card, Integer> reqHeuristic;
	protected Function<Card, Integer> resHeuristic;
	

	@Override
	protected void playRequest() {
		//#! TODO logic
		// if market closed
		//		if dominates trumps
		//			drain trumps
		//		if trumps out
		//			play strongest, not dominated
		//		else play weakest
		// else
		//		if trumps out
		//			play strongest, not dominated
		//		if has dominating J or 9, play that
		//		else play weakest
		//#! add points to hopeful Q or K
	}

	@Override
	protected void playResponse() {
		//#! TODO logic
		// if market closed
		//		order Fitting Suit >> Trump >> Other
		// else
		//		order Fitting Suit >> Other >> Trump
		//		if played score >= 10, pull Trumps
		// order by pivotted Strength
		//#! add points to hopeful Q or K
		
	}

}
