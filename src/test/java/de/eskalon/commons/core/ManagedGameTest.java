package de.eskalon.commons.core;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collection;

import org.junit.jupiter.api.Test;

import de.eskalon.commons.LibgdxUnitTest;
import de.eskalon.commons.screen.IScreen;
import de.eskalon.commons.screen.IScreenManager;
import de.eskalon.commons.screen.transition.IScreenTransition;

public class ManagedGameTest extends LibgdxUnitTest {

	@SuppressWarnings("rawtypes")
	@Test
	public void testConstructor() {
		ManagedGame game = new ManagedGame() {
			@SuppressWarnings("unchecked")
			@Override
			public void create() {
				this.screenManager = new IScreenManager() {

					@Override
					public void addScreen(String name, IScreen screen) {
					}

					@Override
					public IScreen getScreen(String name) {
						return null;
					}

					@Override
					public Collection getScreens() {
						return null;
					}

					@Override
					public void addScreenTransition(String name,
							IScreenTransition screen) {
					}

					@Override
					public IScreenTransition getScreenTransition(String name) {
						return null;
					}

					@Override
					public Collection getScreenTransitions() {
						return null;
					}

					@Override
					public void pushScreen(String name, String transition) {
					}

					@Override
					public IScreen getCurrentScreen() {
						return null;
					}

					@Override
					public void resize(int width, int height) {
					}

					@Override
					public void render(float deltaTime) {
					}

					@Override
					public boolean inTransition() {
						return false;
					}

				};
			}
		};

		assertThrows(IllegalStateException.class, () -> {
			game.resize(1, 1);
		});
		assertThrows(IllegalStateException.class, () -> {
			game.render();
		});

		game.create();
		game.resize(1, 1);
		game.render();

		assertTrue(game.isFocused());
		game.pause();
		assertTrue(!game.isFocused());
		game.resume();
		assertTrue(game.isFocused());
	}

}
