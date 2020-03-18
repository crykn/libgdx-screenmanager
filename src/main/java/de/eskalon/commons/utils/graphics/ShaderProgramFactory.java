package de.eskalon.commons.utils.graphics;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.GdxRuntimeException;

public class ShaderProgramFactory {

	private ShaderProgramFactory() {
		// not needed
	}

	/**
	 * 
	 * @param vertexShader
	 *            the vertex shader code
	 * @param fragmentShader
	 *            the fragment shader code
	 * @param throwException
	 *            whether to throw an exception when the shader couldn't be
	 *            compiled
	 * @param ignorePrepend
	 *            ignorePrepend whether to ignore the code in
	 *            {@link ShaderProgram#prependFragmentCode} and
	 *            {@link ShaderProgram#prependVertexCode}
	 * @return a shader program
	 */
	public static ShaderProgram createShaderProgram(String vertexShader,
			String fragmentShader, boolean throwException,
			boolean ignorePrepend) {
		String prependVertexCode = null, prependFragmentCode = null;
		if (ignorePrepend) {
			prependVertexCode = ShaderProgram.prependVertexCode;
			ShaderProgram.prependVertexCode = null;
			prependFragmentCode = ShaderProgram.prependFragmentCode;
			ShaderProgram.prependFragmentCode = null;
		}

		ShaderProgram program = new ShaderProgram(vertexShader, fragmentShader);

		if (ignorePrepend) {
			ShaderProgram.prependVertexCode = prependVertexCode;
			ShaderProgram.prependFragmentCode = prependFragmentCode;
		}

		if (throwException)
			ShaderPreconditions.checkCompiled(program);

		return program;
	}

	public static ShaderProgram createShaderProgram(String vertexShader,
			String fragmentShader, boolean throwException) {
		return createShaderProgram(vertexShader, fragmentShader, throwException,
				false);
	}

	public static ShaderProgram createShaderProgram(String vertexShader,
			String fragmentShader) {
		return createShaderProgram(vertexShader, fragmentShader, true);
	}

	public static ShaderProgram createShaderProgram(FileHandle vertexShader,
			FileHandle fragmentShader) {
		return createShaderProgram(vertexShader.readString(),
				fragmentShader.readString());
	}

	public static class ShaderPreconditions {

		private ShaderPreconditions() {
			// not needed
		}

		public static void checkCompiled(ShaderProgram program, String msg) {
			if (!program.isCompiled())
				throw new GdxRuntimeException(msg + program.getLog());
		}

		public static void checkCompiled(ShaderProgram program) {
			checkCompiled(program, "");
		}

	}

}
