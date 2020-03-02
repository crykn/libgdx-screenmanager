import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

import de.eskalon.commons.screen.ManagedScreen;

public class BlueScreen extends ManagedScreen {

	private MyGdxGame game;
	private ShapeRenderer shapeRenderer;

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
	}

	@Override
	public void render(float delta) {
		/*
		 * Render a blue triangle on a white background.
		 */
		shapeRenderer.begin(ShapeType.Filled);
		shapeRenderer.setColor(Color.WHITE);
		shapeRenderer.rect(0, 0, game.getWidth(), game.getHeight());
		shapeRenderer.setColor(Color.BLUE);
		shapeRenderer.triangle(50, 50, game.getWidth() - 50, 50, game.getWidth() / 2, game.getHeight() - 50);
		shapeRenderer.end();
	}

	@Override
	public void dispose() {
		shapeRenderer.dispose();
	}

	@Override
	public void resize(int width, int height) {
		// not needed
	}

	@Override
	public void hide() {
		// not needed
	}

}
