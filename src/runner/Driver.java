package runner;

import static audio.AudioManager.initAudioManager;
import static audio.AudioManager.terminateAudioManager;
import static graphics.Window.initWindow;
import static graphics.Window.renderScreen;
import static org.lwjgl.glfw.GLFW.*;

import graphics.Color;
import graphics.Font;
import handlers.ErrorHandler;
import handlers.States;

public class Driver {
    public static final double FPSInverse = 1.0/60;
    public static final float MUSIC_FADE_TIME = 2;
    private static boolean running = false;
    private Game game;

    public static Font ARIAL_25;
    public static Font ARIAL_30;
    public static Font ARIAL_40;
    public static Font ARIAL_50;

    public static Color WHITE = new Color(1, 1, 1, 1);
    public static Color BLACK = new Color(0, 0, 0, 1);
    public static Color ERROR_COLOR = new Color(1, 0, 0, 1);

    private void init() {
        glfwSetErrorCallback(new ErrorHandler());
        running = true;
        States.init();
        initWindow(1920, 1080, false, true);
        initFonts();
        initColors();
        initAudioManager();
        game = new Game(-1);
    }

    public static void initFonts(){
        ARIAL_25 = new Font("Arial", 25);
        ARIAL_30 = new Font("Arial", 30);
        ARIAL_40 = new Font("Arial", 40);
        ARIAL_50 = new Font("Arial", 50);
    }

    public static void initColors(){
        WHITE = new Color(1, 1, 1, 1);
        BLACK = new Color(0, 0, 0, 1);
        ERROR_COLOR = new Color(1, 0, 0, 1);
    }

    private void run(){
        init();
        double unprocessed = 0;
        double time = getTime();

        while (running){
            double time2 = getTime();
            unprocessed += time2 - time;
            time = time2;

            if (unprocessed >= FPSInverse) {

                while (unprocessed >= FPSInverse) {
                    unprocessed -= FPSInverse;
                    game.update();

                    glfwPollEvents();
                }

                if (renderScreen())
                    stopRunning();
            }
        }

        glfwTerminate();
    }

    public static void stopRunning() {running = false; terminateAudioManager();}

    public static double getTime() {return (double)System.nanoTime() / (double) 1000000000;}

    public static void main(String[] args) {
        new Driver().run();
    }
}
