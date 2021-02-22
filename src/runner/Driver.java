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
    private App app;

    public static Color WHITE = new Color(1, 1, 1, 1);
    public static Color BLACK = new Color(0, 0, 0, 1);

    private void init() {
        glfwSetErrorCallback(new ErrorHandler());
        running = true;
        States.init();
        initWindow(1920, 1080, false, true);
        initFonts();
        initColors();
        initAudioManager();
        app = new App();
    }

    public static void initFonts(){}

    public static void initColors(){
        WHITE = new Color(1, 1, 1, 1);
        BLACK = new Color(0, 0, 0, 1);
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
                    app.update();

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
