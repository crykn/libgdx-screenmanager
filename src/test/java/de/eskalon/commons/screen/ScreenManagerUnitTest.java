package de.eskalon.commons.screen;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyFloat;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

import de.eskalon.commons.LibgdxUnitTest;
import de.eskalon.commons.utils.ScreenFboUtils;

public class ScreenManagerUnitTest extends LibgdxUnitTest {

	private static MockedStatic<ScreenFboUtils> utils;

	@BeforeAll
	public static void init2() {
		utils = Mockito.mockStatic(ScreenFboUtils.class);
		utils.when(
				() -> ScreenFboUtils.screenToTexture(any(), any(), anyFloat()))
				.thenAnswer(new Answer<TextureRegion>() {
					@Override
					public TextureRegion answer(InvocationOnMock invocation)
							throws Throwable {
						// Only render the screen
						((ManagedScreen) invocation.getArgument(0))
								.render((float) invocation.getArgument(2));
						return null;
					}
				});

	}

	@AfterAll
	public static void cleanUp2() {
		utils.close();
	}

	@SuppressWarnings({ "rawtypes" })
	protected ScreenManager getMockedScreenManager() {
		ScreenManager sm = Mockito.spy(new ScreenManager());

		// Mock the stuff depending on GL:
		Mockito.doNothing().when(sm).initBuffers();

		return sm;
	}

}
