package aanchev.cardgame;

import aanchev.eventful.EventStream;

public interface UI<E> {
	public void bind(EventStream<E> eventStream);
}
