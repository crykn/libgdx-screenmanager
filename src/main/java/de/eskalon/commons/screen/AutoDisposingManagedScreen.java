/*
 * Copyright 2023 damios
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

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ScreenAdapter;

import de.damios.guacamole.Preconditions;

/**
 * An implementation of {@link ManagedScreen} that automatically disposes this
 * screen after it was {@linkplain #hide() hidden}. This means that an instance
 * of this screen can only be pushed once! If this condition is violated, an
 * {@link IllegalStateException} is thrown in {@link #show()}.
 * <p>
 * Check out the documentation of {@link ManagedScreen}!
 * 
 * @author damios
 */
public abstract class AutoDisposingManagedScreen extends ManagedScreen {

	protected boolean disposed = false;

	@Override
	public void show() {
		Preconditions.checkState(!disposed,
				"This screen has already been disposed and cannot be reused!");
	}

	@Override
	public void hide() {
		disposed = true;
		dispose();
	}

}