import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.utils.viewport.Viewport;

import de.eskalon.commons.core.ManagedGame;
import de.eskalon.commons.screen.BlankScreen;
import de.eskalon.commons.screen.ManagedScreen;
import de.eskalon.commons.screen.transition.ScreenTransition;
import de.eskalon.commons.screen.transition.impl.BlendingScreenTransition;
import de.eskalon.commons.screen.transition.impl.SlicingScreenTransition;
import de.eskalon.commons.screen.transition.impl.SlidingDirection;
import de.eskalon.commons.screen.transition.impl.SlidingOutScreenTransition;

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
		this.screenManager.addScreen("blank", new BlankScreen());

		// Add transitions
		BlendingScreenTransition blendingTransition = new BlendingScreenTransition(batch, 1F, Interpolation.pow2In);
		screenManager.addScreenTransition("blending_transition", blendingTransition);
		SlidingOutScreenTransition slidingOutTransition = new SlidingOutScreenTransition(batch, SlidingDirection.DOWN,
				0.35F);
		screenManager.addScreenTransition("sliding_out_transition", slidingOutTransition);
		SlicingScreenTransition slicingTransition = new SlicingScreenTransition(batch, 5, 1F);
		screenManager.addScreenTransition("slicing_transition", slicingTransition);

		// Push the first screen using a blending transition
		this.screenManager.pushScreen("green", "blending_transition");

		Gdx.app.debug("Game", "Initialization finished.");
	}

}
