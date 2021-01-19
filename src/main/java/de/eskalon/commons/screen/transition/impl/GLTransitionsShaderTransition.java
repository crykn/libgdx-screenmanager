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

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.utils.UIUtils;

/**
 * A transition that is using shader code conforming to the GL Transition
 * Specification v1.
 * <p>
 * This allows using the shaders provided at
 * <a href= "https://gl-transitions.com/gallery">gl-transitions.com</a> without
 * having to adapt their code.
 * <p>
 * The technical details: A GL Transition is a GLSL code that implements a
 * transition function which takes a {@code vec2 uv} pixel position and returns
 * a {@code vec4 color}. This color represents the mix of the {@code from} to
 * the {@code to} textures based on the variation of a contextual progress value
 * from {@code 0.0} to {@code 1.0}.
 * 
 * @since 0.4.0
 * @author damios
 *
 * @see <a href=
 *      "https://github.com/gl-transitions/gl-transitions#gl-transition">Additional
 *      information on the GL Transition spec</a>
 */
public class GLTransitionsShaderTransition extends ShaderTransition {

	// @formatter:off
	private static final String VERT_SHADER = "#version " + getVersion() + "\n" + 
			"#ifdef GL_ES\n" + 
			"precision mediump float;\n" + 
			"#endif\n" + 
			"\n" + 
			"attribute vec3 a_position;\n" + 
			"attribute vec2 a_texCoord0;\n" + 
			"\n" + 
			"uniform mat4 u_projTrans;\n" + 
			"\n" + 
			"varying vec3 v_position;\n" + 
			"varying vec2 v_texCoord0;\n" + 
			"\n" + 
			"void main() {\n" + 
			"	v_position = a_position;\n" + 
			"	v_texCoord0 = a_texCoord0;\n" + 
			"	gl_Position = u_projTrans * vec4(a_position, 1.0);\n" + 
			"}";
	private static final String VERT_SHADER_GLSL_150 = "#version 150\n" + 
			"#ifdef GL_ES\n" + 
			"precision mediump float;\n" + 
			"#endif\n" + 
			"\n" + 
			"in vec3 a_position;\n" + 
			"in vec2 a_texCoord0;\n" + 
			"\n" + 
			"uniform mat4 u_projTrans;\n" + 
			"\n" + 
			"out vec3 v_position;\n" + 
			"out vec2 v_texCoord0;\n" + 
			"\n" + 
			"void main() {\n" + 
			"	v_position = a_position;\n" + 
			"	v_texCoord0 = a_texCoord0;\n" + 
			"	gl_Position = u_projTrans * vec4(a_position, 1.0);\n" + 
			"}";
	private static final String FRAG_SHADER_PREPEND = "#version " + getVersion() + "\n" + 
			"#ifdef GL_ES\n" + 
			"precision mediump float;\n" + 
			"#endif\n" + 
			"\n" + 
			"varying vec3 v_position;\n" + 
			"varying vec2 v_texCoord0;\n" + 
			"\n" + 
			"\n" + 
			"\n" + 
			"uniform sampler2D lastScreen;\n" + 
			"uniform sampler2D currScreen;\n" + 
			"uniform float progress;\n" + 
			"\n" + 
			"vec4 getToColor(vec2 uv) {\n" + 
			"		return texture2D(currScreen, uv);\n" + 
			"}\n" + 
			"\n" + 
			"vec4 getFromColor(vec2 uv) {\n" + 
			"		return texture2D(lastScreen, uv);\n" + 
			"}\n";
	private static final String FRAG_SHADER_PREPEND_GLSL_150  = "#version 150\n" + 
			"#ifdef GL_ES\n" + 
			"precision mediump float;\n" + 
			"#endif\n" + 
			"\n" + 
			"in vec3 v_position;\n" + 
			"in vec2 v_texCoord0;\n" + 
			"\n" + 
			"out vec4 out_Color;\n" + 
			"\n" + 
			"uniform sampler2D lastScreen;\n" + 
			"uniform sampler2D currScreen;\n" + 
			"uniform float progress;\n" + 
			"\n" + 
			"vec4 getToColor(vec2 uv) {\n" + 
			"		return texture(currScreen, uv);\n" + 
			"}\n" + 
			"\n" + 
			"vec4 getFromColor(vec2 uv) {\n" + 
			"		return texture(lastScreen, uv);\n" + 
			"}\n";
	private static final String FRAG_SHADER_POSTPEND = "\nvoid main() {\n"
			+ "	gl_FragColor = transition(v_texCoord0);\n" + 
			"}\n";
	private static final String FRAG_SHADER_POSTPEND_GLSL_150 = "\nvoid main() {\n"
			+ "	out_Color = transition(v_texCoord0);\n" + 
			"}\n";
	// @formatter:on

	private static String getVersion() {
		if (Gdx.app.getType() == ApplicationType.Desktop
				|| Gdx.app.getType() == ApplicationType.HeadlessDesktop)
			return "120"; // Desktop
		return "100"; // GLSL ES (Android, iOS, WebGL)
	}

	/**
	 * Creates a shader transition using a GL Transition code.
	 * <p>
	 * The shader {@linkplain #compileGLTransition(String) has to be compiled}
	 * before {@link #create()} is called.
	 * 
	 * @param duration
	 * @param interpolation
	 */
	public GLTransitionsShaderTransition(float duration,
			@Nullable Interpolation interpolation) {
		super(duration, interpolation);
	}

	/**
	 * The GL Transitions shader code has to be set via this method.
	 * <p>
	 * Do not forget to uncomment/set the uniforms that act as transition
	 * parameters! Please note that in GLSL EL (Web, Android, iOS) uniforms
	 * cannot be set from within the shader code, so this has to be done in Java
	 * via {@link #getProgram()} instead!
	 * <p>
	 * Furthermore, do not forget to replace {@code ratio} in the code with your
	 * screen ratio (width / height).
	 * <p>
	 * Ignores code in {@link ShaderProgram#prependFragmentCode} and
	 * {@link ShaderProgram#prependVertexCode}.
	 * 
	 * @param glTransitionsCode
	 *            the GL Transitions shader code;
	 */
	public void compileGLTransition(String glTransitionsCode) {
		// TODO use PlatformUtils.isMac (see
		// https://github.com/libgdx/libgdx/pull/5960)

		if (Gdx.gl30 != null && UIUtils.isMac) {
			// Mac only supports the OpenGL 3.2 _core_ profile, which is not
			// backward compatible
			super.compileShader(
					VERT_SHADER_GLSL_150, FRAG_SHADER_PREPEND_GLSL_150
							+ glTransitionsCode + FRAG_SHADER_POSTPEND_GLSL_150,
					true);
		} else {
			super.compileShader(VERT_SHADER, FRAG_SHADER_PREPEND
					+ glTransitionsCode + FRAG_SHADER_POSTPEND, true);
		}
	}

	/**
	 * @deprecated Call {@link #compileGLTransition(String)} instead!
	 */
	@Deprecated
	@Override
	public void compileShader(String vert, String frag, boolean ignorePrepend) {
		super.compileShader(vert, frag, ignorePrepend);
	}

}
