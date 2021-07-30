package graphicsLibrary;
import handlers.Cursor;

import static graphicsLibrary.Window.windowHeight;
import static graphicsLibrary.Window.windowWidth;
import static runner.Driver.getTime;

public abstract class Renderable {
    public static final int CENTERED = -1;

    protected float x;
    protected float y;
    protected float width;
    protected float height;
    protected float s;
    protected float t;
    protected float p;
    protected float q;
    protected float rot;
    protected float pivotX;
    protected float pivotY;
    protected boolean rendered;
    protected boolean timed;
    protected double time;
    protected double startTime;

    public Renderable() {
        rendered = false;
        timed = false;
        setCoords(0, 0);
        setSize(0, 0);
        setST(0, 0);
        setPQ(0, 0);
        setRotation(0);
        setPivot(0, 0);
    }

    public Renderable(double time) {
        rendered = false;
        timed = true;
        this.time = time;
        setCoords(0, 0);
        setSize(0, 0);
        setST(0, 0);
        setPQ(0, 0);
        setRotation(0);
        setPivot(0, 0);
    }

    abstract public Texture getTexture();
    abstract public void setOriginalSize();
    abstract public void free();

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

    public void rotate(float rot) {this.rot += rot;}
    public void setRotation(float rot) {this.rot = rot;}

    //Origin for pivot is at the center of the Renderable
    public void setPivot(float x, float y) {pivotX = x; pivotY = y;}
    public void movePivot(float dx, float dy) {pivotX += dx; pivotY += dy;}


    public float s() {return s;}
    public float t() {return t;}
    public float p() {return p;}
    public float q() {return q;}
    public void setST(float s, float t) {this.s = s; this.t = t;}
    public void setPQ(float p, float q) {this.p = p; this.q = q;}
    public void setPQToSize() {p = width; q = height;}
    public void moveST(float ds, float dt) {s += ds; t += dt;}
    public void showWholeTexture() {s = 0; t = 0; p = getTexture().width(); q = getTexture().height();}

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
