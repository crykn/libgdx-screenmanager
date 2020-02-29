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
				game.getScreenManager().pushScreen("red", "test_transition");
				return true;
			}
		});
	}

	@Override
	public void render(float delta) {
		shapeRenderer.setProjectionMatrix(game.getCamera().combined);
		shapeRenderer.begin(ShapeType.Line);
		shapeRenderer.setColor(Color.BLUE);
		shapeRenderer.rect(100, 100, game.getWidth() - 100, game.getHeight() - 100);
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
