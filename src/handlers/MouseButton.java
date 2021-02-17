package handlers;

import org.lwjgl.glfw.GLFWMouseButtonCallback;

import static handlers.States.*;
import static org.lwjgl.glfw.GLFW.*;

public class MouseButton extends GLFWMouseButtonCallback{
    private static byte leftClick = RELEASED;
    private static byte rightClick = RELEASED;

    @Override
    public void invoke(long window, int button, int action, int mods) {
        if (button == GLFW_MOUSE_BUTTON_LEFT) {
            if (action == GLFW_PRESS)
                leftClick = PRESSED;
            else
                leftClick = RELEASED;
        }
        else if (button == GLFW_MOUSE_BUTTON_RIGHT) {
            if (action == GLFW_PRESS)
                rightClick = PRESSED;
            else
                rightClick = RELEASED;
        }
    }

    public static byte leftClick() {return leftClick;}
    public static byte rightClick() {return rightClick;}

    public static void setLeftRepeated() {if (leftClick == PRESSED) leftClick = REPEATED;}
    public static void setRightRepeated() {if (rightClick == PRESSED) rightClick = REPEATED;}
}
