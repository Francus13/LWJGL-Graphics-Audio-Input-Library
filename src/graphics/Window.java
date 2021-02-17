package graphics;

import static java.lang.System.exit;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL46C.*;
import static org.lwjgl.system.MemoryUtil.*;
import static runner.Driver.WHITE;

import handlers.*;

import org.lwjgl.opengl.GL;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Window {
    private static int width, height;
    private static long window;

    private static final Keys keyCallback = new Keys();
    private static final Cursor cursorCallback  = new Cursor();
    private static final MouseButton mouseButtonCallback  = new MouseButton();
    private static final Scroll scrollCallback = new Scroll();

    private static Mesh mesh;
    private static Shader shader;

    private static final List<Renderable> toBeRendered = new ArrayList<>();
    private static int startIndex = 0;

    public static void initWindow(int w, int h, boolean resizable, boolean fullscreen) {
        width = w;
        height = h;

        if (!glfwInit()){
            System.err.println("GLFW initialization failed");
            exit(1);
        }

        if (resizable)
            glfwWindowHint(GLFW_RESIZABLE, GL_FALSE);
        else
            glfwWindowHint(GLFW_RESIZABLE, GL_TRUE);

        if (fullscreen)
            window = glfwCreateWindow(width, height, "Roomie", glfwGetPrimaryMonitor(), NULL);
        else
            window = glfwCreateWindow(width, height, "Roomie", NULL, NULL);

        if (window == NULL){
            System.err.println("Could not create the Window");
            exit(1);
        }

        glfwMakeContextCurrent(window);
        GL.createCapabilities();

        glClearColor(0, 0, 0, 1.0f);
        glfwSetWindowPos(window, 0, 38);
        glfwShowWindow(window);

        glfwSetKeyCallback(window, keyCallback);
        glfwSetCursorPosCallback(window, cursorCallback);
        glfwSetMouseButtonCallback(window, mouseButtonCallback);
        glfwSetScrollCallback(window, scrollCallback);

        mesh = new Mesh();
        shader = new Shader("DefaultShader");

        shader.bind();
        shader.setUniform("matColor", WHITE);

        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
    }

    public static boolean renderScreen(){
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        prepare();

        Iterator<Renderable> toRender = toBeRendered.iterator();

        for (int i = 0; i < startIndex; i++)
            toRender.next();

        while (toRender.hasNext()){
            Renderable r = toRender.next();
            Texture t = r.getTexture();
            if (t == null) {
                r.setRendered(false);
                toRender.remove();
            }
            else
                drawTexture(t, r.x(), r.y(), r.width(), r.height());
        }
        mesh.unbind();

        glfwSwapBuffers(window);

        return glfwWindowShouldClose(window);
    }

    public static int windowWidth() {return width;}
    public static int windowHeight() {return height;}
    public static void setWidth(int w) {width = w;}
    public static void setHeight(int h) {height = h;}
    public static void freeze() {startIndex = toBeRendered.size();}
    public static void unFreeze() {startIndex = 0;}

    public static void render(Renderable r) {
        if (r.isRendered()){
            System.err.println("Window.render(): Renderable is already rendered" + r);
            System.exit(1);
        }
        toBeRendered.add(r);
        r.setRendered(true);
        if (r instanceof Animation)
            ((Animation) r).reset();
        if (r.isTimed())
            r.startTime();
    }

    public static void deRender(Renderable r) {
        r.setRendered(false);
        toBeRendered.remove(r);
    }

    public static void deRenderAll() {
        for (Renderable r: toBeRendered)
            r.setRendered(false);
        toBeRendered.clear();
    }

    public static void reRender(Renderable r){
        toBeRendered.remove(r);
        toBeRendered.add(r);
        if (r instanceof Animation)
            ((Animation) r).reset();
        if (r.isTimed())
            r.startTime();
    }

    private static void prepare(){
        glDisable(GL_DEPTH_TEST);
        Matrix4x4 ortho = Matrix4x4.ortho(0, width, 0, height);
        shader.setUniform("projection", ortho);
        mesh.bind();
    }

    public static void drawTexture(Texture tex, float x, float y, float width, float height){
        glBindTexture(GL_TEXTURE_2D, tex.id());
        shader.setUniform("pixelScale", width, height);
        shader.setUniform("screenPos", x, y);
        mesh.render();
    }
}
