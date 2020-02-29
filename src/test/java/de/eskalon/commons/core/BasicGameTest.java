package de.eskalon.commons.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;

public class BasicGameTest extends de.eskalon.commons.LibgdxUnitTest {

	@Test
	public void testConstructorAndMembers() {
		BasicGame game = new BasicGame();

		// DEV ENV & VERSION
		assertTrue(game.IN_DEV_ENV);
		assertEquals("Development", game.VERSION);

		// Input Processor
		game.create();

		assertNotNull(game.getInputMultiplexer());

		game.getInputMultiplexer().addProcessor(new InputAdapter() {
			@Override
			public boolean keyDown(int keycode) {
				assertEquals(3, keycode);

				return true;
			}
		});
		Gdx.input.getInputProcessor().keyDown(3);

		// Resize
		game.resize(123, 456);
		assertEquals(game.getWidth(), 123);
		assertEquals(game.getHeight(), 456);
	}

}
