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

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;

import de.eskalon.commons.screen.ManagedScreen;
import de.eskalon.commons.screen.ScreenManager;
import de.eskalon.commons.screen.transition.ScreenTransition;
import de.eskalon.commons.utils.BasicInputMultiplexer;

/**
 * A game class that utilizes a {@linkplain ScreenManager screen manager}. Use
 * the {@linkplain #getScreenManager() provided instance} to
 * {@linkplain ScreenManager#pushScreen(String, String, Object...) push
 * screens}.
 * <p>
 * Input listeners have to be added via the game's {@linkplain #inputProcessor
 * input multiplexer}.
 * 
 * @author damios
 */
public class ManagedGame<S extends ManagedScreen, T extends ScreenTransition>
		extends ApplicationAdapter {

	/**
	 * The input multiplexer of the game. Should be used to add input processors
	 * instead of {@code Gdx.input.setInputProcessor(InputProcessor)}.
	 * Otherwise, the automatic registration and unregistration of a
	 * {@linkplain ManagedScreen#getInputProcessors() screen's input processors}
	 * does not work.
	 */
	protected final BasicInputMultiplexer inputProcessor = new BasicInputMultiplexer();
	/**
	 * The game's screen manager. Is used to push new screens/transitions.
	 */
	protected ScreenManager<S, T> screenManager;

	public ManagedGame(ScreenManager<S, T> screenManager) {
		this.screenManager = screenManager;
	}

	public ManagedGame() {
		this(new ScreenManager<S, T>());
	}

	@Override
	public void create() {
		super.create();

		Gdx.input.setInputProcessor(inputProcessor);
		screenManager.initialize(inputProcessor, Gdx.graphics.getWidth(),
				Gdx.graphics.getHeight(), false);
	}

	@Override
	public void render() {
		screenManager.render(Gdx.graphics.getDeltaTime());
	}

	/**
	 * Called when the {@link Application} is resized. This can happen at any
	 * point during a non-paused state, but will never happen before a call to
	 * {@link #create()}.
	 * <p>
	 * {@code resize(0, 0)} calls, which may happen when the game is minimized
	 * on Windows, are ignored.
	 * 
	 * @param width
	 *            the new width in pixels
	 * @param height
	 *            the new height in pixels
	 */
	@Override
	public void resize(int width, int height) {
		if (width == 0 || height == 0) // if the game is minimized on Windows,
										// resize(0, 0) is called. This causes
										// problems, as a framebuffer with these
										// dimensions cannot be created.
										// Therefore, it is simply ignored.
			return;

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
