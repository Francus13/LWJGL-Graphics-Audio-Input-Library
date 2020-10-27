package graphics;

public class RenderableTexture extends Renderable{
    private final Texture texture;

    public RenderableTexture(String fileName) {
        super();
        texture = new Texture("res/Images/" + fileName + ".png");
        setSize(texture.width(), texture.height());
    }

    public RenderableTexture(Texture texture){
        super();
        this.texture = texture;
        setSize(texture.width(), texture.height());
    }

    public void addImage(String fileName, float x, float y) {
        texture.addImage("res/Images/" + fileName + ".png", x, y);
        setSize(texture.width(), texture.height());
    }

    public void setOriginalSize(){
        setSize(texture.width(), texture.height());
    }

    public Texture getTexture() {return texture;}
}
