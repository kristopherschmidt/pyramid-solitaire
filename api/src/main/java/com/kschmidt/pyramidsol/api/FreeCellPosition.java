package com.kschmidt.pyramidsol.api;

import org.apache.commons.lang.builder.EqualsBuilder;


public class FreeCellPosition implements Position {

	private Card card;
	
	public FreeCellPosition(Card card) {
		this.card = card;
	}
	
	public FreeCellPosition(String cardString) {
		this(new Card(cardString));
	}
	
	@Override
	public Card getCard() {
		return card;
	}
	
	public boolean equals(Object o) {
		if (o instanceof FreeCellPosition) {
			FreeCellPosition rhs = (FreeCellPosition)o;
			return new EqualsBuilder().append(card, rhs.card).isEquals();
		}
		return false;
	}
	
	@Override
	public void removeCard(Board board) {
		board.removeFreeCell();
	}

	@Override
	public void addCard(Card card, Board board) {
		board.setFreeCell(card);
	}
	
	public String toString() {
		return "FreeCell("+card+")";
	}

}
