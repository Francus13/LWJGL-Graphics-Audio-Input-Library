package graphicsLibrary;

import static runner.Driver.getTime;

public class RenderableTexture extends Renderable{
    private final Texture texture;

    public RenderableTexture(String fileName) {
        super();
        texture = new Texture("res/Images/" + fileName + ".png");
        setSize(texture.width(), texture.height());
        setPQ(width, height);
    }

    public RenderableTexture(Texture texture){
        super();
        this.texture = texture;
        setSize(texture.width(), texture.height());
        setPQ(width, height);
    }

    public void addImage(String fileName, float x, float y) {
        texture.addImage("res/Images/" + fileName + ".png", x, y);
        setSize(texture.width(), texture.height());
    }

    public void setOriginalSize(){
        setSize(texture.width(), texture.height());
    }

    public void free() {texture.free();}

    public Texture getTexture() {if (timed && getTime() - startTime >= time) return null; return texture;}
}
