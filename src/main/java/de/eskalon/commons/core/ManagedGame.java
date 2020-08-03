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
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;

import de.eskalon.commons.screen.ManagedScreen;
import de.eskalon.commons.screen.ScreenManager;
import de.eskalon.commons.screen.transition.ScreenTransition;
import de.eskalon.commons.utils.BasicInputMultiplexer;

/**
 * A game class that utilizes a {@linkplain ScreenManager screen manager}.
 * <p>
 * Input listeners have to be added via the game's {@linkplain #inputProcessor
 * input multiplexer}.
 * 
 * @author damios
 * 
 * @see ScreenManager How to register screens and tranistions.
 */
public class ManagedGame<S extends ManagedScreen, T extends ScreenTransition>
		extends BasicApplication {

	/**
	 * The input multiplexer of the game. Must be used to add input listeners
	 * instead of {@link Input#setInputProcessor(InputProcessor)}.
	 */
	protected final BasicInputMultiplexer inputProcessor = new BasicInputMultiplexer();
	/**
	 * The game's screen manager. Is used to register and push new
	 * screens/transitions.
	 */
	protected ScreenManager<S, T> screenManager;

	public ManagedGame() {
		super();
		this.screenManager = new ScreenManager<S, T>();
	}

	@Override
	public void create() {
		super.create();

		Gdx.input.setInputProcessor(inputProcessor);
		screenManager.initialize(inputProcessor, getWidth(), getHeight(),
				false);
	}

	@Override
	public void render() {
		screenManager.render(Gdx.graphics.getDeltaTime());
	}

	@Override
	public void resize(int width, int height) {
		super.resize(width, height);

		if (width != 0 && height != 0) // if the window is minimized on Windows,
										// resize(0, 0) is called. However, a
										// framebuffer with these dimensions
										// cannot be created.
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

	/**
	 * Returns the input multiplexer of the game. Must be used to add input
	 * listeners instead of {@link Input#setInputProcessor(InputProcessor)}.
	 *
	 * @return the game's input multiplexer
	 */
	public BasicInputMultiplexer getInputMultiplexer() {
		return inputProcessor;
	}

}
