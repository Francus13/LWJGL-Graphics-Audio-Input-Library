package graphicsLibrary;

interface VisualObject {
    void render();
    void deRender();
    void reRender();

    float x();
    float y();
    float width();
    float height();

    void setCoords(float x, float y);
    void move(float dx, float dy);


    void setRotation(float rot);
    void rotate(float rot);
    void setPivot(float x, float y);
    void movePivot(float dx, float dy);

    boolean isRendered();
    boolean isMouseOn();
}
