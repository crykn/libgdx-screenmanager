package de.eskalon.commons.screen.transition;

import com.badlogic.gdx.graphics.Texture;

import de.eskalon.commons.screen.IScreenManager;

/**
 * The transition effect between two screens.
 * 
 * @see IScreenManager#pushScreen(String, String)
 * 
 * @author damios
 */
public interface IScreenTransition {

	/**
	 * Takes care of actually rendering the transition.
	 * 
	 * @param delta
	 *            the time delta
	 * @param lastScreen
	 *            the old screen as a texture
	 * @param currScreen
	 *            the screen the manager is transitioning to as a texture
	 */
	public void render(float delta, Texture lastScreen, Texture currScreen);

	/**
	 * @return whether the transition is done
	 */
	public boolean isDone();

	/**
	 * Is called to reset the transition for another use.
	 */
	public void reset();

}
