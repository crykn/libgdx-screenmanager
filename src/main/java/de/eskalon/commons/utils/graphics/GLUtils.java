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

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.utils.GdxRuntimeException;

/**
 * OpenGL utilities.
 * 
 * @author damios
 */
public final class GLUtils {

	private GLUtils() {
		throw new UnsupportedOperationException();
	}

	/**
	 * The buffer used internally. A size of 64 bytes is required as at most 16
	 * integer elements can be returned.
	 */
	private static final IntBuffer USED_INT_BUFF = ByteBuffer
			.allocateDirect(16 * Integer.BYTES).order(ByteOrder.nativeOrder())
			.asIntBuffer();

	/**
	 * Returns the name of the currently bound framebuffer
	 * ({@code GL_FRAMEBUFFER_BINDING}).
	 * 
	 * @return the name of the currently bound framebuffer; the initial value is
	 *         {@code 0}, indicating the default framebuffer
	 * 
	 * @see <a href= "https://github.com/libgdx/libgdx/issues/4688">The libGDX
	 *      issue detailing the WebGL problems</a>
	 */
	public static synchronized int getBoundFboHandle() {
		if (Gdx.app.getType() == ApplicationType.WebGL)
			throw new GdxRuntimeException(
					"This operation is not supported on WebGL without the libgdx-screenmanager-gwt extension!");

		IntBuffer intBuf = USED_INT_BUFF;
		Gdx.gl.glGetIntegerv(GL20.GL_FRAMEBUFFER_BINDING, intBuf);
		return intBuf.get(0);
	}

	/**
	 * @return the current gl viewport ({@code GL_VIEWPORT}) as an array,
	 *         containing four values: the x and y window coordinates of the
	 *         viewport, followed by its width and height.
	 */
	public static synchronized int[] getViewport() {
		IntBuffer intBuf = USED_INT_BUFF;
		Gdx.gl.glGetIntegerv(GL20.GL_VIEWPORT, intBuf);

		return new int[] { intBuf.get(0), intBuf.get(1), intBuf.get(2),
				intBuf.get(3) };
	}

}
