package runner;

import static audio.AudioManager.updateMusic;

import static handlers.Keys.setKeysRepeated;
import static handlers.MouseButton.setLeftRepeated;
import static handlers.MouseButton.setRightRepeated;

public class App {
    public App(){

    }

    public void update(){


        updateMusic();
        setLeftRepeated();
        setRightRepeated();
        setKeysRepeated();
    }
}
