package com.mygame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;

import de.eskalon.commons.core.ManagedGame;
import de.eskalon.commons.screen.ManagedScreen;
import de.eskalon.commons.screen.transition.ScreenTransition;
import de.eskalon.commons.screen.transition.impl.BlendingTransition;

public class MyGdxGame extends ManagedGame<ManagedScreen, ScreenTransition> {

	public static final String TITLE = "MyGdxGame";
	private SpriteBatch batch;

	@Override
	public final void create() {
		super.create();

		// Create batch
		this.batch = new SpriteBatch();
		this.batch.setBlendFunctionSeparate(GL20.GL_SRC_ALPHA,
				GL20.GL_ONE_MINUS_SRC_ALPHA, GL20.GL_ONE,
				GL20.GL_ONE_MINUS_SRC_ALPHA); // this allows rendering
												// transparent textures during
												// transitions

		// Enable automatic disposing
		this.screenManager.setAutoDispose(true, true);

		// Push the first screen using a blending transition
		this.screenManager.pushScreen(new GreenScreen(),
				new BlendingTransition(batch, 1F, Interpolation.pow2In));

		Gdx.app.debug("Game", "Initialization finished.");
	}

	public SpriteBatch getBatch() {
		return batch;
	}

}
