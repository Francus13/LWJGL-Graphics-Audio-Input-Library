package runner;

import handlers.ErrorHandler;
import handlers.States;

import static audioLibrary.AudioManager.initAudioManager;
import static audioLibrary.AudioManager.terminateAudioManager;
import static graphicsLibrary.Window.initWindow;
import static graphicsLibrary.Window.renderScreen;
import static org.lwjgl.glfw.GLFW.*;
import static runner.App.initColors;
import static runner.App.initFonts;

public class Driver {
    public static final double FPSInverse = 1.0/60;
    public static final float MUSIC_FADE_TIME = 2;
    private static boolean running = false;
    private App app;

    private void init() {
        glfwSetErrorCallback(new ErrorHandler());
        running = true;
        States.init();
        initColors();
        initFonts();
        initWindow(1920, 1080, false, true, "WINDOW NAME HERE");
        initAudioManager();
        app = new App();
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

        terminateAudioManager();
        glfwTerminate();
    }

    public static void stopRunning() {running = false;}

    public static double getTime() {return (double)System.nanoTime() / (double) 1000000000;}

    public static void main(String[] args) {
        new Driver().run();
    }
}
