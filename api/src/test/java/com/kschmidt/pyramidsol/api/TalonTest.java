package com.kschmidt.pyramidsol.api;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

import com.kschmidt.pyramidsol.api.Card;
import com.kschmidt.pyramidsol.api.Position;
import com.kschmidt.pyramidsol.api.Talon;
import com.kschmidt.pyramidsol.api.TalonPosition;


public class TalonTest {

	private Talon talon = new Talon();
	
	@Test
	public void test() {
		assertTrue(talon.isEmpty());
		assertEquals(0, talon.getCardCount());
	}
	
	@Test
	public void testTopCardsWhenEmpty() {
		assertTrue(talon.getTopCards().isEmpty());
	}
	
	@Test
	public void testTopCards() {
		talon.addCards(new Card("KH"), new Card("7D"), new Card("JS"));
		List<Card> topCards = talon.getTopCards();
		assertEquals(3, topCards.size());
		assertEquals(new Card("KH"), topCards.get(0));
		assertEquals(new Card("7D"), topCards.get(1));
		assertEquals(new Card("JS"), topCards.get(2));
		assertEquals(3, talon.getCardCount());
	}
	
	@Test
	public void testTopCardPositions() {
		talon.addCards(new Card("KH"), new Card("7D"), new Card("JS"));
		List<Position> topCards = talon.getTopCardPositions();
		assertEquals(3, topCards.size());
		assertEquals(new TalonPosition("KH", 1), topCards.get(0));
		assertEquals(new TalonPosition("7D", 2), topCards.get(1));
		assertEquals(new TalonPosition("JS", 3), topCards.get(2));
	}
	
	@Test
	public void testAddCards() {
		talon.addCards(new Card("KH"), new Card("7D"), new Card("JS"));
		assertFalse(talon.isEmpty());
		assertEquals(new Card("KH"), talon.getTopCard(1));
		assertEquals(new Card("7D"), talon.getTopCard(2));
		assertEquals(new Card("JS"), talon.getTopCard(3));
		assertEquals(1, talon.getColumn(1).size());
		assertEquals(1, talon.getColumn(2).size());
		assertEquals(1, talon.getColumn(3).size());
		assertEquals(new Card("KH"), talon.getCard(1, 1));
		assertEquals(new Card("7D"), talon.getCard(2, 1));
		assertEquals(new Card("JS"), talon.getCard(3, 1));
		
		talon.addCards(new Card("2C"), new Card("3S"), new Card("6D"));
		assertFalse(talon.isEmpty());
		assertEquals(new Card("2C"), talon.getTopCard(1));
		assertEquals(new Card("3S"), talon.getTopCard(2));
		assertEquals(new Card("6D"), talon.getTopCard(3));
		assertEquals(2, talon.getColumn(1).size());
		assertEquals(2, talon.getColumn(2).size());
		assertEquals(2, talon.getColumn(3).size());
		assertEquals(new Card("2C"), talon.getCard(1, 1));
		assertEquals(new Card("3S"), talon.getCard(2, 1));
		assertEquals(new Card("6D"), talon.getCard(3, 1));
		assertEquals(new Card("KH"), talon.getCard(1, 2));
		assertEquals(new Card("7D"), talon.getCard(2, 2));
		assertEquals(new Card("JS"), talon.getCard(3, 2));
		
		assertEquals(6, talon.getCardCount());
	}
	
	@Test
	public void testRemoveCards() {
		talon.addCards(new Card("KH"), new Card("7D"), new Card("JS"));
		talon.addCards(new Card("4D"), new Card("JH"), new Card("2C"));
		talon.addCards(new Card("2C"), new Card("3S"), new Card("6D"));
	
		Card c = talon.removeCard(2);
		assertEquals(new Card("3S"), c);
		assertEquals(2, talon.getColumn(2).size());
		assertEquals(new Card("JH"), talon.getTopCard(2));
		assertEquals(new Card("JH"), talon.getCard(2,  1));
		assertEquals(new Card("7D"), talon.getCard(2,  2));
		
		assertEquals(8, talon.getCardCount());
	}
	
	@Test
	public void testAddCard() {
		talon.addCards(new Card("KH"), new Card("7D"), new Card("JS"));
		talon.addCard(2, new Card("AH"));
		
		assertEquals(2, talon.getColumn(2).size());
		assertEquals(new Card("AH"), talon.getTopCard(2));
		assertEquals(new Card("AH"), talon.getCard(2,  1));
		assertEquals(new Card("7D"), talon.getCard(2,  2));
	}

}
