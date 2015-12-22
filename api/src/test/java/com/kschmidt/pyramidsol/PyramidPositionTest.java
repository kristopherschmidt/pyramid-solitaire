package com.kschmidt.pyramidsol;

import static org.junit.Assert.*;

import org.junit.Test;


/** lots of indirect tests through move classes, not covered here */
public class PyramidPositionTest {

	@Test
	public void test() {
		PyramidPosition position = new PyramidPosition(new Card("JS"), 6, 3);
		assertEquals(new Card("JS"), position.getCard());
		assertEquals(6, position.getRowNumber());
		assertEquals(3, position.getColumnNumber());
	}
	
}
