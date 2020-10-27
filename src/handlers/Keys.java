package handlers;

import org.lwjgl.glfw.GLFWKeyCallback;

import static handlers.States.*;
import static org.lwjgl.glfw.GLFW.*;

public class Keys extends GLFWKeyCallback{


    private static final byte[] keys = new byte[KEY_LAST];

    @Override
    public void invoke(long window, int key, int scancode, int action, int mode) {
        if (keyMap.containsKey(key)){
            key = keyMap.get(key);
            if (action == GLFW_RELEASE)
                keys[key] = RELEASED;
            else if (action == GLFW_PRESS)
                keys[key] = PRESSED;
        }
    }

    public static int key(int keycode) {
        byte tmp = keys[keycode];
        if (tmp == PRESSED)
            keys[keycode] = REPEATED;
        return tmp;
    }

    public static boolean otherKeyPressed(int keycode) {
        for (int i = 0; i < keys.length; i++){
            if (i != keycode && keys[i] == PRESSED){
                System.out.println(keycode);
                return true;
            }
        }
        return false;
    }

    public static void setKeysRepeated(){
        for (int i = 0; i < keys.length; i++){
            if (keys[i] == PRESSED)
                keys[i] = REPEATED;
        }
    }
}
