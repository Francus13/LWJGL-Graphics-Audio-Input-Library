package handlers;

import org.lwjgl.glfw.GLFWKeyCallback;

import java.util.ArrayDeque;
import java.util.Deque;

import static handlers.States.*;
import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

public class Keys extends GLFWKeyCallback{
    private final static Deque<Integer> keysToRepeat = new ArrayDeque<>();
    private static final byte[] keys = new byte[KEY_LAST];

    @Override
    public void invoke(long window, int key, int scancode, int action, int mode) {
        if (keyMap.containsKey(key)){
            key = keyMap.get(key);
            if (action == GLFW_RELEASE)
                keys[key] = RELEASED;
            else if (action == GLFW_PRESS){
                keys[key] = PRESSED;
                keysToRepeat.add(key);
            }
        }
    }

    public static int key(int keycode) {return keys[keycode];}

    public static void setKeysRepeated(){
        while (!keysToRepeat.isEmpty()){
            int key = keysToRepeat.pop();
            if (keys[key] == PRESSED)
                keys[key] = REPEATED;
        }
    }
}
