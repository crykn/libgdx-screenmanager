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

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;

import de.eskalon.commons.input.BasicInputMultiplexer;
import de.eskalon.commons.utils.ImplementationVersionUtils;

/**
 * A basic game. Takes care of setting some convenience variables and constants.
 * Furthermore, adds an {@linkplain #getInputMultiplexer() input multiplexer}.
 * 
 * @author damios
 */
class BasicGame extends ApplicationAdapter {

	/**
	 * The version the application is running on. Set via the jar manifest. Is
	 * {@code Development} if the game is started in a development environment.
	 * <p>
	 * Is always {@code Web} on GWT.
	 */
	public final String VERSION;
	/**
	 * Whether the application is running in a development environment. Checks
	 * if a {@linkplain #VERSION version} is set in the jar manifest.
	 * <p>
	 * Is always {@code false} on GWT.
	 */
	public final boolean IN_DEV_ENV;

	/**
	 * @see Graphics#getWidth()
	 */
	protected int viewportWidth;
	/**
	 * @see Graphics#getHeight()
	 */
	protected int viewportHeight;

	private BasicInputMultiplexer inputProcessor = new BasicInputMultiplexer();

	public BasicGame() {
		IN_DEV_ENV = ImplementationVersionUtils.get() == null;
		VERSION = IN_DEV_ENV ? "Development" : ImplementationVersionUtils.get();
	}

	@Override
	public void create() {
		this.viewportWidth = Gdx.graphics.getWidth();
		this.viewportHeight = Gdx.graphics.getHeight();

		Gdx.input.setInputProcessor(inputProcessor);
	}

	/**
	 * @return the viewport width
	 */
	public int getWidth() {
		return this.viewportWidth;
	}

	/**
	 * @return the viewport height
	 */
	public int getHeight() {
		return this.viewportHeight;
	}

	@Override
	public void resize(int width, int height) {
		this.viewportWidth = width;
		this.viewportHeight = height;
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
