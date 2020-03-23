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

package de.eskalon.commons.screen.transition;

import javax.annotation.Nullable;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.google.common.base.Preconditions;

import de.eskalon.commons.screen.transition.impl.BlankTimedTransition;
import de.eskalon.commons.screen.transition.impl.SlidingDirection;
import de.eskalon.commons.screen.transition.impl.SlidingInTransition;
import de.eskalon.commons.screen.transition.impl.SlidingOutTransition;

/**
 * The base class for sliding screen transitions.
 *
 * @author damios
 * 
 * @see SlidingInTransition
 * @see SlidingOutTransition
 */
public class SlidingTransition extends BlankTimedTransition {

	private SlidingDirection dir;
	private SpriteBatch batch;
	/**
	 * {@code true} if the last screen should slide out; {@code false} if the
	 * new screen should slide in.
	 */
	private boolean slideLastScreen;

	public SlidingTransition(SpriteBatch batch, SlidingDirection dir,
			boolean slideLastScreen, float duration,
			@Nullable Interpolation interpolation) {
		super(duration, interpolation);
		Preconditions.checkNotNull(batch);
		Preconditions.checkNotNull(dir);

		this.batch = batch;
		this.dir = dir;
		this.slideLastScreen = slideLastScreen;
	}

	@Override
	public void render(float delta, TextureRegion lastScreen,
			TextureRegion currScreen, float progress) {
		batch.begin();

		if (slideLastScreen) { // slide out
			batch.draw(currScreen, 0, 0);
			batch.draw(lastScreen,
					lastScreen.getRegionWidth() * dir.xPosFactor * progress,
					lastScreen.getRegionHeight() * dir.yPosFactor * progress);
		} else { // slide in
			batch.draw(lastScreen, 0, 0);
			batch.draw(currScreen,
					lastScreen.getRegionWidth() * dir.xPosFactor
							* (progress - 1),
					lastScreen.getRegionHeight() * dir.yPosFactor
							* (progress - 1));
		}

		batch.end();
	}

}
