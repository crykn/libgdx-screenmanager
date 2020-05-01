import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;

import de.eskalon.commons.core.ManagedGame;
import de.eskalon.commons.screen.ManagedScreen;
import de.eskalon.commons.screen.transition.ScreenTransition;
import de.eskalon.commons.screen.transition.impl.BlendingTransition;
import de.eskalon.commons.screen.transition.impl.HorizontalSlicingTransition;
import de.eskalon.commons.screen.transition.impl.SlidingDirection;
import de.eskalon.commons.screen.transition.impl.SlidingOutTransition;

public class MyGdxGame extends ManagedGame<ManagedScreen, ScreenTransition> {

	public static final String TITLE = "MyGdxGame";
	private SpriteBatch batch;

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
		BlendingTransition blendingTransition = new BlendingTransition(batch, 1F, Interpolation.pow2In);
		screenManager.addScreenTransition("blending_transition", blendingTransition);
		SlidingOutTransition slidingOutTransition = new SlidingOutTransition(batch, SlidingDirection.DOWN, 0.35F);
		screenManager.addScreenTransition("sliding_out_transition", slidingOutTransition);
		HorizontalSlicingTransition slicingTransition = new HorizontalSlicingTransition(batch, 5, 1F);
		screenManager.addScreenTransition("slicing_transition", slicingTransition);

		// Push the first screen using a blending transition
		this.screenManager.pushScreen("green", "blending_transition");

		Gdx.app.debug("Game", "Initialization finished.");
	}

	@Override
	public void resize(int width, int height) {
		super.resize(width, height);

		this.batch.getProjectionMatrix().setToOrtho2D(0, 0, width, height);
	}

}
