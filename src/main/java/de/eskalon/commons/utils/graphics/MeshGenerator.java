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

import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;

/**
 * Utility methods for generating meshes.
 * 
 * @author damios
 */
public final class MeshGenerator {

	private MeshGenerator() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param screenWidth
	 * @param screenHeight
	 * @param flipY
	 * @return a screen filling quad
	 */
	public static Mesh createFullScreenQuad(int screenWidth, int screenHeight,
			boolean flipY) {
		return createQuad(0, 0, screenWidth, screenHeight, flipY);
	}

	/**
	 * @param x
	 *            the bottom left x
	 * @param y
	 *            the bottom left y
	 * @param width
	 * @param height
	 * @param flipY
	 * @return a quad with the given dimensions
	 */
	public static Mesh createQuad(float x, float y, float width, float height,
			boolean flipY) {
		return createQuadFromCoordinates(x, y, x + width, y + height, flipY);
	}

	/**
	 * Coordinate system: y-up.
	 * 
	 * @param x1
	 *            the left x
	 * @param y1
	 *            the bottom y
	 * @param x2
	 *            the right x
	 * @param y2
	 *            the top y
	 * @param flipY
	 * @return a quad with the given coordinates
	 */
	public static Mesh createQuadFromCoordinates(float x1, float y1, float x2,
			float y2, boolean flipY) {
		float[] verts = new float[20];
		int i = 0;

		// Bottom left
		verts[i++] = x1;
		verts[i++] = y1;
		verts[i++] = 0;
		verts[i++] = 0;
		verts[i++] = flipY ? 0 : 1;

		// Top left
		verts[i++] = x1;
		verts[i++] = y2;
		verts[i++] = 0;
		verts[i++] = 0;
		verts[i++] = flipY ? 1 : 0;

		// Bottom right
		verts[i++] = x2;
		verts[i++] = y1;
		verts[i++] = 0;
		verts[i++] = 1;
		verts[i++] = flipY ? 0 : 1;

		// Top right
		verts[i++] = x2;
		verts[i++] = y2;
		verts[i++] = 0;
		verts[i++] = 1;
		verts[i++] = flipY ? 1 : 0;

		Mesh mesh = new Mesh(true, 4, 0, VertexAttribute.Position(),
				VertexAttribute.TexCoords(0));

		mesh.setVertices(verts);
		return mesh;
	}

}
