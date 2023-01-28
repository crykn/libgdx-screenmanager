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
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.HdpiUtils;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;

import de.damios.guacamole.Preconditions;
import de.eskalon.commons.screen.transition.BatchTransition;

/**
 * A transition where the new screen is sliding in in horizontal slices.
 * 
 * @since 0.3.0
 * @author damios
 */
public class HorizontalSlicingTransition extends BatchTransition {

	private int sliceCount;

	/**
	 * @param batch
	 *            the sprite batch used to render
	 * @param sliceCount
	 *            the count of slices used; has to be at least {@code 2}
	 * @param durationInSeconds
	 *            the duration (in seconds) over which the transition should happen
	 * @param interpolation
	 *            the interpolation used
	 */
	public HorizontalSlicingTransition(SpriteBatch batch, int sliceCount,
			float durationInSeconds, @Nullable Interpolation interpolation) {
		super(batch, durationInSeconds, interpolation);
		Preconditions.checkArgument(sliceCount >= 2,
				"The slice count has to be at least 2");

		this.sliceCount = sliceCount;
	}

	/**
	 * @param batch
	 *            the sprite batch used to render
	 * @param sliceCount
	 *            the count of slices used; has to be at least {@code 2}
	 * @param durationInSeconds
	 *            the duration (in seconds) over which the transition should happen
	 */
	public HorizontalSlicingTransition(SpriteBatch batch, int sliceCount,
			float durationInSeconds) {
		this(batch, sliceCount, durationInSeconds, null);
	}

	@Override
	public void render(float delta, TextureRegion lastScreen,
			TextureRegion currScreen, float progress) {
		batch.begin();

		batch.draw(lastScreen, 0, 0, width, height);

		int sliceHeight = MathUtils.ceil(height / (float) sliceCount);

		for (int i = 0; i < sliceCount; i++) {
			int y = i * sliceHeight;

			int offsetX = 0;

			if (i % 2 == 0) {
				offsetX = (int) (width * (progress - 1));
			} else {
				offsetX = (int) (width * (1 - progress));
			}

			batch.draw(currScreen.getTexture(), offsetX, y, width, sliceHeight,
					0, HdpiUtils.toBackBufferY(y),
					HdpiUtils.toBackBufferX(width),
					HdpiUtils.toBackBufferY(sliceHeight), false, true);
		}

		batch.end();
	}

}
