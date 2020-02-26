package de.eskalon.commons.screen;

import java.util.Collection;
import java.util.NoSuchElementException;

import javax.annotation.Nullable;

import com.badlogic.gdx.ApplicationListener;

import de.eskalon.commons.screen.transition.IScreenTransition;

/**
 * A screen manager handles the different screens of a game and their
 * transitions.
 * <p>
 * Screens and transitions can be added via {@link #addScreen(String, IScreen)}
 * and {@link #addScreenTransition(String, IScreenTransition)}. To actually show
 * a screen, push it via {@link #pushScreen(String, String)}.
 * 
 * @author damios
 */
public interface IScreenManager<S extends IScreen, T extends IScreenTransition> {

	/**
	 * Adds a screen.
	 *
	 * @param name
	 *            the name of the screen
	 * @param screen
	 *            the screen
	 */
	public void addScreen(String name, S screen);

	/**
	 * Retrieves a screen.
	 *
	 * @param name
	 *            the name of the screen
	 * @return the screen
	 * @throws NoSuchElementException
	 *             when the screen isn't found
	 */
	public S getScreen(String name);

	/**
	 * @return all registered screens.
	 */
	public Collection<S> getScreens();

	public void addScreenTransition(String name, T screen);

	/**
	 * Retrieves a transition.
	 *
	 * @param name
	 *            the name of the transition
	 * @return the transition
	 * @throws NoSuchElementException
	 *             when the transition isn't found
	 */
	public T getScreenTransition(String name);

	/**
	 * @return all registered transitions.
	 */
	public Collection<T> getScreenTransitions();

	public void pushScreen(String name, @Nullable String transitionName);

	public S getCurrentScreen();

	/**
	 * @see ApplicationListener#resize(int, int)
	 */
	public void resize(int width, int height);

	public void render(float deltaTime);

	/**
	 * @return {@code true} if a transition is currently played; otherwise
	 *         {@code false}
	 */
	public boolean inTransition();

}
