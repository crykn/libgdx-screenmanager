import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import de.eskalon.commons.screen.ManagedScreen;

public class GreenScreen extends ManagedScreen {

	private MyGdxGame game;
	private ShapeRenderer shapeRenderer;
	private Viewport viewport;

	public GreenScreen() {
		this.game = (MyGdxGame) Gdx.app.getApplicationListener();
	}

	@Override
	protected void create() {
		this.shapeRenderer = new ShapeRenderer();
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

		// This screen uses a viewport; resize the window to see the effects
		viewport = new FitViewport(game.getWidth(), game.getHeight());
	}

	@Override
	public void render(float delta) {
		viewport.apply(); // you need to apply your viewport first
		shapeRenderer.setProjectionMatrix(viewport.getCamera().combined);

		/*
		 * Render a green circle on a gray background.
		 */
		shapeRenderer.begin(ShapeType.Filled);
		shapeRenderer.setColor(Color.GRAY);
		shapeRenderer.rect(-2000, -1000, 4000, 2000);

		shapeRenderer.setColor(Color.GREEN);
		shapeRenderer.circle(1024 / 2, 720 / 2, 185);
		shapeRenderer.end();
	}

	@Override
	public void resize(int width, int height) {
		viewport.update(width, height, true);
	}

	@Override
	public void dispose() {
		if (isInitialized())
			shapeRenderer.dispose();
	}

	@Override
	public void hide() {
		// not needed
	}

}
