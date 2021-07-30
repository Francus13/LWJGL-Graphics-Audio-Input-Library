package handlers;

import org.lwjgl.glfw.GLFWCursorPosCallback;

public class Cursor extends GLFWCursorPosCallback {

    private static double xPos;
    private static double yPos;

    public void invoke(long window, double x, double y) {
        xPos = x;
        yPos = y;
    }

    public static float x() {return (float) xPos;}
    public static float y() {return (float) yPos;}
}
