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

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;

import de.eskalon.commons.screen.transition.BatchTransition;

/**
 * A transition that blends two screens together over a certain time interval.
 *
 * @since 0.3.0
 * @author damios
 */
public class BlendingTransition extends BatchTransition {

	public BlendingTransition(SpriteBatch batch, float durationInSeconds,
			@Nullable Interpolation interpolation) {
		super(batch, durationInSeconds, interpolation);
	}

	public BlendingTransition(SpriteBatch batch, float durationInSeconds) {
		this(batch, durationInSeconds, null);
	}

	@Override
	public void render(float delta, TextureRegion lastScreen,
			TextureRegion currScreen, float progress) {
		batch.begin();

		// Blends the two screens
		Color c = batch.getColor();
		batch.draw(lastScreen, 0, 0, width, height);

		batch.setColor(c.r, c.g, c.b, progress);
		batch.draw(currScreen, 0, 0, width, height);
		batch.setColor(c.r, c.g, c.b, 1);

		batch.end();
	}

}
