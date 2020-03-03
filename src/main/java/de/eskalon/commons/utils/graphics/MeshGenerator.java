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
		// not used
	}

	/**
	 * @param screenWidth
	 * @param screenHeight
	 * @param flipY
	 * @return a screen filling quad
	 */
	public static Mesh createFullScreenQuad(int screenWidth, int screenHeight,
			boolean flipY) {
		float[] verts = new float[20];
		int i = 0;

		// Bottom left
		verts[i++] = 0;
		verts[i++] = 0;
		verts[i++] = 0;
		verts[i++] = 0;
		verts[i++] = flipY ? 0 : 1;

		// Top left
		verts[i++] = 0;
		verts[i++] = screenHeight;
		verts[i++] = 0;
		verts[i++] = 0;
		verts[i++] = flipY ? 1 : 0;

		// Bottom right
		verts[i++] = screenWidth;
		verts[i++] = 0;
		verts[i++] = 0;
		verts[i++] = 1;
		verts[i++] = flipY ? 0 : 1;

		// Top right
		verts[i++] = screenWidth;
		verts[i++] = screenHeight;
		verts[i++] = 0;
		verts[i++] = 1;
		verts[i++] = flipY ? 1 : 0;

		Mesh mesh = new Mesh(true, 4, 0, VertexAttribute.Position(),
				VertexAttribute.TexCoords(0));

		mesh.setVertices(verts);
		return mesh;
	}

}
