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
import de.eskalon.commons.screen.transition.BasicScreenTransition;

/**
 * Basic implementation of a screen manager. Handles the different screens of a
 * game and their transitions.
 * <p>
 * Uses {@link BasicScreen}s and {@link BasicScreenTransition}s.
 *
 * @author damios
 * 
 * @see <a href=
 *      "https://github.com/crykn/libgdx-screenmanager/wiki/How-the-library-works-in-detail">The
 *      wiki entry detailing the inner workings</a>
 */
public class BasicScreenManager implements
		IScreenManager<BasicScreen, BasicScreenTransition>, Disposable {

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
	private BasicScreen lastScreen;

	/**
	 * The current screen.
	 */
	@Nullable
	private BasicScreen currScreen;

	/**
	 * The transition effect currently rendered.
	 */
	@Nullable
	private BasicScreenTransition transition;

	/**
	 * A map with all initialized screens.
	 */
	private final Map<String, BasicScreen> screens = new ConcurrentHashMap<>();

	/**
	 * A map with all screen transitions.
	 */
	private final Map<String, BasicScreenTransition> transitions = new ConcurrentHashMap<>();

	private final Queue<Tuple<BasicScreenTransition, BasicScreen>> transitionQueue = new ConcurrentLinkedQueue<>();

	private BasicInputMultiplexer gameInputMultiplexer;

	public BasicScreenManager(BasicInputMultiplexer gameInputMultiplexer) {
		this.gameInputMultiplexer = gameInputMultiplexer;
	}

	public void initBuffers(int width, int height) {
		lastFBO = new FrameBuffer(Format.RGBA8888, width, height, false);
		currFBO = new FrameBuffer(Format.RGBA8888, width, height, false);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addScreen(String name, BasicScreen screen) {
		Preconditions.checkNotNull(screen, "screen cannot be null");
		Preconditions.checkArgument(!name.isEmpty(), "name cannot be empty");

		screens.put(name, screen);
	}

	@Override
	public BasicScreen getScreen(String name) {
		Preconditions.checkArgument(!name.isEmpty(), "name cannot be empty");

		BasicScreen screen = this.screens.get(name);

		if (screen == null) {
			throw new NoSuchElementException(String.format(
					"No screen with the name '%s' could be found. Add the screen via #addScreen(String, BasicScreen) first.",
					name));
		}

		return screen;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Collection<BasicScreen> getScreens() {
		return screens.values();
	}

	@Override
	public void addScreenTransition(String name,
			BasicScreenTransition transition) {
		Preconditions.checkNotNull(transition, "screen cannot be null");
		Preconditions.checkArgument(!name.isEmpty(), "name cannot be empty");

		transitions.put(name, transition);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public BasicScreenTransition getScreenTransition(String name) {
		Preconditions.checkArgument(!name.isEmpty(), "name cannot be empty");

		BasicScreenTransition transition = this.transitions.get(name);

		if (transition == null) {
			throw new NoSuchElementException(String.format(
					"No transition with the name '%s' could be found. Add the transition via #addScreenTransition(String, BasicScreenTransition) first.",
					name));
		}

		return transition;
	}

	@Override
	public Collection<BasicScreenTransition> getScreenTransitions() {
		return transitions.values();
	}

	/**
	 * Pushes a screen to be the active screen. The screen has to be added to
	 * the manager beforehand via {@link #addScreen(String, BasicScreen)}.
	 * <p>
	 * {@link Screen#show()} is called on the pushed screen and
	 * {@link Screen#hide()} is called on the previously
	 * {@linkplain #getLastScreen() active screen}, as soon as the transition is
	 * finished. This is always done on the rendering thread (in
	 * {@link #render(float)}).
	 *
	 * @param name
	 *            the name of screen to be pushed
	 * @param transitionName
	 *            the transition effect; can be {@code null}
	 */
	@Override
	public final void pushScreen(String name, @Nullable String transitionName) {
		if (Gdx.app.getLogLevel() >= Application.LOG_DEBUG)
			Gdx.app.debug("ScreenManager", String.format(
					"Screen '%s' was pushed, using the transition '%s'", name,
					transitionName == null ? "null" : transitionName));
		transitionQueue.add(new Tuple<BasicScreenTransition, BasicScreen>(
				transitionName != null ? getScreenTransition(transitionName)
						: null,
				getScreen(name)));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void resize(int width, int height) {
		for (BasicScreen s : screens.values()) {
			if (s.isInitialized()) {
				s.resize(width, height);
			}
		}
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(backgroundColor.r, backgroundColor.g,
				backgroundColor.b, backgroundColor.a);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		if (transition == null) {
			if (!transitionQueue.isEmpty()) {
				/*
				 * Start the queued transition
				 */
				Tuple<BasicScreenTransition, BasicScreen> nextTransition = transitionQueue
						.poll();

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
				} else {
					if (this.lastScreen != null)
						this.lastScreen.hide();

					this.gameInputMultiplexer.addProcessors(
							new Array<>(this.currScreen.getInputProcessors()));
				}

				// render again so no frame is skipped
				render(delta);
			} else {
				/*
				 * Render current screen; no transition is going on
				 */
				this.currScreen.render(delta);
			}
		} else {
			if (!this.transition.isDone()) {
				/*
				 * render the current transition
				 */
				this.transition.render(delta,
						screenToTexture(delta, this.lastScreen, this.lastFBO),
						screenToTexture(delta, this.currScreen, this.currFBO));
			} else {
				/*
				 * the current transition is finished; remove it
				 */
				this.transition = null;
				this.lastScreen.hide();
				this.gameInputMultiplexer.addProcessors(
						new Array<>(this.currScreen.getInputProcessors()));

				// render again so no frame is skipped
				render(delta);
			}
		}
	}

	@Override
	public void dispose() {
		this.lastScreen = null;
		this.currScreen = null;

		lastFBO.dispose();
		currFBO.dispose();

		for (BasicScreen s : screens.values()) {
			if (s.isInitialized()) {
				s.dispose();
			}
		}

		for (BasicScreenTransition t : transitions.values()) {
			if (t instanceof Disposable) {
				((Disposable) t).dispose();
			}
		}
	}

	/**
	 * Renders a {@linkplain BasicScreen screen} into a {@linkplain FrameBuffer
	 * frame buffer object}.
	 * 
	 * @param delta
	 *            time delta
	 * @param screen
	 *            the {@linkplain BasicScreen screen} that gets rendered
	 * @param FBO
	 *            The {@linkplain FrameBuffer frame buffer object} the
	 *            {@linkplain BasicScreen screen} gets rendered into.
	 * @return a texture which contains the rendered {@linkplain BasicScreen
	 *         screen}
	 */
	Texture screenToTexture(float delta, BasicScreen screen, FrameBuffer FBO) {
		FBO.begin();
		Gdx.gl.glClearColor(backgroundColor.r, backgroundColor.g,
				backgroundColor.b, backgroundColor.a);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		screen.render(delta);
		FBO.end();
		return FBO.getColorBufferTexture();
	}

	/**
	 * @return the {@linkplain BasicScreen screen} that was shown before the
	 *         {@linkplain #getCurrentScreen() current screen}
	 */
	@Nullable
	public BasicScreen getLastScreen() {
		return lastScreen;
	}

	/**
	 * @return the current screen; is changed in the first render pass after
	 *         {@link #pushScreen(String, String)} is called.
	 */
	@Override
	public BasicScreen getCurrentScreen() {
		return currScreen;
	}

	/**
	 * @return whether the manager is currently transitioning from the
	 *         {@linkplain #getLastScreen() last screen} towards the
	 *         {@linkplain #getCurrentScreen() current screen}
	 */
	@Override
	public boolean inTransition() {
		return this.transition == null ? false : true;
	}

}
