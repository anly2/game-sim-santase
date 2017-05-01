package aanchev.cardgame.ui;

import aanchev.cardgame.CardGame.GameEvent;
import aanchev.cardgame.UI;
import aanchev.eventful.EventStream;
import aanchev.eventful.Handler;

public class ConsoleUI implements UI<GameEvent> {
	
	private Handler<? extends GameEvent> hook = null;

	public void bind(EventStream<GameEvent> eventStream) {
		if (hook != null)
			throw new IllegalStateException("Already bound! Unbind first.");
		
		hook = eventStream.on(e -> System.out.println(e.toString()));
	}
	
	@Override
	public void unbind(EventStream<GameEvent> eventStream) {
		eventStream.off(hook);
		hook = null;
	}
	
}