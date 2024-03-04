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

import org.jspecify.annotations.Nullable;

import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Interpolation;

import de.damios.guacamole.gdx.graphics.ShaderCompatibilityHelper;

/**
 * A transition that is using shader code conforming to the <i>GL Transition
 * Specification v1</i>. This allows using the shaders provided at
 * <a href= "https://gl-transitions.com/gallery">gl-transitions.com</a> without
 * having to adapt their code.
 * <p>
 * What is a GL Transition? It is a GLSL code that implements a
 * {@code transition} function which takes a {@code vec2 uv} pixel position and
 * returns a {@code vec4 color}. This color represents the mix of the
 * {@code from} to the {@code to} textures based on the variation of a
 * contextual progress value from {@code 0.0} to {@code 1.0}.
 * <p>
 * This transition can be reused.
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
	private static final String VERT_SHADER =
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
	private static final String FRAG_SHADER_PREPEND =
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
	private static final String FRAG_SHADER_POSTPEND = "\nvoid main() {\n"
			+ "	gl_FragColor = transition(v_texCoord0);\n" + 
			"}\n";
	// @formatter:on

	/**
	 * Creates a shader transition with code conforming to the GL Transitions
	 * spec. Please note that this entails the shader being compiled which needs
	 * to happen on the rendering thread!
	 * <p>
	 * Do not forget to set the transition shader's uniforms. In GLSL EL (Web,
	 * Android, iOS) uniforms cannot be set from within the shader code, so this
	 * has to be done in Java via {@link #getProgram()}{@code #setUniform...}
	 * instead! Furthermore, do not forget to replace {@code ratio} in the code
	 * with your screen ratio (width / height). Check out the <a href=
	 * "https://github.com/crykn/libgdx-screenmanager/wiki/How-to-use-GL-Transitions#some-example-code">wiki</a>
	 * for an in-depth explanation and example.
	 * <p>
	 * Code in {@link ShaderProgram#prependFragmentCode} and
	 * {@link ShaderProgram#prependVertexCode} is ignored. The provided shader
	 * is {@linkplain ShaderCompatibilityHelper#fromString(String, String)
	 * automatically ported} from from version 120 (~ OpenGL 2.1) to version 150
	 * (~ OpenGL 3.2) if needed.
	 * 
	 * @param glTransitionsCode
	 *            the GL Transitions shader code
	 * @param duration
	 *            the transition's duration in seconds
	 */
	public GLTransitionsShaderTransition(String glTransitionsCode,
			float duration) {
		this(glTransitionsCode, duration, null);
	}

	/**
	 * Creates a shader transition with code conforming to the GL Transitions
	 * spec. Please note that this entails the shader being compiled which needs
	 * to happen on the rendering thread!
	 * <p>
	 * Do not forget to set the transition shader's uniforms. In GLSL EL (Web,
	 * Android, iOS) uniforms cannot be set from within the shader code, so this
	 * has to be done in Java via {@link #getProgram()}{@code #setUniform...}
	 * instead! Furthermore, do not forget to replace {@code ratio} in the code
	 * with your screen ratio (width / height). Check out the <a href=
	 * "https://github.com/crykn/libgdx-screenmanager/wiki/How-to-use-GL-Transitions#some-example-code">wiki</a>
	 * for an in-depth explanation and example.
	 * <p>
	 * Code in {@link ShaderProgram#prependFragmentCode} and
	 * {@link ShaderProgram#prependVertexCode} is ignored. The provided shader
	 * is {@linkplain ShaderCompatibilityHelper#fromString(String, String)
	 * automatically ported} from from version 120 (~ OpenGL 2.1) to version 150
	 * (~ OpenGL 3.2) if needed.
	 * 
	 * @param glTransitionsCode
	 *            the GL Transitions shader code
	 * @param duration
	 *            the transition's duration in seconds
	 * @param interpolation
	 *            the interpolation to use
	 */
	public GLTransitionsShaderTransition(String glTransitionsCode,
			float duration, @Nullable Interpolation interpolation) {
		super(VERT_SHADER,
				FRAG_SHADER_PREPEND + glTransitionsCode + FRAG_SHADER_POSTPEND,
				true, duration, interpolation, true);
	}

}
