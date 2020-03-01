package de.eskalon.commons.screen.transition;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class TimedScreenTransitionTest {

	@Test
	public void test() {
		TimedScreenTransition t = new TimedScreenTransition(5) {
			@Override
			public void render(float delta, TextureRegion currScreen,
					TextureRegion nextScreen, float progress) {
			}

			@Override
			protected void create() {
			}

			@Override
			public void dispose() {
			}
		};

		t.render(1, null, null);
		assertTrue(!t.isDone());

		t.render(1, null, null);
		t.render(1, null, null);
		t.render(1, null, null);
		t.render(1, null, null);
		assertTrue(t.isDone());

		t.render(1, null, null);
		assertTrue(t.isDone());

		t.reset();
		assertTrue(!t.isDone());
	}

}
