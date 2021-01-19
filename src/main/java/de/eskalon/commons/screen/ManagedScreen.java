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

import javax.annotation.Nullable;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.Viewport;

import de.eskalon.commons.core.ManagedGame;

/**
 * A basic screen for use with a {@link ScreenManager}. Screens are intended as
 * objects that are created only once when the game is started.
 * <p>
 * A screen is always {@linkplain #show() shown} before it is
 * {@linkplain #render(float) rendered}.
 * <p>
 * The {@link #create()} method is called when the screen is first shown. The
 * screen can also be initialized manually by calling
 * {@link #initializeScreen()}, which should normally be done by a loading
 * screen after the assets have been loaded.
 * <p>
 * Use {@link #addInputProcessor(InputProcessor)} to add input processors that
 * are automatically registered and unregistered whenever the screen is
 * {@linkplain #show() shown}/{@linkplain #hide() hidden}.
 * 
 * @author damios
 */
public abstract class ManagedScreen implements Screen {

	/**
	 * @see #addInputProcessor(InputProcessor)
	 */
	private final Array<InputProcessor> inputProcessors = new Array<>(4);
	/**
	 * Contains any arguments given to
	 * {@link ScreenManager#pushScreen(String, String, Object...)}. Is
	 * {@code null}, if no arguments were provided.
	 * <p>
	 * These params are usually accessed in {@link #show()}.
	 */
	@Nullable
	protected Object[] pushParams;

	private boolean initialized = false;

	public boolean isInitialized() {
		return initialized;
	}

	/**
	 * Can be called manually to {@linkplain #create() initialize} the screen -
	 * otherwise this is done when the screen is first shown.
	 */
	public void initializeScreen() {
		if (!initialized) {
			initialized = true;
			create();
			resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		}
	}

	/**
	 * Is responsible for initializing the screen. Is called <i>once</i>.
	 * <p>
	 * Right after this method {@link #resize(int, int)} is called.
	 */
	protected abstract void create();

	/**
	 * Adds an input processor that is automatically registered and unregistered
	 * whenever the screen is {@linkplain #show() shown}/{@linkplain #hide()
	 * hidden}.
	 *
	 * @param processor
	 *            the processor to add
	 */
	protected void addInputProcessor(InputProcessor processor) {
		inputProcessors.add(processor);
	}

	/**
	 * Called when this screen becomes the
	 * {@linkplain ScreenManager#getCurrentScreen() active screen}. At first,
	 * the screen may be rendered as part of a transition.
	 * <p>
	 * If the {@link ScreenManager#pushScreen(String, String, Object...)} call
	 * had any params set, those can be found in {@link #pushParams}.
	 */
	@Override
	public void show() {
		initializeScreen();
	}

	/**
	 * Called when this screen is no longer the
	 * {@linkplain ScreenManager#getCurrentScreen() active screen} for a
	 * {@link ManagedGame} and a possible transition has finished.
	 */
	@Override
	public abstract void hide();

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
	 * set the projection matrix} before using it.
	 * 
	 * @param delta
	 *            the time in seconds since the last render pass
	 */
	@Override
	public abstract void render(float delta);

	/**
	 * Called when the {@linkplain ApplicationListener#resize(int, int) game is
	 * resized}, the screen was {@linkplain #isInitialized() initialized} before
	 * and the new size is different to the previous one.
	 * <p>
	 * In addition, this method is called once right after the screen was
	 * initialized ({@link #create()}).
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
	 * <u>On desktop</u> this is the case, when the game is minimized. <u>On
	 * Android</u> this method is called when the home button is pressed or an
	 * incoming call is received.
	 * 
	 * @see #resume()
	 */
	@Override
	public void pause() {
	}

	/**
	 * Called when the {@link Application} is resumed from a paused state;
	 * usually when it regains focus.
	 * <p>
	 * <u>On Android</u> the OpenGL context is lost on pause. In general, libGDX
	 * will re-create OpenGL objects that were lost, but if, for example, there
	 * are any run-time created textures, they will have to re-created in this
	 * method.
	 * 
	 * @see #pause()
	 * @see <a href=
	 *      "http://bitiotic.com/blog/2013/05/23/libgdx-and-android-application-lifecycle/">Some
	 *      information on the android life-cycle</a>
	 */
	@Override
	public void resume() {
	}

	/**
	 * {@inheritDoc}
	 * 
	 * Is automatically called when the game is closed, without regard as to
	 * whether the screen was {@linkplain #isInitialized() initialized} before.
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
