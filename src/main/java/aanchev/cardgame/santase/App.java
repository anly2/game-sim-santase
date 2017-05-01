package aanchev.cardgame.santase;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

import aanchev.cardgame.CardGame;
import aanchev.cardgame.CardGame.GameEvent;
import aanchev.cardgame.CardGame.GamePlayer;
import aanchev.cardgame.Stats;
import aanchev.cardgame.UI;
import aanchev.cardgame.santase.Recollectors.CasualRecollector;
import aanchev.cardgame.santase.ai.RandomSantaseAIPlayer;
import aanchev.cardgame.santase.ai.SimpleSantaseAIPlayer;

public class App {
    public static void main(String[] args) {
		runScenario1_3();
    }

    
    public static Consumer<CardGame> players(GamePlayer... players) {
    	return g -> g.setPlayers(players);
    }
    
    public static Supplier<CardGame.Sequencer> sequencer(CardGame.Sequencer sequencer) {
    	return () -> sequencer;
    }
    
    @SafeVarargs
    public static Supplier<List<UI<GameEvent>>> observers(UI<GameEvent>... observers) {
    	return () -> Arrays.asList(observers);
    }
    
    public static BiConsumer<CardGame.Sequencer, CardGame> playGames(int n) {
		return (s, g) -> { for (int i=0; i<n; i++) { g.play(); s.cue(g); } };
    }
    
    public static BiConsumer<CardGame.Sequencer, CardGame> playSets(int n) {
		return (s, g) -> { for (int i=0; i<n; i++) s.playSet(g); };
    }

    	
    public static void run(
    		Consumer<CardGame> setPlayers,
    		Supplier<CardGame.Sequencer> getSequencer,
    		Supplier<List<UI<GameEvent>>> getObservers
	) {
    	run(setPlayers, getSequencer, getObservers, 10_000);
    }
    
    public static void run(
    		Consumer<CardGame> setPlayers,
    		Supplier<CardGame.Sequencer> getSequencer,
    		Supplier<List<UI<GameEvent>>> getObservers,
    		int nGameSets
    ) {
    	run(setPlayers, getSequencer, getObservers, playSets(nGameSets));
    }

    public static void run(
    		Consumer<CardGame> setPlayers,
    		Supplier<CardGame.Sequencer> getSequencer,
    		Supplier<List<UI<GameEvent>>> getObservers,
    		BiConsumer<CardGame.Sequencer, CardGame> doRun
    ) {
    	run(setPlayers, getSequencer, getObservers, doRun, null, null);
    }

    public static void run(
    		Consumer<CardGame> setPlayers,
    		Supplier<CardGame.Sequencer> getSequencer,
    		Supplier<List<UI<GameEvent>>> getObservers,
    		BiConsumer<CardGame.Sequencer, CardGame> doRun,
    		BiConsumer<CardGame, List<UI<GameEvent>>> after
	) {
    	run(setPlayers, getSequencer, getObservers, doRun, null, after);
    }
    
    public static void run(
    		Consumer<CardGame> setPlayers,
    		Supplier<CardGame.Sequencer> getSequencer,
    		Supplier<List<UI<GameEvent>>> getObservers,
    		BiConsumer<CardGame.Sequencer, CardGame> doRun,
    		BiConsumer<CardGame, List<UI<GameEvent>>> before,
    		BiConsumer<CardGame, List<UI<GameEvent>>>  after
    ) {
    	CardGame game = Santase.create();
    	setPlayers.accept(game);
    	
    	CardGame.Sequencer sequencer = getSequencer.get();
    	List<UI<GameEvent>> observers = getObservers.get();
    	
    	for (UI<GameEvent> observer : observers)
    		observer.bind(game);
    	
    	if (before != null) before.accept(game, observers);
    	doRun.accept(sequencer, game);
    	if (after != null) after.accept(game, observers);

    	for (UI<GameEvent> observer : observers)
    		observer.unbind(game);
    }

    
    
    protected static void scenarioWinsOf10_000(CardGame.Sequencer sequencer, GamePlayer... players) {

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
    
    private static GamePlayer[] reverse(GamePlayer... players) {
        List<GamePlayer> playersReversed = Arrays.asList(players);
        Collections.reverse(playersReversed);
        return playersReversed.toArray(new GamePlayer[players.length]);
    }
    
    
    protected static void scenario1(GamePlayer... players) {
    	scenarioWinsOf10_000(new Santase.Sequencer(), players);
    }

    protected static void scenario2(GamePlayer... players) {
    	scenarioWinsOf10_000(new Santase.Sequencer(new CasualRecollector(0)), players);
    }
    
    protected static void scenario3(GamePlayer... players) {
    	scenarioWinsOf10_000(new Santase.Sequencer(new CasualRecollector(1)), players);
    }
    
    protected static void scenario4(GamePlayer... players) {
    	scenarioWinsOf10_000(new Santase.Sequencer(new CasualRecollector(0.75)), players);
    }
    
    
    protected static void runScenario1_1() {
    	scenario1(
    		new RandomSantaseAIPlayer().name("Rando A"),
    		new RandomSantaseAIPlayer().name("Rando B")
		);
    }

    protected static void runScenario1_2() {
    	scenario1(
			new SimpleSantaseAIPlayer().name("__A__"),
			new SimpleSantaseAIPlayer().name("__B__")
		);
    }

    protected static void runScenario1_3() {
    	scenario1(
    		new RandomSantaseAIPlayer().name("Rando"),
    		new SimpleSantaseAIPlayer().name("__A__")
		);
    }
}
