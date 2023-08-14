package de.eskalon.commons.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.backends.headless.mock.graphics.MockGraphics;

import de.eskalon.commons.LibgdxUnitTest;
import de.eskalon.commons.screen.ManagedScreen;
import de.eskalon.commons.screen.ScreenManager;
import de.eskalon.commons.screen.transition.ScreenTransition;
import de.eskalon.commons.utils.BasicInputMultiplexer;

public class ManagedGameTest extends LibgdxUnitTest {

	private int i = 0;

	@Test
	@SuppressWarnings({ "rawtypes" })
	public void testConstructor() {
		ManagedGame game = new ManagedGame();

		// Screen Manager
		assertNotNull(game.getScreenManager());
	}

	@Test
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void testInputMultiplexer() {
		ManagedGame game = new ManagedGame();
		game.screenManager = Mockito.mock(ScreenManager.class);
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
	}

	/**
	 * Tests whether the corresponding methods are called in the screen manager.
	 */
	@Test
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void testCorrespondingScreenManagerMethods() {
		Gdx.graphics = Mockito.spy(MockGraphics.class);
		Mockito.when(Gdx.graphics.getWidth()).thenReturn(5);
		Mockito.when(Gdx.graphics.getHeight()).thenReturn(7);

		ManagedGame game = new ManagedGame();
		game.screenManager = new ScreenManager<ManagedScreen, ScreenTransition>() {

			@Override
			public void initialize(BasicInputMultiplexer gameInputMultiplexer,
					int width, int height, boolean hasDepth) {
				assertEquals(0, i);
				i++;
				assertEquals(width, 5);
				assertEquals(height, 7);

				super.initialize(gameInputMultiplexer, width, height, hasDepth);
			}

			@Override
			public void initBuffers() {
				// Mock the initBuffers method as it is using open gl stuff
			}

			@Override
			public void render(float delta) {
				assertEquals(Gdx.graphics.getDeltaTime(), delta);
				assertEquals(1, i);
				i++;
			}

			@Override
			public void resize(int width, int height) {
				assertEquals(3, width);
				assertEquals(4, height);
			}

			@Override
			public void pause() {
				assertEquals(2, i);
				i++;
			}

			@Override
			public void resume() {
				assertEquals(3, i);
				i++;
			}

			@Override
			public void dispose() {
				assertEquals(4, i);
				i++;
			}
		};
		game.create(); // should initialize the screen manager
		game.render();
		game.resize(3, 4);
		game.pause();
		game.resume();
		game.dispose();
	}

}
