package de.eskalon.commons.input;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.utils.Array;

import de.eskalon.commons.utils.BasicInputMultiplexer;

public class BasicInputMultiplexerTest {

	@Test
	public void test() {
		BasicInputMultiplexer i = new BasicInputMultiplexer();
		assertEquals(0, i.getProcessors().size);

		Array<InputProcessor> inputProcessors = new Array<>(4);
		inputProcessors.add(new InputAdapter());
		inputProcessors.add(new InputAdapter());

		i.addProcessors(inputProcessors);
		assertEquals(2, i.getProcessors().size);

		i.removeProcessors(inputProcessors);
		assertEquals(0, i.getProcessors().size);

		i.addProcessor(new InputAdapter());
		i.addProcessor(new InputAdapter());
		i.addProcessor(new InputAdapter());
		assertEquals(3, i.getProcessors().size);

		i.removeProcessors();
		assertEquals(0, i.getProcessors().size);
	}

}