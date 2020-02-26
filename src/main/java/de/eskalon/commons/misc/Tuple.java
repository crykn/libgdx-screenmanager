package de.eskalon.commons.misc;

import java.util.Objects;

import javax.annotation.Nullable;

public class Tuple<X, Y> {

	@Nullable
	public final X x;
	@Nullable
	public final Y y;

	public Tuple(@Nullable X x, @Nullable Y y) {
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

		if (!(other instanceof Tuple)) {
			return false;
		}

		@SuppressWarnings("unchecked")
		Tuple<X, Y> other_ = (Tuple<X, Y>) other;

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
