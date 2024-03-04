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

import org.jspecify.annotations.Nullable;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;

import de.damios.guacamole.Preconditions;
import de.eskalon.commons.screen.transition.impl.SlidingDirection;
import de.eskalon.commons.screen.transition.impl.SlidingInTransition;
import de.eskalon.commons.screen.transition.impl.SlidingOutTransition;

/**
 * The base class for sliding screen transitions. Can be reused.
 *
 * @author damios
 * 
 * @see SlidingInTransition
 * @see SlidingOutTransition
 */
public class SlidingTransition extends BatchTransition {

	private SlidingDirection dir;
	/**
	 * {@code true} if the last screen should slide out; {@code false} if the
	 * new screen should slide in.
	 */
	private boolean slideLastScreen;

	public SlidingTransition(SpriteBatch batch, SlidingDirection dir,
			boolean slideLastScreen, float duration,
			@Nullable Interpolation interpolation) {
		super(batch, duration, interpolation);
		Preconditions.checkNotNull(dir);

		this.dir = dir;
		this.slideLastScreen = slideLastScreen;
	}

	@Override
	public void render(float delta, TextureRegion lastScreen,
			TextureRegion currScreen, float progress) {
		batch.begin();

		if (slideLastScreen) { // slide out
			batch.draw(currScreen, 0, 0, width, height);
			batch.draw(lastScreen, width * dir.xPosFactor * progress,
					height * dir.yPosFactor * progress, width, height);
		} else { // slide in
			batch.draw(lastScreen, 0, 0, width, height);
			batch.draw(currScreen, width * dir.xPosFactor * (progress - 1),
					height * dir.yPosFactor * (progress - 1), width, height);
		}

		batch.end();
	}

}
