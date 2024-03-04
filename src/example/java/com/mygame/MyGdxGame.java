package com.mygame;

import com.badlogic.gdx.Gdx;
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

		// Do some basic stuff
		this.batch = new SpriteBatch();

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
