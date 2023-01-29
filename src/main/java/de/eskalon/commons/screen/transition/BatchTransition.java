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
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import de.damios.guacamole.Preconditions;
import de.eskalon.commons.screen.transition.impl.BlankTimedTransition;

/**
 * The base class for all transitions using a {@link SpriteBatch}.
 * 
 * @author damios
 */
public abstract class BatchTransition extends BlankTimedTransition {

	protected SpriteBatch batch;
	protected Viewport viewport;
	protected int width, height;

	/**
	 * @param batch
	 *            the batch used for rendering the transition. If it is used
	 *            outside of the transitions, don't forget to set the projection
	 *            matrix before using it again!
	 * @param duration
	 *            the transition's duration in seconds
	 * @param interpolation
	 *            the interpolation to use
	 */
	public BatchTransition(SpriteBatch batch, float duration,
			@Nullable Interpolation interpolation) {
		super(duration, interpolation);
		Preconditions.checkNotNull(batch);

		this.batch = batch;
		this.viewport = new ScreenViewport(); // Takes care of rendering the
												// transition over the whole
												// screen
	}

	@Override
	public final void render(float delta, TextureRegion lastScreen,
			TextureRegion currScreen) {
		viewport.apply();
		batch.setProjectionMatrix(viewport.getCamera().combined);
		super.render(delta, lastScreen, currScreen);
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * The batch's projection matrix is already set.
	 * 
	 * @param delta
	 *            the {@linkplain #interpolation interpolated} time delta
	 * @param lastScreen
	 *            the old screen as a texture region
	 * @param currScreen
	 *            the screen the manager is transitioning to as a texture region
	 * @param progress
	 *            the progress of the transition; from {@code 0} (excl.) to
	 *            {@code 1} (incl.)
	 */
	@Override
	public abstract void render(float delta, TextureRegion lastScreen,
			TextureRegion currScreen, float progress);

	@Override
	public void resize(int width, int height) {
		this.width = width;
		this.height = height;

		viewport.update(width, height, true);
	}

}
