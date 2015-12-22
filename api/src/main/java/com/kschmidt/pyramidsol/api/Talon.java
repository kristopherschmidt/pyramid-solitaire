package com.kschmidt.pyramidsol.api;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;


public class Talon {

	private List<Stack<Card>> columns = new ArrayList<Stack<Card>>();

	public Talon() {
		columns.add(0, new Stack<Card>());
		columns.add(1, new Stack<Card>());
		columns.add(2, new Stack<Card>());
	}
	
	//public for JSON serialization - shouldn't use in api
	public List<Stack<Card>> getColumns() {
		return columns;
	}

	public boolean isEmpty() {
		return columns.get(0).isEmpty() && columns.get(1).isEmpty()
				&& columns.get(2).isEmpty();
	}

	public void addCards(Card card1, Card card2, Card card3) {
		columns.get(0).push(card1);
		columns.get(1).push(card2);
		columns.get(2).push(card3);
	}
	
	public void addCards(String card1String, String card2String, String card3String) {
		this.addCards(new Card(card1String), new Card(card2String), new Card(card3String));
	}
	
	public void addCard(int column, Card card) {
		columns.get(column - 1).push(card);
	}


	public Card getTopCard(int columnNumber) {
		Stack<Card> column = getColumn(columnNumber);
		if (!column.isEmpty()) {
			return column.peek();
		} else {
			return null;
		}
	}

	public Stack<Card> getColumn(int columnNumber) {
		return columns.get(columnNumber - 1);
	}

	public Card getCard(int columnNumber, int cardNumber) {
		Stack<Card> column = getColumn(columnNumber);
		return column.get(column.size() - cardNumber);
	}

	public Card removeCard(int columnNumber) {
		return getColumn(columnNumber).pop();
	}

	public String toString() {
		return columns.toString();
	}

	public List<Card> getTopCards() {
		List<Card> topCards = new ArrayList<Card>();
		Card card = getTopCard(1);
		if (card != null) {
			topCards.add(card);
		}
		card = getTopCard(2);
		if (card != null) {
			topCards.add(card);
		}
		card = getTopCard(3);
		if (card != null) {
			topCards.add(card);
		}
		return topCards;
	}

	public List<Position> getTopCardPositions() {
		List<Position> topCardPositions = new ArrayList<Position>();
		for (int i = 0; i < columns.size(); ++i) {
			Card card = getTopCard(i+1);
			if (card != null) {
				topCardPositions.add(new TalonPosition(card, i+1));
			}
		}
		return topCardPositions;
	}

	public int getCardCount() {
		int count = 0;
		for (int i = 0; i < columns.size(); ++i) {
			count += columns.get(i).size();
		}
		return count;
	}

}
