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

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.utils.DefaultTextureBinder;
import com.badlogic.gdx.graphics.g3d.utils.RenderContext;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import de.damios.guacamole.Preconditions;
import de.damios.guacamole.gdx.graphics.QuadMeshGenerator;
import de.damios.guacamole.gdx.graphics.ShaderProgramFactory;
import de.eskalon.commons.screen.transition.TimedTransition;

/**
 * A transition that is using a shader to render the two transitioning screens.
 * <p>
 * The following uniforms are set before rendering and thus have to be specified
 * in the shader code:
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

	protected ShaderProgram program;
	protected Viewport viewport;

	private RenderContext renderContext;
	/**
	 * A screen filling quad.
	 */
	private Mesh screenQuad;
	private int projTransLoc;
	private int lastScreenLoc, currScreenLoc;
	private int progressLoc;

	/**
	 * Creates a shader transition.
	 * <p>
	 * The shader {@linkplain #compileShader(String, String, boolean) has to be
	 * compiled} before {@link #create()} is called.
	 * 
	 * @param duration
	 *            the transition's duration in seconds
	 * 
	 * @see #ShaderTransition(OrthographicCamera, float, Interpolation)
	 */
	public ShaderTransition(float duration) {
		this(duration, null);
	}

	/**
	 * Creates a shader transition.
	 * <p>
	 * The shader {@linkplain #compileShader(String, String, boolean) has to be
	 * compiled} before {@link #create()} is called.
	 * 
	 * @param duration
	 *            the transition's duration in seconds
	 * @param interpolation
	 *            the interpolation to use
	 */
	public ShaderTransition(float duration,
			@Nullable Interpolation interpolation) {
		super(duration, interpolation);
	}

	/**
	 * @param vert
	 *            the vertex shader code
	 * @param frag
	 *            the fragment shader code
	 * @param ignorePrepend
	 *            whether to ignore the code in
	 *            {@link ShaderProgram#prependFragmentCode} and
	 *            {@link ShaderProgram#prependVertexCode}
	 */
	public void compileShader(String vert, String frag, boolean ignorePrepend) {
		Preconditions.checkNotNull(vert, "The vertex shader cannot be null.");
		Preconditions.checkNotNull(frag, "The fragment shader cannot be null.");

		this.program = ShaderProgramFactory.fromString(vert, frag, true,
				ignorePrepend);
	}

	@Override
	protected void create() {
		Preconditions.checkState(this.program != null,
				"The shader has to be compiled before the transition can be created!");

		this.viewport = new ScreenViewport(); // Takes care of rendering the
												// transition over the whole
												// screen

		this.projTransLoc = this.program.getUniformLocation("u_projTrans");
		this.lastScreenLoc = this.program.getUniformLocation("lastScreen");
		this.currScreenLoc = this.program.getUniformLocation("currScreen");
		this.progressLoc = this.program.getUniformLocation("progress");

		this.renderContext = new RenderContext(
				new DefaultTextureBinder(DefaultTextureBinder.ROUNDROBIN));
	}

	@Override
	public void render(float delta, TextureRegion lastScreen,
			TextureRegion currScreen, float progress) {
		viewport.apply();

		this.renderContext.begin();
		this.program.bind();

		// Set uniforms
		this.program.setUniformMatrix(this.projTransLoc,
				viewport.getCamera().combined);
		this.program.setUniformf(this.progressLoc, progress);
		this.program.setUniformi(this.lastScreenLoc,
				renderContext.textureBinder.bind(lastScreen.getTexture()));
		this.program.setUniformi(this.currScreenLoc,
				renderContext.textureBinder.bind(currScreen.getTexture()));

		// Render the screens using the shader
		this.screenQuad.render(this.program, GL20.GL_TRIANGLE_STRIP);

		this.renderContext.end();
	}

	@Override
	public void resize(int width, int height) {
		viewport.update(width, height, true);

		if (this.screenQuad != null)
			this.screenQuad.dispose();
		this.screenQuad = QuadMeshGenerator.createFullScreenQuad(width, height,
				true);
	}

	@Override
	public void dispose() {
		if (this.program != null)
			this.program.dispose();
		if (this.screenQuad != null)
			this.screenQuad.dispose();
	}

	/**
	 * @return the used shader
	 */
	public ShaderProgram getProgram() {
		return program;
	}

}
