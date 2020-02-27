import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

import de.eskalon.commons.screen.BasicScreen;

public class BlueScreen extends BasicScreen {

	private MyGdxGame game;
	private ShapeRenderer shapeRenderer;

	public BlueScreen(MyGdxGame game) {
		this.game = game;
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
		shapeRenderer.rect(100, 100, game.getViewportWidth() - 100, game.getViewportHeight() - 100);
		shapeRenderer.end();
	}

	@Override
	public void hide() {
		// not needed
	}

	@Override
	public void dispose() {
		shapeRenderer.dispose();
	}

}
