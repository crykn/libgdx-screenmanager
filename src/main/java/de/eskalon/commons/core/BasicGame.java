package de.eskalon.commons.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;

import de.eskalon.commons.input.BasicInputMultiplexer;
import de.eskalon.commons.screen.BasicScreen;
import de.eskalon.commons.screen.BasicScreenManager;
import de.eskalon.commons.screen.transition.BasicScreenTransition;

/**
 * A basic game. Takes care of setting some convenience variables and constants;
 * uses a screen manager. Furthermore, adds an
 * {@linkplain #getInputMultiplexer() input multiplexer}.
 * 
 * @see BasicScreenManager How to register screens and tranistions.
 * @author damios
 */
public class BasicGame extends ManagedGame<BasicScreen, BasicScreenTransition> {

	/**
	 * The version the application is running on. Set via the jar manifest. Is
	 * {@code Development} if the game is started in a development environment.
	 */
	public final String VERSION;
	/**
	 * Whether the application is running in a development environment. Checks
	 * if a {@linkplain #VERSION version} is set in the jar manifest.
	 */
	public final boolean IN_DEV_ENV;

	/**
	 * @see Graphics#getWidth()
	 */
	protected int viewportWidth;
	/**
	 * @see Graphics#getHeight()
	 */
	protected int viewportHeight;

	private BasicInputMultiplexer inputProcessor = new BasicInputMultiplexer();

	public BasicGame() {
		super();
		IN_DEV_ENV = getClass().getPackage().getImplementationVersion() == null;
		VERSION = IN_DEV_ENV ? "Development"
				: getClass().getPackage().getImplementationVersion();

		this.screenManager = new BasicScreenManager(inputProcessor);
	}

	@Override
	public void create() {
		this.viewportWidth = Gdx.graphics.getWidth();
		this.viewportHeight = Gdx.graphics.getHeight();

		Gdx.input.setInputProcessor(inputProcessor);

		((BasicScreenManager) this.screenManager).initBuffers(viewportWidth,
				viewportHeight);
	}

	/**
	 * @return the viewport width
	 */
	public int getViewportWidth() {
		return this.viewportWidth;
	}

	/**
	 * @return the viewport height
	 */
	public int getViewportHeight() {
		return this.viewportHeight;
	}

	@Override
	public void resize(int width, int height) {
		this.viewportWidth = width;
		this.viewportHeight = height;
	}

	/**
	 * Returns the input multiplexer of the game. Should be used to add input
	 * listeners instead of {@link Input#setInputProcessor(InputProcessor)}.
	 *
	 * @return the game's input multiplexer
	 */
	public BasicInputMultiplexer getInputMultiplexer() {
		return inputProcessor;
	}

	@Override
	public void dispose() {
		((BasicScreenManager) screenManager).dispose();
	}

}
