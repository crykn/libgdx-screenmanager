package de.eskalon.commons.screen.transition;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

import de.eskalon.commons.LibgdxUnitTest;

public class TimedScreenTransitionTest extends LibgdxUnitTest{

	@Test
	public void test() {
		TimedTransition t = new TimedTransition(5) {
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

			@Override
			public void resize(int width, int height) {
			}
		};

		t.render(1, null, null);
		assertTrue(!t.isDone());

		t.render(1, null, null);
		t.render(1, null, null);
		t.render(1, null, null);
		t.resize(12, 15);
		t.render(1, null, null);
		assertTrue(t.isDone());

		t.render(1, null, null);
		assertTrue(t.isDone());

		t.reset();
		assertTrue(!t.isDone());
	}

}
