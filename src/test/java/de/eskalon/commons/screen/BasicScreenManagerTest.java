package de.eskalon.commons.screen;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.NoSuchElementException;
import java.util.concurrent.TimeoutException;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.internal.util.reflection.FieldSetter;

import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;

import de.eskalon.commons.LibgdxUnitTest;
import de.eskalon.commons.core.BasicGame;
import de.eskalon.commons.input.BasicInputMultiplexer;

public class BasicScreenManagerTest extends LibgdxUnitTest {

	private int i = 1, k = 0;
	private boolean firstRenderPassScreen2 = true;
	private BasicScreen testScreen;

	private static int width = 123, height = 234;

	/**
	 * Tests whether create(), show(), hide() and dispose() are called at the
	 * right time and the input listeners are registered/unregistered.
	 * Furthermore the exceptions for
	 * addScreen(...)/addScreenTransition(...)/pushScreen(...) are tested.
	 */
	@Test
	public void testScreenHandling() throws TimeoutException,
			InterruptedException, NoSuchFieldException, SecurityException {
		BasicInputMultiplexer mult = new BasicInputMultiplexer();
		BasicScreenManager sm = Mockito.spy(new BasicScreenManager(mult));

		// Mock the private fbo fields
		FieldSetter.setField(sm,
				BasicScreenManager.class.getDeclaredField("lastFBO"),
				Mockito.mock(FrameBuffer.class));
		FieldSetter.setField(sm,
				BasicScreenManager.class.getDeclaredField("currFBO"),
				Mockito.mock(FrameBuffer.class));

		testScreen = new BasicScreen() {
			@Override
			public void show() {
				initializeScreen(); // instead of super.show();
				assertEquals(3, i);
				i = 4;
			}

			@Override
			protected void create() {
				addInputProcessor(new InputAdapter());

				if (k == 0) { // First pass
					k = 1;
					assertEquals(2, i);
					i = 3;
				} else { // Second manual pass shouldn't trigger this method
							// again
					fail();
				}
			}

			@Override
			public void render(float delta) {
			}

			@Override
			public void hide() {
				assertEquals(6, i);
				i = 7;
			}

			@Override
			public void dispose() {
				assertEquals(13, i);
				i = 14;
			}

			@Override
			public void resize(int width, int height) {
				assertEquals(BasicScreenManagerTest.width, width);
				assertEquals(BasicScreenManagerTest.height, height);
			}

			@Override
			public boolean equals(Object obj) {
				return this == obj;
			}
		};

		BasicScreen test2Screen = new BasicScreen() {
			@Override
			public void show() {
				initializeScreen(); // instead of super.show();
				assertEquals(5, i);
				i = 6;
			}

			@Override
			protected void create() {
				addInputProcessor(new InputAdapter());
				addInputProcessor(new InputAdapter());

				assertEquals(4, i);
				i = 5;
			}

			@Override
			public void render(float delta) {
				if (firstRenderPassScreen2) // the first render pass happens
											// when the transition is polled; so
											// ignore that one
					firstRenderPassScreen2 = false;
				else
					i++;
			}

			@Override
			public void hide() {
			}

			@Override
			public void dispose() {
				assertEquals(14, i);

				i = 15;
			}

			@Override
			public void resize(int width, int height) {
			}

			@Override
			public boolean equals(Object obj) {
				return this == obj;
			}
		};

		String screenName = "Test";
		String screen2Name = "Test2";

		// Add screens
		sm.addScreen(screenName, testScreen);
		assertEquals(1, i);
		i = 2;
		assertEquals(testScreen, sm.getScreen(new String(screenName)));
		assertEquals(null, sm.getCurrentScreen());
		assertEquals(0, mult.getProcessors().size);

		// Push screen
		assertEquals(testScreen.isInitialized(), false);
		sm.pushScreen(new String(screenName), null);
		assertEquals(0, mult.getProcessors().size);
		sm.render(1);
		assertEquals(1, mult.getProcessors().size);
		assertEquals(testScreen.isInitialized(), true);
		assertEquals(testScreen, sm.getCurrentScreen());

		// Add new screen; load it; and then push it
		sm.addScreen(screen2Name, test2Screen);
		test2Screen.initializeScreen();
		sm.pushScreen(new String(screen2Name), null);
		sm.render(1F);
		assertEquals(2, mult.getProcessors().size);
		assertEquals(7, i);
		i = 8;
		assertEquals(test2Screen.isInitialized(), true);
		assertEquals(test2Screen, sm.getCurrentScreen());

		assertEquals(2, sm.getScreens().size());

		// Render screen 2
		sm.render(0.3F);
		sm.render(0.3F);
		sm.render(0.3F);
		sm.render(0.3F);
		assertEquals(12, i);
		i = 13;

		// Try to load the first screen manually
		testScreen.initializeScreen();

		// Check for exceptions
		assertThrows(IllegalArgumentException.class, () -> {
			sm.addScreen("", testScreen);
		});

		assertThrows(NullPointerException.class, () -> {
			sm.addScreen("", null);
		});

		assertThrows(IllegalArgumentException.class, () -> {
			sm.pushScreen("", null);
		});

		assertThrows(NoSuchElementException.class, () -> {
			sm.pushScreen("123", null);
		});

		assertThrows(NoSuchElementException.class, () -> {
			sm.pushScreen(screenName, "123");
		});

		assertThrows(IllegalArgumentException.class, () -> {
			sm.getScreen("");
		});

		assertThrows(NoSuchElementException.class, () -> {
			sm.getScreen("123");
		});

		assertThrows(IllegalArgumentException.class, () -> {
			sm.getScreenTransition("");
		});

		assertThrows(NoSuchElementException.class, () -> {
			sm.getScreenTransition("123");
		});

		sm.resize(width, height);

		// Dispose everything
		sm.dispose();
	}

}
