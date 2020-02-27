import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import de.eskalon.commons.core.BasicGame;

public class MyGdxGame extends BasicGame {

	private Viewport viewport;
	private SpriteBatch batch;
	private OrthographicCamera camera;

	@Override
	public final void create() {
		super.create();

		// Do some basic stuff
		this.batch = new SpriteBatch();
		this.camera = new OrthographicCamera();
		this.viewport = new FitViewport(1024, 720, camera);

		// Add screens
		this.screenManager.addScreen("red", new RedScreen(this));
		this.screenManager.addScreen("blue", new RedScreen(this));

		// Add transitions
		TestTransition transition = new TestTransition(this);
		screenManager.addScreenTransition("test_transition", transition);

		// Push the first screen without a transition
		this.screenManager.pushScreen("red", null);

		Gdx.app.debug("Game", "Initialization finished.");
	}
	
	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
		viewport.update(width, height);
	}

	public SpriteBatch getSpriteBatch() {
		return batch;
	}

	public OrthographicCamera getCamera() {
		return camera;
	}

}
