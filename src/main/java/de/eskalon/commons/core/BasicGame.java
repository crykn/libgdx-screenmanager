package de.eskalon.commons.core;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;

import de.eskalon.commons.input.BasicInputMultiplexer;

/**
 * A basic game. Takes care of setting some convenience variables and constants.
 * Furthermore, adds an {@linkplain #getInputMultiplexer() input multiplexer}.
 * 
 * @author damios
 */
class BasicGame extends ApplicationAdapter {

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
		IN_DEV_ENV = getClass().getPackage().getImplementationVersion() == null;
		VERSION = IN_DEV_ENV ? "Development"
				: getClass().getPackage().getImplementationVersion();
	}

	@Override
	public void create() {
		this.viewportWidth = Gdx.graphics.getWidth();
		this.viewportHeight = Gdx.graphics.getHeight();

		Gdx.input.setInputProcessor(inputProcessor);
	}

	/**
	 * @return the viewport width
	 */
	public int getWidth() {
		return this.viewportWidth;
	}

	/**
	 * @return the viewport height
	 */
	public int getHeight() {
		return this.viewportHeight;
	}

	@Override
	public void resize(int width, int height) {
		this.viewportWidth = width;
		this.viewportHeight = height;
	}

	/**
	 * Returns the input multiplexer of the game. Must be used to add input
	 * listeners instead of {@link Input#setInputProcessor(InputProcessor)}.
	 *
	 * @return the game's input multiplexer
	 */
	public BasicInputMultiplexer getInputMultiplexer() {
		return inputProcessor;
	}

}
