
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

/**
 * Starts the application for the desktop-based builds.
 */
public class DesktopLauncher {

	public static void main(String[] args) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = MyGdxGame.TITLE;
		config.height = 720;
		config.width = 1024;
		config.resizable = false;

		try {
			// Start the game
			new LwjglApplication(new MyGdxGame(), config);
		} catch (Exception e) {
			System.err.println("An unexpected error occurred while starting the game: " + e.getLocalizedMessage());
			System.exit(-1);
		}
	}

}
