package de.eskalon.commons.core;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.google.common.base.Preconditions;

import de.eskalon.commons.screen.IScreen;
import de.eskalon.commons.screen.IScreenManager;
import de.eskalon.commons.screen.transition.IScreenTransition;

/**
 * A basic game class that utilizes a {@linkplain IScreenManager screen
 * manager}.
 * <p>
 * The {@linkplain #screenManager used screen manager} has to be set in
 * {@link #create()}!
 * 
 * @author damios
 */
abstract class ManagedGame<S extends IScreen, T extends IScreenTransition>
		extends ApplicationAdapter {

	protected IScreenManager<S, T> screenManager;
	private boolean isFocused = true;

	@Override
	public abstract void create();

	@Override
	public void resize(int width, int height) {
		super.resize(width, height);

		Preconditions.checkState(screenManager != null,
				"A screen manager has to be set first");
		screenManager.resize(width, height);
	}

	@Override
	public void render() {
		Preconditions.checkState(screenManager != null,
				"A screen manager has to be set first");
		screenManager.render(Gdx.graphics.getDeltaTime());
	}

	@Override
	public void pause() {
		isFocused = false;
	}

	@Override
	public void resume() {
		isFocused = true;
	}

	/**
	 * @return whether the game is currently focused.
	 * @see ApplicationListener#pause()
	 */
	public boolean isFocused() {
		return isFocused;
	}

	public IScreenManager<S, T> getScreenManager() {
		return screenManager;
	}

}
