package aanchev.cardgame.ui;

import aanchev.eventful.EventStream;

public interface UI<E> {
	public void bind(EventStream<E> eventStream);
	public void unbind(EventStream<E> eventStream);
}
