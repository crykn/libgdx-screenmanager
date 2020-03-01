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

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * A transition that blends two screens together over a certain time interval.
 *
 * @author damios
 */
public class BlendingScreenTransition extends TimedScreenTransition {

	private SpriteBatch batch;

	public BlendingScreenTransition(SpriteBatch batch) {
		super(1F);

		this.batch = batch;
	}

	@Override
	protected void create() {
		// not needed
	}

	@Override
	public void render(float delta, TextureRegion lastScreen,
			TextureRegion currScreen, float progress) {
		batch.begin();

		// Blends the two screens
		Color c = batch.getColor();
		batch.setColor(c.r, c.g, c.b, 1);
		batch.draw(lastScreen, 0, 0);

		batch.setColor(c.r, c.g, c.b, progress);
		batch.draw(currScreen, 0, 0);

		batch.end();
	}

	@Override
	public void dispose() {
		// not needed
	}
}
