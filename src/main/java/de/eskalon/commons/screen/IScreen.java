package de.eskalon.commons.screen;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;

/**
 * Represents one of many application screens, such as a main menu, a settings
 * menu, the game screen and so on.
 * <p>
 * Is a more streamlined version of the {@linkplain Screen libgdx screen
 * interface}.
 * 
 * @author damios
 * 
 * @see IScreenManager
 */
public interface IScreen extends Screen {

	/**
	 * Called when this screen becomes the current screen and after a possible
	 * transition has finished.
	 */
	@Override
	public void show();

	/**
	 * Called when the screen should render itself.
	 * 
	 * @param delta
	 *            the time in seconds since the last render pass
	 */
	@Override
	public void render(float delta);

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void resize(int newWidth, int newHeight);

	/**
	 * Called when this screen is no longer the current screen for a
	 * {@link Game}. After that the screen can still be rendered as part of a
	 * transition.
	 */
	@Override
	public void hide();

	/** {@inheritDoc} */
	@Override
	public void dispose();

	/**
	 * @deprecated Not called. See {@link ManagedGame#isFocused()}.
	 */
	@Deprecated
	public default void pause() {
	}

	/** @see ApplicationListener#resume() */
	@Deprecated
	public default void resume() {
	}
}
