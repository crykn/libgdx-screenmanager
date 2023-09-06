package de.eskalon.commons.screen;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.concurrent.TimeoutException;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import de.eskalon.commons.screen.transition.ScreenTransition;
import de.eskalon.commons.utils.BasicInputMultiplexer;
import de.eskalon.commons.utils.ScreenFboUtils;

public class ScreenManagerTest2 extends ScreenManagerUnitTest {

	private int i = 0;
	private int k = 0;
	private int z = 0;

	private boolean firstRenderPassTransition = true,
			firstRenderPassScreen2 = true;

	/**
	 * Tests whether the screens are shown and hidden correctly while a
	 * transition is rendering as well as whether the input handlers are
	 * unregistered while transitioning.
	 */
	@SuppressWarnings({ "unchecked" })
	@Test
	public void testScreenLifecycleWhileTransition()
			throws TimeoutException, InterruptedException {
		BasicInputMultiplexer mult = new BasicInputMultiplexer();
		ScreenManager<ManagedScreen, ScreenTransition> sm = getMockedScreenManager();
		sm.initialize(mult, 5, 5, false);

		ManagedScreen testScreen = new ManagedScreen() {

			{
				addInputProcessor(new InputAdapter());
			}

			@Override
			public void show() {
				assertEquals(0, i);
				i = 1;
			}

			@Override
			public void render(float delta) {
				z++;
				switch (z) { // ignore the first render pass when this screen is
								// pushed
				case 2: { // second render pass; while the transition is
							// rendered
					assertEquals(2, i);
					i = 3;
					break;
				}
				case 3: {
					assertEquals(15, (int) delta);
					assertEquals(5, i);
					i = 6;
					break;
				}
				case 4: {
					fail();
				}
				}
			}

			@Override
			public void hide() {
				assertEquals(8, i);
				i = 9;
			}

			@Override
			public void dispose() {
			}

			@Override
			public void resize(int width, int height) {
			}

			@Override
			public boolean equals(Object obj) {
				return this == obj;
			}
		};

		ManagedScreen test2Screen = new ManagedScreen() {

			{
				addInputProcessor(new InputAdapter());
				addInputProcessor(new InputAdapter());
				addInputProcessor(new InputAdapter());
			}

			@Override
			public void show() {
				assertEquals(1, i);
				i = 2;
			}

			@Override
			public void render(float delta) {
				if (firstRenderPassScreen2) { // ignore the first render pass
												// when the second screen is
												// pushed
					firstRenderPassScreen2 = false;
					assertEquals(3, i);
					i = 4;
				} else {
					if (delta == 15) {
						assertEquals(6, i);
						i = 7;
					} else {
						assertEquals(9, i);
						i = 10;
					}
				}
			}

			@Override
			public void hide() {
			}

			@Override
			public void dispose() {
			}

			@Override
			public void resize(int width, int height) {
			}

			@Override
			public boolean equals(Object obj) {
				return this == obj;
			}
		};

		ScreenTransition transition = new ScreenTransition() {

			{
				assertEquals(0, k);
				k = 1;
			}

			@Override
			public void show() {
				assertEquals(1, k);
				k = 2;
			}

			@Override
			public void render(float delta, TextureRegion currScreen,
					TextureRegion nextScreen) {
				if (firstRenderPassTransition) {
					firstRenderPassTransition = false;
					assertEquals(4, i);
					i = 5;
				} else {
					assertEquals(7, i);
					assertEquals(2, k);
					k = (int) delta;
				}
			}

			@Override
			public boolean isDone() {
				return k == 15;
			}

			@Override
			public void resize(int width, int height) {
			}

			@Override
			public void dispose() {
			}
		};

		// Push the first screen
		sm.pushScreen(testScreen, null);
		assertEquals(null, sm.getCurrentScreen());
		sm.render(1F);
		assertEquals(1, i);

		assertEquals(1, mult.getProcessors().size);
		assertEquals(testScreen, sm.getCurrentScreen());
		assertEquals(null, sm.getLastScreen());

		// Push the second screen using a transition
		sm.pushScreen(test2Screen, transition);
		sm.render(1F);
		assertEquals(5, i);

		assertEquals(0, mult.getProcessors().size);
		assertEquals(test2Screen, sm.getCurrentScreen());
		assertEquals(testScreen, sm.getLastScreen());
		assertTrue(!transition.isDone());

		// Let a few seconds pass, so the transition finishes
		sm.render(15);

		assertEquals(0, mult.getProcessors().size);
		assertEquals(test2Screen, sm.getCurrentScreen()); // didn't change
		assertEquals(testScreen, sm.getLastScreen()); // didn't change
		assertTrue(transition.isDone());

		assertEquals(7, i);
		i = 8;

		// In the next render pass the transition is finished
		sm.render(1);
		assertEquals(10, i);
		i = 11; // end

		assertEquals(3, mult.getProcessors().size);
		assertEquals(null, sm.getLastScreen());
	}

	/**
	 * Tests that pushing the same screen twice in succession is ignored.
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	public void testIdenticalDoublePush() {
		BasicInputMultiplexer mult = new BasicInputMultiplexer();

		ScreenManager sm = getMockedScreenManager();
		sm.initialize(mult, 5, 5, false);

		ManagedScreen firstScreen = new ManagedScreenAdapter();

		ManagedScreen mainScreen = new ManagedScreenAdapter() {
			boolean isShown = false;

			@Override
			public void show() {
				System.out.println("SHOW");
				assertFalse(isShown);
				isShown = true;
			};

			@Override
			public void hide() {
				assertTrue(isShown);
				isShown = false;
			}

			@Override
			public void resize(int width, int height) {
			}
		};

		sm.pushScreen(firstScreen, null);
		sm.render(1F);

		sm.pushScreen(mainScreen, null);
		sm.render(1F);

		sm.pushScreen(mainScreen, null);
		sm.render(1F);
	}

	/**
	 * Tests whether a screen's input processor is removed from the game, even
	 * though it was deleted from the screen's list of processors beforehand.
	 */
	@SuppressWarnings({ "unchecked" })
	@Test
	public void testRemovingProcessor() {
		BasicInputMultiplexer mult = new BasicInputMultiplexer();
		ScreenManager<ManagedScreen, ScreenTransition> sm = getMockedScreenManager();
		sm.initialize(mult, 5, 5, false);

		ManagedScreen mainScreen = new ManagedScreenAdapter() {
			boolean doneOnce = false;

			@Override
			public void show() {
				addInputProcessor(new InputAdapter());
				addInputProcessor(new InputAdapter());
			}

			@Override
			public void render(float delta) {
				if (!doneOnce) {
					doneOnce = true;

					this.getInputProcessors().removeIndex(0);
				}
			}

			@Override
			public void resize(int width, int height) {
			}
		};

		ManagedScreen secondScreen = new ManagedScreenAdapter() {
			@Override
			public void resize(int width, int height) {
			}
		};

		sm.pushScreen(mainScreen, null);
		sm.render(1F);
		assertEquals(2, mult.size());

		sm.pushScreen(secondScreen, null);
		sm.render(1F);
		assertEquals(0, mult.size());
	}

}
