# LWJGL Graphics-and-More Library
This is a library for creating an application with graphics, audio, and user input built upon LWJGL.
To use this library, put the res and src folders in a Java project using the LWJGL library.

`Driver.update()` calls the application loop, where `App.update()` is called at least once every frame before the frame is rendered.

# Usage
Your application should run by calling `Driver.main()`.

App.java is the only file that needs to be edited.
App.java needs to have a `public static void initApp()` and a `public static void update()`.
See Input Usage for some possible requirements in `App.update()`.

In order to prevent applications feeling even slower when the computer is unable to reliably render frames at the frame rate,
time is measured in realtime and not by the number of frames that have been rendered.
Thus, `App.update` may be called multiple times per actual frame render.

## Graphics

### Renderable
Everything that is rendered on screen is composed of Renderables.
There are three kinds of Renderables: RenderableTextures, Animations, and Texts.
Renderables have an x-coordinate, a y-coordinate, a width, a height, and a boolean indicating if they are currently rendered.

To start rendering a Renderable r, call `Window.render(r)`.
Renderables are rendered on the screen in the order that they passed to `Window.render()`.
Do not call `Window.render(r)` multiple times before calling `Window.deRender(r)` first.

To stop rendering a Renderable r, call `Window.deRender(r)`.
Do not call `Window.deRender(r)` without calling `Window.render(r)` first.

Underlying RenderableTextures and Texts are one Texture object, which contains all the information about the image it renders.
Animations contain a Texture for each image that it can render.
When possible, it is wise to reuse Texture objects in order to conserve memory and processing power, and methods to do so are described later.
In order to free a Texture t, call `t.free()`.
To free a Renderable r and its underlying Texture or Textures, call `r.free()`.

### RenderableTexture
A RenderableTexture renders a still image in .png format.
Calling `new RenderableTexture(filename)` creates a new RenderableTexture that renders the image at /res/Images/filename.png.

If two RenderableTextures use the same image, it is recommended that they also use the same Texture in order to conserve memory and processing power.
A RenderableTexture can be created using preexisting Texture t by calling `new RenderableTexture(t)`.
To get the Texture of an existing RenderableTexture r, call `r.getTexture()`.

An image in .png format can be drawn on top of the current image of a Texture in a RenderableTexture r by calling
`r.addImage(filename, x, y)`where the image at filename is placed at the x- and y-coordinates of the current Texture.

### Animation
An Animation renders multiple still images in .png format in a sequence.
Calling `new Animation(filename, numFrames, doesRepeat)` creates a new Animation with frames = numFrames that renders the images in the folder at /res/Images/filename.
All the images in /res/Images/filename are of the form _X.png where the image is the Xth frame of the Animation.
For all X between 0 (inclusive) and numFrames (exclusive), filename/_X.png must exist.
The boolean doesRepeat indicates whether the Animation will loop or whether it will stop rendering after all of its frames have been rendered.

By default, the frame rate of the Animation is equal to the frame rate of the application, as specified in Driver.java.
To change this, pass the desired frame rate to a fourth parameter of the constructor.

Since `App.update()` may be called multiple times per actual frame render, some frames of an Animation may not be rendered.
This is to ensure that an Animation is rendered correctly in real time.

An Animation a can be reset to the first frame by calling `a.reset()`.
When an Animation is passed to `Window.render()` or `Window.reRender()`, it is reset.

### Text
A Text renders a completely transparent image with a piece of text drawn on it.
A Text has the following properties:

Textbox - the image that is rendered.
It is created with a piece of text and the font, color, and alignment of the Text object.

Font - the font of the text. The font also specifies the size of the text.

Alignment - should always be Text.LEFT, Text.CENTER, or Text.RIGHT, indicating how the text should be aligned.
The default alignment is Text.LEFT.

Color - the color of the text.
The default color is Driver.BLACK.


Except as described later, a Text is constructed with a String of text and a Font.
To use a specific alignment or color instead of the default ones, they should be passed into the constructor.
If a width is specified in the constructor, then the text will wrap vertically so that the text will not exceed the width.
If, additionally, a height is specified in the constructor, then the text will not exceed the height.
This ensures that if there is too much text, it will be vertically cut off.

Text can be timed, causing it to stop being rendered after a specified time.
A Text is timed if a time and a pauseTime are specified at the end its constructor.
The time parameter specifies how long the text will be rendered before it stops being rendered.
Calling `addTextToQueue()` with a Text that is timed adds text to a queue to be rendered in the future.
If a timed Text stops being rendered because its time has passed and there is text in the queue,
then the next text in the queue will be rendered after a duration of seconds equal to pauseTime.
If a time is passed into `addTextToQueue`, then that time becomes the Text's time.

`Text.createTextTexture()` takes a text, a font, and optionally a color and returns a Texture that can be used as a textbox in a Text.
This allows Textures for Text to be reused in order to conserve memory and processing power.
A Texture t created this way can be used either as `new Text(t)`, `new Text(t, alignment)`, or `setTextTexture(t)`.
When the textbox of a Text is set in either of these ways, it is recommended to not call
`updateText()`, `updateTextTimed()`, or `addTextToQueue()`, as these functions may cause an error or not work as intended.
Also, if a Text is timed, then do not set its texture with `setTextTexture()`.

The transparent image that is the base for all Text is at /res/Images/Blank.png, and this image is required for Text to work.
The text is drawn onto this image, and then the image is trimmed to the size of the text.
If a width or height are specified in the constructor of a Text, then the image created within the constructor will instead be trimmed to the width and/or height.
The size of Blank.png is 1920x1080, and an error will occur if a Text is made with greater dimensions than Blank.png.
The size of Blank.png can be changed by replacing it with a different fully-transparent image called Blank.png of the desired size.

### Font
Calling `new Font(name, size)` creates a size-point Font using the font file at /res/Fonts/name.ttf.
Creating a Font requires a lot of processing power, so Fonts can be created when the application is started when Font variables are initialized in `Driver.initFonts()`.
Remember to include licenses for used fonts somewhere in the project directory.

### Color
Calling 'new Color(r, g, b, a)' creates a new RGBA color where r, g, b, and a are between 0 and 1 inclusive.
Colors can be created when the application is started when Color variables are initialized in `Driver.initColors()`.
Do not remove Driver.BLACK or Driver.WHITE or their initializations.

### NumberTextureGetter
Since applications may want to render the same number multiple times in the same font and color,
NumberTextureGetter abstracts the process of reusing the number textures in order to same memory and processing power.
`NumberTextureGetter.getNumberTexture(num, font)` returns a Texture used exactly as
`Text.createTextTexture()` where the text is the specified number in the specified font.
A Color can also be passed into `NumberTextureGetter.getNumberTexture()` in order to make the text of the specified color.

### Changing Window Properties
To change the frame rate, change the dividend of FPSInverse in Driver.java to the desired frames per second.

To change the resolution, change the first two parameters of initWindow() in Driver.init() to the desired width and height respectively.

To make the application windowed instead of fullscreen, change the third parameter of initWindow() in Driver.init() to false.

### Shader
Default .fs and .vs shader files are included in /res/Shaders, so there is no need to create additional shaders.
Calling `new Shader(filename)` creates a new Shader using /res/Shaders/filename.fs and /res/Shaders/filename.vs.
However, this library does not support an abstraction for implementing a different Shader.
In addition, this library does not support switching shaders while the application is running.
In order to implement a different Shader, Window.java needs to be edited, and new `setUniform()` functions may need to be implemented in Shader.java.

## Audio
All audio files must be in .ogg format, and all audio must be in either 16-bit mono or 16-bit stereo format.

### Sound
Calling `new Sound(filename)` creates a new Sound that plays the audio at /res/Audio/Sounds/filename.ogg.
To play a Sound s, call `AudioManager.play(s)`.
Calling `AudioManager.play(s, numTimes)` plays s numTimes times subsequently.
To play an array of Sounds sArr subsequently, call `AudioManager.play(sArr)`.
In order to play multiple Sounds at the same time instead of subsequently, call `AudioManager.play()` multiple times.
To free a Sound s, call `AudioManager.free(s)`.

### Music
Unlike with Sound objects, only one Music can be played at a time.
To play a Music m, call `AudioManager.play(m)`.
When `AudioManager.play()` is called, the Music that is currently playing will fade out for a duration of seconds equal to Driver.MUSIC_FADE_TIME.
If there is no Music currently playing, then no Music will be played for this duration.
Then, the Music that was passed into `AudioManager.play()` will fade in for a duration of seconds equal Driver.MUSIC_FADE_TIME.
To stop playing any music, call `AudioManager.play(null)`.
The default value for Driver.MUSIC_FADE_TIME is 2, and it can be changed in Driver.java.
To free a Music m, call `AudioManager.free(m)`.
If no Music is being used in the application, `updateMusic()` can be removed from the end of `App.update()`.

### Volume
There are three volumes: master, sound, and music.
To change the master volume to v, call `setMasterVolume(v)`.
To change the sound volume to v, call `setSoundVolume(v)`.
To change the music volume to v, call `setMusicVolume(v)`.
The volume parameter should be between 0 and 100, where 0 represents no volume and 100 represents full volume.
Changing the master volume changes the volume of all Sounds and Musics that are playing and will be played.
Changing the sound and music volume will do the same, but only for Sounds and Musics respectively.

## User Input

### Cursor
To get the x-coordinate of the cursor, call `Cursor.x()`.
To get the y-coordinate of the cursor, call `Cursor.y()`.

### MouseButton
The left mouse button and right mouse button will each be in one of three states: States.RELEASED, States.PRESSED, and States.REPEATED.
The states of the left and right mouse buttons can be queried by calling `MouseButton.leftClick()` and `MouseButton.rightClick` respectively.
At the end of `App.update()` is `setLeftRepeated()`, which will set the left mouse button's state to States.REPEATED if it is currently States.PRESSED.
`setLeftRepeated()` can be removed if the state of the left mouse button is never queried in the application.
`setRightRepeated()` acts similarly for the right mouse button's state.

### Scroll
Whenever the scroll is moved vertically during the application, it will add or subtract from the scroll offset.
Calling `Scroll.offset()` will return the scroll offset and reset it to zero.

### Keys
States.java contains a list of keys that are tracked, as well as a function `init()` that maps GLFW keys to the tracked keys.
All tracked keys will each be in one of three states: States.RELEASED, States.PRESSED, and States.REPEATED.
The state of a key k is queried by calling `Keys.key(k)`.
At the end of `App.update()` is `setKeysRepeated()`, which will set the state of all keys that are currently in States.PRESSED to States.REPEATED.
`setKeysRepeated()` can be removed if the state of any key is never queried in the application.
In order to add or remove tracked keys, add or remove that key from the list in States.java, add or remove the mapping from the GLFW key to that key in `States.init()`,
and update States.KEY_LAST to 1 + the greatest value of a tracked key.

# Limitations
All images must be in .png format

All audio must be in .ogg format

All fonts must be of .tff format

Currently, the resolution and window size cannot be changed at runtime.

If your graphics card is old, the shaders may not be supported.
To fix this, change the versions of the shaders by editing the first line of all .fs and .vs files appropriately.
The OpenGL versions can be found here: https://en.wikipedia.org/wiki/OpenGL_Shading_Language.
