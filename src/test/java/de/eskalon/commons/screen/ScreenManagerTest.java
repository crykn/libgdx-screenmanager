package de.eskalon.commons.screen;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.concurrent.TimeoutException;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import de.eskalon.commons.screen.transition.ScreenTransition;
import de.eskalon.commons.utils.BasicInputMultiplexer;

public class ScreenManagerTest extends ScreenManagerUnitTest {

	private int i = 2, k = 0;
	private boolean firstRenderPassScreen2 = true;

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
		ScreenManager sm = getMockedScreenManager();
		sm.initialize(mult, 5, 5, false);

		ManagedScreen testScreen = new ManagedScreenAdapter() {

			{
				addInputProcessor(new InputAdapter());

				if (k == 0) {
					k = 1;
					assertEquals(2, i);
					i = 3;
				}
			}

			@Override
			public void show() {
				assertEquals(4, i);
				i = 5;
			}

			@Override
			public void hide() {
				assertEquals(6, i);
				i = 7;
			}

			@Override
			public void resize(int width, int height) {
			}

			@Override
			public boolean equals(Object obj) {
				return this == obj;
			}
		};

		ManagedScreen test2Screen = new ManagedScreenAdapter() {

			{
				addInputProcessor(new InputAdapter());
				addInputProcessor(new InputAdapter());

				assertEquals(3, i);
				i = 4;
			}

			@Override
			public void show() {
				assertEquals(5, i);
				i = 6;
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

		// Push screen
		sm.pushScreen(testScreen, null);
		assertEquals(0, mult.getProcessors().size);
		assertEquals(null, sm.getCurrentScreen());
		sm.render(1);
		assertEquals(1, mult.getProcessors().size);
		assertEquals(testScreen, sm.getCurrentScreen());

		// Push second screen
		sm.pushScreen(test2Screen, null);
		sm.render(1F);
		assertEquals(2, mult.getProcessors().size);
		assertEquals(7, i);
		i = 8;
		assertEquals(test2Screen, sm.getCurrentScreen());

		// Render screen 2
		sm.render(0.3F);
		sm.render(0.3F);
		sm.render(0.3F);
		sm.render(0.3F);
		assertEquals(12, i);
		i = 13;
	}

	@Test
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void testExceptions() {
		ScreenManager sm = getMockedScreenManager();
		ManagedScreen testScreen = new ManagedScreenAdapter() {
			@Override
			public void resize(int width, int height) {
			}
		};

		ScreenTransition testTransition = new ScreenTransition() {
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
			public void resize(int width, int height) {
			}

		};

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

		// Push screen
		assertThrows(NullPointerException.class, () -> {
			sm.pushScreen((ManagedScreen) null, null);
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
		ScreenManager sm = getMockedScreenManager();
		sm.initialize(Mockito.spy(new BasicInputMultiplexer()), 5, 5, false);

		String screen1Name = "s1";
		String screen2Name = "s2";
		String screen3Name = "s3";
		String transition1Name = "t1";
		String transition2Name = "t2";

		/*
		 * The last screen; rendered as part of a transition.
		 */
		ManagedScreen s1 = new ManagedScreenAdapter() {
			@Override
			public void resize(int width, int height) {
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
		};

		/*
		 * The current screen.
		 */
		ManagedScreen s2 = new ManagedScreenAdapter() {
			@Override
			public void resize(int width, int height) {
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
		};

		/*
		 * A screen that is queued, but never shown.
		 */
		ManagedScreen s3 = new ManagedScreenAdapter() {
			@Override
			public void show() {
				fail();
			}

			@Override
			public void hide() {
				fail();
			}

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
		};

		/*
		 * A never ending transition.
		 */
		ScreenTransition t1 = new ScreenTransition() {
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
			public void resize(int width, int height) {
				resizeCount++;
			}
		};

		/*
		 * A transition that is queued, but never shown.
		 */
		ScreenTransition t2 = new ScreenTransition() {
			@Override
			public void dispose() {
			}

			@Override
			public void show() {
				fail();
			}

			@Override
			public void hide() {
				fail();
			}

			@Override
			public void render(float delta, TextureRegion lastScreen,
					TextureRegion currScreen) {
				fail();
			}

			@Override
			public boolean isDone() {
				return false;
			}

			@Override
			public void resize(int width, int height) {
				fail();
			}
		};

		// Make screen1 the current screen
		sm.pushScreen(s1, null);
		sm.render(1);

		// Only screen1 is paused and resumed
		sm.pause();
		assertEquals(1, pauseCount);
		pauseState++;

		sm.resume();
		assertEquals(1, resumeCount);
		resumeState++;

		// Make screen2 the current screen; the transition is going on
		sm.pushScreen(s2, t1);
		sm.render(1);

		assertEquals(3, resizeCount); // the two screens and the transition were
										// automatically resized when they were
										// first shown

		sm.pushScreen(s3, t2); // these are never rendered, because t1 never
								// ends

		// resize()
		assertEquals(3, resizeCount); // nothing changes just by pushing another
										// screen
		sm.resize(5, 5); // ignored
		sm.resize(10, 10);
		assertEquals(6, resizeCount);
		sm.resize(10, 10); // ignored
		assertEquals(6, resizeCount);

		// only change width _or_ height
		sm.resize(10, 15);
		assertEquals(9, resizeCount);
		sm.resize(20, 15);
		assertEquals(12, resizeCount);

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
		ScreenManager sm = getMockedScreenManager();
		sm.initialize(new BasicInputMultiplexer(), 5, 5, false);

		ManagedScreen s1 = new ManagedScreenAdapter() {
			@Override
			public void resize(int width, int height) {
			}

			@Override
			public void dispose() {
				disposeCount++;
			}
		};

		ManagedScreen s2 = new ManagedScreenAdapter() {
			@Override
			public void resize(int width, int height) {
			}

			@Override
			public void dispose() {
				disposeCount++;
			}
		};

		ScreenTransition t1 = new ScreenTransition() {
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
			public void resize(int width, int height) {
			}
		};

		ScreenTransition t2 = new ScreenTransition() {
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
			public void resize(int width, int height) {
			}
		};

		sm.pushScreen(s1, t1);
		sm.render(1);
		sm.pushScreen(s2, t2);

		// Dispose everything
		sm.dispose();

		assertEquals(4, disposeCount);
	}

}
