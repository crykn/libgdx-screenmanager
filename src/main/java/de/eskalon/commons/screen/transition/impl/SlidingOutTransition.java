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
 * A transition where the last screen is sliding out. Can be reused.
 * 
 * @since 0.3.0
 * @author damios
 * 
 * @see SlidingInTransition
 */
public class SlidingOutTransition extends SlidingTransition {

	/**
	 * @param batch
	 *            the sprite batch used to render; is <i>not</i> disposed by the
	 *            transition
	 * @param dir
	 *            the direction the last screen should slide to
	 * @param duration
	 *            the duration (in seconds) over which the slide should happen
	 * @param interpolation
	 *            the interpolation used
	 */
	public SlidingOutTransition(SpriteBatch batch, SlidingDirection dir,
			float duration, @Nullable Interpolation interpolation) {
		super(batch, dir, true, duration, interpolation);
	}

	/**
	 * @param batch
	 *            the sprite batch used to render; is <i>not</i> disposed by the
	 *            transition
	 * @param dir
	 *            the direction the last screen should slide to
	 * @param duration
	 *            the duration (in seconds) over which the slide should happen
	 */
	public SlidingOutTransition(SpriteBatch batch, SlidingDirection dir,
			float duration) {
		this(batch, dir, duration, null);
	}

}
