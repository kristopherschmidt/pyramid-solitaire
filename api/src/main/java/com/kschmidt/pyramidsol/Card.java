package com.kschmidt.pyramidsol;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

public class Card {

	private String rank;
	private String suit;
	private int value;

	public Card(String name) {
		this(name.substring(0,  1), name.substring(1, 2));
	}
	
	public Card(String rank, String suit) {
		this.rank = rank;
		this.suit = suit;
		if ("A".equals(rank)) {
			value = 1;
		} else if ("T".equals(rank)) {
			value = 10;
		} else if ("J".equals(rank)) {
			value = 11;
		} else if ("Q".equals(rank)) {
			value = 12;
		} else if ("K".equals(rank)) {
			value = 13;
		} else {
			value = Integer.parseInt(rank);
		}
	}

	public int getValue() {
		return value;
	}

	public String getName() {
		return rank + suit;
	}

	public boolean canRemove(Card card) {
		return value + card.value == 13;
	}
	
	public String toString() {
		return rank+suit;
	}
	
	public boolean equals(Object o) {
		if (o instanceof Card) {
			Card c = (Card)o;
			return new EqualsBuilder().append(this.rank, c.rank).append(this.suit, c.suit).isEquals();
		}
		return false;
	}
	
	public int hashCode() {
		return new HashCodeBuilder().append(rank).append(suit).toHashCode();
	}
}
