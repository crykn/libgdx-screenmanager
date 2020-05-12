package de.eskalon.commons.core;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class BasicGameTest extends de.eskalon.commons.LibgdxUnitTest {

	@Test
	public void testConstructorAndMembers() {
		BasicApplication game = new BasicApplication();

		// Resize
		game.resize(123, 456);
		assertEquals(game.getWidth(), 123);
		assertEquals(game.getHeight(), 456);
	}

}