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

package de.eskalon.commons.core;

import com.badlogic.gdx.Gdx;

import de.eskalon.commons.screen.ManagedScreen;
import de.eskalon.commons.screen.ScreenManager;
import de.eskalon.commons.screen.transition.ScreenTransition;

/**
 * A game class that utilizes a {@linkplain ScreenManager screen manager}.
 * 
 * @author damios
 * 
 * @see ScreenManager How to register screens and tranistions.
 */
public class ManagedGame<S extends ManagedScreen, T extends ScreenTransition>
		extends BasicGame {

	protected ScreenManager<S, T> screenManager;

	public ManagedGame() {
		super();
		this.screenManager = new ScreenManager<S, T>();
	}

	@Override
	public void create() {
		super.create();

		screenManager.initialize(getInputMultiplexer(), getWidth(),
				getHeight());
	}

	@Override
	public void render() {
		screenManager.render(Gdx.graphics.getDeltaTime());
	}

	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
		screenManager.resize(width, height);
	}

	@Override
	public void pause() {
		screenManager.pause();
	}

	@Override
	public void resume() {
		screenManager.resume();
	}

	public ScreenManager<S, T> getScreenManager() {
		return screenManager;
	}

	@Override
	public void dispose() {
		screenManager.dispose();
	}

}
