package com.mygame;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

/**
 * Starts the application for the desktop-based builds.
 */
public class DesktopLauncher {

	public static void main(String[] args) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setTitle(MyGdxGame.TITLE);
		config.setWindowedMode(1024, 720);

		try {
			// Start the game
			new Lwjgl3Application(new MyGdxGame(), config);
		} catch (Exception e) {
			System.err.println(
					"An unexpected error occurred while starting the game:");
			e.printStackTrace();
			System.exit(-1);
		}
	}

}
