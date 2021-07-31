package graphicsLibrary;

import java.util.ArrayList;
import java.util.List;

import static graphicsLibrary.Texture.PADDING;
import static graphicsLibrary.Texture.emptyTexture;
import static runner.App.BLACK;
import static runner.Driver.getTime;

public class Text extends Renderable{
    public static final int LEFT = 0;
    public static final int CENTER = 1;
    public static final int RIGHT = 2;

    private Texture textTexture;
    private Font font;
    private int alignment;
    private Color color;
    private boolean variableSized;
    private boolean variableHeight;

    public static Texture createTextTexture(String text, Font font) {return createTextTexture(new String[] {text}, new Font[] {font}, new Color[] {BLACK});}
    public static Texture createTextTexture(String text, Font font, Color color) {return createTextTexture(new String[] {text}, new Font[] {font}, new Color[] {color});}
    public static Texture createTextTexture(String[] text, Font[] font) {return createTextTexture(text, font, new Color[] {BLACK});}

    public static Texture createTextTexture(String[] text, Font[] fonts, Color[] colors){
        trimSpaces(text);
        int[] sizes = getVarSize(text, fonts);
        return new Texture(text, fonts, colors, LEFT, sizes[0] + PADDING + 1, sizes[1]);
    }

    public void setTextTexture(Texture texture){
        if (texture == null)
            texture = emptyTexture;

        if (alignment == CENTER)
            x += (this.textTexture.width() - texture.width()) / 2f;
        else if (alignment == RIGHT)
            x += this.textTexture.width() - texture.width();

        setSize(texture.width(), texture.height());
        setPQToSize();
        this.textTexture = texture;
    }

    public Text (int alignment) {this(null, alignment);}
    public Text (Texture texture) {this(texture, LEFT);}

    public Text (Texture texture, int alignment){
        super();

        if (texture == null)
            texture = emptyTexture;

        setSize(texture.width(), texture.height());
        setPQToSize();
        setVarialbes(null, alignment, BLACK, false, false);
        this.textTexture = texture;
    }

    public Text(String text, Font font, int alignment, float... size) {this(new String[] {text}, new Font[] {font}, alignment, new Color[] {BLACK}, size);}
    public Text(String text, Font font, int alignment, Color color, float... size) {this(new String[] {text}, new Font[] {font}, alignment, new Color[] {color}, size);}
    public Text(String[] text, Font[] fonts, int alignment, float... size) {this(text, fonts, alignment, new Color[] {BLACK}, size);}

    public Text(String[] text, Font[] fonts, int alignment, Color[] colors, float... size) {
        super();

        trimSpaces(text);
        if (size.length == 0){
            int[] sizes = getVarSize(text, fonts);
            setSize(sizes[0] + PADDING + 1, sizes[1]);
            setVarialbes(fonts[0], alignment, colors[0], true, false);
        }
        else if (size.length == 1) {
            addNewLines(text, fonts, size[0]);
            setSize(size[0], getVarHeight(text, fonts[0]));
            setVarialbes(fonts[0], alignment, colors[0], false, true);
        }
        else {
            addNewLines(text, fonts, size[0]);
            setSize(size[0], size[1]);
            setVarialbes(fonts[0], alignment, colors[0], false, false);
        }

        setPQToSize();
        if (text.length == 1 && text[0].length() == 0)
            textTexture = emptyTexture;
        else
            textTexture = new Texture(text, fonts, colors, this.alignment, (int) this.width, (int) this.height);
    }

    public void updateText(String text) {updateText(new String[] {text}, new Font[] {font}, new Color[] {color});}
    public void updateText(String text, Font font) {updateText(new String[] {text}, new Font[] {font}, new Color[] {color});}
    public void updateText(String text, Color color) {updateText(new String[] {text}, new Font[] {font}, new Color[] {color});}
    public void updateText(String text, Font font, Color color) {updateText(new String[] {text}, new Font[] {font}, new Color[] {color});}
    public void updateText(String[] text) {updateText(text, new Font[] {font}, new Color[] {color});}
    public void updateText(String[] text, Font[] fonts) {updateText(text, fonts, new Color[] {color});}
    public void updateText(String[] text, Color[] colors) {updateText(text, new Font[] {font}, colors);}

    public void updateText(String[] text, Font[] fonts, Color[] colors){
        if (variableSized) {
            int[] size = getVarSize(text, fonts);

            if (alignment == CENTER)
                x -= (size[0] + PADDING - this.width) / 2;
            else if (alignment == RIGHT)
                x -= size[0] + PADDING - this.width;

            setSize(size[0] + PADDING + 1, size[1]);
            setPQToSize();
        }
        else
            addNewLines(text, fonts, width);

        if (variableHeight){
            setSize(width, getVarHeight(text, fonts[0]));
            setPQToSize();
        }


        font = fonts[0];
        color = colors[0];
        if (textTexture != emptyTexture)
            textTexture.free();
        if (text.length == 1 && text[0].length() == 0)
            textTexture = emptyTexture;
        else
            textTexture = new Texture(text, fonts, colors, alignment, (int) width, (int) this.height);
    }

    public void setOriginalSize(){
        setSize(textTexture.width(), textTexture.height());
        setPQToSize();
    }

    public void setAlignment(int alignment) {this.alignment = alignment;}


    @Override
    public float yCenter(Renderable r) {return super.yCenter(r) - 1;}

    public void free() {if (textTexture != emptyTexture) textTexture.free(); color = null; font = null;}

    public Texture getTexture() {if (timed && getTime() - startTime >= time) return null; return textTexture;}


    private void setVarialbes(Font font, int alignment, Color color, boolean variableSized, boolean variableHeight){
        this.font = font;
        this.alignment = alignment;
        this.color = color;
        this.variableSized = variableSized;
        this.variableHeight = variableHeight;
    }


    private static void trimSpaces(String[] text){
        for (int i = 0; i < text.length; i++){
            String str = text[i];
            int startIndex = 0;
            int endIndex = str.length();
            while (startIndex < str.length() && str.charAt(startIndex) == ' ')
                startIndex++;
            while (endIndex > startIndex && str.charAt(endIndex - 1) == ' ')
                endIndex--;
            if (startIndex != endIndex)
                text[i] = str.substring(startIndex, endIndex);
        }
    }

    private static int[] getVarSize(String[] text, Font[] fonts){
        Font defaultFont = fonts[0];
        int width = PADDING;
        int height = defaultFont.getAscent() - defaultFont.getDescent() + 1 + PADDING;
        int lineLength = PADDING;
        for (int i = 0; i < text.length; i++){
            String[] words = text[i].split(" ");
            char[][] chars = new char[words.length][];
            for (int j = 0; j < words.length; j++)
                chars[j] = words[j].toCharArray();

            Font font = fonts[i % fonts.length];
            for (char[] word : chars){
                for (int j = 0; j < word.length; j++){
                    if (word[j] == '\n'){
                        if (j == 0)
                            lineLength -= 2 * defaultFont.getSpaceAdvance();
                        else if (j == word.length - 1)
                            lineLength -= defaultFont.getSpaceAdvance();

                        if (lineLength > width)
                            width = lineLength;
                        lineLength = PADDING;
                        height += fonts[0].getVAdvance() + PADDING;
                    }
                    else
                        lineLength += font.getGlyph(word[j]).hAdvance();
                }

                lineLength += defaultFont.getSpaceAdvance();
            }
        }

        lineLength -= 2 * defaultFont.getSpaceAdvance();
        if (lineLength > width)
            width = lineLength;
        return new int[] {width, height};
    }

    private static int getVarHeight(String[] text, Font defaultFont){
        int height = defaultFont.getAscent() - defaultFont.getDescent() + 1 + PADDING;
        for (String str : text){
            char[] chars = str.toCharArray();
            for (char c: chars){
                if (c == '\n')
                    height += defaultFont.getVAdvance() + PADDING;
            }
        }

        return height + 1;
    }

    private static void addNewLines(String[] text, Font[] fonts, float width){
        Font defaultFont = fonts[0];
        int lineLength = PADDING;
        for (int i = 0; i < text.length; i++){
            List<Integer> newLineIndices = new ArrayList<>(5);
            String[] words = text[i].split(" ");
            char[][] chars = new char[words.length][];
            for (int j = 0; j < words.length; j++)
                chars[j] = words[j].toCharArray();

            Font font = fonts[i % fonts.length];
            for (int j = 0; j < chars.length; j++){
                for (char c : chars[j]){
                    if (c == '\n'){
                        lineLength = PADDING;
                    }
                    else
                        lineLength += font.getGlyph(c).hAdvance();

                    if (lineLength + PADDING >= width){
                        newLineIndices.add(j);
                        lineLength = PADDING;
                        for (char value: chars[j]){
                            if (value == '\n'){
                                lineLength = PADDING;
                            }
                            else
                                lineLength += font.getGlyph(value).hAdvance();
                        }
                        break;
                    }
                }

                lineLength += defaultFont.getSpaceAdvance();
            }

            lineLength += defaultFont.getSpaceAdvance();

            int lastSpaceIndex = 0;
            int lastJ = 0;
            for (int j: newLineIndices){
                if (j == 0)
                    text[i] = '\n' + text[i];
                else {
                    for (int k = lastJ; k < j; k++)
                        lastSpaceIndex = text[i].indexOf(' ', lastSpaceIndex + 1);
                    lastJ = j;
                    text[i] = text[i].substring(0, lastSpaceIndex) + '\n' + text[i].substring(lastSpaceIndex + 1);
                }
            }
        }
    }
}
