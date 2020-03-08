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

package de.eskalon.commons.screen.transition.impl;

import javax.annotation.Nullable;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.utils.DefaultTextureBinder;
import com.badlogic.gdx.graphics.g3d.utils.RenderContext;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Interpolation;
import com.google.common.base.Preconditions;

import de.eskalon.commons.screen.transition.TimedTransition;
import de.eskalon.commons.utils.graphics.MeshGenerator;

/**
 * A transition that is using a shader to render the two transitioning screens.
 * <p>
 * The following uniforms are set before rendering:
 * <ul>
 * <li>vertex shader:</li>
 * <ul>
 * <li>{@code uniform mat4 u_projTrans}</li>
 * </ul>
 * <li>fragment shader:</li>
 * <ul>
 * <li>{@code uniform sampler2D lastScreen}</li>
 * <li>{@code uniform sampler2D currScreen}</li>
 * <li>{@code uniform float progress}</li>
 * </ul>
 * </ul>
 * 
 * @version 0.4.0
 * @author damios
 * 
 * @see GLTransitionsShaderTransition
 */
public class ShaderTransition extends TimedTransition {

	private ShaderProgram program;

	// Shader construction stuff
	private boolean ignorePrepend;
	protected String vertCode, fragCode;

	private OrthographicCamera camera;
	private RenderContext renderContext;
	/**
	 * A screen filling quad.
	 */
	private Mesh screenQuad;
	private int projTransLoc;
	private int lastScreenLoc, currScreenLoc;
	private int progressLoc;

	public ShaderTransition(String vert, String frag, OrthographicCamera camera,
			float duration) {
		this(vert, frag, camera, duration, null);
	}

	public ShaderTransition(String vert, String frag, OrthographicCamera camera,
			float duration, @Nullable Interpolation interpolation) {
		this(vert, frag, camera, duration, interpolation, false);
	}

	/**
	 * Creates a shader transition. The shader program is compiled in the
	 * {@link #create()} method.
	 * 
	 * @param vert
	 *            the vertex shader code
	 * @param frag
	 *            the fragment shader code
	 * @param camera
	 *            the camera
	 * @param duration
	 *            the duration of the transition
	 * @param interpolation
	 *            the interpolation to use
	 * @param ignorePrepend
	 *            whether to ignore the code in
	 *            {@link ShaderProgram#prependFragmentCode} and
	 *            {@link ShaderProgram#prependVertexCode}
	 */
	public ShaderTransition(String vert, String frag, OrthographicCamera camera,
			float duration, @Nullable Interpolation interpolation,
			boolean ignorePrepend) {
		super(duration, interpolation);

		Preconditions.checkNotNull(vert, "The vertex shader cannot be null.");
		Preconditions.checkNotNull(frag, "The fragment shader cannot be null.");
		Preconditions.checkNotNull(camera, "The camera cannot be null");

		this.camera = camera;
		this.ignorePrepend = ignorePrepend;
		this.vertCode = vert;
		this.fragCode = frag;
	}

	@Override
	protected void create() {
		// Compile the shader
		String prependVertexCode = null, prependFragmentCode = null;
		if (ignorePrepend) {
			prependVertexCode = ShaderProgram.prependVertexCode;
			ShaderProgram.prependVertexCode = null;
			prependFragmentCode = ShaderProgram.prependFragmentCode;
			ShaderProgram.prependFragmentCode = null;
		}

		this.program = new ShaderProgram(vertCode, fragCode);

		if (ignorePrepend) {
			ShaderProgram.prependVertexCode = prependVertexCode;
			ShaderProgram.prependFragmentCode = prependFragmentCode;
		}

		Preconditions.checkArgument(this.program.isCompiled(),
				"Failed to compile shader program: " + this.program.getLog());

		// Get the uniform locations
		this.projTransLoc = this.program.getUniformLocation("u_projTrans");
		this.lastScreenLoc = this.program.getUniformLocation("lastScreen");
		this.currScreenLoc = this.program.getUniformLocation("currScreen");
		this.progressLoc = this.program.getUniformLocation("progress");

		this.renderContext = new RenderContext(
				new DefaultTextureBinder(DefaultTextureBinder.ROUNDROBIN));

		resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
	}

	@Override
	public void render(float delta, TextureRegion lastScreen,
			TextureRegion currScreen, float progress) {
		this.renderContext.begin();
		this.program.begin();

		// Set uniforms
		this.program.setUniformMatrix(this.projTransLoc, camera.combined);
		this.program.setUniformf(this.progressLoc, progress);
		this.program.setUniformi(this.lastScreenLoc,
				renderContext.textureBinder.bind(lastScreen.getTexture()));
		this.program.setUniformi(this.currScreenLoc,
				renderContext.textureBinder.bind(currScreen.getTexture()));

		// Render the screens using the shader
		this.screenQuad.render(this.program, GL20.GL_TRIANGLE_STRIP);

		this.program.end();
		this.renderContext.end();
	}

	@Override
	public void resize(int width, int height) {
		if (this.screenQuad != null)
			this.screenQuad.dispose();
		this.screenQuad = MeshGenerator.createFullScreenQuad(width, height,
				true);
	}

	@Override
	public void dispose() {
		this.program.dispose();
		if (this.screenQuad != null)
			this.screenQuad.dispose();
	}

}
