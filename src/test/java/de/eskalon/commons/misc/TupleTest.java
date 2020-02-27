package de.eskalon.commons.misc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class TupleTest {

	@SuppressWarnings({ "unlikely-arg-type", "unchecked", "rawtypes" })
	@Test
	public void test() {
		Tuple t = new Tuple("abc", Integer.valueOf(123));

		// Equals
		assertEquals("(abc,123)", t.toString());
		assertTrue(t.equals(t));
		assertTrue(!t.equals("asdf"));
		assertTrue(t.equals(new Tuple("abc", Integer.valueOf(123))));
		assertTrue(!t.equals(new Tuple("abc", null)));
		assertTrue(!t.equals(new Tuple(null, Integer.valueOf(123))));

		// Hashcode
		assertEquals(2988058, t.hashCode());
	}

}
