package com.kschmidt.pyramidsol;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;


public class RemoveCardsMove implements Move {

	private Position position1;
	private Position position2;

	public RemoveCardsMove(Position position1, Position position2) {
		this.position1 = position1;
		this.position2 = position2;
		int total = position1.getCard().getValue();
		if (position2 != null) {
			total += position2.getCard().getValue();
		}
		if (total != 13) {
			throw new IllegalArgumentException("Values do not add up to 13: "+position1+", "+position2);
		}
	}

	public RemoveCardsMove(Position position1) {
		this(position1, null);
	}

	public Position getPosition1() {
		return position1;
	}

	public Position getPosition2() {
		return position2;
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof RemoveCardsMove) {
			RemoveCardsMove m = (RemoveCardsMove) o;
			return new EqualsBuilder().append(this.position1, m.position1)
					.append(this.position2, m.position2).isEquals();
		}
		return false;
	}

	public String toString() {
		if (position2 != null) {
			return "Remove - " + position1 + " " + position2;
		} else {
			return "Remove - " + position1;
		}
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(position1).append(position2)
				.toHashCode();
	}

	@Override
	public void execute(Board board) {
		position1.removeCard(board);
		if (position2 != null) {
			position2.removeCard(board);
		}
	}

	@Override
	public void undo(Board board) {
		position1.addCard(position1.getCard(), board);
		if (position2 != null) {
			position2.addCard(position2.getCard(), board);
		}
	}

	public boolean containsCardValue(int value) {
		return position1.getCard().getValue() == value
				|| (position2 != null && position2.getCard().getValue() == value);
	}
	
	public String getTypeName() {
		return "RemoveCardsMove";
	}

}
