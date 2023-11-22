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

import java.util.LinkedList;
import java.util.Queue;
import java.util.function.Supplier;

import javax.annotation.Nullable;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.HdpiUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.ScreenUtils;

import de.damios.guacamole.Preconditions;
import de.damios.guacamole.annotations.Beta;
import de.damios.guacamole.gdx.graphics.NestableFrameBuffer;
import de.damios.guacamole.gdx.log.Logger;
import de.damios.guacamole.gdx.log.LoggerService;
import de.damios.guacamole.tuple.Pair;
import de.eskalon.commons.screen.transition.ScreenTransition;
import de.eskalon.commons.utils.BasicInputMultiplexer;
import de.eskalon.commons.utils.ScreenFboUtils;

/**
 * A screen manager that handles the different screens of a game and their
 * transitions.
 * <p>
 * Has to be {@linkplain #initialize(BasicInputMultiplexer, int, int)
 * initialized} before it can be used.
 * <p>
 * To actually show a screen, push it via
 * {@link #pushScreen(ManagedScreen, ScreenTransition)}.
 * <p>
 * As the screen manager is using framebuffers internally, screens and
 * transitions have to use a {@link NestableFrameBuffer} if they want to use
 * framebuffers as well!
 * 
 * @author damios
 * 
 * @see <a href=
 *      "https://github.com/crykn/libgdx-screenmanager/wiki/Screen-Lifecycle">The
 *      wiki entry detailing a screen's life-cycle</a>
 */
public class ScreenManager<S extends ManagedScreen, T extends ScreenTransition>
		implements Disposable {

	private Logger LOG = LoggerService.getLogger(ScreenManager.class);

	/**
	 * This framebuffer is used to store the content of the previously active
	 * screen while a transition is played.
	 */
	private FrameBuffer lastFBO;

	/**
	 * This framebuffer is used to store the content of the active screen while
	 * a transition is played.
	 */
	private FrameBuffer currFBO;

	/**
	 * The screen that was shown before the {@linkplain #currScreen current
	 * screen}.
	 */
	protected @Nullable ManagedScreen lastScreen;

	/**
	 * The current screen.
	 */
	protected @Nullable ManagedScreen currScreen;

	/**
	 * The input processors of the {@linkplain #currScreen current screen}.
	 */
	private Array<InputProcessor> currentProcessors = new Array<>();

	/**
	 * The blank screen used internally when no screen has been pushed yet.
	 */
	private BlankScreen blankScreen;

	/**
	 * The transition effect currently rendered.
	 */
	protected @Nullable T transition;

	protected final Queue<Pair<Supplier<T>, Supplier<S>>> transitionQueue = new LinkedList<>();

	private BasicInputMultiplexer gameInputMultiplexer;

	protected int currentWidth, currentHeight;

	private boolean initialized = false;

	protected boolean hasDepth; // needed, when the framebuffers are (re)created
	protected boolean autoDisposeScreens = false;
	protected boolean autoDisposeTransitions = false;

	public void initialize(BasicInputMultiplexer gameInputMultiplexer,
			int screenWidth, int screenHeight, boolean hasDepth) {
		this.gameInputMultiplexer = gameInputMultiplexer;
		this.currentWidth = screenWidth;
		this.currentHeight = screenHeight;
		this.hasDepth = hasDepth;
		this.blankScreen = new BlankScreen();
		this.currScreen = this.blankScreen;

		initBuffers();

		this.initialized = true;
	}

	protected void initBuffers() {
		if (lastFBO != null)
			lastFBO.dispose();
		lastFBO = new NestableFrameBuffer(Format.RGBA8888,
				HdpiUtils.toBackBufferX(currentWidth),
				HdpiUtils.toBackBufferY(currentHeight), hasDepth);
		if (currFBO != null)
			currFBO.dispose();
		currFBO = new NestableFrameBuffer(Format.RGBA8888,
				HdpiUtils.toBackBufferX(currentWidth),
				HdpiUtils.toBackBufferY(currentHeight), hasDepth);
	}

	/**
	 * Sets the {@code hasDepth} attribute of the internal framebuffers and
	 * recreates them.
	 * 
	 * @param hasDepth
	 */
	public void setHasDepth(boolean hasDepth) {
		this.hasDepth = hasDepth;

		initBuffers();
	}

	/**
	 * Enables automatic disposing for screens and/or transitions. If set to
	 * {@code true}, {@code dispose()} is called right after {@code hide()}.
	 * 
	 * @param autoDisposeScreens
	 *            if set to {@code true}, screens are automatically
	 *            {@linkplain ManagedScreen#dispose() disposed} after they are
	 *            {@linkplain ManagedScreen#hide() hidden}; {@code false} by
	 *            default
	 * @param autoDisposeTransitions
	 *            if set to {@code true}, transitions are automatically
	 *            {@linkplain ScreenTransition#dispose() disposed} after they
	 *            are {@linkplain ScreenTransition#hide() hidden}; {@code false}
	 *            by default
	 */
	public void setAutoDispose(boolean autoDisposeScreens,
			boolean autoDisposeTransitions) {
		this.autoDisposeScreens = autoDisposeScreens;
		this.autoDisposeTransitions = autoDisposeTransitions;
	}

	/**
	 * Pushes a screen to be the active screen. If there is still a transition
	 * ongoing, the pushed one is queued. If screen and transition should be
	 * instantiated lazily, use {@link #pushScreen(Supplier, Supplier)}. This is
	 * useful, when the constructors need to run on the rendering thread.
	 * <p>
	 * {@link Screen#show()} is called on the pushed screen and
	 * {@link Screen#hide()} is called on the previously
	 * {@linkplain #getLastScreen() active screen}, as soon as the transition is
	 * finished. This is always done on the rendering thread (when
	 * {@link #render(float)} is called next).
	 * <p>
	 * If the same screen is pushed twice in a row, the second call is being
	 * ignored.
	 *
	 * @param screen
	 *            the screen to be pushed
	 * @param transition
	 *            the transition effect; can be {@code null}
	 * 
	 * @see #pushScreen(Supplier, Supplier)
	 */
	public void pushScreen(S screen, @Nullable T transition) {
		Preconditions.checkNotNull(screen, "screen cannot be null");

		if (LoggerService.isDebugEnabled())
			LOG.debug("Screen '%s' was pushed, using the transition '%s'",
					screen.getClass().getSimpleName(),
					transition == null ? "null"
							: transition.getClass().getSimpleName());

		pushScreen(() -> screen, () -> transition);
	}

	/**
	 * Pushes a screen to be the active screen. If there is still a transition
	 * ongoing, the pushed one is queued.
	 * <p>
	 * The provided {@link Supplier}s are called on the rendering thread, which
	 * is useful if the screen's or transition's constructors perform OpenGL
	 * operations. This has the advantage that OpenGL calls can be done in the
	 * constructors without using {@link Application#postRunnable(Runnable)} or
	 * moving code to
	 * {@link ManagedScreen#show()}/{@link ScreenTransition#show()}.
	 * <p>
	 * If the same screen is pushed twice in a row, the second call is being
	 * ignored.
	 * 
	 * @param screenSupplier
	 *            a {@link Supplier} for the screen to be pushed
	 * @param transitionSupplier
	 *            a {@link Supplier} for the transition effect; can be
	 *            {@code null}
	 * 
	 * @see #pushScreen(ManagedScreen, ScreenTransition)
	 */
	@Beta
	public void pushScreen(Supplier<S> screenSupplier,
			@Nullable Supplier<T> transitionSupplier) {
		Preconditions.checkNotNull(screenSupplier,
				"screenSupplier cannot be null");

		transitionQueue.add(new Pair<Supplier<T>, Supplier<S>>(
				transitionSupplier, screenSupplier));
	}

	/**
	 * Renders the screens and transitions.
	 * 
	 * @param delta
	 *            the time delta since the last {@link #render(float)} call; in
	 *            seconds
	 */
	public void render(float delta) {
		Preconditions.checkState(initialized,
				"The screen manager has to be initalized first!");

		if (transition == null) {
			if (!transitionQueue.isEmpty()) {
				/* Start the next queued transition */
				Pair<Supplier<T>, Supplier<S>> nextTransition = transitionQueue
						.poll();
				ManagedScreen tmp = nextTransition.y.get();
				if (tmp == currScreen) { // one can't push the same screen twice
											// in a row
					if (LoggerService.isDebugEnabled())
						LOG.debug(
								"Screens cannot be pushed twice; the second call to push '%s' was ignored",
								tmp.getClass().getSimpleName());

					render(delta); // render again so no frame is skipped
					return;
				}

				this.lastScreen = currScreen;
				this.currScreen = tmp;
				this.transition = nextTransition.x == null ? null
						: nextTransition.x.get();

				this.gameInputMultiplexer.removeProcessors(currentProcessors);

				initializeScreen(this.currScreen);

				if (this.transition != null) {
					initializeTransition(this.transition);
				} else { // a screen was pushed without transition
					finalizeScreen(this.lastScreen);
					this.lastScreen = null;

					this.currentProcessors = new Array<>(
							this.currScreen.getInputProcessors());
					this.gameInputMultiplexer.addProcessors(currentProcessors);
				}

				render(delta); // render again so no frame is skipped
			} else {
				/* Render the current screen; no transition is going on */
				ScreenUtils.clear(currScreen.getClearColor(), true);
				this.currScreen.render(delta);
			}
		} else {
			if (!this.transition.isDone()) {
				/* Render the current transition */
				ScreenUtils.clear(this.transition.getClearColor(), true);
				this.transition.render(delta,
						ScreenFboUtils.screenToTexture(this.lastScreen,
								this.lastFBO, delta),
						ScreenFboUtils.screenToTexture(this.currScreen,
								this.currFBO, delta));
			} else {
				/* The current transition is finished; remove it */
				finalizeTransition(this.transition);
				this.transition = null;

				finalizeScreen(this.lastScreen);
				this.lastScreen = null;

				this.currentProcessors = new Array<>(
						this.currScreen.getInputProcessors());
				this.gameInputMultiplexer.addProcessors(currentProcessors);

				render(delta); // render again so no frame is skipped
			}
		}
	}

	protected void initializeScreen(ManagedScreen newScreen) {
		newScreen.show();
		newScreen.resize(currentWidth, currentHeight);
	}

	protected void initializeTransition(T newTransition) {
		newTransition.show();
		newTransition.resize(currentWidth, currentHeight);
	}

	protected void finalizeScreen(ManagedScreen oldScreen) {
		oldScreen.hide();
		if (autoDisposeScreens)
			oldScreen.dispose();
	}

	protected void finalizeTransition(T oldTransition) {
		oldTransition.hide();
		if (autoDisposeTransitions)
			oldTransition.dispose();
	}

	/**
	 * @see ManagedScreen#resize(int, int)
	 */
	public void resize(int width, int height) {
		Preconditions.checkState(initialized,
				"The screen manager has to be initialized first!");

		if (currentWidth != width || currentHeight != height) {
			currentWidth = width;
			currentHeight = height;

			// Resize screens & transitions
			currScreen.resize(width, height);

			if (lastScreen != null)
				lastScreen.resize(width, height);

			if (transition != null)
				transition.resize(width, height);

			// Recreate buffers
			initBuffers();
		}
	}

	/**
	 * @see ManagedScreen#pause()
	 */
	public void pause() {
		Preconditions.checkState(initialized,
				"The screen manager has to be initalized first!");

		if (lastScreen != null)
			lastScreen.pause();

		currScreen.pause();
	}

	/**
	 * @see ManagedScreen#resume()
	 */
	public void resume() {
		Preconditions.checkState(initialized,
				"The screen manager has to be initalized first!");

		if (lastScreen != null)
			lastScreen.resume();

		currScreen.resume();
	}

	/**
	 * Disposes the screen manager and any screen and transitions pushed, which
	 * were not yet {@linkplain ManagedScreen#hide() hidden}, regardless of
	 * whether they already started being rendered.
	 */
	@Override
	public void dispose() {
		// Current screens & transitions
		if (lastScreen != null) {
			lastScreen.dispose();
			lastScreen = null;
		}

		if (currScreen != null) {
			currScreen.dispose();
			currScreen = null;
		}

		if (transition != null) {
			transition.dispose();
			transition = null;
		}

		// Queued screens & transitions
		for (Pair<Supplier<T>, Supplier<S>> pair : transitionQueue) {
			pair.y.get().dispose();

			if (pair.x != null)
				pair.x.get().dispose();
		}
		transitionQueue.clear();

		// FBOs
		if (lastFBO != null) {
			lastFBO.dispose();
			lastFBO = null;
		}

		if (currFBO != null) {
			currFBO.dispose();
			currFBO = null;
		}
	}

	/**
	 * @return is {@code null} if no transition is going on; otherwise returns
	 *         the previous {@linkplain ManagedScreen screen} that is still
	 *         rendered as part of the transition
	 */
	@SuppressWarnings("unchecked")
	public @Nullable S getLastScreen() {
		if (lastScreen == blankScreen)
			return null; // return null, as the blank screen is not the right
							// type

		return (S) lastScreen;
	}

	/**
	 * @return the current screen; is {@code null} before the first screen was
	 *         pushed
	 */
	@SuppressWarnings("unchecked")
	public @Nullable S getCurrentScreen() {
		if (currScreen == blankScreen)
			return null; // return null, as the blank screen is not the right
							// type

		return (S) currScreen;
	}

	/**
	 * @return {@code true} when a transition is currently rendered
	 */
	public boolean isTransitioning() {
		return transition != null;
	}

}
