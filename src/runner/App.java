package runner;

import graphicsLibrary.Color;

import static audioLibrary.AudioManager.updateMusic;
import static handlers.States.*;

public class App {
    public static final boolean debug = false;

    public App(){

    }

    public void update(){
        updateMusic();
        updateInput();
    }



    public static void initFonts(){

    }

    public static Color BLACK;
    public static Color WHITE;

    public static void initColors(){
        BLACK = new Color(0, 0, 0, 1);
        WHITE = new Color(1, 1, 1, 1);
    }
}
