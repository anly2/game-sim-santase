package aanchev.cardgame.santase;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import aanchev.cardgame.santase.Santase.Move.Victory;
import aanchev.cardgame.CardGame.GameEvent;
import aanchev.cardgame.santase.Santase.Player;
import aanchev.cardgame.santase.Santase.Sequencer.SetWon;
import aanchev.cardgame.ui.Stats;
import aanchev.eventful.EventStream;
import aanchev.eventful.Handler;

public class WinStats implements Stats<GameEvent>{
	
	private Map<Player, Integer> gameWinCounts = new HashMap<>();
	private Map<Player, Integer> setWinCounts = new HashMap<>();
	
	private List<Handler<? extends GameEvent>> hooks = new LinkedList<>();
	
	public void bind(EventStream<GameEvent> game) {
		if (!hooks.isEmpty())
			throw new IllegalStateException("Already bound! Unbind first.");
		
		hooks.add(game.on((Victory e) -> 
			gameWinCounts.compute(e.winner, (p, w) -> w==null? 0 : w+1) ));
		
		hooks.add(game.on((SetWon e) -> 
			setWinCounts.compute(e.winner, (p, w) -> w==null? 0 : w+1) ));
	}
	
	
	@Override
	public void unbind(EventStream<GameEvent> eventStream) {
		for (Handler<? extends GameEvent> hook : hooks)
			eventStream.off(hook);
		hooks.clear();
	}
	
	
	public void report() {
		System.out.println("--- Game Win Counts ---");
		final int totalGames = gameWinCounts.values().stream().mapToInt(Integer::valueOf).sum();
		gameWinCounts.forEach((p,w) -> {
			System.out.format("%s: %5f%%%n", p, ((double) w / totalGames));
		});
		System.out.println("... out of "+totalGames+" games.");
		System.out.println();
		
		System.out.println("--- Set Win Counts ---");
		final int totalSets = setWinCounts.values().stream().mapToInt(Integer::valueOf).sum();
		setWinCounts.forEach((p,w) -> {
			System.out.format("%s: %5f%%%n", p, ((double) w / totalSets));
		});
		System.out.println("... out of "+totalSets+" sets.");
		System.out.println();
	}
}
