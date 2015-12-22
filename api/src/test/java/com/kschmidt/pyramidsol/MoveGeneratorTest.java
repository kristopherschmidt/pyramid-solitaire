package com.kschmidt.pyramidsol;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.List;

import org.junit.Before;
import org.junit.Test;


public class MoveGeneratorTest {

	private Board b;
	private MoveGenerator moveGenerator;

	@Before
	public void setUp() {
		b = new Board();
		moveGenerator = new MoveGenerator(b);
	}

	@Test
	public void testGenerateMovesWithNoTalon() throws IOException {
		b.loadBoard("full.pys");
		assertMoves(2, new RemoveCardsMove(new PyramidPosition("5C", 7, 2),
				new PyramidPosition("8H", 7, 1)), new RemoveCardsMove(
				new PyramidPosition("3H", 7, 5),
				new PyramidPosition("TC", 7, 3)));
	}

	@Test
	public void testGenerateRemovingKing() throws IOException {
		b.loadBoard("full.pys");
		b.drawCardsToTalon();
		assertMoves(3, new RemoveCardsMove(new TalonPosition("KS", 1)));
	}

	@Test
	public void testGenerateMoveFromTalonToPyramid() throws IOException {
		b.loadBoard("full.pys");
		b.drawCardsToTalon();
		b.drawCardsToTalon();
		assertMoves(4, new RemoveCardsMove(new PyramidPosition("4S", 7, 7),
				new TalonPosition("9D", 3)));
	}

	@Test
	public void testGenerateMoveFromFreeCell() throws IOException {
		// 9D in talon matches 4S moved to free cell
		b.loadBoard("full.pys");
		b.drawCardsToTalon();
		b.drawCardsToTalon();
		Card card = b.getPyramid(7, 7);
		new FreeCellMove(new PyramidPosition(card, 7, 7)).execute(b);
		assertMoves(4, new RemoveCardsMove(new FreeCellPosition("4S"),
				new TalonPosition("9D", 3)));
	}

	@Test
	public void testGenerateDeckMove() throws IOException {
		b.loadBoard("full.pys");
		List<Move> deckMove = moveGenerator.getDeckMoves();
		assertNotNull(deckMove);
		b.drawCardsToTalon();
		assertNotNull(deckMove);
		// pull everything else into the talon
		for (int i = 0; i < 7; ++i) {
			b.drawCardsToTalon();
		}
		assertTrue(moveGenerator.getDeckMoves().isEmpty());
	}

	@Test
	public void testGenerateFreeCellMoves() throws IOException {
		b.loadBoard("full.pys");
		List<Move> freeCellMoves = moveGenerator.getFreeCellMoves();
		assertEquals(7, freeCellMoves.size());
	}

	@Test
	public void testGenerateFreeCellMovesWhenFreeCellOccupied()
			throws IOException {
		b.loadBoard("full.pys");
		new FreeCellMove(new PyramidPosition("8H", 7, 1)).execute(b);
		List<Move> freeCellMoves = moveGenerator.getFreeCellMoves();
		assertTrue(freeCellMoves.isEmpty());
	}

	@Test
	public void testAllMoves() throws IOException {
		b.loadBoard("full.pys");
		List<Move> allMoves = moveGenerator.getAllMoves();
		assertEquals(10, allMoves.size());
		assertTrue(allMoves.contains(new DeckMove()));
		int freeCellMoves = 0;
		for (int i = 0; i < allMoves.size(); ++i) {
			if (allMoves.get(i) instanceof FreeCellMove) {
				freeCellMoves++;
			}
		}
		assertEquals(7, freeCellMoves);
	}

	/*********************** Next section tests actual moves ********************************************/

	// TODO does actually making the moves live here?
	@Test
	public void testActualMovesWithNoTalon() throws IOException {
		b.loadBoard("full.pys");
		makeAllMoves();
		// System.out.println("Board with moves made: "+b);
		Board b2 = loadBoard("fullremoved.pys");
		assertEquals(b, b2);
	}

	private List<Move> makeAllMoves() {
		List<Move> moves = moveGenerator.getRemovalMoves();
		for (int i = 0; i < moves.size(); ++i) {
			moves.get(i).execute(b);
		}
		return moves;
	}

	@Test
	public void testActualRemovingKing() throws IOException {
		b.loadBoard("full.pys");
		b.setPyramid(new Card("KD"), 7, 7);
		makeAllMoves();
		Board b2 = loadBoard("fullremoved.pys");
		b2.setPyramid((Card)null, 7, 7);
		assertEquals(b, b2);
	}

	@Test
	public void testActualMoveFromTalonToPyramid() throws IOException {
		b.loadBoard("full.pys");
		b.drawCardsToTalon();
		b.drawCardsToTalon();
		makeAllMoves();
		assertNull(b.getPyramid(7, 7));
		assertEquals(1, b.getTalon().getColumn(1).size());
		assertEquals(new Card("KS"), b.getTalon().getTopCard(1));
		assertEquals(2, b.getTalon().getColumn(2).size());
		assertEquals(new Card("QD"), b.getTalon().getTopCard(2));
		assertEquals(1, b.getTalon().getColumn(3).size());
		assertEquals(new Card("6C"), b.getTalon().getTopCard(3));
	}

	@Test
	public void testActualMovesTwice() throws IOException {
		b.loadBoard("full.pys");
		b.drawCardsToTalon();
		b.drawCardsToTalon();
		makeAllMoves();
		// takes off the king still left on talon column 1
		makeAllMoves();
		assertNull(b.getPyramid(7, 7));
		assertEquals(0, b.getTalon().getColumn(1).size());
		assertEquals(2, b.getTalon().getColumn(2).size());
		assertEquals(new Card("QD"), b.getTalon().getTopCard(2));
		assertEquals(1, b.getTalon().getColumn(3).size());
		assertEquals(new Card("6C"), b.getTalon().getTopCard(3));
	}

	private Board loadBoard(String filename) throws IOException {
		Board b = new Board();
		b.loadBoard(filename);
		return b;
	}

	private List<Move> assertMoves(int totalNumberOfMoves,
			Move... expectedMoves) {
		List<Move> actualMoves = moveGenerator.getRemovalMoves();
		assertEquals(totalNumberOfMoves, actualMoves.size());
		for (int i = 0; i < expectedMoves.length; ++i) {
			assertTrue("contains: " + expectedMoves[i],
					actualMoves.contains(expectedMoves[i]));
		}
		return actualMoves;
	}

}
