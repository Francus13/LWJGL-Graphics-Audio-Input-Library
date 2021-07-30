package graphicsLibrary;

public class TextBox implements VisualObject{
    private Text text;
    private final DynamicBox box;

    public TextBox(Text text, Texture boxTexture, float borderLength){
        this.text = text;
        box = new DynamicBox(boxTexture, borderLength, text.width(), text.height());
    }

    public Text getText() {return text;}
    public void setText(Text text) {this.text = text; updateSize();}
    public void setTextTexture(Texture texture) {text.setTextTexture(texture); updateSize();}
    public void updateText(String text) {this.text.updateText(text); updateSize();}
    public void updateText(String text, Font font) {this.text.updateText(text, font); updateSize();}
    public void updateText(String text, Color color) {this.text.updateText(text, color); updateSize();}
    public void updateText(String text, Font font, Color color) {this.text.updateText(text, font, color); updateSize();}
    public void updateText(String[] text) {this.text.updateText(text); updateSize();}
    public void updateText(String[] text, Font[] fonts) {this.text.updateText(text, fonts); updateSize();}
    public void updateText(String[] text, Color[] colors) {this.text.updateText(text, colors); updateSize();}
    public void updateText(String[] text, Font[] fonts, Color[] colors) {this.text.updateText(text, fonts, colors); updateSize();}

    public void updateSize(){
        box.setSize(text.width(), text.height());
    }


    public void render(){
        box.render();
        Window.render(text);
    }

    public void deRender(){
        box.deRender();
        Window.deRender(text);
    }

    public void reRender(){
        box.reRender();
        Window.reRender(text);
    }

    public float x(){
        return box.x();
    }

    public float y(){
        return box.y();
    }

    public float width(){
        return box.width();
    }

    public float height(){
        return 0;
    }

    public void setCoords(float x, float y){
        box.setCoords(x, y);
        text.setCoords(x + box.borderLength(), y + box.borderLength());
    }

    public void move(float dx, float dy){

    }

    public void setRotation(float rot){

    }

    public void rotate(float rot){

    }

    public void setPivot(float x, float y){

    }

    public void movePivot(float dx, float dy){

    }

    public boolean isRendered(){
        return false;
    }

    public boolean isMouseOn(){
        return false;
    }
}
