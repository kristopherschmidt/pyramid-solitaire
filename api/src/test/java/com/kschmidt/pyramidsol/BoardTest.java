package com.kschmidt.pyramidsol;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.List;

import org.junit.Test;


public class BoardTest {

	@Test
	public void testLoad() throws IOException {
		Board b = loadBoard("full.pys");
		assertEquals(8, b.getPyramid(1, 1).getValue());
		assertEquals(3, b.getPyramid(2, 1).getValue());
		assertEquals(11, b.getPyramid(2, 2).getValue());
		assertEquals(11, b.getPyramid(3, 1).getValue());
		assertEquals(6, b.getPyramid(3, 2).getValue());
		assertEquals(8, b.getPyramid(3, 3).getValue());
		assertEquals(3, b.getPyramid(4, 1).getValue());
		assertEquals(4, b.getPyramid(4, 2).getValue());
		assertEquals(10, b.getPyramid(4, 3).getValue());
		assertEquals(1, b.getPyramid(4, 4).getValue());
		assertEquals(7, b.getPyramid(5, 1).getValue());
		assertEquals(8, b.getPyramid(5, 2).getValue());
		assertEquals(1, b.getPyramid(5, 3).getValue());
		assertEquals(13, b.getPyramid(5, 4).getValue());
		assertEquals(2, b.getPyramid(5, 5).getValue());
		assertEquals(4, b.getPyramid(6, 1).getValue());
		assertEquals(10, b.getPyramid(6, 2).getValue());
		assertEquals(3, b.getPyramid(6, 3).getValue());
		assertEquals(2, b.getPyramid(6, 4).getValue());
		assertEquals(5, b.getPyramid(6, 5).getValue());
		assertEquals(10, b.getPyramid(6, 6).getValue());
		assertEquals(8, b.getPyramid(7, 1).getValue());
		assertEquals(5, b.getPyramid(7, 2).getValue());
		assertEquals(10, b.getPyramid(7, 3).getValue());
		assertEquals(2, b.getPyramid(7, 4).getValue());
		assertEquals(3, b.getPyramid(7, 5).getValue());
		assertEquals(2, b.getPyramid(7, 6).getValue());
		assertEquals(4, b.getPyramid(7, 7).getValue());

		assertEquals(24, b.getDeck().size());
		assertEquals(new Card("KS"), b.getDeck().getCard(1));
		assertEquals(new Card("QH"), b.getDeck().getCard(2));
		assertEquals(new Card("6C"), b.getDeck().getCard(3));
		assertEquals(new Card("QC"), b.getDeck().getCard(24));
		
		assertEquals(28, b.getPyramidCardCount());

	}

	@Test(expected = IllegalArgumentException.class)
	public void testInvalidLoadRepeatsInPyramid() throws IOException {
		loadBoard("invalid/repeats_in_pyramid.pys");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testInvalidLoadDeckRepeatsInPyramid() throws IOException {
		loadBoard("invalid/repeat_pyramid_deck.pys");
	}

	@Test(expected = IllegalStateException.class)
	public void testInvalidLoadRepeatsInDeck() throws IOException {
		loadBoard("invalid/repeats_in_deck.pys");
	}

	@Test
	public void testBoardEquals() throws IOException {
		Board b = loadBoard("full.pys");
		Board bcopy = loadBoard("full.pys");
		Board bpartial = loadBoard("partial.pys");
		assertEquals(b, bcopy);
		assertFalse(b.equals(bpartial));
	}

	@Test
	public void testBottomCardsInFullPyramid() throws IOException {
		Board b = loadBoard("full.pys");
		List<Card> bottomCards = b.getPyramidBottom();
		assertEquals(7, bottomCards.size());
		assertEquals(8, bottomCards.get(0).getValue());
		assertEquals(5, bottomCards.get(1).getValue());
		assertEquals(10, bottomCards.get(2).getValue());
		assertEquals(2, bottomCards.get(3).getValue());
		assertEquals(3, bottomCards.get(4).getValue());
		assertEquals(2, bottomCards.get(5).getValue());
		assertEquals(4, bottomCards.get(6).getValue());
	}

	@Test
	public void testBottomPositionsInFullPyramid() throws IOException {
		Board b = loadBoard("full.pys");
		List<Position> bottomPositions = b
				.getPyramidBottomPositions();
		assertEquals(7, bottomPositions.size());
		System.out.println(bottomPositions);
		assertTrue(bottomPositions.contains(new PyramidPosition("8H", 7, 1)));
		assertTrue(bottomPositions.contains(new PyramidPosition("5C", 7, 2)));
		assertTrue(bottomPositions.contains(new PyramidPosition("TC", 7, 3)));
		assertTrue(bottomPositions.contains(new PyramidPosition("2H", 7, 4)));
		assertTrue(bottomPositions.contains(new PyramidPosition("3H", 7, 5)));
		assertTrue(bottomPositions.contains(new PyramidPosition("2C", 7, 6)));
		assertTrue(bottomPositions.contains(new PyramidPosition("4S", 7, 7)));
	}

	@Test
	public void testBottomCardsWithSomeRemoved() throws Exception {
		Board b = loadBoard("partial.pys");
		List<Card> bottomCards = b.getPyramidBottom();
		assertEquals(3, bottomCards.size());
		assertTrue(bottomCards.contains(new Card("TC")));
		assertTrue(bottomCards.contains(new Card("KH")));
		assertTrue(bottomCards.contains(new Card("7S")));
	}

	@Test
	public void testDeckToTalon() throws IOException {
		Board b = loadBoard("full.pys");
		assertTrue(b.getTalon().isEmpty());
		b.drawCardsToTalon();
		assertFalse(b.getTalon().isEmpty());
		assertEquals(new Card("KS"), b.getTalon().getTopCard(1));
		assertEquals(new Card("QH"), b.getTalon().getTopCard(2));
		assertEquals(new Card("6C"), b.getTalon().getTopCard(3));
	}

	@Test
	public void testFreeCell() throws IOException {
		Board b = loadBoard("full.pys");
		assertTrue(b.isFreeCellEmpty());
		assertEquals(new Card("8H"), b.getPyramid(7, 1));
		Card card = b.getPyramid(7, 1);
		new FreeCellMove(new PyramidPosition(card, 7, 1)).execute(b);
		assertEquals(new Card("8H"), b.getFreeCell());
		assertNull(b.getPyramid(7, 1));
		
		b.removeFreeCell();
		assertNull(b.getFreeCell());
	}
	
	@Test
	public void testEmptyDeck() throws IOException {
		Board b = loadBoard("full.pys");
		assertFalse(b.isDeckEmpty());
		b.drawCardsToTalon();
		assertFalse(b.isDeckEmpty());
		for (int i = 0; i < 7; ++i) {
			b.drawCardsToTalon();
		}
		assertTrue(b.isDeckEmpty());
	}
	
	@Test
	public void testPyramidCardCount() throws IOException {
		Board b = loadBoard("full.pys");
		assertEquals(28, b.getPyramidCardCount());
		b = loadBoard("partial.pys");
		assertEquals(19, b.getPyramidCardCount());
		b = new Board();
		assertEquals(0, b.getPyramidCardCount());
	}
	
	@Test
	public void testCardsCovered() throws IOException {
		Board b = loadBoard("full.pys");
		assertEquals(0, b.getCardsCoveredCount(1, 1));
		assertEquals(1, b.getCardsCoveredCount(2, 1));
		assertEquals(1, b.getCardsCoveredCount(2, 2));
		assertEquals(2, b.getCardsCoveredCount(3, 1));
		assertEquals(3, b.getCardsCoveredCount(3, 2));
		assertEquals(2, b.getCardsCoveredCount(3, 3));
		assertEquals(3, b.getCardsCoveredCount(4, 1));
		assertEquals(5, b.getCardsCoveredCount(4, 2));
		assertEquals(5, b.getCardsCoveredCount(4, 3));
		assertEquals(3, b.getCardsCoveredCount(4, 4));
		assertEquals(15, b.getCardsCoveredCount(7, 4));
	}
	
	@Test
	public void testInitializeFromDeck() {
		Board b = new Board();
		assertEquals(0, b.getPyramidCardCount());
		assertEquals(52, b.getDeck().size());
		b = new Board(new Deck());
		assertEquals(28, b.getPyramidCardCount());
		assertEquals(24, b.getDeck().size());
		System.out.println(b);
	}
	
	@Test
	public void testGetPositionForCard() throws IOException {
		Board b = loadBoard("full.pys");
		b.drawCardsToTalon();
		new FreeCellMove(new PyramidPosition(b.getPyramid(7, 1), 7, 1)).execute(b);
		Position position = b.getPositionForCard("2H");
		assertEquals(new Card("2H"), position.getCard());
		assertTrue(position instanceof PyramidPosition);
		PyramidPosition pyramidPosition = (PyramidPosition)position;
		assertEquals(7, pyramidPosition.getRowNumber());
		assertEquals(4, pyramidPosition.getColumnNumber());
		
		position = b.getPositionForCard("6C");
		assertEquals(new Card("6C"), position.getCard());
		assertTrue(position instanceof TalonPosition);
		TalonPosition talonPosition = (TalonPosition)position;
		assertEquals(3, talonPosition.getColumn());
		
		position = b.getPositionForCard("8H");
		assertEquals(new Card("8H"), position.getCard());
		assertTrue(position instanceof FreeCellPosition);
		
		assertNull(b.getPositionForCard("QS"));
	}

	private Board loadBoard(String filename) throws IOException {
		Board b = new Board();
		b.loadBoard(filename);
		return b;
	}

}
