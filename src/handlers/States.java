package handlers;

import java.util.HashMap;
import java.util.Map;

import static handlers.Keys.setKeysRepeated;
import static handlers.MouseButton.setLeftRepeated;
import static handlers.MouseButton.setRightRepeated;
import static org.lwjgl.glfw.GLFW.*;

public class States {
    public static final byte RELEASED = 0;
    public static final byte PRESSED = 1;
    public static final byte REPEATED = 2;

    public static final int KEY_A = 0;
    public static final int KEY_B = 1;
    public static final int KEY_C = 2;
    public static final int KEY_D = 3;
    public static final int KEY_E = 4;
    public static final int KEY_F = 5;
    public static final int KEY_G = 6;
    public static final int KEY_H = 7;
    public static final int KEY_I = 8;
    public static final int KEY_J = 9;
    public static final int KEY_K = 10;
    public static final int KEY_L = 11;
    public static final int KEY_M = 12;
    public static final int KEY_N = 13;
    public static final int KEY_O = 14;
    public static final int KEY_P = 15;
    public static final int KEY_Q = 16;
    public static final int KEY_R = 17;
    public static final int KEY_S = 18;
    public static final int KEY_T = 19;
    public static final int KEY_U = 20;
    public static final int KEY_V = 21;
    public static final int KEY_W = 22;
    public static final int KEY_X = 23;
    public static final int KEY_Y = 24;
    public static final int KEY_Z = 25;
    public static final int KEY_1 = 26;
    public static final int KEY_2 = 27;
    public static final int KEY_3 = 28;
    public static final int KEY_4 = 29;
    public static final int KEY_5 = 30;
    public static final int KEY_6 = 31;
    public static final int KEY_7 = 32;
    public static final int KEY_8 = 33;
    public static final int KEY_9 = 34;
    public static final int KEY_0 = 35;
    public static final int KEY_ESCAPE = 36;
    public static final int KEY_ENTER = 37;
    public static final int KEY_COMMA = 38;
    public static final int KEY_APOSTROPHE = 39;
    public static final int KEY_PERIOD = 40;
    public static final int KEY_SLASH = 41;
    public static final int KEY_BACKSLASH = 42;
    public static final int KEY_SPACE = 43;
    public static final int KEY_MINUS = 44;
    public static final int KEY_EQUAL = 45;
    public static final int KEY_SEMICOLON = 46;
    public static final int KEY_LEFT_BRACKET = 47;
    public static final int KEY_RIGHT_BRACKET = 48;
    public static final int KEY_LAST = 49;

    protected static Map<Integer, Integer> keyMap;

    public static void init(){
        keyMap = new HashMap<>();
        keyMap.put(GLFW_KEY_A, KEY_A);
        keyMap.put(GLFW_KEY_B, KEY_B);
        keyMap.put(GLFW_KEY_C, KEY_C);
        keyMap.put(GLFW_KEY_D, KEY_D);
        keyMap.put(GLFW_KEY_E, KEY_E);
        keyMap.put(GLFW_KEY_F, KEY_F);
        keyMap.put(GLFW_KEY_G, KEY_G);
        keyMap.put(GLFW_KEY_H, KEY_H);
        keyMap.put(GLFW_KEY_I, KEY_I);
        keyMap.put(GLFW_KEY_J, KEY_J);
        keyMap.put(GLFW_KEY_K, KEY_K);
        keyMap.put(GLFW_KEY_L, KEY_L);
        keyMap.put(GLFW_KEY_M, KEY_M);
        keyMap.put(GLFW_KEY_N, KEY_N);
        keyMap.put(GLFW_KEY_O, KEY_O);
        keyMap.put(GLFW_KEY_P, KEY_P);
        keyMap.put(GLFW_KEY_Q, KEY_Q);
        keyMap.put(GLFW_KEY_R, KEY_R);
        keyMap.put(GLFW_KEY_S, KEY_S);
        keyMap.put(GLFW_KEY_T, KEY_T);
        keyMap.put(GLFW_KEY_U, KEY_U);
        keyMap.put(GLFW_KEY_V, KEY_V);
        keyMap.put(GLFW_KEY_W, KEY_W);
        keyMap.put(GLFW_KEY_X, KEY_X);
        keyMap.put(GLFW_KEY_Y, KEY_Y);
        keyMap.put(GLFW_KEY_Z, KEY_Z);
        keyMap.put(GLFW_KEY_1, KEY_1);
        keyMap.put(GLFW_KEY_2, KEY_2);
        keyMap.put(GLFW_KEY_3, KEY_3);
        keyMap.put(GLFW_KEY_4, KEY_4);
        keyMap.put(GLFW_KEY_5, KEY_5);
        keyMap.put(GLFW_KEY_6, KEY_6);
        keyMap.put(GLFW_KEY_7, KEY_7);
        keyMap.put(GLFW_KEY_8, KEY_8);
        keyMap.put(GLFW_KEY_9, KEY_9);
        keyMap.put(GLFW_KEY_0, KEY_0);
        keyMap.put(GLFW_KEY_ESCAPE, KEY_ESCAPE);
        keyMap.put(GLFW_KEY_ENTER, KEY_ENTER);
        keyMap.put(GLFW_KEY_COMMA, KEY_COMMA);
        keyMap.put(GLFW_KEY_APOSTROPHE, KEY_APOSTROPHE);
        keyMap.put(GLFW_KEY_PERIOD, KEY_PERIOD);
        keyMap.put(GLFW_KEY_SLASH, KEY_SLASH);
        keyMap.put(GLFW_KEY_BACKSLASH, KEY_BACKSLASH);
        keyMap.put(GLFW_KEY_SPACE, KEY_SPACE);
        keyMap.put(GLFW_KEY_MINUS, KEY_MINUS);
        keyMap.put(GLFW_KEY_EQUAL, KEY_EQUAL);
        keyMap.put(GLFW_KEY_SEMICOLON, KEY_SEMICOLON);
        keyMap.put(GLFW_KEY_LEFT_BRACKET, KEY_LEFT_BRACKET);
        keyMap.put(GLFW_KEY_RIGHT_BRACKET, KEY_RIGHT_BRACKET);
    }

    public static void updateInput(){
        setLeftRepeated();
        setRightRepeated();
        setKeysRepeated();
    }
}
