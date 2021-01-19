import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import de.eskalon.commons.screen.ManagedScreen;

public class BlueScreen extends ManagedScreen {

	private MyGdxGame game;
	private ShapeRenderer shapeRenderer;
	private Viewport viewport;

	public BlueScreen() {
		this.game = (MyGdxGame) Gdx.app.getApplicationListener();
	}

	@Override
	protected void create() {
		this.shapeRenderer = new ShapeRenderer();
		this.addInputProcessor(new InputAdapter() {
			@Override
			public boolean touchDown(int screenX, int screenY, int pointer, int button) {
				/*
				 * Switch using two transitions that are queued.
				 */
				game.getScreenManager().pushScreen("blank", "slicing_transition");
				game.getScreenManager().pushScreen("green", "sliding_out_transition");
				return true;
			}
		});

		this.viewport = new ScreenViewport(); // a ScreenViewport provides the default behaviour
	}

	@Override
	public void render(float delta) {
		viewport.apply();
		shapeRenderer.setProjectionMatrix(viewport.getCamera().combined);

		/*
		 * Render a blue triangle on a white background.
		 */
		shapeRenderer.begin(ShapeType.Filled);
		shapeRenderer.setColor(Color.BLUE);
		shapeRenderer.triangle(50, 50, game.getWidth() - 50, 50, game.getWidth() / 2, game.getHeight() - 50);
		shapeRenderer.end();
	}

	@Override
	public void dispose() {
		if (isInitialized())
			shapeRenderer.dispose();
	}

	@Override
	public void resize(int width, int height) {
		viewport.update(width, height, true);
	}

	@Override
	public void hide() {
		// not needed
	}

	@Override
	public Color getClearColor() {
		return Color.WHITE;
	}

}
