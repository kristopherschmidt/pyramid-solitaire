package com.kschmidt.pyramidsol.persistence;

import java.io.IOException;

import org.junit.Ignore;
import org.junit.Test;

import com.kschmidt.pyramidsol.Board;

public class MongoBoardRepositoryTest {

	@Test
	@Ignore
	public void testSaveAndRetrieveStartingBoard() throws IOException {
		Board b = new Board();
		b.loadBoard("full.pys");
		MongoBoardRepository repo = new MongoBoardRepository();
		repo.save(b);
		// Board b2 = repo.loadBoard("full.pys");
		// assertEquals(b, b2);
	}
	
	@Test
	@Ignore
	public void testRetrieveStartingBoard() throws IOException {
		MongoBoardRepository repo = new MongoBoardRepository();
		repo.load();
		// Board b2 = repo.loadBoard("full.pys");
		// assertEquals(b, b2);
	}

}
