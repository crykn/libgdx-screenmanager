/*
 * Copyright 2020 damios
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at:
 * http://www.apache.org/licenses/LICENSE-2.0
 *  
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.eskalon.commons.screen.transition;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Disposable;

import de.eskalon.commons.screen.ScreenManager;

/**
 * A transition effect between two screen for use with a {@link ScreenManager}.
 * Transitions are intended as objects that are created only once when the game
 * is started and are then reused.
 * <p>
 * The {@link #create()} method is called when the transition is first used. The
 * transition can also be initialized manually by calling
 * {@link #initializeScreenTransition()}, which should normally be done by a
 * loading screen after the assets have been loaded.
 * 
 * @author damios
 * 
 * @see ScreenManager#pushScreen(String, String, Object...)
 */
public abstract class ScreenTransition implements Disposable {

	private boolean initialized = false;

	public boolean isInitialized() {
		return initialized;
	}

	/**
	 * Can be called manually to {@linkplain #create() initialize} the
	 * transition - otherwise this is done when the transition is first
	 * rendered.
	 */
	public void initializeScreenTransition() {
		if (!initialized) {
			initialized = true;
			create();
			resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		}
	}

	/**
	 * Is responsible for initializing the transition. Is called <i>once</i>.
	 */
	protected abstract void create();

	/**
	 * Takes care of actually rendering the transition.
	 * 
	 * @param delta
	 *            the time delta
	 * @param lastScreen
	 *            the old screen as a texture region
	 * @param currScreen
	 *            the screen the manager is transitioning to as a texture region
	 */
	public abstract void render(float delta, TextureRegion lastScreen,
			TextureRegion currScreen);

	/**
	 * @return whether the transition is done
	 */
	public abstract boolean isDone();

	/**
	 * Called when the {@linkplain ApplicationListener#resize(int, int) game is
	 * resized}, the transition was {@linkplain #isInitialized() initialized}
	 * before and the new size is different to the previous one.
	 * <p>
	 * In addition, this method is called once right after the transition was
	 * initialized ({@link #create()}).
	 * 
	 * @param width
	 *            the new width in pixels
	 * @param height
	 *            the new height in pixels
	 */
	public abstract void resize(int width, int height);

	/**
	 * Is called to reset the transition for another use.
	 * 
	 * This is done right before the transition is to be used.
	 */
	public void reset() {
		initializeScreenTransition();
	}

}
