package graphicsLibrary;

import handlers.Cursor;
import handlers.Keys;
import handlers.MouseButton;
import handlers.Scroll;
import org.lwjgl.opengl.GL;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static java.lang.System.exit;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL46C.*;
import static org.lwjgl.system.MemoryUtil.NULL;
import static runner.App.WHITE;
import static runner.App.debug;

public class Window {
    private static int width, height;
    private static long window;
    private static boolean changedSize;

    private static final Keys keyCallback = new Keys();
    private static final Cursor cursorCallback  = new Cursor();
    private static final MouseButton mouseButtonCallback  = new MouseButton();
    private static final Scroll scrollCallback = new Scroll();

    private static Mesh mesh;
    private static Shader shader;

    private static final List<Renderable> toBeRendered = new ArrayList<>();
    private static int startIndex = 0;

    public static void initWindow(int w, int h, boolean resizable, boolean fullscreen, String windowName) {
        width = w;
        height = h;
        changedSize = true;

        if (!glfwInit()){
            System.err.println("GLFW initialization failed");
            exit(1);
        }

        if (resizable)
            glfwWindowHint(GLFW_RESIZABLE, GL_FALSE);
        else
            glfwWindowHint(GLFW_RESIZABLE, GL_TRUE);

        if (fullscreen)
            window = glfwCreateWindow(width, height, windowName, glfwGetPrimaryMonitor(), NULL);
        else
            window = glfwCreateWindow(width, height, windowName, NULL, NULL);

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
        mesh.bind();

        shader = new Shader("DefaultShader");
        shader.bind();
        shader.setUniform("color", WHITE);
        //Change input values to shift the plane of rendering. Increasing width and height decreases plane
        Matrix4x4 orthoMatrix = new Matrix4x4();
        orthoMatrix.ortho(0, width, 0, height);
        shader.setUniform("projection", new Matrix4x4());

        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        glDisable(GL_DEPTH_TEST);
    }

    public static boolean renderScreen(){
        if (changedSize){
            Matrix4x4 orthoMatrix = new Matrix4x4();
            orthoMatrix.ortho(0, width, 0, height);
            shader.setUniform("projection", new Matrix4x4());
        }

        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

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
                drawTexture(t, r.x, r.y, r.width, r.height, r.s, r.t, r.p, r.q, r.rot, r.pivotX, r.pivotY);
        }

        glfwSwapBuffers(window);

        return glfwWindowShouldClose(window);
    }

    public static int windowWidth() {return width;}
    public static int windowHeight() {return height;}
    public static void setWidth(int w) {width = w; changedSize = true;}
    public static void setHeight(int h) {height = h; changedSize = true;}
    public static void freeze() {startIndex = toBeRendered.size();}
    public static void unFreeze() {startIndex = 0;}

    public static void render(Renderable r) {
        if (r.isRendered())
            System.err.println("Window.render(): Renderable is already rendered: " + r);
        else {
            toBeRendered.add(r);
            r.setRendered(true);
            if (r instanceof Animation)
                ((Animation) r).reset();
            if (r.isTimed())
                r.startTime();
        }
    }

    public static void deRender(Renderable r) {
        if (debug && !r.isRendered())
            System.err.println("Window.deRender(): Renderable is not rendered: " + r);
        r.setRendered(false);
        toBeRendered.remove(r);
    }

    public static void deRenderAll() {
        for (int i = startIndex; i < toBeRendered.size(); i++)
            toBeRendered.get(i).setRendered(false);
        toBeRendered.clear();
    }

    public static void reRender(Renderable r){
        toBeRendered.remove(r);
        toBeRendered.add(r);
        r.setRendered(true);
        if (r instanceof Animation)
            ((Animation) r).reset();
        if (r.isTimed())
            r.startTime();
    }

    public static void drawTexture(Texture tex, float x, float y, float width, float height, float s, float t, float p, float q, float rot, float pivotX, float pivotY){
        glBindTexture(GL_TEXTURE_2D, tex.id());
        shader.setUniform("size", width, height);
        shader.setUniform("screenPos", x, y);
        shader.setUniform("stpqCoords", s, t, p, q);
        shader.setUniform("rotation", rot);
        shader.setUniform("pivotCoords", pivotX, pivotY);
        shader.setUniform("textureSize", tex.width(), tex.height());
        mesh.render();
    }
}
