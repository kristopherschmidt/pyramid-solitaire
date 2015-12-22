package com.kschmidt.pyramidsol.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.commons.lang.builder.EqualsBuilder;

public class Deck {

	public static final String[] SUITS = new String[] { "H", "D", "S", "C" };
	public static final String[] RANKS = new String[] { "A", "2", "3", "4",
			"5", "6", "7", "8", "9", "T", "J", "Q", "K" };

	private Map<String, Card> cardMap = new HashMap<String, Card>();
	private ArrayList<Card> cards = new ArrayList<Card>();

	public Deck() {
		initCards();
	}

	private void initCards() {
		for (int i = 0; i < RANKS.length; i++) {
			for (int j = 0; j < SUITS.length; j++) {
				Card card = new Card(RANKS[i], SUITS[j]);
				cardMap.put(card.getName(), card);
				cards.add(card);
			}
		}
	}

	public Card getCard(String cardName) {
		Card card = cardMap.get(cardName);
		if (card == null) {
			throw new IllegalArgumentException("Card: "+cardName + " is not in the deck");
		}
		return card;
	}

	public int size() {
		return cardMap.size();
	}
	
	public boolean isEmpty() {
		return cardMap.isEmpty();
	}

	/** pulls a specific card out of the deck, regardless of order */
	public Card drawCard(String cardName) {
		if (!hasCard(cardName)) {
			throw new IllegalArgumentException("Card: "+cardName+" is not in the deck");
		}
		Card c = cardMap.remove(cardName);
		cards.remove(new Card(cardName));
		return c;
	}

	/** pulls the top card off the deck */
	public Card drawCard() {
		Card topCard = getCard(1);
		return drawCard(topCard.getName());
	}
	
	public boolean equals(Object o) {
		return EqualsBuilder.reflectionEquals(this, o);
	}

	public Card getCard(int position) {
		return cards.get(position - 1);
	}
	
	public void setCard(int position, String cardName) {
		int cardCurrentPosition = cards.indexOf(new Card(cardName));
		if (cardCurrentPosition == -1) {
			throw new IllegalArgumentException("Card: "+cardName+" is no longer in the deck");
		}
		Card cardInNewPosition = getCard(position);
		cards.set(position - 1, getCard(cardName));
		cards.set(cardCurrentPosition, cardInNewPosition);
	}

	public List<Card> getCards() {
		return cards;
	}
	
	public String toString() {
		return cards.toString();
	}

	public boolean hasCard(String cardName) {
		return cardMap.containsKey(cardName);
	}

	public void addCard(Card card) {
		if (hasCard(card.getName())) {
			throw new IllegalArgumentException("Card: "+card+" is already in the deck");
		}
		Card c = cardMap.put(card.getName(), card);
		cards.add(0, card);
	}

	public void shuffle() {
		Random random = new Random();
		for (int i = 0; i < 52*7; ++i) {
			int index1 = random.nextInt(52);
			int index2 = random.nextInt(52);
			Card card1 = cards.get(index1);
			Card card2 = cards.get(index2);
			cards.set(index1, card2);
			cards.set(index2, card1);
		}
	}


}
