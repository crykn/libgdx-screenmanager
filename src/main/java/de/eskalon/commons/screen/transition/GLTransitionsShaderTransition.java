package de.eskalon.commons.screen.transition;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Interpolation;

public class GLTransitionsShaderTransition extends ShaderTransition {

	private static final String VERT_SHADER = "#ifdef GL_ES\n" + 
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
			"    v_texCoord0 = a_texCoord0;\n" + 
			"    gl_Position = u_projTrans * vec4(a_position, 1.0);\n" + 
			"}";
	private static final String FRAG_SHADER_PREFIX = "#ifdef GL_ES\n" + 
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
			"}\n" + 
			"\n" + 
			"void main() {\n" + 
			"	out_Color = transition(v_texCoord0);\n" + 
			"}\n";
	
	public GLTransitionsShaderTransition(String glTransitionsCode,
			OrthographicCamera camera, float duration,
			Interpolation interpolation) {
		super(VERT_SHADER, FRAG_SHADER_PREFIX + glTransitionsCode, camera,
				duration, interpolation);
	}

}
