package runner;

import gameObjects.beings.enemies.Enemy;
import gameObjects.beings.enemies.TestEnemy;
import gameObjects.beings.players.Player;
import gameObjects.beings.players.TestPlayer;

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

public class Game {
    public static Random ran;

    public static Player player;
    private static boolean pausable;
    private static Scenario scenario;


    public Game(long seed){
        if (seed == -1)
            ran = new Random();
        else
            ran = new Random(seed);

        pausable = false;
        initCardTextures();
        player = new TestPlayer();
        scenario = new Battle(new Enemy[] {new TestEnemy(), new TestEnemy()});
    }

    public static void setPausable(boolean isPausable) {pausable = isPausable;}

    public static void setScenario(Scenario s) {scenario = s;}

    public void update(){
        if (pausable && Keys.key(KEY_ESCAPE) == PRESSED){
            scenario = new Paused(scenario);
            pausable = false;
        }

        scenario.update();

        updateMusic();
        setLeftRepeated();
        setKeysRepeated();
    }
}
