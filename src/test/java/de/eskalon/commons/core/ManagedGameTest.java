package de.eskalon.commons.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.badlogic.gdx.Gdx;

import de.eskalon.commons.LibgdxUnitTest;
import de.eskalon.commons.input.BasicInputMultiplexer;
import de.eskalon.commons.screen.ManagedScreen;
import de.eskalon.commons.screen.ScreenManager;
import de.eskalon.commons.screen.transition.ScreenTransition;

public class ManagedGameTest extends LibgdxUnitTest {

	private int i = 1;

	@Test
	@SuppressWarnings({ "rawtypes" })
	public void testConstrcutor() {
		ManagedGame game = new ManagedGame();

		// Screen Manager
		assertNotNull(game.getScreenManager());
	}

	/**
	 * Tests whether the corresponding methods are called in the screen manager.
	 */
	@Test
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void testCorrespondingScreenManagerMethods() {
		ManagedGame game = new ManagedGame();

		// Mock the initBuffers method as it is using open gl stuff
		game.screenManager = new ScreenManager<ManagedScreen, ScreenTransition>(
				Mockito.spy(new BasicInputMultiplexer()), 5, 5) {

			@Override
			public void initBuffers() {
				assertEquals(1, i);
				i++;
			}

			@Override
			public void render(float delta) {
				assertEquals(Gdx.graphics.getDeltaTime(), delta);
				assertEquals(2, i);
				i++;
			}

			@Override
			public void resize(int width, int height) {
				assertEquals(3, width);
				assertEquals(4, height);
			}

			@Override
			public void pause() {
				assertEquals(3, i);
				i++;
			}

			@Override
			public void resume() {
				assertEquals(4, i);
				i++;
			}

			@Override
			public void dispose() {
				assertEquals(5, i);
				i++;
			}
		};

		game.create();
		game.render();
		game.resize(3, 4);
		game.pause();
		game.resume();
		game.dispose();
	}

}
