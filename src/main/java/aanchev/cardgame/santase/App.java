package aanchev.cardgame.santase;

import static java.util.Collections.reverse;

import aanchev.cardgame.CardGame;
import aanchev.cardgame.CardGame.GameEvent;
import aanchev.cardgame.Stats;
import aanchev.cardgame.santase.Recollectors.CasualRecollector;
import aanchev.cardgame.santase.ai.RandomSantaseAIPlayer;
import aanchev.cardgame.santase.ai.SimpleSantaseAIPlayer;

public class App {
    public static void main(String[] args) {
		run();
    }
    
    private static void run() {
        /* Setup Game */
    	CardGame game = Santase.create();
    	
        game.setPlayers(
//        		new RandomSantaseAIPlayer().name("Rando A"),
//        		new RandomSantaseAIPlayer().name("Rando B")
//        		new SimpleSantaseAIPlayer().name("__A__"),
//        		new SimpleSantaseAIPlayer().name("__B__")
        		new RandomSantaseAIPlayer().name("Rando"),
        		new SimpleSantaseAIPlayer().name("__A__")
        );
        
        /* Setup Game Sequencer*/
//        CardGame.Sequencer sequencer = new Santase.Sequencer();
//        CardGame.Sequencer sequencer = new Santase.Sequencer(new CasualRecollector());
        CardGame.Sequencer sequencer = new Santase.Sequencer(new CasualRecollector(0.75));
//        CardGame.Sequencer sequencer = new Santase.Sequencer(new CasualRecollector(1));

        /* Setup Overall Observers */
//        new ConsoleUI().bind(game);
        Stats<GameEvent> stats = new WinStats();
        stats.bind(game);

        /* Simulate */
        System.out.println("Simulating...");
        
        System.out.println("\n...players straight...\n");
        
        for (int k=0; k<2; k++) {
        	/* Setup "Local" Observers */
            Stats<GameEvent> lstats = new WinStats();
            lstats.bind(game);
            
            /* Do play */
	        for (int i=0; i<10_000; i++) {
	        	sequencer.playSet(game);
	        }
	        
	        /* Local report*/
	        lstats.report();
	        lstats.unbind(game);
	        
	        /* Shift */
	        reverse(game.getPlayers());
	        System.out.println("\n...players reversed...\n");
        }
        
        
        /* Overall Report */
        System.out.println("\n\nOverall:\n");
        stats.report();
    }
}
