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

package de.eskalon.commons.utils;

import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.utils.Array;

/**
 * An InputProcessor that delegates to an ordered list of other
 * {@link InputProcessor}s. Delegation for an event stops if a processor returns
 * {@code true}, which indicates that the event was handled.
 * <p>
 * This class adds some convenience methods for quickly changing all of the
 * added input processors.
 * 
 * @author damios
 */
public class BasicInputMultiplexer extends InputMultiplexer {

	/**
	 * Removes all input processors.
	 *
	 * @see #clear()
	 */
	public void removeProcessors() {
		this.clear();
	}

	/**
	 * Removes all input processors contained in the given array.
	 *
	 * @param processors
	 *            the processor to remove
	 * @see #removeProcessor(InputProcessor)
	 */
	public void removeProcessors(Array<InputProcessor> processors) {
		for (InputProcessor p : processors) {
			removeProcessor(p);
		}
	}

	public void addProcessors(Array<InputProcessor> processors) {
		getProcessors().addAll(processors);
	}

}