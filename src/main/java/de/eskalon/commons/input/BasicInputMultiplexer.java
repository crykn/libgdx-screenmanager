package de.eskalon.commons.input;

import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.utils.Array;

/**
 * An InputProcessor that delegates to an ordered list of other
 * {@link InputProcessor}. Delegation for an event stops if a processor returns
 * {@code true}, which indicates that the event was handled.
 * <p>
 * This class adds some convenience methods for quickly changing all of the
 * registered input processors.
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