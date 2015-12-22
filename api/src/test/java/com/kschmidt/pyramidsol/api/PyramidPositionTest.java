package com.kschmidt.pyramidsol.api;

import static org.junit.Assert.*;

import org.junit.Test;

import com.kschmidt.pyramidsol.api.Card;
import com.kschmidt.pyramidsol.api.PyramidPosition;


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
