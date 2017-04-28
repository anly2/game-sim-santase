package aanchev.cardgame.santase;

import aanchev.cardgame.CardGame;
import aanchev.cardgame.CardGame.GameEvent;
import aanchev.cardgame.Stats;
import aanchev.cardgame.santase.Recollectors.CasualRecollector;
import aanchev.cardgame.santase.ai.SimpleSantaseAIPlayer;

public class App {
    public static void main(String[] args) {
		run();
    }
    
    private static void run() {
        /* Setup Game */
    	CardGame game = Santase.create();
    	
        game.setPlayers(
        		new SimpleSantaseAIPlayer(),
        		new SimpleSantaseAIPlayer()
        );
        
        /* Setup Game Sequencer*/
//        CardGame.Sequencer sequencer = new Santase.Sequencer();
//        CardGame.Sequencer sequencer = new Santase.Sequencer(new CasualRecollector());
        CardGame.Sequencer sequencer = new Santase.Sequencer(new CasualRecollector(0.75));

        /* Setup Observers */
//        new ConsoleUI().bind(game);
        Stats<GameEvent> stats = new WinStats();
        stats.bind(game);

        /* Do play */
        for (int i=0; i<100_000; i++) {
        	sequencer.playSet(game);
        }
        
        
        /* Report */
        stats.report();
    }
}
