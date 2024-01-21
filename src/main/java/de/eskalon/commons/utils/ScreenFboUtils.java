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

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.utils.ScreenUtils;

import de.damios.guacamole.Preconditions;
import de.damios.guacamole.annotations.Beta;
import de.damios.guacamole.gdx.graphics.GLUtils;
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

	@Beta
	public static int[] retrieveFboStatus() {
		int previousFBOHandle = GLUtils.getBoundFboHandle();
		int[] previousViewport = GLUtils.getViewport();

		return new int[] { previousFBOHandle, previousViewport[0],
				previousViewport[1], previousViewport[2], previousViewport[3] };
	}

	@Beta
	public static void restoreFboStatus(int[] status) {
		Preconditions.checkArgument(status.length == 5);
		Gdx.gl20.glBindFramebuffer(GL20.GL_FRAMEBUFFER, status[0]);
		Gdx.gl20.glViewport(status[1], status[2], status[3], status[4]);
	}

}
