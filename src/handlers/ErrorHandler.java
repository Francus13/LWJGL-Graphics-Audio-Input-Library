package handlers;

import org.lwjgl.glfw.GLFWErrorCallback;

public class ErrorHandler extends GLFWErrorCallback {

    @Override
    public void invoke(int error, long description) {
        System.err.println("Invoked GLFWErrorCallback - Integer is " + error + ": " + getDescription(description));
    }
}