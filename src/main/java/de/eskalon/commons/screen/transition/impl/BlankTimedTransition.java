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

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;

import de.eskalon.commons.screen.transition.TimedTransition;

/**
 * A blank screen transition going on for a given duration.
 * 
 * @since 0.3.0
 * @author damios
 */
public class BlankTimedTransition extends TimedTransition {

	public BlankTimedTransition(float duration,
			@Nullable Interpolation interpolation) {
		super(duration, interpolation);
	}

	public BlankTimedTransition(float duration) {
		this(duration, null);
	}

	@Override
	public void render(float delta, TextureRegion lastScreen,
			TextureRegion currScreen, float progress) {
		// do nothing
	}

	@Override
	protected void create() {
		// not needed
	}

	@Override
	public void resize(int width, int height) {
		// not needed
	}

	@Override
	public void dispose() {
		// not needed
	}

}
