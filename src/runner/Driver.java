package runner;

import static graphics.Font.initFonts;
import static graphics.Window.initWindow;
import static graphics.Window.renderScreen;
import static runner.Timer.getTime;
import static org.lwjgl.glfw.GLFW.*;
import static runner.Game.initGame;

import graphics.Window;
import static audio.AudioManager.*;
import handlers.ErrorHandler;
import handlers.States;

public class Driver {
    public static final double FPSInverse = 1.0/60;
    private static boolean running = false;

    private void init() {
        glfwSetErrorCallback(new ErrorHandler());
        running = true;
        States.init();
        initWindow(1920, 1080);
        initFonts();
        initAudioManager();
        initGame(-1);
    }

    private void run(){
        init();
        double unprocessed = 0;
        double frameTime = 0;
        int frames = 0;
        double time = getTime();

        while (running){
            double time2 = getTime();
            unprocessed += time2 - time;
            frameTime += time2 - time;
            time = time2;

            updateMusic();

            if (unprocessed >= FPSInverse) {

                while (unprocessed >= FPSInverse) {
                    unprocessed -= FPSInverse;
                    Game.update();

                    glfwPollEvents();
                    if (frameTime >= 1.0) {
                        frameTime = 0;
                        System.out.println("FPS: " + frames);
                        frames = 0;
                    }
                }

                if (renderScreen())
                    stopRunning();
                frames++;
            }
        }

        terminateAudioManager();
        glfwTerminate();
    }

    public static void stopRunning() {running = false;}

    public static void main(String[] args) {
        new Driver().run();
    }
}
