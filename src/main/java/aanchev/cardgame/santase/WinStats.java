package aanchev.cardgame.santase;

import java.util.HashMap;
import java.util.Map;

import aanchev.cardgame.CardGame.GameEvent;
import aanchev.cardgame.Stats;
import aanchev.cardgame.santase.Santase.Move.Victory;
import aanchev.cardgame.santase.Santase.Player;
import aanchev.cardgame.santase.Santase.Sequencer.SetWon;
import aanchev.eventful.EventStream;

public class WinStats implements Stats<GameEvent>{
	
	private Map<Player, Integer> gameWinCounts = new HashMap<>();
	private Map<Player, Integer> setWinCounts = new HashMap<>();
	
	
	public void bind(EventStream<GameEvent> game) {
		game.on((Victory e) -> 
			gameWinCounts.compute(e.winner, (p, w) -> w==null? 0 : w+1) );
		
		game.on((SetWon e) -> 
			setWinCounts.compute(e.winner, (p, w) -> w==null? 0 : w+1) );
	}
	
	
	public void report() {
		System.out.println("--- Game Win Counts ---");
		final int totalGames = gameWinCounts.values().stream().mapToInt(Integer::valueOf).sum();
		gameWinCounts.forEach((p,w) -> {
			System.out.format("%s: %5f%n", p, ((double) w / totalGames));
		});
		System.out.println();
		
		System.out.println("--- Set Win Counts ---");
		final int totalSets = setWinCounts.values().stream().mapToInt(Integer::valueOf).sum();
		setWinCounts.forEach((p,w) -> {
			System.out.format("%s: %5f%n", p, ((double) w / totalSets));
		});
		System.out.println();
	}
}
