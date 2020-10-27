package runner;

import audio.Music;
import gameObjects.beings.players.Player;
import graphics.*;

import static audio.AudioManager.play;
import static graphics.Renderable.CENTERED;
import static graphics.Text.CENTER;
import static graphics.Window.*;
import static handlers.Keys.key;
import static handlers.Keys.setKeysRepeated;
import static handlers.MouseButton.setLeftRepeated;
import static handlers.States.*;
import static handlers.MouseButton.leftClick;

import handlers.Cursor;
import scenarios.*;

import java.util.*;

public class Game {
    public static Random ran;
    private static Scenario scenario;

    private static boolean pausable;

    private static Player player;

    public static void initGame(long seed){
        if (seed == -1)
            ran = new Random();
        else
            ran = new Random(seed);

        initRenderables();

        scenario = new Menu();

        Paused.initRenderables();

        pausable = false;
    }

    public static void update() {
        if (pausable && key(KEY_ESCAPE) == PRESSED){
            scenario = new Paused(scenario);
            pausable = false;
        }

        scenario.update();

        if (leftClick() == PRESSED)
            setLeftRepeated();
        setKeysRepeated();
    }

    private static void initRenderables(){


        initText();
    }

    private static void initText(){

    }

    public static void setPausable(boolean isPausable) {pausable = isPausable;}

    public static void renderUI(){

    }
    public static void deRenderUI(){

    }

    public static void renderItemEffect(){

    }

    public static void deRenderItemEffect(){

    }

    private static void updateEquipmentBar(){

    }

    public static void setScenario(Scenario s) {scenario = s;}

    public static Player player() {return player;}


    public static boolean inEquipment() {return true;}
    public static void equipItem(){

    }
}
