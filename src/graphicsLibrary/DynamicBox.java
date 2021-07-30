package graphicsLibrary;

public class DynamicBox implements VisualObject{
    private final RenderableTexture topLeft;
    private final RenderableTexture bottomLeft;
    private final RenderableTexture topRight;
    private final RenderableTexture bottomRight;
    private final Texture boxTexture;
    private final float borderLength;
    private float width;
    private float height;

    public DynamicBox(Texture boxTexture, float borderLength, float width, float height){
        this.boxTexture = boxTexture;
        this.borderLength = borderLength;
        this.width = 0;
        this.height = 0;

        topLeft = new RenderableTexture(boxTexture);
        bottomLeft = new RenderableTexture(boxTexture);
        topRight = new RenderableTexture(boxTexture);

        bottomRight = new RenderableTexture(boxTexture);
        bottomRight.setST(boxTexture.width() - borderLength, boxTexture.height() - borderLength);
        bottomRight.setSize(borderLength, borderLength);
        bottomRight.setPQToSize();

        topLeft.setPivot(borderLength / 2, borderLength / 2);

        setSize(width, height);
    }

    //Resets the pivot to the center of the box
    public void setSize(float width, float height){
        if (width + 2 * borderLength > boxTexture.width()){
            System.err.println("DynamicBox width of " + width + " is greater than its Texture width (" + boxTexture.width() + ") - 2 * borderLength (" + borderLength + ")");
        }
        if (height + 2 * borderLength > boxTexture.height()){
            System.err.println("DynamicBox height of " + height + " is greater than its Texture height (" + boxTexture.height() + ") - 2 * borderLength (" + borderLength + ")");
        }

        topLeft.setSize(width + borderLength, height + borderLength);
        topLeft.setPQToSize();

        bottomLeft.setST(0, boxTexture.height() - borderLength);
        bottomLeft.setSize(width + borderLength, borderLength);
        bottomLeft.setPQToSize();

        topRight.setST(boxTexture.width() - borderLength, 0);
        topRight.setSize(borderLength, height + borderLength);
        topRight.setPQToSize();

        this.width = width + 2 * borderLength;
        this.height = height + 2 * borderLength;

        setCoords(topLeft.x(), topLeft.y());
        setPivot(0, 0);
    }


    public void render(){
        Window.render(topLeft);
        Window.render(bottomLeft);
        Window.render(topRight);
        Window.render(bottomRight);
    }

    public void deRender(){
        Window.deRender(topLeft);
        Window.deRender(bottomLeft);
        Window.deRender(topRight);
        Window.deRender(bottomRight);
    }

    public void reRender(){
        Window.reRender(topLeft);
        Window.reRender(bottomLeft);
        Window.reRender(topRight);
        Window.reRender(bottomRight);
    }

    public float x() {return topLeft.x();}
    public float y() {return topLeft.y();}
    public float width() {return width;}
    public float height() {return height;}

    public void setCoords(float x, float y){
        topLeft.setCoords(x, y);
        bottomLeft.setCoords(x, y + topLeft.height);
        topRight.setCoords(x + topLeft.width, y);
        bottomRight.setCoords(x + topLeft.width, y + topLeft.height);
    }

    public void move(float dx, float dy){
        topLeft.move(dx, dy);
        bottomLeft.move(dx, dy);
        topRight.move(dx, dy);
        bottomRight.move(dx, dy);
    }

    public void setRotation(float rot){
        topLeft.setRotation(rot);
        bottomLeft.setRotation(rot);
        topRight.setRotation(rot);
        bottomRight.setRotation(rot);
    }

    public void rotate(float rot){
        topLeft.rotate(rot);
        bottomLeft.rotate(rot);
        topRight.rotate(rot);
        bottomRight.rotate(rot);
    }

    public void setPivot(float x, float y){
        topLeft.setPivot(x + borderLength / 2, y + borderLength / 2);
        bottomLeft.setPivot(x + borderLength / 2, y - (height - borderLength) / 2);
        topRight.setPivot(x - (width - borderLength) / 2, y + borderLength / 2);
        bottomRight.setPivot(x - (width - borderLength) / 2, y - (height - borderLength) / 2);
    }

    public void movePivot(float dx, float dy){
        topLeft.movePivot(dx, dy);
        bottomLeft.movePivot(dx, dy);
        topRight.movePivot(dx, dy);
        bottomRight.movePivot(dx, dy);
    }

    public boolean isRendered() {return topLeft.isRendered();}
    public boolean isMouseOn() {return topLeft.isMouseOn() || bottomLeft.isMouseOn() || topRight.isMouseOn() || bottomRight.isMouseOn();}

    public float borderLength() {return borderLength;}
}
