package de.eskalon.commons.utils.graphics;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.GdxRuntimeException;

/**
 * A factory for {@link ShaderProgram}s, that supports some convenience options.
 * 
 * @author damios
 * 
 */
public final class ShaderProgramFactory {

	private ShaderProgramFactory() {
		throw new UnsupportedOperationException();
	}

	/**
	 * Creates a {@link ShaderProgram}.
	 * 
	 * @param vertexShader
	 *            the vertex shader code
	 * @param fragmentShader
	 *            the fragment shader code
	 * @param throwException
	 *            whether to throw an exception when the shader couldn't be
	 *            compiled
	 *            ({@link ShaderPreconditions#checkCompiled(ShaderProgram)})
	 * @param ignorePrepend
	 *            whether to ignore the code in
	 *            {@link ShaderProgram#prependFragmentCode} and
	 *            {@link ShaderProgram#prependVertexCode}; is useful to prevent
	 *            the version being set twice
	 * @return the shader program
	 */
	public static ShaderProgram fromString(String vertexShader,
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

	/**
	 * @param vertexShader
	 * @param fragmentShader
	 * @param throwException
	 *            whether to throw an exception when the shader couldn't be
	 *            compiled
	 * @return the shader program
	 */
	public static ShaderProgram fromString(String vertexShader,
			String fragmentShader, boolean throwException) {
		return fromString(vertexShader, fragmentShader, throwException, false);
	}

	/**
	 * Creates a ShaderProgram and automatically throws an
	 * {@link GdxRuntimeException} when it couldn't be compiled.
	 * 
	 * @param vertexShader
	 * @param fragmentShader
	 * @return the shader program
	 */
	public static ShaderProgram fromString(String vertexShader,
			String fragmentShader) {
		return fromString(vertexShader, fragmentShader, true);
	}

	/**
	 * Creates a ShaderProgram and automatically throws an
	 * {@link GdxRuntimeException} when it couldn't be compiled.
	 * 
	 * @param vertexShader
	 * @param fragmentShader
	 * @return the shader program
	 */
	public static ShaderProgram fromFile(FileHandle vertexShader,
			FileHandle fragmentShader) {
		return fromString(vertexShader.readString(),
				fragmentShader.readString());
	}

	/**
	 * A simple preconditions class used to check whether a
	 * {@link ShaderProgram} was properly compiled.
	 * 
	 * @author damios
	 */
	public static final class ShaderPreconditions {

		private ShaderPreconditions() {
			throw new UnsupportedOperationException();
		}

		/**
		 * Throws a {@link GdxRuntimeException} when the program was not
		 * compiled. The compilation log is appended to {@code msg}.
		 * 
		 * @param program
		 * @param msg
		 *            the exception's message
		 */
		public static void checkCompiled(ShaderProgram program, String msg) {
			if (!program.isCompiled())
				throw new GdxRuntimeException(msg + program.getLog());
		}

		/**
		 * Throws a {@link GdxRuntimeException} when the program was not
		 * compiled. The compilation log is printed as part of the exception's
		 * message.
		 * 
		 * @param program
		 */
		public static void checkCompiled(ShaderProgram program) {
			checkCompiled(program, "");
		}

	}

}
