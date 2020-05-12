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

/**
 * A basic game application. Takes care of setting some convenience variables.
 * 
 * @author damios
 */
class BasicApplication extends ApplicationAdapter {

	/**
	 * @see Graphics#getWidth()
	 */
	protected int viewportWidth;
	/**
	 * @see Graphics#getHeight()
	 */
	protected int viewportHeight;

	@Override
	public void create() {
		this.viewportWidth = Gdx.graphics.getWidth();
		this.viewportHeight = Gdx.graphics.getHeight();
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

}
