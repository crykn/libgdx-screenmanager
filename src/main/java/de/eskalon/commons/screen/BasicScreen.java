package de.eskalon.commons.screen;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.utils.Array;

import de.eskalon.commons.core.BasicGame;

/**
 * A basic screen for use with {@link BasicGame}. Screens are intended as
 * objects that are created only once when the game is started.
 * <p>
 * The {@link #create()} method is called when the screen is first shown. The
 * screen can also be initialized manually by calling
 * {@link #initializeScreen()}, which should normally be done by a loading
 * screen after the assets have been loaded.
 * <p>
 * Use {@link #addInputProcessor(InputProcessor)} to add input processors.
 * 
 * @author damios
 */
public abstract class BasicScreen implements IScreen {

	/**
	 * @see #addInputProcessor(InputProcessor)
	 */
	private final Array<InputProcessor> inputProcessors = new Array<>(4);

	private boolean initialized = false;

	protected abstract BasicGame getGame();

	public boolean isInitialized() {
		return initialized;
	}

	/**
	 * Can be called manually to {@linkplain #create() initialize} the screen -
	 * otherwise this is done when the screen is first shown.
	 */
	public void initializeScreen() {
		if (!initialized) {
			initialized = true;
			create();
		}
	}

	/**
	 * Is responsible for initializing the screen. Is called <i>once</i>.
	 */
	protected abstract void create();

	/**
	 * Adds an input processor that is automatically registered and unregistered
	 * whenever the screen is {@linkplain #show() shown}/{@linkplain #hide()
	 * hidden}.
	 *
	 * @param processor
	 *            The processor to add.
	 */
	protected void addInputProcessor(InputProcessor processor) {
		inputProcessors.add(processor);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void show() {
		initializeScreen();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * Before this method is called, the previously rendered stuff is cleared.
	 */
	@Override
	public abstract void render(float delta);

	/**
	 * {@inheritDoc}
	 * 
	 * Is automatically called when the game is closed and the screen was
	 * {@linkplain #isInitialized() initialized} before.
	 */
	@Override
	public abstract void dispose();

	@Override
	public void resize(int width, int height) {
		// ignore it as by default the game can't be resized
	}

	public Array<InputProcessor> getInputProcessors() {
		return inputProcessors;
	}

}
