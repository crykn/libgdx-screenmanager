package de.eskalon.commons.screen.transition;

import de.eskalon.commons.core.BasicGame;

/**
 * A basic screen transition for use with {@link BasicGame}. Transitions are
 * intended as objects that are created only once when the game is started.
 * <p>
 * The {@link #create()} method is called when the transition is first used. It
 * can also be initialized manually by calling
 * {@link #initializeScreenTransition()}, which should normally be done by a
 * loading screen after the assets have been loaded.
 * 
 * @author damios
 */
public abstract class BasicScreenTransition implements IScreenTransition {

	private boolean initialized = false;

	/**
	 * Can be called manually to {@linkplain #create() initialize} the
	 * transition - otherwise this is done when the transition is first
	 * rendered.
	 */
	public void initializeScreenTransition() {
		if (!initialized) {
			initialized = true;
			create();
		}
	}

	/**
	 * Is responsible for initializing the transition. Is called <i>once</i>.
	 */
	protected abstract void create();

	/**
	 * {@inheritDoc}
	 * 
	 * This is done right before the transition is to be used.
	 */
	@Override
	public void reset() {
		initializeScreenTransition();
	}

}
