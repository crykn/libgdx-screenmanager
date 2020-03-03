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
 * @author damios
 * 
 * @see GLTransitionsShaderTransition
 */
public class ShaderTransition extends TimedTransition {

	private ShaderProgram program;

	private OrthographicCamera camera;
	/**
	 * A screen filling quad.
	 */
	private Mesh screenQuad;
	private int projTransLoc;
	private int lastScreenLoc, currScreenLoc;
	private int progressLoc;

	public ShaderTransition(String vert, String frag, OrthographicCamera camera,
			float duration, @Nullable Interpolation interpolation) {
		super(duration, interpolation);

		Preconditions.checkNotNull(vert, "The vertex shader cannot be null.");
		Preconditions.checkNotNull(frag, "The fragment shader cannot be null.");
		Preconditions.checkNotNull(camera);

		this.camera = camera;
		this.program = new ShaderProgram(vert, frag);

		Preconditions.checkArgument(this.program.isCompiled(),
				"Failed to compile shader program: " + this.program.getLog());
	}

	@Override
	protected void create() {
		this.projTransLoc = this.program.getUniformLocation("u_projTrans");
		this.lastScreenLoc = this.program.getUniformLocation("lastScreen");
		this.currScreenLoc = this.program.getUniformLocation("currScreen");
		this.progressLoc = this.program.getUniformLocation("progress");

		resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
	}

	@Override
	public void render(float delta, TextureRegion lastScreen,
			TextureRegion currScreen, float progress) {
		this.program.begin();

		// Set uniforms
		this.program.setUniformMatrix(this.projTransLoc, camera.combined);
		this.program.setUniformf(this.progressLoc, progress);
		this.program.setUniformi(this.lastScreenLoc, 1);
		this.program.setUniformi(this.currScreenLoc, 2);

		// Bind textures
		Gdx.gl20.glActiveTexture(GL20.GL_TEXTURE1);
		lastScreen.getTexture().bind();
		Gdx.gl20.glActiveTexture(GL20.GL_TEXTURE2);
		currScreen.getTexture().bind();
		Gdx.gl20.glActiveTexture(GL20.GL_TEXTURE0);

		// Render the screens using the shader
		this.screenQuad.render(this.program, GL20.GL_TRIANGLE_STRIP);

		this.program.end();
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
		this.screenQuad.dispose();
	}

}
