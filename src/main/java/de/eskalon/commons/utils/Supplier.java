package de.eskalon.commons.utils;

/**
 * 
 * A copy of Java's Supplier for pre-Java 8 support.
 */
@FunctionalInterface
public interface Supplier<T> {

	T get();
}
