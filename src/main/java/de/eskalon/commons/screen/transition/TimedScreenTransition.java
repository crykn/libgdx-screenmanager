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

import com.badlogic.gdx.graphics.Texture;

/**
 * A screen transition that lasts for a certain duration.
 * 
 * @author damios
 */
public abstract class TimedScreenTransition extends ScreenTransition {

	private float duration;
	private float timePassed;

	/**
	 * @param duration
	 *            the transition's duration in seconds.
	 */
	public TimedScreenTransition(float duration) {
		this.duration = duration;
	}

	@Override
	public void reset() {
		super.reset();
		this.timePassed = 0;
	}

	@Override
	public final void render(float delta, Texture lastScreen,
			Texture currScreen) {
		this.timePassed = this.timePassed + delta;

		float progress = this.timePassed / duration;

		render(delta, lastScreen, currScreen, progress > 1F ? 1F : progress);
	}

	/**
	 * The render method to use in the timed transition.
	 * 
	 * @param delta
	 * @param lastScreen
	 * @param currScreen
	 * @param progress
	 *            the progress of the transition; from {@code 0} (excl.) to
	 *            {@code 1} (incl.)
	 */
	public abstract void render(float delta, Texture lastScreen,
			Texture currScreen, float progress);

	@Override
	public boolean isDone() {
		if (this.timePassed >= this.duration) {
			return true;
		}
		return false;
	}

}
