package graphics;

import org.lwjgl.BufferUtils;
import static org.lwjgl.opengl.GL11.*;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.stb.STBImage.stbi_image_free;
import static org.lwjgl.stb.STBImage.stbi_load;

public class Texture {
    public static final Texture emptyTexture = new Texture();
    private final int id;
    public final static int PADDING = 4;
    private int width;
    private int height;

    private Texture(){
        id = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, id);

        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

        width = 0;
        height = 0;

        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, 0 ,0,
                0, GL_RGBA, GL_UNSIGNED_BYTE, BufferUtils.createByteBuffer(0));
    }

    public Texture(String fileName){
        IntBuffer width = BufferUtils.createIntBuffer(1);
        IntBuffer height = BufferUtils.createIntBuffer(1);
        IntBuffer channels = BufferUtils.createIntBuffer(1);

        ByteBuffer data = stbi_load(fileName, width, height, channels, 4);

        if (data == null){
            System.err.println("Could not retrieve texture data: " + fileName);
            System.exit(1);
        }

        id = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, id);

        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

        this.width = width.get();
        this.height = height.get();

        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, this.width, this.height,
                0, GL_RGBA, GL_UNSIGNED_BYTE, data);
        stbi_image_free(data);
    }

    public Texture(String text, Font font, Color color, int alignment, int width, int height){
        if (width > 1920 || height > 1080){
            System.err.println("A textbox cannot have a width greater than 1920 or a height greater than 1080: " + text);
            System.exit(1);
        }

        IntBuffer w = BufferUtils.createIntBuffer(1);
        IntBuffer h = BufferUtils.createIntBuffer(1);
        IntBuffer channels = BufferUtils.createIntBuffer(1);

        ByteBuffer data = stbi_load("res/Images/Blank.png", w, h, channels, 4);

        if (data == null){
            System.err.println("Could not retrieve blank file data");
            System.exit(1);
        }

        id = glGenTextures();

        glBindTexture(GL_TEXTURE_2D, id);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);

        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height,
                0, GL_RGBA, GL_UNSIGNED_BYTE, data);
        stbi_image_free(data);

        glPixelStorei(GL_UNPACK_ALIGNMENT, 1);

        glPixelTransferf(GL_RED_BIAS, color.r());
        glPixelTransferf(GL_GREEN_BIAS, color.g());
        glPixelTransferf(GL_BLUE_BIAS, color.b());

        String[] words = text.split(" ");
        char[][] chars = new char[words.length][];
        for (int i = 0; i < words.length; i++){
            chars[i] = words[i].toCharArray();
        }

        int y = font.getAscent() + PADDING;
        int lineLength = PADDING;
        Glyph glyph;

        if (alignment == Text.LEFT) {
            int x = PADDING;

            for (int i = 0; i < chars.length; i++){

                for (int j = 0; j < chars[i].length; j++){
                    lineLength += font.getGlyph(chars[i][j]).hAdvance();
                    if (lineLength + PADDING >= width) {
                        x = PADDING;
                        lineLength = PADDING;
                        for (int k = 0; k < chars[i].length; k++)
                            lineLength += font.getGlyph(chars[i][k]).hAdvance();
                        y += font.getVAdvance() + PADDING;
                        break;
                    }
                }

                for (int j = 0; j < chars[i].length; j++) {
                    glyph = font.getGlyph(chars[i][j]);
                    glTexSubImage2D(GL_TEXTURE_2D, 0, x + glyph.leftBearing(), y + glyph.ascent(),
                            glyph.width(), glyph.height(), GL_ALPHA, GL_UNSIGNED_BYTE, glyph.imageData());
                    x += glyph.hAdvance();
                }

                lineLength += font.getSpaceAdvance();
                x += font.getSpaceAdvance();
            }
        }

        if (alignment == Text.CENTER){
            int x = width / 2;
            int startWordIndex = 0;
            int endWordIndex = 0;
            boolean render = false;
            boolean lastRender = false;

            for (int i = 0; i < chars.length; i++) {

                for (int j = 0; j < chars[i].length; j++) {
                    lineLength += font.getGlyph(chars[i][j]).hAdvance();
                    if (lineLength + PADDING >= width) {
                        lineLength = PADDING;
                        for (int k = 0; k < chars[i].length; k++)
                            lineLength += font.getGlyph(chars[i][k]).hAdvance();
                        render = true;
                        if (i == chars.length - 1)
                            lastRender = true;
                        break;
                    }
                }

                if (lastRender){
                    for (int k = startWordIndex; k < endWordIndex; k++) {
                        for (int j = 0; j < chars[k].length; j++) {
                            glyph = font.getGlyph(chars[k][j]);
                            glTexSubImage2D(GL_TEXTURE_2D, 0, x + glyph.leftBearing(), y + glyph.ascent(),
                                    glyph.width(), glyph.height(), GL_ALPHA, GL_UNSIGNED_BYTE, glyph.imageData());
                            x += glyph.hAdvance();
                        }

                        x += font.getSpaceAdvance();
                    }
                    y += font.getVAdvance() + PADDING;
                    startWordIndex = endWordIndex;
                }

                if (i == chars.length - 1) {
                    x = (width - lineLength) / 2;
                    endWordIndex++;
                    render = true;
                }

                if(render) {
                    for (int k = startWordIndex; k < endWordIndex; k++) {
                        for (int j = 0; j < chars[k].length; j++) {
                            glyph = font.getGlyph(chars[k][j]);
                            glTexSubImage2D(GL_TEXTURE_2D, 0, x + glyph.leftBearing(), y + glyph.ascent(),
                                    glyph.width(), glyph.height(), GL_ALPHA, GL_UNSIGNED_BYTE, glyph.imageData());
                            x += glyph.hAdvance();
                        }

                        x += font.getSpaceAdvance();
                    }

                    render = false;
                    y += font.getVAdvance() + PADDING;
                    startWordIndex = endWordIndex;
                }

                x = (width - lineLength) / 2;
                lineLength += font.getSpaceAdvance();
                endWordIndex++;
            }
        }

        if (alignment == Text.RIGHT){
            int x = width;
            int startWordIndex = 0;
            int endWordIndex = 0;
            boolean render = false;
            boolean lastRender = false;

            for (int i = 0; i < chars.length; i++) {

                for (int j = 0; j < chars[i].length; j++) {
                    lineLength += font.getGlyph(chars[i][j]).hAdvance();
                    if (lineLength + PADDING >= width) {
                        lineLength = PADDING;
                        for (int k = 0; k < chars[i].length; k++)
                            lineLength += font.getGlyph(chars[i][k]).hAdvance();
                        render = true;
                        if (i == chars.length - 1)
                            lastRender = true;
                        break;
                    }
                }

                if (lastRender){
                    for (int k = startWordIndex; k < endWordIndex; k++) {
                        for (int j = 0; j < chars[k].length; j++) {
                            glyph = font.getGlyph(chars[k][j]);
                            glTexSubImage2D(GL_TEXTURE_2D, 0, x + glyph.leftBearing(), y + glyph.ascent(),
                                    glyph.width(), glyph.height(), GL_ALPHA, GL_UNSIGNED_BYTE, glyph.imageData());
                            x += glyph.hAdvance();
                        }

                        x += font.getSpaceAdvance();
                    }
                    y += font.getVAdvance() + PADDING;
                    startWordIndex = endWordIndex;
                }

                if (i == chars.length - 1) {
                    x = width - lineLength;
                    endWordIndex++;
                    render = true;
                }

                if(render) {
                    for (int k = startWordIndex; k < endWordIndex; k++) {
                        for (int j = 0; j < chars[k].length; j++) {
                            glyph = font.getGlyph(chars[k][j]);
                            glTexSubImage2D(GL_TEXTURE_2D, 0, x + glyph.leftBearing(), y + glyph.ascent(),
                                    glyph.width(), glyph.height(), GL_ALPHA, GL_UNSIGNED_BYTE, glyph.imageData());
                            x += glyph.hAdvance();
                        }

                        x += font.getSpaceAdvance();
                    }

                    render = false;
                    y += font.getVAdvance() + PADDING;
                    startWordIndex = endWordIndex;
                }

                x = width - lineLength;
                lineLength += font.getSpaceAdvance();
                endWordIndex++;
            }
        }

        glPixelTransferf(GL_RED_BIAS, 0);
        glPixelTransferf(GL_GREEN_BIAS, 0);
        glPixelTransferf(GL_BLUE_BIAS, 0);

        glPixelStorei(GL_UNPACK_ALIGNMENT, 4);
    }

    public void addImage(String fileName, float x, float y){
        IntBuffer width = BufferUtils.createIntBuffer(1);
        IntBuffer height = BufferUtils.createIntBuffer(1);
        IntBuffer channels = BufferUtils.createIntBuffer(1);

        ByteBuffer data = stbi_load(fileName, width, height, channels, 4);

        if (data == null) {
            System.err.println("Could not retrieve texture data");
            System.exit(1);
        }

        glTexSubImage2D(GL_TEXTURE_2D, 0, (int) x, (int) y,
                width.get(), height.get(), GL_RGBA, GL_UNSIGNED_BYTE, data);
        stbi_image_free(data);
    }

    public int id() {return id;}
    public int width() {return width;}
    public int height() {return height;}
}
