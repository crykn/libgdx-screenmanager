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
import com.badlogic.gdx.math.Interpolation;
import com.google.common.base.Preconditions;

import de.eskalon.commons.screen.transition.TimedTransition;

/**
 * A transition where the new screen is sliding in in vertical slices.
 * 
 * @since 0.3.0
 * @author damios
 */
public class VerticalSlicingTransition extends TimedTransition {

	private SpriteBatch batch;
	private int sliceCount = 5;

	/**
	 * @param batch
	 *            the sprite batch used to render
	 * @param sliceCount
	 *            the count of slices used; has to be at least {@code 2}
	 * @param duration
	 *            the duration over which the transition should happen
	 * @param interpolation
	 *            the interpolation used
	 */
	public VerticalSlicingTransition(SpriteBatch batch, int sliceCount,
			float duration, @Nullable Interpolation interpolation) {
		super(duration, interpolation);
		Preconditions.checkArgument(sliceCount >= 2);
		this.batch = batch;
		this.sliceCount = sliceCount;
	}

	/**
	 * @param batch
	 *            the sprite batch used to render
	 * @param sliceCount
	 *            the count of slices used; has to be at least {@code 2}
	 * @param duration
	 *            the duration over which the transition should happen
	 */
	public VerticalSlicingTransition(SpriteBatch batch, int sliceCount,
			float duration) {
		this(batch, sliceCount, duration, null);
	}

	@Override
	protected void create() {
		// not needed
	}

	@Override
	public void render(float delta, TextureRegion lastScreen,
			TextureRegion currScreen, float progress) {
		batch.begin();
		batch.draw(lastScreen, 0, 0);

		int sliceWidth = lastScreen.getRegionWidth() / sliceCount;

		for (int i = 0; i < sliceCount; i++) {
			int x = i * sliceWidth;

			int offsetY = 0;

			if (i % 2 == 0) {
				offsetY = (int) (currScreen.getRegionHeight() * (progress - 1));
			} else {
				offsetY = (int) (currScreen.getRegionHeight() * (1 - progress));
			}

			batch.draw(currScreen.getTexture(), x, offsetY, sliceWidth,
					currScreen.getRegionHeight(), x, 0, sliceWidth,
					currScreen.getRegionHeight(), false, true);
		}
		batch.end();
	}

	@Override
	public void dispose() {
		// not needed
	}

}
