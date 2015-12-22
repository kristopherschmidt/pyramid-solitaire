package com.kschmidt.pyramidsol.api;

import org.apache.commons.lang.builder.EqualsBuilder;


public class PyramidPosition implements Position {

	private Card card;
	private int rowNumber;
	private int columnNumber;

	public PyramidPosition () {
		
	}
	
	public PyramidPosition(Card card, int rowNumber, int columnNumber) {
		this.card = card;
		this.rowNumber = rowNumber;
		this.columnNumber = columnNumber;
	}
	
	public PyramidPosition(String cardString, int rowNumber, int columnNumber) {
		this(new Card(cardString), rowNumber, columnNumber);
	}

	@Override
	public Card getCard() {
		return card;
	}
	
	public int getRowNumber() {
		return rowNumber;
	}
	
	public int getColumnNumber() {
		return columnNumber;
	}

	public boolean equals(Object o) {
		if (o instanceof PyramidPosition) {
			PyramidPosition rhs = (PyramidPosition) o;
			return new EqualsBuilder().append(card, rhs.card)
					.append(rowNumber, rhs.rowNumber)
					.append(columnNumber, rhs.columnNumber).isEquals();
		} else {
			return false;
		}
	}

	public String toString() {
		return "Pyramid(" + card + "," + rowNumber + "," + columnNumber + ")";
	}

	/* (non-Javadoc)
	 * @see com.kschmidt.pyramidsol.api.api.Position#removeCard(com.kschmidt.pyramidsol.api.api.Board)
	 */
	@Override
	public void removeCard(Board board) {
		board.setPyramid((Card)null, rowNumber, columnNumber);
	}

	@Override
	public void addCard(Card card, Board board) {
		board.setPyramid(card, rowNumber, columnNumber);
	}

}
