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

package de.eskalon.commons.screen.transition.impl;

import javax.annotation.Nullable;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;

import de.eskalon.commons.screen.transition.SlidingTransition;

/**
 * A transition where the new screen is sliding in.
 * 
 * @since 0.3.0
 * @author damios
 * 
 * @see SlidingOutTransition
 */
public class SlidingInTransition extends SlidingTransition {

	/**
	 * @param batch
	 *            the sprite batch used to render
	 * @param dir
	 *            the direction the new screen should slide to
	 * @param duration
	 *            the duration (in seconds) over which the slide should happen
	 * @param interpolation
	 *            the interpolation used
	 */
	public SlidingInTransition(SpriteBatch batch, SlidingDirection dir,
			float duration, @Nullable Interpolation interpolation) {
		super(batch, dir, false, duration, interpolation);
	}

	/**
	 * @param batch
	 *            the sprite batch used to render
	 * @param dir
	 *            the direction the new screen should slide to
	 * @param duration
	 *            the duration (in seconds) over which the slide should happen
	 */
	public SlidingInTransition(SpriteBatch batch, SlidingDirection dir,
			float duration) {
		this(batch, dir, duration, null);
	}

}
