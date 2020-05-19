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
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;

import de.eskalon.commons.LibgdxUnitTest;
import de.eskalon.commons.screen.transition.ScreenTransition;
import de.eskalon.commons.utils.BasicInputMultiplexer;

public class ScreenManagerTest extends LibgdxUnitTest {

	private int i = 1, k = 0;
	private boolean firstRenderPassScreen2 = true;
	private ManagedScreen testScreen;

	private static int width = 123, height = 234;

	/**
	 * Tests whether create(), show() and hide() and are called at the right
	 * time and the input listeners are registered/unregistered. Furthermore the
	 * exceptions for addScreen(...)/addScreenTransition(...)/pushScreen(...)
	 * are tested.
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test
	public void testScreenLifecycle() throws TimeoutException,
			InterruptedException, NoSuchFieldException, SecurityException {
		BasicInputMultiplexer mult = new BasicInputMultiplexer();
		ScreenManager sm = Mockito.spy(new ScreenManager());
		// Mock initBuffers() as it is using open gl stuff
		Mockito.doNothing().when(sm).initBuffers();
		sm.initialize(mult, 5, 5, false);

		testScreen = new TestScreen() {
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
			public void hide() {
				assertEquals(6, i);
				i = 7;
			}

			@Override
			public void resize(int width, int height) {
				assertEquals(ScreenManagerTest.width, width);
				assertEquals(ScreenManagerTest.height, height);
			}

			@Override
			public boolean equals(Object obj) {
				return this == obj;
			}
		};

		ManagedScreen test2Screen = new TestScreen() {
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
	}

	@Test
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void testExceptions() {
		ScreenManager sm = Mockito.spy(new ScreenManager());
		String screenName = "s1";
		String transitionName = "t1";
		ManagedScreen testScreen = new TestScreen() {
			@Override
			public void resize(int width, int height) {
			}
		};
		sm.addScreen(screenName, testScreen);

		ScreenTransition testTransition = new ScreenTransition() {
			@Override
			public void dispose() {
			}

			@Override
			protected void create() {
			}

			@Override
			public void render(float delta, TextureRegion lastScreen,
					TextureRegion currScreen) {
			}

			@Override
			public boolean isDone() {
				return false;
			}

			@Override
			public void resize(int width, int height) {
			}

		};
		sm.addScreenTransition(transitionName, testTransition);

		// Screen manager not initalized
		assertThrows(IllegalStateException.class, () -> {
			sm.render(1f);
		});
		assertThrows(IllegalStateException.class, () -> {
			sm.pause();
		});
		assertThrows(IllegalStateException.class, () -> {
			sm.resume();
		});
		assertThrows(IllegalStateException.class, () -> {
			sm.resize(5, 5);
		});

		// Add screen
		assertThrows(IllegalArgumentException.class, () -> {
			sm.addScreen("", testScreen);
		});

		assertThrows(NullPointerException.class, () -> {
			sm.addScreen("", null);
		});

		// Add transition
		assertThrows(IllegalArgumentException.class, () -> {
			sm.addScreenTransition("", testTransition);
		});

		assertThrows(NullPointerException.class, () -> {
			sm.addScreenTransition("", null);
		});

		// Push screen
		assertThrows(IllegalArgumentException.class, () -> {
			sm.pushScreen("", null);
		});

		assertThrows(NoSuchElementException.class, () -> {
			sm.pushScreen("123", null);
		});

		assertThrows(NoSuchElementException.class, () -> {
			sm.pushScreen(screenName, "123");
		});

		// Get screen
		assertThrows(IllegalArgumentException.class, () -> {
			sm.getScreen("");
		});

		assertThrows(NoSuchElementException.class, () -> {
			sm.getScreen("123");
		});

		// Get screen transition
		assertThrows(IllegalArgumentException.class, () -> {
			sm.getScreenTransition("");
		});

		assertThrows(NoSuchElementException.class, () -> {
			sm.getScreenTransition("123");
		});
	}

	int resizeCount = 0;
	int pauseState = 0;
	int pauseCount = 0;
	int resumeState = 0;
	int resumeCount = 0;

	/**
	 * Tests whether pause(), resume() and resize() are called on the right
	 * screens.
	 */
	@Test
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void testApplicationListenerEvents() {
		ScreenManager sm = Mockito.spy(new ScreenManager());
		// Mock the initBuffers & the screenToTexture method as they are using
		// open gl stuff
		Mockito.doNothing().when(sm).initBuffers();
		Mockito.doReturn(null).when(sm).screenToTexture(Mockito.any(),
				Mockito.any(), Mockito.anyFloat());
		sm.initialize(Mockito.spy(new BasicInputMultiplexer()), 5, 5, false);

		String screen1Name = "s1";
		String screen2Name = "s2";
		String screen3Name = "s3";
		String transition1Name = "t1";
		String transition2Name = "t2";

		/*
		 * The last screen; rendered as part of a transition.
		 */
		sm.addScreen(screen1Name, new TestScreen() {
			@Override
			public void resize(int width, int height) {
				if (width == 5 || height == 5)
					fail();

				resizeCount++;
			}

			@Override
			public void pause() {
				pauseCount++;
			}

			@Override
			public void resume() {
				resumeCount++;
			}
		});

		/*
		 * The current screen.
		 */
		sm.addScreen(screen2Name, new TestScreen() {
			@Override
			public void resize(int width, int height) {
				if (width == 5 || height == 5)
					fail();

				resizeCount++;
			}

			@Override
			public void pause() {
				pauseCount++;

				if (pauseState == 0)
					fail();
			}

			@Override
			public void resume() {
				resumeCount++;

				if (resumeState == 0)
					fail();
			}
		});

		/*
		 * A screen that is neither initialized nor shown.
		 */
		sm.addScreen(screen3Name, new TestScreen() {
			@Override
			public void resize(int width, int height) {
				fail(); // should never be resized
			}

			@Override
			public void pause() {
				fail(); // should not be paused
			}

			@Override
			public void resume() {
				fail(); // should not be resumed
			}
		});

		/*
		 * A never ending transition.
		 */
		sm.addScreenTransition(transition1Name, new ScreenTransition() {
			@Override
			public void dispose() {
			}

			@Override
			public void render(float delta, TextureRegion lastScreen,
					TextureRegion currScreen) {
			}

			@Override
			public boolean isDone() {
				return false;
			}

			@Override
			protected void create() {
			}

			@Override
			public void resize(int width, int height) {
				if (width == 5 || height == 5)
					fail();

				resizeCount++;
			}
		});

		/*
		 * A transition that is not initialized
		 */
		sm.addScreenTransition(transition2Name, new ScreenTransition() {
			@Override
			public void dispose() {
			}

			@Override
			public void render(float delta, TextureRegion lastScreen,
					TextureRegion currScreen) {
			}

			@Override
			public boolean isDone() {
				return false;
			}

			@Override
			protected void create() {
			}

			@Override
			public void resize(int width, int height) {
				fail();
			}
		});

		// Make screen1 the current screen
		sm.pushScreen(screen1Name, null);
		sm.render(1);

		// Only screen1 is paused and resumed
		sm.pause();
		assertEquals(1, pauseCount);
		pauseState++;

		sm.resume();
		assertEquals(1, resumeCount);
		resumeState++;

		// Make screen2 the current screen; the transition is going on
		sm.pushScreen(screen2Name, transition1Name);
		sm.render(1);

		// resize()
		sm.resize(5, 5); // ignored
		sm.resize(10, 10);
		assertEquals(3, resizeCount);
		sm.resize(10, 10); // ignored
		assertEquals(3, resizeCount);

		// only change width _or_ height
		sm.resize(10, 15);
		assertEquals(6, resizeCount);
		sm.resize(20, 15);
		assertEquals(9, resizeCount);

		// pause() & resume()
		sm.pause();
		assertEquals(3, pauseCount);

		sm.resume();
		assertEquals(3, resumeCount);
	}

	private int disposeCount = 0;

	/**
	 * Tests the functionality of the dispose() method.
	 */
	@Test
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void testDispose() throws NoSuchFieldException, SecurityException {
		ScreenManager sm = Mockito.spy(new ScreenManager());

		// Mock the private fbo fields
		FieldSetter.setField(sm,
				ScreenManager.class.getDeclaredField("lastFBO"),
				Mockito.mock(FrameBuffer.class));
		FieldSetter.setField(sm,
				ScreenManager.class.getDeclaredField("currFBO"),
				Mockito.mock(FrameBuffer.class));

		sm.addScreen("s1", new TestScreen() {
			@Override
			public void resize(int width, int height) {
			}

			@Override
			public void dispose() {
				disposeCount++;
			}
		});

		sm.addScreen("s2", new TestScreen() {
			@Override
			public void resize(int width, int height) {
			}

			@Override
			public void dispose() {
				disposeCount++;
			}
		});

		sm.addScreenTransition("t1", new ScreenTransition() {
			@Override
			public void dispose() {
				disposeCount++;
			}

			@Override
			public void render(float delta, TextureRegion lastScreen,
					TextureRegion currScreen) {
			}

			@Override
			public boolean isDone() {
				return false;
			}

			@Override
			protected void create() {
			}

			@Override
			public void resize(int width, int height) {
			}
		});

		sm.addScreenTransition("t2", new ScreenTransition() {
			@Override
			public void dispose() {
				disposeCount++;
			}

			@Override
			public void render(float delta, TextureRegion lastScreen,
					TextureRegion currScreen) {
			}

			@Override
			public boolean isDone() {
				return false;
			}

			@Override
			protected void create() {
			}

			@Override
			public void resize(int width, int height) {
			}
		});

		sm.getScreen("s2").initializeScreen();
		sm.getScreenTransition("t2").initializeScreenTransition();

		// Dispose everything
		sm.dispose();

		assertEquals(4, disposeCount);
	}

	/**
	 * Implements some empty default methods to reduce boilerplate code.
	 */
	abstract class TestScreen extends ManagedScreen {
		@Override
		protected void create() {
		}

		@Override
		public void hide() {
		}

		@Override
		public void render(float delta) {
		}

		@Override
		public void dispose() {
		}
	}

}
