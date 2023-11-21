package com.mygame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import de.eskalon.commons.screen.ManagedScreen;
import de.eskalon.commons.screen.transition.impl.HorizontalSlicingTransition;
import de.eskalon.commons.screen.transition.impl.SlidingDirection;
import de.eskalon.commons.screen.transition.impl.SlidingOutTransition;

public class BlueScreen extends ManagedScreen {

	private MyGdxGame game;
	private ShapeRenderer shapeRenderer;
	private Viewport viewport;

	public BlueScreen() {
		this.game = (MyGdxGame) Gdx.app.getApplicationListener();

		this.shapeRenderer = new ShapeRenderer();
		this.addInputProcessor(new InputAdapter() {
			@Override
			public boolean touchDown(int screenX, int screenY, int pointer,
					int button) {
				/*
				 * Switch using two transitions that are queued.
				 */
				game.getScreenManager().pushScreen(new BlankScreen(),
						new HorizontalSlicingTransition(game.getBatch(), 5,
								1F));
				game.getScreenManager().pushScreen(new GreenScreen(),
						new SlidingOutTransition(game.getBatch(),
								SlidingDirection.DOWN, 0.35F));
				return true;
			}
		});

		this.viewport = new ScreenViewport(); // a ScreenViewport provides the
												// default behaviour
	}

	@Override
	public void render(float delta) {
		viewport.apply();
		shapeRenderer.setProjectionMatrix(viewport.getCamera().combined);

		/*
		 * Render a blue triangle on a white background.
		 */
		shapeRenderer.begin(ShapeType.Filled);
		shapeRenderer.setColor(Color.BLUE);
		shapeRenderer.triangle(50, 50, Gdx.graphics.getWidth() - 50, 50,
				Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() - 50);
		shapeRenderer.end();
	}

	@Override
	public void resize(int width, int height) {
		viewport.update(width, height, true);
	}

	@Override
	public void dispose() {
		shapeRenderer.dispose();
	}

	@Override
	public Color getClearColor() {
		return Color.WHITE;
	}

}
