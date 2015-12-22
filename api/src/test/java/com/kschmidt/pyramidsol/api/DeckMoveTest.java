package com.kschmidt.pyramidsol.api;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

import com.kschmidt.pyramidsol.api.Board;
import com.kschmidt.pyramidsol.api.Card;
import com.kschmidt.pyramidsol.api.DeckMove;


public class DeckMoveTest {

	@Test
	public void test() throws IOException {
		Board b = new Board();
		b.loadBoard("full.pys");
		assertTrue(b.getTalon().isEmpty());
		new DeckMove().execute(b);
		assertFalse(b.getTalon().isEmpty());
	}
	
	@Test
	public void testUndo() throws IOException {
		Board b = new Board();
		b.loadBoard("full.pys");
		new DeckMove().execute(b);
		assertFalse(b.getTalon().isEmpty());
		assertEquals(new Card("KC"), b.getDeck().getCard(1));
		new DeckMove().undo(b);
		assertTrue(b.getTalon().isEmpty());
		assertEquals(new Card("KS"), b.getDeck().getCard(1));
		assertEquals(new Card("QH"), b.getDeck().getCard(2));
		assertEquals(new Card("6C"), b.getDeck().getCard(3));
	}

}
