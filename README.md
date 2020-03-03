## What is libgdx-screenmanager?

[![Release](https://jitpack.io/v/crykn/libgdx-screenmanager.svg)](https://jitpack.io/#crykn/libgdx-screenmanager) [![Build Status](https://travis-ci.com/crykn/libgdx-screenmanager.svg?branch=master)](https://travis-ci.com/crykn/libgdx-screenmanager)

This library is a screen manager for libGDX games. It allows comfortably changing the rendered screen while using transition effects. The library's easy to use nature makes it possible to integrate libgdx-screenmanager into any project without much effort.

## Features

* Allows easily **changing the rendered screen** (`game.getScreenManager().pushScreen("screen-name", "transition-name");`)
* Adds **screen transition effects** for when a screen is changed
   * The included transitions can be found [here](https://github.com/crykn/libgdx-screenmanager/tree/master/src/main/java/de/eskalon/commons/screen/transition/impl)
   * Transition shaders are supported as well (see the [gl-transitions](https://gl-transitions.com/gallery) project for a collection of some very well made ones)
* **Automatically registers/unregisters** the screen's **input processors** whenever the screen is shown/hidden
* There are `create()` methods for screens and transitions that are called _once_ before a screen/transition is shown. This allows easily initializing them when everything else has already been loaded
* The whole library is well documented and includes [tests](https://github.com/crykn/libgdx-screenmanager/tree/master/src/test/java) for  everything that isn't graphical


![](https://raw.githubusercontent.com/crykn/libgdx-screenmanager/master/showcase.gif)
> ###### A small example using different transitions

<br/>


## Example code

The following example shows how to use libgdx-screenmanager in your code. You can find the full example [here](https://github.com/crykn/libgdx-screenmanager/tree/master/example). 

The library is very easy to use: The game has to extends `ManagedGame`, all screen have to inherit from `ManagedScreen`. Screens and transitions have to be registered with the screen manager before they can be used. To push a screen `game.getScreenManager().pushScreen("screen-name", "transition-name");` has to be called. If no transition should be used, just call `pushScreen("screen-name", null);`.

```java
public class MyGdxGame extends ManagedGame {

	@Override
	public final void create() {
		super.create();

		// Do some basic stuff
		this.batch = new SpriteBatch();

		// Add screens
		this.screenManager.addScreen("green", new GreenScreen());
		this.screenManager.addScreen("blue", new BlueScreen());
		// ...

		// Add transitions
		BlendingScreenTransition blendingTransition = new BlendingScreenTransition(batch, 1F);
		screenManager.addScreenTransition("blending_transition", blendingTransition);
		// ... 

		// Push the first screen using a blending transition
		this.screenManager.pushScreen("green", "blending_transition");

		Gdx.app.debug("Game", "Initialization finished.");
	}

}
```

**Some additional notes:**

* Input processors have to be added in a screen via `ManagedScreen#addInputProcessor(...)`. This is needed so the input processors can be automatically registered/unregistered when the screen is shown/hidden.


## How the library works in detail

The following wiki entries detail some features of the library:

- The [life-cycle of a screen](https://github.com/crykn/libgdx-screenmanager/wiki/A-screen's-lifecycle) that is pushed
- The [custom FrameBuffer implementation](https://github.com/crykn/libgdx-screenmanager/wiki/Custom-FrameBuffer-implementation) that allows nested fbos
- Where to [initialize the screens & transitions](https://github.com/crykn/libgdx-screenmanager/wiki/Where-to-initialize-screens-and-transitions)