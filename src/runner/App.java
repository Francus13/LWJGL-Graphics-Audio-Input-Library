package runner;

import handlers.Keys;
import scenarios.Battle;
import scenarios.Paused;
import scenarios.Scenario;

import java.util.Random;

import static audio.AudioManager.updateMusic;
import static gameObjects.cards.Card.initCardTextures;

import static handlers.Keys.setKeysRepeated;
import static handlers.MouseButton.setLeftRepeated;
import static handlers.States.*;

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
