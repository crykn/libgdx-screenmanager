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

package de.eskalon.commons.screen;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.Viewport;

import de.eskalon.commons.core.ManagedGame;
import de.eskalon.commons.screen.transition.ScreenTransition;

/**
 * A basic screen for use with a {@link ScreenManager}. To render it, the screen
 * has to be
 * {@linkplain ScreenManager#pushScreen(ManagedScreen, ScreenTransition)
 * pushed}.
 * <p>
 * Use {@link #addInputProcessor(InputProcessor)} to add input processors that
 * are automatically registered and unregistered whenever the screen is
 * {@linkplain #show() shown}/{@linkplain #hide() hidden}.
 * <p>
 * Note that only under certain conditions {@link #dispose()} is called
 * automatically. Check out the method's javadoc for more information!
 * 
 * @author damios
 * 
 * @see <a href=
 *      "https://github.com/crykn/libgdx-screenmanager/wiki/Screen-Lifecycle">The
 *      wiki entry detailing a screen's life-cycle</a>
 * @see <a href=
 *      "https://github.com/crykn/libgdx-screenmanager/wiki/Lifecycle-Example">An
 *      example lifecycle for a screen</a>
 */
public abstract class ManagedScreen implements Screen {

	/**
	 * @see #addInputProcessor(InputProcessor)
	 */
	private final Array<InputProcessor> inputProcessors = new Array<>(4);

	/**
	 * Adds an input processor that is automatically registered and unregistered
	 * whenever the screen is {@linkplain #show() shown}/{@linkplain #hide()
	 * hidden}.
	 * <p>
	 * Input processors added <i>during</i> rendering (so after {@link #show()},
	 * but before {@link #hide()} are only registered, when the screen is shown
	 * a second time.
	 *
	 * @param processor
	 *            the processor to add
	 */
	protected void addInputProcessor(InputProcessor processor) {
		inputProcessors.add(processor);
	}

	/**
	 * Called when this screen becomes the
	 * {@linkplain ScreenManager#getCurrentScreen() active screen}. Note that at
	 * first, the screen may be rendered as part of a transition.
	 * <p>
	 * If you want to reuse screen instances, this is the place where the screen
	 * should be reset.
	 * <p>
	 * Right after this method, {@link #resize(int, int)} is called.
	 * 
	 * @see #hide()
	 */
	@Override
	public void show() {
		// don't do anything by default
	}

	/**
	 * Called when this screen is no longer the
	 * {@linkplain ScreenManager#getCurrentScreen() active screen} for a
	 * {@link ManagedGame} and a possible transition has finished.
	 * 
	 * @see #show()
	 */
	@Override
	public void hide() {
		// don't do anything by default
	}

	/**
	 * Called when the screen should render itself.
	 * <p>
	 * Before this method is called, the previously rendered stuff is cleared
	 * with the {@linkplain #getClearColor() clear color}.
	 * <p>
	 * If you are using any {@link Viewport}s, be sure to
	 * {@linkplain Viewport#apply() apply} them first. When using the same
	 * {@link SpriteBatch} as the transitions, don't forget to
	 * {@linkplain SpriteBatch#setProjectionMatrix(com.badlogic.gdx.math.Matrix4)
	 * set the projection matrix} before using it. For example:
	 * 
	 * <pre>
	 * {@code
	 * viewport.apply();
	 * spriteBatch.setProjectionMatrix(viewport.getCamera().combined);
	 * 
	 * // And then render your stuff:
	 * spriteBatch.begin();
	 * // ...
	 * spriteBatch.end();
	 * }
	 * </pre>
	 * 
	 * @param delta
	 *            the time in seconds since the last render pass
	 */
	@Override
	public abstract void render(float delta);

	/**
	 * Called when the {@linkplain ApplicationListener#resize(int, int) game is
	 * resized} while this screen is rendered and the new size is different to
	 * the previous one.
	 * <p>
	 * In addition, this method is called right after {@link #show()}.
	 * 
	 * @param width
	 *            the new width in pixels
	 * @param height
	 *            the new height in pixels
	 */
	@Override
	public abstract void resize(int width, int height);

	/**
	 * Called when the {@link Application} is paused while this screen is
	 * rendered.
	 * <p>
	 * The game is usually paused when it is not active or visible on-screen.
	 * <u>On Android</u>, this is the case when the home button is pressed or an
	 * incoming call is received. <u>On desktop</u>, this method is called when
	 * the game is minimized. However, {@link #pause()} is <i>not</i> called,
	 * when the game just loses focus. This has to be detected with a
	 * Lwjgl3WindowListener. <u>On iOS</u>, this method is called when the app
	 * is about to move from the active to inactive state, e.g. when an incoming
	 * call is received. <u>On web</u>, pause events are tied to the
	 * {@code hidden} document property, which determines whether the page is
	 * not even partially visible.
	 * 
	 * @see #resume()
	 */
	@Override
	public void pause() {
		// don't do anything by default
	}

	/**
	 * Called when the {@link Application} is resumed from a paused state;
	 * usually when it regains focus.
	 * <p>
	 * <u>On (older) Android devices</u>, the OpenGL context might be lost on
	 * pause. In general, libGDX will re-create OpenGL objects that were lost,
	 * but if, for example, there are any run-time created textures, they will
	 * have to be re-created in this method.
	 * 
	 * @see #pause()
	 * @see <a href=
	 *      "http://bitiotic.com/blog/2013/05/23/libgdx-and-android-application-lifecycle/">Some
	 *      information on the android lifecycle</a>
	 */
	@Override
	public void resume() {
		// don't do anything by default
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * Is called automatically in two cases:
	 * <ul>
	 * <li>when the screen manager is disposed and this screen was pushed, but
	 * not yet {@linkplain #hide() hidden}; it does not matter whether the
	 * screen was actually rendered. In other words, {@link #dispose()} is
	 * called for the current screen, a screen which is rendered as part of a
	 * transition, as well as any screens still queued to be shown.</li>
	 * <li>If users want automatic disposing for screens on which
	 * {@link #hide()} has been called previously (and which were not pushed a
	 * second time), this can be enabled via
	 * {@link ScreenManager#setAutoDispose(boolean, boolean)}.</li>
	 * </ul>
	 */
	@Override
	public abstract void dispose();

	public Array<InputProcessor> getInputProcessors() {
		return inputProcessors;
	}

	/**
	 * @return the color to clear the screen with before the rendering is
	 *         started
	 */
	public Color getClearColor() {
		return Color.BLACK;
	}

}
