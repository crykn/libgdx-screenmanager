/*
 * Copyright 2023 damios
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

package de.eskalon.commons.utils;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.utils.ScreenUtils;

import de.eskalon.commons.screen.ManagedScreen;

public class ScreenFboUtils {

	private ScreenFboUtils() {
		throw new UnsupportedOperationException();
	}

	/**
	 * Renders a {@linkplain ManagedScreen screen} into a texture region using
	 * the given {@linkplain FrameBuffer framebuffer}.
	 * 
	 * @param screen
	 *            the screen to be rendered
	 * @param fbo
	 *            the framebuffer the screen gets rendered into
	 * @param delta
	 *            the time delta
	 * 
	 * @return a texture region which contains the rendered screen
	 */
	public static TextureRegion screenToTexture(ManagedScreen screen,
			FrameBuffer fbo, float delta) {
		fbo.begin();
		ScreenUtils.clear(screen.getClearColor(), true);
		screen.render(delta);
		fbo.end();

		Texture texture = fbo.getColorBufferTexture();

		// flip the texture
		TextureRegion textureRegion = new TextureRegion(texture);
		textureRegion.flip(false, true);

		return textureRegion;
	}

}
