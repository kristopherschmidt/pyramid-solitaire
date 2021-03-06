package com.kschmidt.pyramidsol.api;


public interface Position {

	public abstract Card getCard();

	public abstract void removeCard(Board board);

	public abstract void addCard(Card card, Board board);

}