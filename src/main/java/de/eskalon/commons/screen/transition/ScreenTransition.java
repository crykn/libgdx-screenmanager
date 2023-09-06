/*
 * Copyright 2020 damios
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at:
 * http://www.apache.org/licenses/LICENSE-2.0
 *  
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.eskalon.commons.screen.transition;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Disposable;

import de.eskalon.commons.screen.ManagedScreen;
import de.eskalon.commons.screen.ScreenManager;

/**
 * A transition effect between two screen for use with a {@link ScreenManager}.
 * <p>
 * Note that only under certain conditions {@link #dispose()} is called
 * automatically. Check out the method's javadoc for more information!
 * 
 * @author damios
 * 
 * @see ScreenManager#pushScreen(ManagedScreen, ScreenTransition)
 */
public abstract class ScreenTransition implements Disposable {

	/**
	 * Called before this transition starts rendering. If you want to reuse
	 * transition instances, this is the place where the transition should be
	 * reset.
	 * <p>
	 * Right after this method, {@link #resize(int, int)} is called.
	 * <p>
	 * Behaves similar to {@link ManagedScreen#show()}.
	 */
	public void show() {
		// don't do anything by default
	}

	/**
	 * Called after this transition stops rendering. This is the last chance to
	 * obtain {@linkplain ScreenManager#getLastScreen() the last screen} which
	 * was rendered as part of the transition.
	 * <p>
	 * Behaves similar to {@link ManagedScreen#hide()}.
	 */
	public void hide() {
		// don't do anything by default
	}

	/**
	 * Takes care of actually rendering the transition.
	 * 
	 * @param delta
	 *            the time delta in seconds
	 * @param lastScreen
	 *            the old screen as a texture region
	 * @param currScreen
	 *            the screen the manager is transitioning to as a texture region
	 */
	public abstract void render(float delta, TextureRegion lastScreen,
			TextureRegion currScreen);

	/**
	 * @return whether the transition is done; after that is the case, the
	 *         transition stops and {@link #hide()} is called
	 */
	public abstract boolean isDone();

	/**
	 * Called when the {@linkplain ApplicationListener#resize(int, int) game is
	 * resized} while this transition is rendered and the new size is different
	 * to the previous one.
	 * <p>
	 * In addition, this method is called right after {@link #show()}.
	 * <p>
	 * Behaves similar to {@link ManagedScreen#resize(int, int)}.
	 * 
	 * @param width
	 *            the new width in pixels
	 * @param height
	 *            the new height in pixels
	 */
	public abstract void resize(int width, int height);

	/**
	 * {@inheritDoc}
	 * <p>
	 * Is called automatically in two cases:
	 * <ul>
	 * <li>when the screen manager is disposed and this transition was pushed,
	 * but not yet {@linkplain #hide() hidden}; it does not matter whether the
	 * transition was actually rendered. In other words, {@link #dispose()} is
	 * called for the ongoing transition as well as any transitions still queued
	 * to be shown.</li>
	 * <li>If users want automatic disposing for transitions on which
	 * {@link #hide()} has been called previously (and which were not pushed a
	 * second time), this can be enabled via
	 * {@link ScreenManager#setAutoDispose(boolean, boolean)}.</li>
	 * </ul>
	 */
	@Override
	public abstract void dispose();

}
