package graphics;

import java.nio.ByteBuffer;

public class Glyph {
    private final ByteBuffer data;
    private final int width;
    private final int height;
    private final int ascent;
    private final int hAdvance;
    private final int leftBearing;

    public Glyph(ByteBuffer imageData, int width, int height, int ascent, int hAdvance, int leftBearing){
        data = imageData;
        this.width = width;
        this.height = height;
        this.ascent = ascent;
        this.hAdvance = hAdvance;
        this.leftBearing = leftBearing;
    }

    public ByteBuffer imageData() {return data;}
    public int width() {return width;}
    public int height() {return height;}
    public int ascent() {return ascent;}
    public int hAdvance() {return hAdvance;}
    public int leftBearing() {return leftBearing;}
}
