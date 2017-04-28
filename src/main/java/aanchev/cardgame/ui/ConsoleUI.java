package aanchev.cardgame.ui;

import aanchev.cardgame.CardGame.GameEvent;
import aanchev.cardgame.UI;
import aanchev.eventful.EventStream;

public class ConsoleUI implements UI<GameEvent> {
	public void bind(EventStream<GameEvent> eventStream) {
		eventStream.on(e -> System.out.println(e.toString()));
	}
	
}