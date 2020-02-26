package de.eskalon.commons;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.mockito.Mockito;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.graphics.GL20;

/**
 * This class is the base class for all game tests. It takes care of starting
 * the game headlessly.
 */
public abstract class LibgdxUnitTest {

	private static Application application;

	@BeforeAll
	public static void init() {
		application = new HeadlessApplication(new ApplicationAdapter() {
		});

		// Use Mockito to mock the OpenGL methods since we are running
		// headlessly
		Gdx.gl20 = Mockito.mock(GL20.class);
		Gdx.gl = Gdx.gl20;
	}

	@AfterAll
	public static void cleanUp() {
		application.exit();
		application = null;
	}

}
