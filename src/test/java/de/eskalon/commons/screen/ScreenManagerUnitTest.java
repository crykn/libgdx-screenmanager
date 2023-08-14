package de.eskalon.commons.screen;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyFloat;

import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.Stubber;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

import de.eskalon.commons.LibgdxUnitTest;

public class ScreenManagerUnitTest extends LibgdxUnitTest {

	@SuppressWarnings({ "rawtypes" })
	protected ScreenManager getMockedScreenManager() {
		ScreenManager sm = Mockito.spy(new ScreenManager());

		// Mock the stuff depending on GL
		Mockito.doNothing().when(sm).initBuffers();
		Mockito.doReturn(null).when(sm).screenToTexture(any(), any(),
				anyFloat());
		Mockito.when(sm.screenToTexture(any(), any(), anyFloat()))
				.thenAnswer(new Answer<TextureRegion>() {
					@Override
					public TextureRegion answer(InvocationOnMock invocation)
							throws Throwable {
						// only render the screen
						((ManagedScreen) invocation.getArgument(0))
								.render((float) invocation.getArgument(2));
						return null;
					}
				});

		return sm;
	}

}
