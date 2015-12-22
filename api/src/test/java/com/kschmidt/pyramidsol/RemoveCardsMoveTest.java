package com.kschmidt.pyramidsol;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

public class RemoveCardsMoveTest {

	private Board b;
	private Talon t;

	@Before
	public void setUp() throws IOException {
		b = new Board();
		b.loadBoard("full.pys");
		t = b.getTalon();
	}

	@Test
	public void testPyramidToPyramid() throws IOException {
		Move m = new RemoveCardsMove(new PyramidPosition("8H", 7, 1),
				new PyramidPosition("5S", 7, 2));
		m.execute(b);
		assertNull(b.getPyramid(7, 1));
		assertNull(b.getPyramid(7, 2));
		m.undo(b);
		assertEquals(new Card("8H"), b.getPyramid(7, 1));
		assertEquals(new Card("5S"), b.getPyramid(7, 2));
	}

	@Test
	public void testKingToPyramid() throws IOException {
		// puts board in illegal state but oh well, easiest way to get a
		// removable king in pyramid
		b.setPyramid("KS", 7, 4);
		Move m = new RemoveCardsMove(new PyramidPosition("KS", 7, 4));
		m.execute(b);
		assertNull(b.getPyramid(7, 4));
		m.undo(b);
		assertEquals(new Card("KS"), b.getPyramid(7, 4));
	}

	@Test
	public void testTalonToPyramid() {
		b.drawCardsToTalon();
		b.drawCardsToTalon();
		Move m = new RemoveCardsMove(new PyramidPosition("4S", 7, 7),
				new TalonPosition("9D", 3));
		m.execute(b);
		assertNull(b.getPyramid(7, 7));
		assertEquals(new Card("6C"), t.getTopCard(3));
		m.undo(b);
		assertEquals(new Card("4S"), b.getPyramid(7, 7));
		assertEquals(new Card("9D"), t.getTopCard(3));
	}

	@Test
	public void testTalonToTalon() throws IOException {
		// no pairs in full.pys talon draws so make one up, using full.pys talon
		// cards, so the board is legal
		Talon talon = new Talon();
		b.setTalon(talon);
		talon.addCards("QH", "AC", "6D");
		Move m = new RemoveCardsMove(new TalonPosition("QH", 1),
				new TalonPosition("AC", 2));
		m.execute(b);
		assertNull(talon.getTopCard(1));
		assertNull(talon.getTopCard(2));
		m.undo(b);
		assertEquals(new Card("QH"), talon.getTopCard(1));
		assertEquals(new Card("AC"), talon.getTopCard(2));
	}

	@Test
	public void testKingToTalon() throws IOException {
		b.drawCardsToTalon();
		Move m = new RemoveCardsMove(new TalonPosition("KS", 1));
		m.execute(b);
		assertNull(t.getTopCard(1));
		m.undo(b);
		assertEquals(new Card("KS"), t.getTopCard(1));
	}

	@Test
	public void testKingToFreeCell() {
		b.drawCardsToTalon();
		Move m = new FreeCellMove(new TalonPosition("KS", 1));
		m.execute(b);
		assertEquals(new Card("KS"), b.getFreeCell());
		m = new RemoveCardsMove(new FreeCellPosition("KS"));
		m.execute(b);
		assertNull(b.getFreeCell());
		m.undo(b);
		assertEquals(new Card("KS"), b.getFreeCell());
	}

	@Test
	public void testFreeCellToPyramid() {
		Move m = new FreeCellMove(new PyramidPosition("8H", 7, 1));
		m.execute(b);
		assertEquals(new Card("8H"), b.getFreeCell());
		m = new RemoveCardsMove(new PyramidPosition("5C", 7, 2),
				new FreeCellPosition("8H"));
		m.execute(b);
		assertNull(b.getPyramid(7, 1));
		assertNull(b.getPyramid(7, 2));
		assertNull(b.getFreeCell());
		m.undo(b);
		assertNull(b.getPyramid(7, 1));
		assertEquals(new Card("5C"), b.getPyramid(7, 2));
		assertEquals(new Card("8H"), b.getFreeCell());
	}

	@Test
	public void testFreeCellToTalon() {
		b.drawCardsToTalon();
		b.drawCardsToTalon();
		Move m = new FreeCellMove(new PyramidPosition("4S", 7, 7));
		m.execute(b);
		assertEquals(new Card("4S"), b.getFreeCell());
		m = new RemoveCardsMove(new FreeCellPosition("4S"), new TalonPosition(
				"9D", 3));
		m.execute(b);
		assertEquals(new Card("6C"), b.getTalon().getTopCard(3));
		assertNull(b.getFreeCell());
		m.undo(b);
		assertEquals(new Card("9D"), b.getTalon().getTopCard(3));
		assertEquals(new Card("4S"), b.getFreeCell());
	}

	@Test
	public void testForSpecificCardInMove() {
		// don't set up a board, shouldn't need it
		RemoveCardsMove m = new RemoveCardsMove(
				new PyramidPosition("7S", 3, 1),
				new PyramidPosition("6S", 4, 3));
		assertTrue(m.containsCardValue(6));
		assertTrue(m.containsCardValue(7));
		assertFalse(m.containsCardValue(3));

		m = new RemoveCardsMove(new TalonPosition("KD", 3));
		assertTrue(m.containsCardValue(13));
		assertFalse(m.containsCardValue(5));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testCantRemoveCardsNotTotalingThirteen() {
		new RemoveCardsMove(new PyramidPosition("7S", 3, 1),
				new PyramidPosition("8H", 4, 3));
	}
	

	@Test(expected = IllegalArgumentException.class)
	public void testCantRemoveSingleCardNotTotalingThirteen() {
		new RemoveCardsMove(new PyramidPosition("7S", 3, 1));
	}

	// freecell-talon
}
