package graphics;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.security.AccessControlException;
import java.util.HashMap;
import java.util.Map;

import org.lwjgl.BufferUtils;
import org.lwjgl.stb.STBTTFontinfo;

import static org.lwjgl.stb.STBTruetype.*;

public class Font {
    public static Font TIMES_NEW_ROMAN_32;
    public static Font ARIAL_32;
    public static Font ARIAL_96;
    public static Font ARIAL_25;
    public static Font ARIAL_50;
    public static Font OLD_32;
    public static Font PIXELATED_32;
    public static Font COMIC_SANS_24;

    private final int ascent;
    private final int descent;
    private final int vAdvance;
    private final int spaceAdvance;

    private final Map<Character, Glyph> glyphMap;

    public Font(String name, int size) {
        STBTTFontinfo fontInfo = STBTTFontinfo.create();
        try {
            File fontFile = new File("./res/Fonts/" + name + ".ttf");
            byte[] fontData = new byte[(int) fontFile.length()];
            DataInputStream dis = new DataInputStream(new FileInputStream(fontFile));
            dis.readFully(fontData);

            ByteBuffer fontDataBuffer = BufferUtils.createByteBuffer(fontData.length);
            fontDataBuffer.put(fontData);
            fontDataBuffer.flip();
            if (!stbtt_InitFont(fontInfo, fontDataBuffer)){
                throw new IllegalStateException("Failed to initialize font information.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        float scale = stbtt_ScaleForPixelHeight(fontInfo, size * 2);

        IntBuffer ascent1 = BufferUtils.createIntBuffer(1);
        IntBuffer descent = BufferUtils.createIntBuffer(1);
        IntBuffer lineGap = BufferUtils.createIntBuffer(1);

        stbtt_GetFontVMetrics(fontInfo, ascent1, descent, lineGap);
        this.ascent = (int) (ascent1.get() * scale);
        this.descent = (int) (descent.get() * scale);
        vAdvance = (int) (this.ascent - this.descent + lineGap.get() * scale);

        IntBuffer spaceAdvance = BufferUtils.createIntBuffer(1);
        stbtt_GetCodepointHMetrics(fontInfo, ' ', spaceAdvance, null);
        this.spaceAdvance = (int) (spaceAdvance.get() * scale);

        glyphMap = new HashMap<>();

        for (int c = 33; c < 127; c++){
            IntBuffer width = BufferUtils.createIntBuffer(1);
            IntBuffer height = BufferUtils.createIntBuffer(1);
            IntBuffer ascent = BufferUtils.createIntBuffer(1);
            ByteBuffer glyphData = null;
            try {
                glyphData = stbtt_GetCodepointBitmap(fontInfo, 0, scale, c, width, height, null, ascent);
            }
            catch (IllegalAccessError | AccessControlException e){
                System.out.println("|||||||||||");
            }

            if (glyphData == null) {
                System.err.println("Could not retrieve glyph data for " + c);
                System.exit(1);
            }

            IntBuffer hAdvance = BufferUtils.createIntBuffer(1);
            IntBuffer leftBearing = BufferUtils.createIntBuffer(1);
            stbtt_GetCodepointHMetrics(fontInfo, c, hAdvance, leftBearing);
            glyphMap.put((char) c, new Glyph(glyphData, width.get(), height.get(), ascent.get(), (int) (hAdvance.get() * scale),
                    (int) (leftBearing.get() * scale)));

        }
    }

    public static void initFonts(){
        TIMES_NEW_ROMAN_32 = new Font("Times New Roman", 32);
        ARIAL_32 = new Font("Arial", 32);
        ARIAL_96 = new Font("Arial", 96);
        ARIAL_25 = new Font("Arial", 25);
        ARIAL_50 = new Font("Arial", 50);
        OLD_32 = new Font("UnifrakturCook-Bold", 32);
        PIXELATED_32 = new Font("PressStart2P-Regular", 32);
        COMIC_SANS_24 = new Font("Comic Sans", 24);
    }

    public Glyph getGlyph(char c) {return glyphMap.get(c);}

    public int getAscent() {return ascent;}
    public int getDescent() {return descent;}
    public int getVAdvance() {return vAdvance;}
    public int getSpaceAdvance() {return spaceAdvance;}
}