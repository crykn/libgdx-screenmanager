package com.mygame;

import de.eskalon.commons.screen.ManagedScreen;

public class BlankScreen extends ManagedScreen {

	@Override
	public void render(float delta) {
		// do nothing except having the screen cleared
	}

	@Override
	public void resize(int width, int height) {
		// do nothing
	}

	@Override
	public void dispose() {
		// do nothing
	}

}
