package graphics;

import java.util.ArrayDeque;
import java.util.Deque;

import static graphics.Color.BLACK;
import static graphics.Texture.PADDING;
import static graphics.Texture.emptyTexture;
import static graphics.Window.deRender;
import static runner.Timer.getTime;

public class Text extends Renderable{
    public static final int LEFT = 0;
    public static final int CENTER = 1;
    public static final int RIGHT = 2;

    private Texture textBox;
    private final Font font;
    private final int alignment;
    private Color color;
    private final boolean variableSized;
    private final boolean variableHeight;
    private int fixedWidth;

    private double pauseTime;
    private boolean isPaused;
    private Deque<String> textToRender;
    private Deque<Double> timesToRender;

    public Text (Texture textBox){
        super();

        font = null;
        alignment = 0;
        color = null;
        variableSized = false;
        variableHeight = false;
        this.textBox = textBox;
        setSize(textBox.width(), textBox.height());
    }

    public static Texture createTextTexture(String text, Font font){
        char[] chars = text.toCharArray();
        int width = PADDING;
        for (char c: chars){
            if (c == ' ')
                width += font.getSpaceAdvance();
            else
                width += font.getGlyph(c).hAdvance();
        }

        return new Texture(text, font, BLACK, LEFT, width + PADDING + 1,
                font.getAscent() - font.getDescent() + 1 + PADDING);
    }

    public void setTextTexture(Texture textBox){
        this.textBox = textBox;
        setSize(textBox.width(), textBox.height());
    }

    public Text(String text, Font font, int alignment) {
        super();

        char[] chars = text.toCharArray();
        int width = PADDING;
        for (char c: chars){
            if (c == ' ')
                width += font.getSpaceAdvance();
            else
                width += font.getGlyph(c).hAdvance();
        }

        setSize(width + PADDING + 1, font.getAscent() - font.getDescent() + 1 + PADDING);
        this.font = font;
        this.alignment = alignment;
        this.color = BLACK;
        variableSized = true;
        variableHeight = false;
        textBox = new Texture(text, this.font, this.color, this.alignment, (int) this.width, (int) this.height);
    }

    public Text(String text, Font font, int alignment, Color color) {
        super();

        char[] chars = text.toCharArray();
        int width = PADDING;
        for (char c: chars){
            if (c == ' ')
                width += font.getSpaceAdvance();
            else
                width += font.getGlyph(c).hAdvance();
        }

        setSize(width + PADDING + 1, font.getAscent() - font.getDescent() + 1 + PADDING);
        this.font = font;
        this.alignment = alignment;
        this.color = color;
        variableSized = true;
        variableHeight = false;
        textBox = new Texture(text, this.font, this.color, this.alignment, (int) this.width, (int) this.height);
    }

    public Text(String text, Font font, int alignment, float width) {
        super();

        String[] words = text.split(" ");
        char[][] chars = new char[words.length][];
        for (int i = 0; i < words.length; i++){
            chars[i] = words[i].toCharArray();
        }

        int y = font.getAscent() - font.getDescent() + 1 + PADDING;
        int lineLength = PADDING;

        for (char[] aChar : chars){
            for (char c : aChar){
                lineLength += font.getGlyph(c).hAdvance();
                if (lineLength + PADDING >= width){
                    lineLength = PADDING;
                    for (char value : aChar)
                        lineLength += font.getGlyph(value).hAdvance();
                    y += font.getVAdvance() + PADDING;
                    break;
                }
            }

            lineLength += font.getSpaceAdvance();
        }

        setSize(width, y);
        this.font = font;
        this.alignment = alignment;
        this.color = BLACK;
        variableSized = false;
        variableHeight = true;
        fixedWidth = (int) width;
        textBox = new Texture(text, this.font, this.color, this.alignment, (int) this.width, (int) this.height);
    }

    public Text(String text, Font font, int alignment, Color color, float width) {
        super();

        String[] words = text.split(" ");
        char[][] chars = new char[words.length][];
        for (int i = 0; i < words.length; i++){
            chars[i] = words[i].toCharArray();
        }

        int y = font.getAscent() - font.getDescent() + 1 + PADDING;
        int lineLength = PADDING;

        for (char[] aChar : chars){
            for (char c : aChar){
                lineLength += font.getGlyph(c).hAdvance();
                if (lineLength + PADDING >= width){
                    lineLength = PADDING;
                    for (char value : aChar)
                        lineLength += font.getGlyph(value).hAdvance();
                    y += font.getVAdvance() + PADDING;
                    break;
                }
            }

            lineLength += font.getSpaceAdvance();
        }

        setSize(width, y);
        this.font = font;
        this.alignment = alignment;
        this.color = color;
        variableSized = false;
        variableHeight = true;
        fixedWidth = (int) width;
        textBox = new Texture(text, this.font, this.color, this.alignment, (int) this.width, (int) this.height);
    }

    public Text(String text, Font font, int alignment, float width, float height) {
        super();

        setSize(width, height);
        this.font = font;
        this.alignment = alignment;
        this.color = BLACK;
        variableSized = false;
        variableHeight = false;
        textBox = new Texture(text, this.font, this.color, this.alignment, (int) this.width, (int) this.height);
    }

    public Text(String text, Font font, int alignment,  Color color, float width, float height) {
        super();

        setSize(width, height);
        this.font = font;
        this.alignment = alignment;
        this.color = color;
        variableSized = false;
        variableHeight = false;
        textBox = new Texture(text, this.font, this.color, this.alignment, (int) this.width, (int) this.height);
    }

    public Text(String text, Font font, int alignment, double time, double pauseTime) {
        super(time);

        char[] chars = text.toCharArray();
        int width = PADDING;
        for (char c: chars){
            if (c == ' ')
                width += font.getSpaceAdvance();
            else
                width += font.getGlyph(c).hAdvance();
        }

        setSize(width + PADDING + 1, font.getAscent() - font.getDescent() + 1 + PADDING);
        this.font = font;
        this.alignment = alignment;
        this.color = BLACK;
        textBox = new Texture(text, this.font, this.color, this.alignment, (int) this.width, (int) this.height);

        variableSized = true;
        variableHeight = false;
        this.pauseTime = pauseTime;
        isPaused = false;
        textToRender = new ArrayDeque<>();
        timesToRender = new ArrayDeque<>();
    }

    public Text(String text, Font font, int alignment, Color color, double time, double pauseTime) {
        super(time);

        char[] chars = text.toCharArray();
        int width = PADDING;
        for (char c: chars){
            if (c == ' ')
                width += font.getSpaceAdvance();
            else
                width += font.getGlyph(c).hAdvance();
        }

        setSize(width + PADDING + 1, font.getAscent() - font.getDescent() + 1 + PADDING);
        this.font = font;
        this.alignment = alignment;
        this.color = color;
        textBox = new Texture(text, this.font, this.color, this.alignment, (int) this.width, (int) this.height);

        variableSized = true;
        variableHeight = false;
        this.pauseTime = pauseTime;
        isPaused = false;
        textToRender = new ArrayDeque<>();
        timesToRender = new ArrayDeque<>();
    }

    public Text(String text, Font font, int alignment, float width, float height, double time, double pauseTime) {
        super(time);

        setSize(width, height);
        this.font = font;
        this.alignment = alignment;
        this.color = BLACK;
        textBox = new Texture(text, this.font, this.color, this.alignment, (int) this.width, (int) this.height);

        variableSized = false;
        variableHeight = false;
        this.pauseTime = pauseTime;
        isPaused = false;
        textToRender = new ArrayDeque<>();
        timesToRender = new ArrayDeque<>();
    }

    public Text(String text, Font font, int alignment,  Color color, float width, float height, double time, double pauseTime) {
        super(time);

        setSize(width, height);
        this.font = font;
        this.alignment = alignment;
        this.color = color;
        textBox = new Texture(text, this.font, this.color, this.alignment, (int) this.width, (int) this.height);

        variableSized = false;
        variableHeight = false;
        this.pauseTime = pauseTime;
        isPaused = false;
        textToRender = new ArrayDeque<>();
        timesToRender = new ArrayDeque<>();
    }

    public void updateText(String text){
        if (variableSized) {
            char[] chars = text.toCharArray();
            int width = PADDING;
            for (char c : chars) {
                if (c == ' ')
                    width += font.getSpaceAdvance();
                else
                    width += font.getGlyph(c).hAdvance();
            }

            if (alignment == CENTER){
                x -= (width + PADDING - this.width) / 2;
            }

            else if (alignment == RIGHT){
                x -= width + PADDING - this.width;
            }

            setSize(width + PADDING + 1, font.getAscent() - font.getDescent() + 1 + PADDING);
        }

        else if (variableHeight){
            char[] chars = text.toCharArray();
            boolean useTWidth = true;
            int tWidth = PADDING;
            int height = font.getAscent() - font.getDescent() + 1 + PADDING;
            for (char c: chars){
                if (c == ' '){
                    tWidth += font.getSpaceAdvance();

                    if (tWidth >= width - PADDING){
                        height += font.getVAdvance() + PADDING;
                        useTWidth = false;
                        tWidth = PADDING;
                    }
                }
                else {
                    tWidth += font.getGlyph(c).hAdvance();

                    if (tWidth >= width - PADDING){
                        height += font.getVAdvance() + PADDING;
                        useTWidth = false;
                        tWidth = PADDING + font.getGlyph(c).hAdvance();
                    }
                }
            }

            if (alignment == CENTER){
                x -= (width + PADDING - this.width) / 2;
            }

            else if (alignment == RIGHT){
                x -= width + PADDING - this.width;
            }

            if (useTWidth)
                setSize(tWidth, height);
            else
                setSize(width, height);
        }

        textBox = new Texture(text, font, color, alignment, (int) width, (int) this.height);
    }

    public void updateText(String text, Color color){
        if (variableSized) {
            char[] chars = text.toCharArray();
            int width = PADDING;
            for (char c : chars) {
                if (c == ' ')
                    width += font.getSpaceAdvance();
                else
                    width += font.getGlyph(c).hAdvance();
            }

            if (alignment == CENTER){
                x -= (width + PADDING - this.width) / 2;
            }

            else if (alignment == RIGHT){
                x -= width + PADDING - this.width;
            }

            setSize(width + PADDING + 1, font.getAscent() - font.getDescent() + 1 + PADDING);
        }

        else if (variableHeight){
            char[] chars = text.toCharArray();
            boolean useTWidth = true;
            int tWidth = PADDING;
            int height = font.getAscent() - font.getDescent() + 1 + PADDING;
            for (char c: chars){
                if (c == ' '){
                    tWidth += font.getSpaceAdvance();

                    if (tWidth >= fixedWidth - PADDING){
                        height += font.getVAdvance() + PADDING;
                        useTWidth = false;
                        tWidth = PADDING;
                    }
                }
                else {
                    tWidth += font.getGlyph(c).hAdvance();

                    if (tWidth >= fixedWidth - PADDING){
                        height += font.getVAdvance() + PADDING;
                        useTWidth = false;
                        tWidth = PADDING + font.getGlyph(c).hAdvance();
                    }
                }
            }

            if (useTWidth)
                setSize(tWidth , height);
            else
                setSize(fixedWidth, height);
        }

        this.color = color;
        textBox = new Texture(text, font, color, alignment, (int) width, (int) this.height);
    }

    public void setColor(Color color) {this.color = color;}

    public void addTextToQueue(String text){
        if (pauseTime == 0 && textBox == null){
            updateText(text);
        }
        else {
            textToRender.add(text);
            timesToRender.add(time);
        }
    }

    public void addTextToQueue(String text, double time){
        if (pauseTime == 0 && textBox == null){
            updateText(text);
            this.time = time;
        }
        else {
            textToRender.add(text);
            timesToRender.add(time);
        }
    }

    public void setPaused() {
        isPaused = true;
        if (isRendered()){
            if (textToRender.isEmpty()) {
                deRender(this);
            }
            else {
                textBox = null;
                startTime = getTime();
            }
        }
    }

    public void updateTextTimed(String text){
        updateText(text);
        startTime = getTime();
    }

    private void update(){
        double newTime = getTime();
        if (isPaused){
            if (newTime - startTime > pauseTime) {
                isPaused = false;
                updateText(textToRender.poll());
                time = timesToRender.poll();
                startTime = newTime;
            }
        }
        else {
            if (newTime - startTime > time) {
                if (pauseTime == 0){
                    if (!textToRender.isEmpty()){
                        updateText(textToRender.poll());
                        time = timesToRender.poll();
                        startTime = newTime;
                    }
                    else
                        textBox = null;
                }
                else {
                    isPaused = true;
                    if (!textToRender.isEmpty()){
                        textBox = emptyTexture;
                        startTime = newTime;
                    }
                    else
                        textBox = null;
                }
            }
        }
    }

    public void setOriginalSize(){
        setSize(textBox.width(), textBox.height());
    }

    Texture getTexture(){
        if (isTimed())
            update();
        return textBox;
    }
}
