## What is libgdx-screenmanager?

[![Release](https://jitpack.io/v/crykn/libgdx-screenmanager.svg)](https://jitpack.io/#crykn/libgdx-screenmanager) [![Build Status](https://travis-ci.com/crykn/libgdx-screenmanager.svg?branch=master)](https://travis-ci.com/crykn/libgdx-screenmanager) [![GWT Compatible](https://img.shields.io/badge/GWT-compatible-informational)](https://github.com/crykn/libgdx-screenmanager/wiki/How-to-get-it-working-with-GWT)

This library is a screen manager for libGDX games. It allows comfortably changing the rendered screen while using transition effects. The library's easy to use nature makes it possible to integrate libgdx-screenmanager into any project without much effort.

## Features

![](https://raw.githubusercontent.com/crykn/libgdx-screenmanager/master/showcase/gl_transitions_2.gif)
> ###### A small example using different transitions. Look at the [showcases folder](https://github.com/crykn/libgdx-screenmanager/tree/master/showcase) for more gifs.

* Allows easily **changing the rendered screen**: `game.getScreenManager().pushScreen("screen_name", "transition_name")`
* Adds **screen transition effects** for when a screen is changed. The included transitions can be found [here](https://github.com/crykn/libgdx-screenmanager/wiki/Available-transitions). Furthermore, transition shaders are supported as well. See the [GL Transitions](https://gl-transitions.com/gallery) project for a collection of some very well made ones.
* **Automatically registers/unregisters** a screen's **input processors** whenever the screen is shown/hidden
* There are `create()` methods for screens and transitions that are called _once_ before a screen/transition is shown. This allows easily initializing them, when everything else has already been loaded
* The whole library is [well documented](https://github.com/crykn/libgdx-screenmanager/wiki) and includes [tests](https://github.com/crykn/libgdx-screenmanager/tree/master/src/test/java) for  everything that isn't graphical

## Example code

The following example shows how to use libgdx-screenmanager in your code. You can find the full example [here](https://github.com/crykn/libgdx-screenmanager/tree/master/example). 

The library is very easy to use: The game has to extend `ManagedGame`, all screen have to inherit from `ManagedScreen`. Screens and transitions have to be registered with the screen manager before they can be used. To push a screen `game.getScreenManager().pushScreen("screen-name", "transition-name")` has to be called. If no transition should be used, just call `pushScreen("screen-name", null)`.

```java
public class MyGdxGame extends ManagedGame<ManagedScreen, ScreenTransition> {

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


## Documentation

In the [wiki](https://github.com/crykn/libgdx-screenmanager/wiki) you can find articles detailing the usage of the library and its inner workings.
