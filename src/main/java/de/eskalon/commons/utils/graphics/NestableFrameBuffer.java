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

package de.eskalon.commons.utils.graphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.GLFrameBuffer;

/**
 * An implementation of the libGDX {@link FrameBuffer} that supports nested
 * framebuffers. This allows using multiple framebuffers inside each other:
 * 
 * <pre>
 * {@code 
 * fbo0.begin();
 * // Stuff is rendered into fbo0
 * fbo1.begin();
 * // Stuff is rendered into fbo1
 * fbo1.end();
 * // Stuff is rendered into fbo0 again 
 * // this is where the default FrameBuffer implementation would break
 * fbo0.end();
 * }
 * </pre>
 * 
 * @author damios
 * @see <a href=
 *      "https://github.com/crykn/libgdx-screenmanager/wiki/Custom-FrameBuffer-implementation">The
 *      wiki entry detailing the reasoning behind the implementation</a>
 */
public class NestableFrameBuffer extends FrameBuffer {

	private int previousFBOHandle = -1;
	private int[] previousViewport = new int[4];
	private boolean isActive = false;

	/**
	 * Creates a new NestableFrameBuffer having the given dimensions and
	 * potentially a depth buffer attached.
	 * 
	 * @param format
	 *            the format of the color buffer; according to the OpenGL ES 2.0
	 *            spec, only {@link Format#RGB565}, {@link Format#RGBA4444} and
	 *            {@code RGB5_A1} are color-renderable
	 * @param width
	 *            the width of the framebuffer in pixels
	 * @param height
	 *            the height of the framebuffer in pixels
	 * @param hasDepth
	 *            whether to attach a depth buffer
	 */
	public NestableFrameBuffer(Pixmap.Format format, int width, int height,
			boolean hasDepth) {
		super(format, width, height, hasDepth, false);
	}

	protected NestableFrameBuffer(
			GLFrameBufferBuilder<? extends GLFrameBuffer<Texture>> bufferBuilder) {
		super(bufferBuilder);
	}

	/**
	 * Binds the frame buffer and sets the viewport accordingly, so everything
	 * gets drawn to it.
	 */
	@Override
	public void begin() {
		if (isActive)
			throw new IllegalStateException(
					"end() has to be called before another draw can begin!");
		isActive = true;

		previousFBOHandle = GLUtils.getBoundFboHandle();
		bind();

		previousViewport = GLUtils.getViewport();
		setFrameBufferViewport();
	}

	/**
	 * Unbinds the framebuffer, all drawing will be performed to the
	 * {@linkplain #previousFBOHandle previous framebuffer} (usually the normal
	 * one) from here on.
	 */
	@Override
	public void end() {
		end(previousViewport[0], previousViewport[1], previousViewport[2],
				previousViewport[3]);
	}

	/**
	 * Unbinds the framebuffer and sets viewport sizes, all drawing will be
	 * performed to the {@linkplain #previousFBOHandle previous framebuffer}
	 * (usually the normal one) from here on.
	 *
	 * @param x
	 *            the x-axis position of the viewport in pixels
	 * @param y
	 *            the y-asis position of the viewport in pixels
	 * @param width
	 *            the width of the viewport in pixels
	 * @param height
	 *            the height of the viewport in pixels
	 */
	@Override
	public void end(int x, int y, int width, int height) {
		if (!isActive)
			throw new IllegalStateException("begin() has to be called first!");
		isActive = false;

		if (GLUtils.getBoundFboHandle() != framebufferHandle) {
			throw new IllegalStateException("The currently bound framebuffer ("
					+ GLUtils.getBoundFboHandle()
					+ ") doesn't match this one. Make sure the nested framebuffers are closed in the same order they were opened in!");
		}

		Gdx.gl20.glBindFramebuffer(GL20.GL_FRAMEBUFFER, previousFBOHandle);
		Gdx.gl20.glViewport(x, y, width, height);
	}

	@Override
	protected void build() {
		int previousFBOHandle = GLUtils.getBoundFboHandle();
		super.build();
		Gdx.gl20.glBindFramebuffer(GL20.GL_FRAMEBUFFER, previousFBOHandle);
	}

	/**
	 * A builder for a NestableFrameBuffer. Useful to add certain attachments.
	 */
	public static class NestableFrameBufferBuilder extends FrameBufferBuilder {
		public NestableFrameBufferBuilder(int width, int height) {
			super(width, height);
		}

		@Override
		public FrameBuffer build() {
			return new NestableFrameBuffer(this);
		}
	}

}
