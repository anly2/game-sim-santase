package aanchev.cardgame.santase;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import aanchev.cardgame.CardGame;
import aanchev.cardgame.CardGame.GameEvent;
import aanchev.cardgame.CardGame.GamePlayer;
import aanchev.cardgame.CardGameSimulator;
import aanchev.cardgame.santase.Recollectors.CasualRecollector;
import aanchev.cardgame.santase.ai.AdvancedSantaseAIPlayer;
import aanchev.cardgame.santase.ai.AverageSantaseAIPlayer;
import aanchev.cardgame.santase.ai.RandomSantaseAIPlayer;
import aanchev.cardgame.santase.ai.SimpleSantaseAIPlayer;
import aanchev.cardgame.ui.Stats;

public class SantaseScenarios extends CardGameSimulator {
	
	protected void scenarioWinsOf10_000(CardGame.Sequencer sequencer, GamePlayer... players) {
	
	    System.out.println("Simulating 10 000 sets ...");
	    
	    final WinStats overall = new WinStats();
	
	    System.out.println("\n... with players straight ...\n");
		run(
				players(players),
				sequencer(sequencer),
				observers(new WinStats(), overall),
				playSets(10_000),
				(g,o) -> ((Stats<GameEvent>) o.get(0)).report()
		);
	
	    System.out.println("\n... with players reversed ...\n");
		run(
				players(reverse(players)),
				sequencer(sequencer),
				observers(new WinStats(), overall),
				playSets(10_000),
				(g,o) -> ((Stats<GameEvent>) o.get(0)).report()
		);
	    
	    
	    System.out.println("\n\nOverall:\n");
	    overall.report();
	}
	
	private GamePlayer[] reverse(GamePlayer... players) {
	    List<GamePlayer> playersReversed = Arrays.asList(players);
	    Collections.reverse(playersReversed);
	    return playersReversed.toArray(new GamePlayer[players.length]);
	}
	
	
	protected void scenario1(GamePlayer... players) {
		scenarioWinsOf10_000(new Santase.Sequencer(), players);
	}
	
	protected void scenario2(GamePlayer... players) {
		scenarioWinsOf10_000(new Santase.Sequencer(new CasualRecollector(0)), players);
	}
	
	protected void scenario3(GamePlayer... players) {
		scenarioWinsOf10_000(new Santase.Sequencer(new CasualRecollector(1)), players);
	}
	
	protected void scenario4(GamePlayer... players) {
		scenarioWinsOf10_000(new Santase.Sequencer(new CasualRecollector(0.75)), players);
	}
	
	
	//@Test
	public void runScenario1_1() {
		scenario1(
			new RandomSantaseAIPlayer().name("Rando A"),
			new RandomSantaseAIPlayer().name("Rando B")
		);
	}
	
	//@Test
	public void runScenario1_2() {
		scenario1(
			new SimpleSantaseAIPlayer().name("__A__"),
			new SimpleSantaseAIPlayer().name("__B__")
		);
	}
	
	//@Test
	public void runScenario1_3() {
		scenario1(
			new RandomSantaseAIPlayer().name("Rando"),
			new SimpleSantaseAIPlayer().name("__A__")
		);
	}

	//@Test
	public void runScenario1_4() {
		scenario1(
			new RandomSantaseAIPlayer().name("Rando"),
			new AverageSantaseAIPlayer().name("__A__")
		);
	}


	//@Test
	public void runScenario1_5() {
		scenario1(
			new AverageSantaseAIPlayer().name("__A__"),
			new AverageSantaseAIPlayer().name("__B__")
		);
	}
	
	@Test
	public void runScenario1_6() {
		scenario1(
			new RandomSantaseAIPlayer().name("Rando"),
			new AdvancedSantaseAIPlayer().name("__B__")
		);
	}
	
	//@Test
	public void runScenario1_7() {
		scenario1(
			new AdvancedSantaseAIPlayer().name("__A__"),
			new AdvancedSantaseAIPlayer().name("__B__")
		);
	}
}
