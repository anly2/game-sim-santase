package aanchev.cardgame;

import java.util.Arrays;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

import aanchev.cardgame.CardGame.GameEvent;
import aanchev.cardgame.CardGame.GamePlayer;
import aanchev.cardgame.santase.Santase;
import aanchev.cardgame.ui.UI;

public class CardGameSimulator {

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

}
