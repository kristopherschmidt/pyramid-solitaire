package com.kschmidt.pyramidsol;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;


public class DeckTest {

	private Deck deck;
	
	@Before
	public void setUp() {
		deck = new Deck();
	}

	@Test
	public void testSizeOfDeck() {
		assertEquals(52, deck.size());
	}

	@Test
	public void testCardValue() {
		assertEquals(1, deck.getCard("AS").getValue());
		assertEquals(2, deck.getCard("2H").getValue());
		assertEquals(11, deck.getCard("JC").getValue());
		assertEquals(12, deck.getCard("QD").getValue());
		assertEquals(13, deck.getCard("KS").getValue());
	}
	
	@Test
	public void testCanRemove() {
		assertTrue(deck.getCard("AS").canRemove(deck.getCard("QH")));
		assertFalse(deck.getCard("AS").canRemove(deck.getCard("KH")));
		assertFalse(deck.getCard("AS").canRemove(deck.getCard("2H")));
		assertFalse(deck.getCard("AS").canRemove(deck.getCard("JH")));
		
		assertTrue(deck.getCard("QS").canRemove(deck.getCard("AH")));
		assertTrue(deck.getCard("JS").canRemove(deck.getCard("2H")));
		assertTrue(deck.getCard("TS").canRemove(deck.getCard("3H")));
		assertTrue(deck.getCard("9S").canRemove(deck.getCard("4H")));

	}
	
	@Test
	public void testInitialPosition() {
		assertEquals(52, deck.getCards().size());
		assertEquals(deck.getCard("AH"), deck.getCard(1));
		assertEquals(deck.getCard("AD"), deck.getCard(2));
		assertEquals(deck.getCard("AS"), deck.getCard(3));
		assertEquals(deck.getCard("AC"), deck.getCard(4));
		assertEquals(deck.getCard("KH"), deck.getCard(49));
		assertEquals(deck.getCard("KD"), deck.getCard(50));
		assertEquals(deck.getCard("KS"), deck.getCard(51));
		assertEquals(deck.getCard("KC"), deck.getCard(52));
	}
	
	@Test(expected = IndexOutOfBoundsException.class)
	public void testGetCardOutOfBoundsHigh() {
		deck.getCard(53);
	}
	
	@Test(expected = IndexOutOfBoundsException.class)
	public void testGetCardOutOfBoundsLow() {
		deck.getCard(0);
	}
	
	@Test(expected = IndexOutOfBoundsException.class)
	public void testGetCardOutOfBoundsAfterDrawing() {
		deck.drawCard();
		deck.drawCard();
		deck.getCard(51);
	}
	
	@Test
	public void testSetGetPosition() {
		deck.setCard(1, "KC");
		assertEquals(new Card("KC"), deck.getCard(1));
		//swapped with former position
		assertEquals(new Card("AH"), deck.getCard(52));
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testSetPositionOfCardNotInDeck() {
		deck.drawCard("KC");
		deck.setCard(1, "KC");
	}
	
	@Test
	public void testDrawCard() {
		assertNotNull(deck.getCard("AH"));
		Card c = deck.drawCard("AH");
		assertEquals(new Card("AH"), c);
		assertEquals(51, deck.size());
		assertFalse(deck.hasCard("AH"));
		assertEquals(51, deck.getCards().size());
		assertEquals(deck.getCard("AD"), deck.getCard(1));
		assertEquals(deck.getCard("AS"), deck.getCard(2));
		assertEquals(deck.getCard("AC"), deck.getCard(3));
		assertEquals(deck.getCard("2H"), deck.getCard(4));
		assertEquals(deck.getCard("KH"), deck.getCard(48));
		assertEquals(deck.getCard("KD"), deck.getCard(49));
		assertEquals(deck.getCard("KS"), deck.getCard(50));
		assertEquals(deck.getCard("KC"), deck.getCard(51));
	}
	
	@Test
	public void testDrawCardFromTop() {
		Card c = deck.drawCard();
		assertEquals(new Card("AH"), c);
		assertEquals(51, deck.size());
		assertEquals(new Card("AD"), deck.getCard(1));
		assertEquals(new Card("KC"), deck.getCard(51));
	}
	
	@Test
	public void testAddCard() {
		Card c = deck.drawCard();
		deck.addCard(c);
		assertEquals(52, deck.size());
		assertEquals(c, deck.getCard(1));
		assertEquals(new Card("KC"), deck.getCard(52));
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testAddCardWhenCardAlreadyExists() {
		deck.addCard(deck.getCard(24));
	}
	
	@Test
	public void testIsEmpty() {
		for (int i = 0; i < 52; ++i) {
			assertFalse(deck.isEmpty());
			deck.drawCard();
		}
		assertTrue(deck.isEmpty());
	}
	
	@Test
	public void testShuffle() {
		deck.shuffle();
		//yes I know this isn't deterministic and will fail some of the time. how else to test true random
		assertFalse(new Card("KC").equals(deck.getCard(52)));
		assertFalse(new Card("AC").equals(deck.getCard(1)));
	}
	
	//test setting card that does not exist in the deck
	//test setting high bound for position after cards removed from deck
	
}
