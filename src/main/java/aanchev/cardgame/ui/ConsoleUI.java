package aanchev.cardgame.ui;

import aanchev.cardgame.UI;
import aanchev.eventful.EventStream;

public class ConsoleUI implements UI<Object> {
	public void bind(EventStream<Object> eventStream) {
		eventStream.on(e -> System.out.println(e.toString()));
	}
	
}