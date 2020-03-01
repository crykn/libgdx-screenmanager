import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.Viewport;

import de.eskalon.commons.core.ManagedGame;
import de.eskalon.commons.screen.ManagedScreen;
import de.eskalon.commons.screen.transition.BlendingScreenTransition;
import de.eskalon.commons.screen.transition.ScreenTransition;

public class MyGdxGame extends ManagedGame<ManagedScreen, ScreenTransition> {

	public static final String TITLE = "MyGdxGame";
	private Viewport viewport;
	private SpriteBatch batch;
	private OrthographicCamera camera;

	@Override
	public final void create() {
		super.create();

		// Do some basic stuff
		this.batch = new SpriteBatch();

		// Add screens
		this.screenManager.addScreen("green", new GreenScreen());
		this.screenManager.addScreen("blue", new BlueScreen());

		// Add transitions
		BlendingScreenTransition transition = new BlendingScreenTransition(batch);
		screenManager.addScreenTransition("blending_transition", transition);

		// Push the first screen with a blending transition
		this.screenManager.pushScreen("green", "blending_transition");

		Gdx.app.debug("Game", "Initialization finished.");
	}

}
