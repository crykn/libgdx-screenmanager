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

import java.util.Objects;

import javax.annotation.Nullable;

/**
 * A tuple.
 * 
 * @author damios
 */
public class Pair<X, Y> {

	@Nullable
	public final X x;
	@Nullable
	public final Y y;

	public Pair(@Nullable X x, @Nullable Y y) {
		this.x = x;
		this.y = y;
	}

	@Override
	public String toString() {
		return "(" + x + "," + y + ")";
	}

	@Override
	public boolean equals(Object other) {
		if (other == this) {
			return true;
		}

		if (!(other instanceof Pair)) {
			return false;
		}

		@SuppressWarnings("unchecked")
		Pair<X, Y> other_ = (Pair<X, Y>) other;

		return Objects.equals(other_.x, this.x)
				&& Objects.equals(other_.y, this.y);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((x == null) ? 0 : x.hashCode());
		result = prime * result + ((y == null) ? 0 : y.hashCode());
		return result;
	}

}
