/*
 * Copyright 2020 damios
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at:
 * http://www.apache.org/licenses/LICENSE-2.0
 *  
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.eskalon.commons.screen;

import java.util.Collection;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.annotation.Nullable;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.google.common.base.Preconditions;

import de.eskalon.commons.input.BasicInputMultiplexer;
import de.eskalon.commons.misc.Tuple;
import de.eskalon.commons.screen.transition.ScreenTransition;

/**
 * A screen manager that handles the different screens of a game and their
 * transitions.
 * <p>
 * Screens and transitions can be added via
 * {@link #addScreen(String, ManagedScreen)} and
 * {@link #addScreenTransition(String, ScreenTransition)}. To actually show a
 * screen, push it via {@link #pushScreen(String, String)}.
 * 
 * @author damios
 * 
 * @see <a href=
 *      "https://github.com/crykn/libgdx-screenmanager/wiki/A-screen's-lifecycle">The
 *      wiki entry detailing a screen's life-cycle</a>
 */
public class ScreenManager<S extends ManagedScreen, T extends ScreenTransition>
		implements Disposable {

	/**
	 * The background color for all screens.
	 */
	protected Color backgroundColor = Color.BLACK;

	/**
	 * This frame buffer object is used to store the content of the previously
	 * active screen while a transition is played.
	 */
	private FrameBuffer lastFBO;

	/**
	 * This frame buffer object is used to store the content of the active
	 * screen while a transition is played.
	 */
	private FrameBuffer currFBO;

	/**
	 * The screen that was shown before the {@linkplain #currScreen current
	 * screen}.
	 */
	@Nullable
	private S lastScreen;

	/**
	 * The current screen.
	 */
	@Nullable
	private S currScreen;

	/**
	 * The transition effect currently rendered.
	 */
	@Nullable
	private T transition;

	/**
	 * A map with all initialized screens.
	 */
	private final Map<String, S> screens = new ConcurrentHashMap<>();

	/**
	 * A map with all screen transitions.
	 */
	private final Map<String, T> transitions = new ConcurrentHashMap<>();

	private final Queue<Tuple<T, S>> transitionQueue = new ConcurrentLinkedQueue<>();

	private BasicInputMultiplexer gameInputMultiplexer;

	private int currentWidth, currentHeight;

	public ScreenManager(BasicInputMultiplexer gameInputMultiplexer, int width,
			int height) {
		this.gameInputMultiplexer = gameInputMultiplexer;
		this.currentWidth = width;
		this.currentHeight = height;
	}

	public void initBuffers() {
		if (lastFBO != null)
			lastFBO.dispose();
		lastFBO = new FrameBuffer(Format.RGBA8888, currentWidth, currentHeight,
				false);
		if (currFBO != null)
			currFBO.dispose();
		currFBO = new FrameBuffer(Format.RGBA8888, currentWidth, currentHeight,
				false);
	}

	/**
	 * Adds a screen. If a screen with the same name was added before it is
	 * replaced.
	 *
	 * @param name
	 *            the name of the screen
	 * @param screen
	 *            the screen
	 */
	public void addScreen(String name, S screen) {
		Preconditions.checkNotNull(screen, "screen cannot be null");
		Preconditions.checkArgument(!name.isEmpty(), "name cannot be empty");

		screens.put(name, screen);
	}

	/**
	 * Retrieves a screen.
	 *
	 * @param name
	 *            the name of the screen
	 * @return the screen
	 * @throws NoSuchElementException
	 *             when the screen isn't found
	 */
	public S getScreen(String name) {
		Preconditions.checkArgument(!name.isEmpty(), "name cannot be empty");

		S screen = this.screens.get(name);

		if (screen == null) {
			throw new NoSuchElementException(String.format(
					"No screen with the name '%s' could be found. Add the screen via #addScreen(String, ManagedScreen) first.",
					name));
		}

		return screen;
	}

	/**
	 * @return all registered screens.
	 */
	public Collection<S> getScreens() {
		return screens.values();
	}

	/**
	 * Adds a transition. If a transition with the same name was added before it
	 * is replaced.
	 *
	 * @param name
	 *            the name of the transition
	 * @param transition
	 *            the transition
	 */
	public void addScreenTransition(String name, T transition) {
		Preconditions.checkNotNull(transition, "screen cannot be null");
		Preconditions.checkArgument(!name.isEmpty(), "name cannot be empty");

		transitions.put(name, transition);
	}

	/**
	 * Retrieves a transition.
	 *
	 * @param name
	 *            the name of the transition
	 * @return the transition
	 * @throws NoSuchElementException
	 *             when the transition isn't found
	 */
	public T getScreenTransition(String name) {
		Preconditions.checkArgument(!name.isEmpty(), "name cannot be empty");

		T transition = this.transitions.get(name);

		if (transition == null) {
			throw new NoSuchElementException(String.format(
					"No transition with the name '%s' could be found. Add the transition via #addScreenTransition(String, ScreenTransition) first.",
					name));
		}

		return transition;
	}

	/**
	 * @return all registered transitions.
	 */
	public Collection<T> getScreenTransitions() {
		return transitions.values();
	}

	/**
	 * Pushes a screen to be the active screen. If there is still a transition
	 * is ongoing, the pushed one is queued. The screen has to be added to the
	 * manager beforehand via {@link #addScreen(String, ManagedScreen)}.
	 * <p>
	 * {@link Screen#show()} is called on the pushed screen and
	 * {@link Screen#hide()} is called on the previously
	 * {@linkplain #getLastScreen() active screen}, as soon as the transition is
	 * finished. This is always done on the rendering thread (in
	 * {@link #render(float)}).
	 * <p>
	 * The given transition is ignored if this is the first screen to be pushed,
	 * as there is no screen to transition from.
	 *
	 * @param name
	 *            the name of screen to be pushed
	 * @param transitionName
	 *            the transition effect; can be {@code null}
	 */
	public final void pushScreen(String name, @Nullable String transitionName) {
		if (Gdx.app.getLogLevel() >= Application.LOG_DEBUG)
			Gdx.app.debug("ScreenManager", String.format(
					"Screen '%s' was pushed, using the transition '%s'", name,
					transitionName == null ? "null" : transitionName));
		transitionQueue.add(new Tuple<T, S>(
				transitionName != null ? getScreenTransition(transitionName)
						: null,
				getScreen(name)));
	}

	public void render(float delta) {
		Gdx.gl.glClearColor(backgroundColor.r, backgroundColor.g,
				backgroundColor.b, backgroundColor.a);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		if (transition == null) {
			if (!transitionQueue.isEmpty()) {
				/*
				 * Start the queued transition
				 */
				Tuple<T, S> nextTransition = transitionQueue.poll();

				if (this.currScreen != null) {
					this.gameInputMultiplexer.removeProcessors(
							new Array<>(this.currScreen.getInputProcessors()));
				}

				this.lastScreen = currScreen;
				this.currScreen = nextTransition.y;
				this.currScreen.show();
				this.transition = nextTransition.x;

				if (this.transition != null) {
					this.transition.reset();
				} else { // a screen was pushed without transition
					if (this.lastScreen != null)
						this.lastScreen.hide();

					this.gameInputMultiplexer.addProcessors(
							new Array<>(this.currScreen.getInputProcessors()));
				}

				// render again so no frame is skipped
				render(delta);
			} else {
				Preconditions.checkState(this.currScreen != null,
						"A screen has to be pushed before the rendering can begin!");
				/*
				 * Render current screen; no transition is going on
				 */
				this.currScreen.render(delta);
			}
		} else {
			if (!this.transition.isDone() && this.lastScreen != null) {
				/*
				 * render the current transition; skip this step if there was no
				 * lastScreen (i.e. this is the first pushed screen)
				 */
				this.transition.render(delta,
						screenToTexture(delta, this.lastScreen, this.lastFBO),
						screenToTexture(delta, this.currScreen, this.currFBO));
			} else {
				/*
				 * the current transition is finished; remove it
				 */
				this.transition = null;
				if (lastScreen != null)
					this.lastScreen.hide();
				this.gameInputMultiplexer.addProcessors(
						new Array<>(this.currScreen.getInputProcessors()));

				// render again so no frame is skipped
				render(delta);
			}
		}
	}

	/**
	 * @see ManagedScreen#resize(int, int)
	 */
	public void resize(int width, int height) {
		if (currentWidth != width || currentHeight != height) {
			this.currentWidth = width;
			this.currentHeight = height;

			for (S s : screens.values()) {
				if (s.isInitialized()) {
					s.resize(width, height);
				}
			}

			initBuffers();
		}
	}

	/**
	 * @see ManagedScreen#pause()
	 */
	public void pause() {
		if (inTransition() && lastScreen != null)
			lastScreen.pause();

		if (currScreen != null)
			currScreen.pause();
	}

	/**
	 * @see ManagedScreen#resume()
	 */
	public void resume() {
		if (inTransition() && lastScreen != null)
			lastScreen.resume();

		if (currScreen != null)
			currScreen.resume();
	}

	/**
	 * Disposes the screens, the transitions and the internally used frame
	 * buffer objects.
	 */
	@Override
	public void dispose() {
		this.lastScreen = null;
		this.currScreen = null;

		if (lastFBO != null)
			lastFBO.dispose();
		if (currFBO != null)
			currFBO.dispose();

		for (S s : screens.values()) {
			s.dispose();
		}

		for (T t : transitions.values()) {
			t.dispose();
		}
	}

	/**
	 * Renders a {@linkplain ManagedScreen screen} into a
	 * {@linkplain FrameBuffer frame buffer object}.
	 * 
	 * @param delta
	 *            the time delta
	 * @param screen
	 *            the screen to be rendered
	 * @param FBO
	 *            the frame buffer object the screen gets rendered into
	 * @return a texture which contains the rendered screen
	 */
	Texture screenToTexture(float delta, S screen, FrameBuffer FBO) {
		FBO.begin();
		Gdx.gl.glClearColor(backgroundColor.r, backgroundColor.g,
				backgroundColor.b, backgroundColor.a);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		screen.render(delta);
		FBO.end();
		return FBO.getColorBufferTexture();
	}

	/**
	 * @return the {@linkplain ManagedScreen screen} that was shown before the
	 *         {@linkplain #getCurrentScreen() current screen}
	 */
	@Nullable
	public S getLastScreen() {
		return lastScreen;
	}

	/**
	 * @return the current screen; is changed in the first render pass after
	 *         {@link #pushScreen(String, String)} is called.
	 */
	public S getCurrentScreen() {
		return currScreen;
	}

	/**
	 * @return whether the manager is currently transitioning from the
	 *         {@linkplain #getLastScreen() last screen} towards the
	 *         {@linkplain #getCurrentScreen() current screen}
	 */
	public boolean inTransition() {
		return this.transition == null ? false : true;
	}

}
