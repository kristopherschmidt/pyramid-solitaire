package com.kschmidt.pyramidsol.api;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

import com.kschmidt.pyramidsol.api.Board;
import com.kschmidt.pyramidsol.api.Card;
import com.kschmidt.pyramidsol.api.FreeCellMove;
import com.kschmidt.pyramidsol.api.PyramidPosition;
import com.kschmidt.pyramidsol.api.TalonPosition;


public class FreeCellMoveTest {

	@Test
	public void testFromPyramid() throws IOException {
		Board b = new Board();
		b.loadBoard("full.pys");
		FreeCellMove move = new FreeCellMove(new PyramidPosition("5C", 7, 2));
		move.execute(b);
		assertNull(b.getPyramid(7, 2));
		assertEquals(new Card("5C"), b.getFreeCell());
	}
	
	@Test
	public void testFromTalon() throws IOException {
		Board b = new Board();
		b.loadBoard("full.pys");
		b.drawCardsToTalon();
		FreeCellMove move = new FreeCellMove(new TalonPosition("6C", 3));
		move.execute(b);
		assertNull(b.getTalon().getTopCard(3));
		assertEquals(new Card("6C"), b.getFreeCell());
	}
	
	@Test(expected=IllegalStateException.class)
	public void testWhenFreeCellIsOccupied() throws IOException {
		Board b = new Board();
		b.loadBoard("full.pys");
		b.drawCardsToTalon();
		//move the 5C in
		FreeCellMove move = new FreeCellMove(new PyramidPosition("5C", 7, 2));
		move.execute(b);
		//now we should not be able to move the 6C in
		move = new FreeCellMove(new TalonPosition("6C", 3));
		move.execute(b);
	}
	
	@Test
	public void testUndoFromPyramid() throws IOException {
		Board b = new Board();
		b.loadBoard("full.pys");
		FreeCellMove move = new FreeCellMove(new PyramidPosition("5C", 7, 2));
		move.execute(b);
		assertNull(b.getPyramid(7, 2));
		assertEquals(new Card("5C"), b.getFreeCell());
		move.undo(b);
		assertEquals(new Card("5C"), b.getPyramid(7, 2));
		assertTrue(b.isFreeCellEmpty());
	}
	
	@Test
	public void testUndoFromTalon() throws IOException {
		Board b = new Board();
		b.loadBoard("full.pys");
		b.drawCardsToTalon();
		FreeCellMove move = new FreeCellMove(new TalonPosition("6C", 3));
		move.execute(b);
		assertNull(b.getTalon().getTopCard(3));
		assertEquals(new Card("6C"), b.getFreeCell());
		move.undo(b);
		assertEquals(new Card("6C"), b.getTalon().getTopCard(3));
		assertTrue(b.isFreeCellEmpty());
	}

}
