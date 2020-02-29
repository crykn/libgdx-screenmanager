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
