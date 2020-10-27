package graphics;

import handlers.Cursor;

import static handlers.MouseButton.leftClick;
import static handlers.States.*;

public class Slider {
    private final RenderableTexture sliderBase;
    private final RenderableTexture sliderDragger;

    private boolean hasChanged;
    private float value;
    private final float length;
    private final float maxValue;
    private final float draggerOffset;

    private boolean isSelected;
    private double prevX;
    private float draggerCenterToCursorOffset;

    public Slider(float length, float height, float value, float maxValue){
        this.length = length;
        this.maxValue = maxValue;
        this.value = value;
        draggerOffset = getDraggerOffset((int) length);
        hasChanged = false;
        isSelected = false;

        sliderBase = new RenderableTexture("Sliders/" + (int) length);
        sliderDragger = new RenderableTexture("Sliders/Dragger");
        sliderDragger.setSize(sliderDragger.width, sliderDragger.height * height / sliderBase.height);
        sliderBase.setSize(sliderBase.width, height);
    }

    public Slider(Slider slider, float height, float value, float maxValue){
        this.length = slider.length;
        this.maxValue = maxValue;
        this.value = value;
        draggerOffset = getDraggerOffset((int) length);
        hasChanged = false;
        isSelected = false;

        sliderBase = new RenderableTexture(slider.sliderBase.getTexture());
        sliderDragger = new RenderableTexture(slider.sliderDragger.getTexture());
        sliderDragger.setSize(sliderDragger.width, sliderDragger.height * height / sliderBase.height);
        sliderBase.setSize(sliderBase.width, height);
    }

    public void setCoords(float x, float y){
        sliderBase.setCoords(x, y);
        sliderDragger.setCoords(x + draggerOffset + (value / maxValue) * length - sliderDragger.width / 2,
                y + (sliderBase.height - sliderDragger.height) / 2);
    }

    public int value() {hasChanged = false; return (int) value;}
    public boolean hasChanged() {return hasChanged;}

    public void setValue(int v){
        if (v < 0 || v > maxValue){
            System.err.println("Invalid slider value: v");
            System.exit(1);
        }

        sliderDragger.move(length * (v - value) / maxValue, 0);
        hasChanged = true;
        value = v;
    }

    public void update(){
        if (isSelected){
            if (leftClick() == RELEASED)
                isSelected = false;
            else {
                hasChanged = true;
                double x = Cursor.x();
                if (x - draggerCenterToCursorOffset < sliderBase.x + draggerOffset){
                    x = sliderBase.x + draggerOffset + draggerCenterToCursorOffset;
                    value = 0;
                }
                else if (x - draggerCenterToCursorOffset > sliderBase.x + draggerOffset + length){
                    x = sliderBase.x + draggerOffset + length + draggerCenterToCursorOffset;
                    value = maxValue;
                }
                else {
                    value += maxValue * (x - prevX) / (length);
                }
                sliderDragger.move((float) (x - prevX), 0);
                prevX = x;
            }
        }
        else if (leftClick() == PRESSED && sliderDragger.isMouseOn()){
            isSelected = true;

            prevX = Cursor.x();
            draggerCenterToCursorOffset = (float) prevX - (sliderDragger.x + sliderDragger.width / 2);
        }
    }

    public void render(){
        Window.render(sliderBase);
        Window.render(sliderDragger);
    }

    public void deRender(){
        Window.deRender(sliderBase);
        Window.deRender(sliderDragger);
    }

    public boolean isRendered() {return sliderBase.isRendered();}

    public float width() {return sliderBase.width;}

    private static float getDraggerOffset(int length) {return 0;}
}
