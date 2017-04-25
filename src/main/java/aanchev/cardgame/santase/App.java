package aanchev.cardgame.santase;

import aanchev.cardgame.CardGame;
import aanchev.cardgame.santase.ai.SantaseAIPlayer;
import aanchev.cardgame.ui.ConsoleUI;

public class App 
{
    public static void main( String[] args )
    {
        CardGame game = Santase.create();
        
        game.useUI(new ConsoleUI());
        game.setPlayers(
        		new SantaseAIPlayer(),
        		new SantaseAIPlayer()
        );
        
        game.play();
    }
}
