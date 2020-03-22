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

package de.eskalon.commons.screen.transition.impl;

/**
 * An enum denoting the slide direction for the respective transitions.
 * 
 * @author damios
 * 
 * @see SlidingInTransition
 * @see SlidingOutTransition
 * @see PushTransition
 */
public enum SlidingDirection {
	UP(0, 1), DOWN(0, -1), LEFT(-1, 0), RIGHT(1, 0);

	public final int xPosFactor;
	public final int yPosFactor;

	SlidingDirection(int xPosFactor, int yPosFactor) {
		this.xPosFactor = xPosFactor;
		this.yPosFactor = yPosFactor;
	}

}
