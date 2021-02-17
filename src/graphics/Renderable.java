package graphics;
import handlers.Cursor;

import static graphics.Window.windowHeight;
import static graphics.Window.windowWidth;
import static runner.Driver.getTime;

public abstract class Renderable {
    public static final int CENTERED = -1;

    protected float x;
    protected float y;
    protected float width;
    protected float height;
    protected boolean rendered;
    protected boolean timed;
    protected double time;
    protected double startTime;

    public Renderable() {
        rendered = false;
        timed = false;
    }

    public Renderable(double time) {
        rendered = false;
        timed = true;
        this.time = time;
    }

    abstract Texture getTexture();
    abstract void setOriginalSize();
    abstract void free();

    public float x() {return x;}
    public float y() {return y;}
    public void setCoords(float x, float y) {
        if (x == CENTERED)
            this.x = (windowWidth() - width) / 2;
        else
            this.x = x;
        if (y == CENTERED)
            this.y = (windowHeight() - height) / 2;
        else
            this.y = y;
    }
    public void setCoordsBL(float x, float y){
        if (x == CENTERED)
            this.x = (windowWidth() - width) / 2;
        else
            this.x = x;
        if (y == CENTERED)
            this.y = (windowHeight() - height) / 2;
        else
            this.y = y - height;
    }

    public float xCenter(Renderable r){return r.x + (r.width - width) / 2;}
    public float yCenter(Renderable r){return r.y + (r.height - height) / 2;}
    public void centerAround(Renderable r){x = xCenter(r); y = yCenter(r);}


    public void move(float dx, float dy) {x += dx; y += dy;}

    public float width() {return width;}
    public float height() {return height;}
    public void setSize(float w, float h) {width = w; height = h;}

    public boolean isRendered() {return rendered;}
    public void setRendered(boolean rendered) {this.rendered = rendered;}

    public boolean isMouseOn() {
        return rendered && Cursor.x() >= x && Cursor.x() < x + width &&
                Cursor.y() >= y && Cursor.y() < y + height;
    }

    public boolean isTimed() {return timed;}
    public void setTimed(boolean timed) {this.timed = timed;}
    public void setTime(double time) {this.time = time;}
    public void startTime() {startTime = getTime();}
    public void unFreeze(double time) {startTime += time;}
}
