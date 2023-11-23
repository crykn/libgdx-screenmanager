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

/**
 * Convenience implementation of {@link ManagedScreen} inspired by
 * {@link ScreenAdapter}. Inherit from this class and only override the methods
 * that you need.
 * <p>
 * Check out the documentation of {@link ManagedScreen}!
 * 
 * @author Frosty-J
 */
public class ManagedScreenAdapter extends ManagedScreen {

	@Override
	public void render(float delta) {
		// don't do anything by default
	}

	@Override
	public void resize(int width, int height) {
		// don't do anything by default
	}

	@Override
	public void dispose() {
		// don't do anything by default
	}

}