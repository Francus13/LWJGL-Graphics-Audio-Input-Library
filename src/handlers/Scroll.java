package handlers;

import org.lwjgl.glfw.GLFWScrollCallback;

public class Scroll extends GLFWScrollCallback {

    private static double scrollOffset;

    @Override
    public void invoke(long window, double xoffset, double yoffset) {scrollOffset += yoffset;}

    public static double offset() {
        double tmp = scrollOffset;
        scrollOffset = 0;
        return tmp;
    }
}
