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

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;

import de.damios.guacamole.Preconditions;

/**
 * A screen transition that lasts for a certain duration.
 * <p>
 * The transition is reset on {@link #show()} and can thus be reused.
 * 
 * @author damios
 */
public abstract class TimedTransition extends ScreenTransition {

	protected @Nullable Interpolation interpolation;
	protected float duration;
	protected float timePassed;

	/**
	 * @param duration
	 *            the transition's duration in seconds
	 * @param interpolation
	 *            the interpolation to use
	 * 
	 * @see <a href=
	 *      "https://github.com/libgdx/libgdx/wiki/Interpolation#visual-display-of-interpolations">A
	 *      visual representation of the different interpolation modes</a>
	 */
	public TimedTransition(float duration,
			@Nullable Interpolation interpolation) {
		Preconditions.checkArgument(duration > 0);
		this.interpolation = interpolation;
		this.duration = duration;
	}

	/**
	 * @param duration
	 *            the transition's duration in seconds
	 */
	public TimedTransition(float duration) {
		this(duration, null);
	}

	@Override
	public void show() {
		this.timePassed = 0;
	}

	@Override
	public void render(float delta, TextureRegion lastScreen,
			TextureRegion currScreen) {
		this.timePassed = this.timePassed + delta;

		float progress = this.timePassed / duration;
		if (interpolation != null)
			progress = interpolation.apply(progress);

		render(delta, lastScreen, currScreen, progress > 1F ? 1F : progress);
	}

	/**
	 * The render method to use in the timed transition.
	 * 
	 * @param delta
	 *            the {@linkplain #interpolation interpolated} time delta in
	 *            seconds
	 * @param lastScreen
	 *            the old screen as a texture region
	 * @param currScreen
	 *            the screen the manager is transitioning to as a texture region
	 * @param progress
	 *            the progress of the transition; from {@code 0} (excl.) to
	 *            {@code 1} (incl.)
	 */
	public abstract void render(float delta, TextureRegion lastScreen,
			TextureRegion currScreen, float progress);

	@Override
	public boolean isDone() {
		if (this.timePassed >= this.duration) {
			return true;
		}
		return false;
	}

}
