package aanchev.cardgame.santase;

import aanchev.cardgame.CardGame;
import aanchev.cardgame.santase.ai.SimpleSantaseAIPlayer;
import aanchev.cardgame.ui.ConsoleUI;

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
        CardGame.Sequencer sequencer = new Santase.Sequencer();

        /* Setup Observers */
        new ConsoleUI().bind(game);
        

        /* Do play */
        for (int i=0; i<1000; i++) {
        	sequencer.playSet(game);
        }
    }
}
