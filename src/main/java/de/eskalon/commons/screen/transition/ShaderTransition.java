package de.eskalon.commons.screen.transition;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Interpolation;
import com.google.common.base.Preconditions;

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
			float duration, Interpolation interpolation) {
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

		this.screenQuad = this.createFullScreenQuad();
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

	/**
	 * @return a screen filling quad
	 */
	public Mesh createFullScreenQuad() {
		float[] verts = new float[20];
		int i = 0;

		// Structure here is as follows:
		// x-coordinate (position) (left to right)
		// y-coordinate (position) (bottom to top)
		// z-coordinate (position) (depth)
		// u-coordinate (texture coordinate)
		// v-coordinate (texture coordinate)

		// TODO: texture coordinates are somehow wrong. Could be the fault of
		// the camera too.

		// lower left vertex
		verts[i++] = 0;
		verts[i++] = 0;
		verts[i++] = 0;
		verts[i++] = 0;
		verts[i++] = 0;

		// upper left vertex
		verts[i++] = 0;
		verts[i++] = Gdx.graphics.getHeight();
		verts[i++] = 0;
		verts[i++] = 0;
		verts[i++] = 1;

		// lower right vertex
		verts[i++] = Gdx.graphics.getWidth();
		verts[i++] = 0;
		verts[i++] = 0;
		verts[i++] = 1;
		verts[i++] = 0;

		// upper right vertex
		verts[i++] = Gdx.graphics.getWidth();
		verts[i++] = Gdx.graphics.getHeight();
		verts[i++] = 0;
		verts[i++] = 1;
		verts[i++] = 1;

		Mesh mesh = new Mesh(true, 4, 0,
				new VertexAttribute(Usage.Position, 3,
						ShaderProgram.POSITION_ATTRIBUTE),
				new VertexAttribute(Usage.TextureCoordinates, 2,
						ShaderProgram.TEXCOORD_ATTRIBUTE + "0"));

		mesh.setVertices(verts);
		return mesh;
	}

	@Override
	public void dispose() {
		this.program.dispose();
	}

}
