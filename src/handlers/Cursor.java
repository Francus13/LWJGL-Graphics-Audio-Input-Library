package handlers;

import org.lwjgl.glfw.GLFWCursorPosCallback;

public class Cursor extends GLFWCursorPosCallback {

    private static double xPos;
    private static double yPos;

    public void invoke(long window, double x, double y) {
        xPos = x;
        yPos = y;
    }

    public static double x() {return xPos;}
    public static double y() {return yPos;}
}
