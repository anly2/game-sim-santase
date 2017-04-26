package aanchev.cardgame.santase;

import aanchev.cardgame.CardGame;
import aanchev.cardgame.santase.ai.SimpleSantaseAIPlayer;
import aanchev.cardgame.ui.ConsoleUI;

public class App 
{
    public static void main( String[] args )
    {
        CardGame game = Santase.create();
        
        game.useUI(new ConsoleUI());
        game.setPlayers(
        		new SimpleSantaseAIPlayer(),
        		new SimpleSantaseAIPlayer()
        );
        
        game.play();
    }
}
