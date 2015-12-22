package com.kschmidt.pyramidsol.api;

import org.apache.commons.lang.builder.EqualsBuilder;


public class TalonPosition implements Position {

	private Card card;
	private int column;
	
	public TalonPosition(Card card, int column) {
		this.card = card;
		this.column = column;
	}
	
	public TalonPosition(String cardString, int column) {
		this(new Card(cardString), column);
	}
	
	public Card getCard() {
		return card;
	}
	
	public int getColumn() {
		return column;
	}

	@Override
	public void removeCard(Board board) {
		board.getTalon().removeCard(column);
	}
	
	@Override
	public void addCard(Card card, Board board) {
		board.getTalon().addCard(column, card);
	}
	
	public boolean equals(Object o) {
		if (o instanceof TalonPosition) {
			TalonPosition rhs = (TalonPosition)o;
			return new EqualsBuilder().append(card, rhs.card).append(column, rhs.column).isEquals();
		}
		return false;
	}
	
	public String toString() {
		return "TalonPosition("+card+","+column+")";
	}

	
}
