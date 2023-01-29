package de.eskalon.commons.screen;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ScreenAdapter;

/**
 * Convenience implementation of {@link ManagedScreen} inspired by
 * {@link ScreenAdapter}. Inherit from this class and only override the methods
 * that you need.
 * <p>
 * Check out the documentation of {@link ManagedScreen}!
 * 
 * @author Frosty-J
 */
public class ManagedScreenAdapter extends ManagedScreen {

	@Override
	protected void create() {
		// don't do anything by default
	}

	@Override
	public void hide() {
		// don't do anything by default
	}

	@Override
	public void render(float delta) {
		// don't do anything by default
	}

	@Override
	public void resize(int width, int height) {
		// don't do anything by default
	}

	@Override
	public void dispose() {
		// don't do anything by default
	}

}