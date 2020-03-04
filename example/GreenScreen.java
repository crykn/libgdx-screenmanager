import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

import de.eskalon.commons.screen.ManagedScreen;

public class GreenScreen extends ManagedScreen {

	private MyGdxGame game;
	private ShapeRenderer shapeRenderer;

	public GreenScreen() {
		this.game = (MyGdxGame) Gdx.app.getApplicationListener();
	}

	@Override
	protected void create() {
		this.shapeRenderer = new ShapeRenderer();
		this.shapeRenderer.getProjectionMatrix().setToOrtho2D(0, 0, game.getWidth(), game.getHeight());
		this.addInputProcessor(new InputAdapter() {
			@Override
			public boolean touchDown(int screenX, int screenY, int pointer, int button) {
				/*
				 * Switch using a blending transition.
				 */
				game.getScreenManager().pushScreen("blue", "blending_transition");
				return true;
			}
		});
	}

	@Override
	public void render(float delta) {
		/*
		 * Render a green circle.
		 */
		shapeRenderer.begin(ShapeType.Filled);
		shapeRenderer.setColor(Color.GREEN);
		shapeRenderer.circle(game.getWidth() / 2, game.getHeight() / 2, game.getHeight() / 2 - 40);
		shapeRenderer.end();
	}

	@Override
	public void dispose() {
		if (isInitialized())
			shapeRenderer.dispose();
	}

	@Override
	public void resize(int width, int height) {
		this.shapeRenderer.getProjectionMatrix().setToOrtho2D(0, 0, width, height);
		this.shapeRenderer.updateMatrices();
	}

	@Override
	public void hide() {
		// not needed
	}

}
