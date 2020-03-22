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

/**
 * A transition where the new screen is sliding in, while the last screen is
 * sliding out. Thus, the new screen is pushing the last screen out, so to
 * speak.
 * 
 * @since 0.5.1
 * @author damios
 */
public class PushTransition extends BlankTimedTransition {

	private SlidingDirection dir;
	private SpriteBatch batch;

	/**
	 * @param batch
	 *            the sprite batch used to render
	 * @param dir
	 *            the direction of the push
	 * @param duration
	 *            the duration over which the transition should happen
	 * @param interpolation
	 *            the interpolation used
	 */
	public PushTransition(SpriteBatch batch, SlidingDirection dir,
			float duration, @Nullable Interpolation interpolation) {
		super(duration, interpolation);
		Preconditions.checkNotNull(batch);
		Preconditions.checkNotNull(dir);

		this.batch = batch;
		this.dir = dir;
	}

	/**
	 * @param batch
	 *            the sprite batch used to render
	 * @param dir
	 *            the direction of the push
	 * @param duration
	 *            the duration over which the transition should happen
	 */
	public PushTransition(SpriteBatch batch, SlidingDirection dir,
			float duration) {
		this(batch, dir, duration, null);
	}

	@Override
	public void render(float delta, TextureRegion lastScreen,
			TextureRegion currScreen, float progress) {
		batch.begin();

		batch.draw(currScreen,
				lastScreen.getRegionWidth() * dir.xPosFactor * (progress - 1),
				lastScreen.getRegionHeight() * dir.yPosFactor * (progress - 1));
		batch.draw(lastScreen,
				lastScreen.getRegionWidth() * dir.xPosFactor * progress,
				lastScreen.getRegionHeight() * dir.yPosFactor * progress);

		batch.end();
	}

}
