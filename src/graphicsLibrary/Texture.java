package graphicsLibrary;

import org.lwjgl.BufferUtils;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.stb.STBImage.stbi_image_free;
import static org.lwjgl.stb.STBImage.stbi_load;

public class Texture {
    public static final Texture emptyTexture = new Texture();
    private final int id;
    public final static int PADDING = 4;
    private final int width;
    private final int height;

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

    //For Text Textures
    public Texture(String[] text, Font[] fonts, Color[] colors, int alignment, int width, int height){
        if (width > 1920 || height > 1080){
            StringBuilder t = new StringBuilder();
            for (String s: text)
                t.append(s);
            System.err.println("A textbox cannot have a width greater than 1920 or a height greater than 1080: " + t);
            System.exit(1);
        }

        this.width = width;
        this.height = height;

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

        Font defaultFont = fonts[0];
        int y = defaultFont.getAscent();
        Glyph glyph;

        char[][][] chars = new char[text.length][][];
        for (int i = 0; i < text.length; i++){
            String[] words = text[i].split(" ");
            chars[i] = new char[words.length][];
            for (int j = 0; j < words.length; j++){
                chars[i][j] = words[j].toCharArray();
            }
        }

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        if (alignment == Text.LEFT){
            int x = PADDING;
            for (int i = 0; i < text.length; i++){
                Font font = fonts[i % fonts.length];
                Color color = colors[i % colors.length];

                glPixelTransferf(GL_RED_BIAS, color.r());
                glPixelTransferf(GL_GREEN_BIAS, color.g());
                glPixelTransferf(GL_BLUE_BIAS, color.b());

                for (char[] word: chars[i]){
                    for (int j = 0; j < word.length; j++){
                        if (word[j] == '\n'){
                            x = PADDING;
                            if (j == word.length - 1)
                                x -= defaultFont.getSpaceAdvance();
                            y += defaultFont.getVAdvance() + PADDING;
                            continue;
                        }

                        glyph = font.getGlyph(word[j]);
                        glTexSubImage2D(GL_TEXTURE_2D, 0, x + glyph.leftBearing(), y + glyph.ascent(),
                                glyph.width(), glyph.height(), GL_ALPHA, GL_UNSIGNED_BYTE, glyph.imageData());
                        x += glyph.hAdvance();
                    }

                    x += defaultFont.getSpaceAdvance();
                }
            }
        }
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        if (alignment == Text.CENTER){
            int x = width / 2;
            int l = 0;
            int m = 0;
            int n = 0;

            Font font = fonts[0];
            Color color = colors[0];

            glPixelTransferf(GL_RED_BIAS, color.r());
            glPixelTransferf(GL_GREEN_BIAS, color.g());
            glPixelTransferf(GL_BLUE_BIAS, color.b());

            for (int i = 0; i < text.length; i++){
                for (int j = 0; j < chars[i].length; j++){
                    for (int k = 0; k < chars[i][j].length; k++){
                        if (chars[i][j][k] == '\n'){
                            if (j == 0 && k == 0)
                                x += defaultFont.getSpaceAdvance() / 2;

                            while (!(l == i && m == j && n == k)){
                                glyph = font.getGlyph(chars[l][m][n]);
                                glTexSubImage2D(GL_TEXTURE_2D, 0, x + glyph.leftBearing(), y + glyph.ascent(),
                                        glyph.width(), glyph.height(), GL_ALPHA, GL_UNSIGNED_BYTE, glyph.imageData());
                                x += glyph.hAdvance();

                                n++;
                                if (n == chars[l][m].length){
                                    n = 0;
                                    m++;
                                    x += defaultFont.getSpaceAdvance();
                                    if (m == chars[l].length){
                                        m = 0;
                                        l++;

                                        font = fonts[l % fonts.length];
                                        color = colors[l % colors.length];

                                        glPixelTransferf(GL_RED_BIAS, color.r());
                                        glPixelTransferf(GL_GREEN_BIAS, color.g());
                                        glPixelTransferf(GL_BLUE_BIAS, color.b());
                                    }
                                }
                            }

                            n++;
                            if (n == chars[l][m].length){
                                n = 0;
                                m++;
                                if (m == chars[l].length){
                                    m = 0;
                                    l++;

                                    font = fonts[m % fonts.length];
                                    color = colors[m % colors.length];

                                    glPixelTransferf(GL_RED_BIAS, color.r());
                                    glPixelTransferf(GL_GREEN_BIAS, color.g());
                                    glPixelTransferf(GL_BLUE_BIAS, color.b());
                                }
                            }

                            y += fonts[0].getVAdvance() + PADDING;
                            x = width / 2;
                            if (j == chars[i].length - 1 && k == chars[i][j].length - 1)
                                x += defaultFont.getSpaceAdvance() / 2;

                            continue;
                        }

                        x -= fonts[i % fonts.length].getGlyph(chars[i][j][k]).hAdvance() / 2;
                    }

                    x -= defaultFont.getSpaceAdvance() / 2;
                }
            }

            x += defaultFont.getSpaceAdvance() / 2;
            while (l != chars.length){
                glyph = font.getGlyph(chars[l][m][n]);
                glTexSubImage2D(GL_TEXTURE_2D, 0, x + glyph.leftBearing(), y + glyph.ascent(),
                        glyph.width(), glyph.height(), GL_ALPHA, GL_UNSIGNED_BYTE, glyph.imageData());
                x += glyph.hAdvance();

                n++;
                if (n == chars[l][m].length){
                    n = 0;
                    m++;
                    x += defaultFont.getSpaceAdvance();
                    if (m == chars[l].length){
                        m = 0;
                        l++;

                        font = fonts[m % fonts.length];
                        color = colors[m % colors.length];

                        glPixelTransferf(GL_RED_BIAS, color.r());
                        glPixelTransferf(GL_GREEN_BIAS, color.g());
                        glPixelTransferf(GL_BLUE_BIAS, color.b());
                    }
                }
            }
        }
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        if (alignment == Text.RIGHT){
            int x = width - PADDING;
            int l = 0;
            int m = 0;
            int n = 0;

            Font font = fonts[0];
            Color color = colors[0];

            glPixelTransferf(GL_RED_BIAS, color.r());
            glPixelTransferf(GL_GREEN_BIAS, color.g());
            glPixelTransferf(GL_BLUE_BIAS, color.b());

            for (int i = 0; i < text.length; i++){
                for (int j = 0; j < chars[i].length; j++){
                    for (int k = 0; k < chars[i][j].length; k++){
                        if (chars[i][j][k] == '\n'){
                            if (j == 0 && k == 0)
                                x += defaultFont.getSpaceAdvance();

                            while (!(l == i && m == j && n == k)){
                                glyph = font.getGlyph(chars[l][m][n]);
                                glTexSubImage2D(GL_TEXTURE_2D, 0, x + glyph.leftBearing(), y + glyph.ascent(),
                                        glyph.width(), glyph.height(), GL_ALPHA, GL_UNSIGNED_BYTE, glyph.imageData());
                                x += glyph.hAdvance();

                                n++;
                                if (n == chars[l][m].length){
                                    n = 0;
                                    m++;
                                    x += defaultFont.getSpaceAdvance();
                                    if (m == chars[l].length){
                                        m = 0;
                                        l++;

                                        font = fonts[l % fonts.length];
                                        color = colors[l % colors.length];

                                        glPixelTransferf(GL_RED_BIAS, color.r());
                                        glPixelTransferf(GL_GREEN_BIAS, color.g());
                                        glPixelTransferf(GL_BLUE_BIAS, color.b());
                                    }
                                }
                            }

                            n++;
                            if (n == chars[l][m].length){
                                n = 0;
                                m++;
                                if (m == chars[l].length){
                                    m = 0;
                                    l++;

                                    font = fonts[m % fonts.length];
                                    color = colors[m % colors.length];

                                    glPixelTransferf(GL_RED_BIAS, color.r());
                                    glPixelTransferf(GL_GREEN_BIAS, color.g());
                                    glPixelTransferf(GL_BLUE_BIAS, color.b());
                                }
                            }

                            y += fonts[0].getVAdvance() + PADDING;
                            x = width - PADDING;
                            if (j == chars[i].length - 1 && k == chars[i][j].length - 1)
                                x += defaultFont.getSpaceAdvance();

                            continue;
                        }

                        x -= fonts[i % fonts.length].getGlyph(chars[i][j][k]).hAdvance();
                    }

                    x -= defaultFont.getSpaceAdvance();
                }
            }

            x += defaultFont.getSpaceAdvance();
            while (l != chars.length){
                glyph = font.getGlyph(chars[l][m][n]);
                glTexSubImage2D(GL_TEXTURE_2D, 0, x + glyph.leftBearing(), y + glyph.ascent(),
                        glyph.width(), glyph.height(), GL_ALPHA, GL_UNSIGNED_BYTE, glyph.imageData());
                x += glyph.hAdvance();

                n++;
                if (n == chars[l][m].length){
                    n = 0;
                    m++;
                    x += defaultFont.getSpaceAdvance();
                    if (m == chars[l].length){
                        m = 0;
                        l++;

                        font = fonts[m % fonts.length];
                        color = colors[m % colors.length];

                        glPixelTransferf(GL_RED_BIAS, color.r());
                        glPixelTransferf(GL_GREEN_BIAS, color.g());
                        glPixelTransferf(GL_BLUE_BIAS, color.b());
                    }
                }
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

    //For DynamicTextbox Textures

    public void free() {glDeleteTextures(id);}

    public int id() {return id;}
    public int width() {return width;}
    public int height() {return height;}
}
