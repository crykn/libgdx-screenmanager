import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;

import de.eskalon.commons.screen.transition.TimedScreenTransition;

public class TestTransition extends TimedScreenTransition {

	private MyGdxGame game;

	public TestTransition(MyGdxGame game) {
		super(1F);

		this.game = game;
	}

	@Override
	protected void create() {
		// not needed
	}

	@Override
	public void render(float delta, Texture lastScreen, Texture currScreen, float progress) {
		game.getSpriteBatch().begin();

		// Blends the two screens
		Color c = game.getSpriteBatch().getColor();
		game.getSpriteBatch().setColor(c.r, c.g, c.b, 1);
		game.getSpriteBatch().draw(lastScreen, 0, 0);

		game.getSpriteBatch().setColor(c.r, c.g, c.b, progress);
		game.getSpriteBatch().draw(currScreen, 0, 0);

		game.getSpriteBatch().end();
	}

}
