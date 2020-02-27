## What is libgdx-screenmanager?

[![Release](https://jitpack.io/v/crykn/libgdx-screenmanager.svg)](https://jitpack.io/#crykn/libgdx-screenmanager) [![Build Status](https://travis-ci.com/crykn/libgdx-screenmanager.svg?branch=master)](https://travis-ci.com/crykn/libgdx-screenmanager) [![Code Coverage](https://codecov.io/gh/crykn/libgdx-screenmanager/branch/master/graph/badge.svg)](https://codecov.io/gh/crykn/libgdx-screenmanager)

This library is a screen manager for libGDX games. It allows comfortably changing the rendered screen while using transition effects. The library's easy to use nature makes it possible to integrate libgdx-screenmanager into any project without much effort.

## Features

* Allows easily **changing the rendered screen** (`game.getScreenManager().pushScreen("screen-name", "transition-name");`)
* Adds **screen transition effects** for when a screen is changed (see `IScreenTransition`)
* **Automatically registers/unregisters** the screen's **input processors** whenever the screen is shown/hidden
* There are `create()` methods for screens and transitions that are called _once_ before a screen/transition is shown. This allows easily initializing them when everything else has already been loaded
* The whole library is well documented and includes [tests](https://github.com/crykn/libgdx-screenmanager/tree/master/src/test/java) for nearly everything

## Example code

The following example shows how to use libgdx-screenmanager in your code. You can find the full example [here](https://github.com/crykn/libgdx-screenmanager/tree/master/example). 

The library is very easy to use: The game has to extends `BasicGame`, all screen have to inherit from `BasicScreen`. Screens and transitions have to be registered with the screen manager before they can be used. To push a screen `game.getScreenManager().pushScreen("screen-name", "transition-name");` has to be called. If no transition should be used, just call `pushScreen("screen-name", null);`.

```java
public class MyGdxGame extends BasicGame {

	@Override
	public final void create() {
		super.create();

		// ...

		// Add screens
		this.screenManager.addScreen("red", new RedScreen(this));
		this.screenManager.addScreen("blue", new RedScreen(this));

		// Add transitions
		TestTransition transition = new TestTransition(this);
		screenManager.addScreenTransition("test_transition", transition);

		// Push the first screen without a transition
		this.screenManager.pushScreen("red", null);
	}

}
```

**Some additional notes:**

* Screens and transitions are intended as classes that are instantiated _once_ at the beginning. Their state can be reset in `IScreen#show()`/`IScreenTransition#reset()`, if needed.
* The screen's/transitions's `create()` method is either called when they are first shown or when they are manually initialized. The latter is mostly done by a loading screen after the screen's assets have been loaded.
* Input processors have to be added in a screen via `BasicScreen#addInputProcessor(...)`. This is needed so the input processors can be automatically registered/unregistered when the screen is shown/hidden.
* After a screen was pushed, the actual change of the screen happens in the first `game.render(...)` call after that. This ensures that `IScreen#show()` and `IScreen#hide()` are only called on the rendering thread.


## How the library works in detail

The inner workings are detailed [here](https://github.com/crykn/libgdx-screenmanager/wiki/How-the-library-works-in-detail).