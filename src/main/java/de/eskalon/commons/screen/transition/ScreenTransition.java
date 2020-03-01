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

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Disposable;

import de.eskalon.commons.screen.ScreenManager;

/**
 * A transition effect between two screen for use with a {@link ScreenManager}.
 * Transitions are intended as objects that are created only once when the game
 * is started.
 * <p>
 * The {@link #create()} method is called when the transition is first used. It
 * can also be initialized manually by calling
 * {@link #initializeScreenTransition()}, which should normally be done by a
 * loading screen after the assets have been loaded.
 * 
 * @author damios
 * 
 * @see ScreenManager#pushScreen(String, String)
 */
public abstract class ScreenTransition implements Disposable {

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
	 * Is called to reset the transition for another use.
	 * 
	 * This is done right before the transition is to be used.
	 */
	public void reset() {
		initializeScreenTransition();
	}

}
