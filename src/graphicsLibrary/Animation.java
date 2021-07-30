package graphicsLibrary;

import static runner.Driver.FPSInverse;
import static runner.Driver.getTime;

public class Animation extends Renderable{

    private final Texture[] frames;
    private int pointer;
    private final boolean doesFinish;

    public Animation(String fileName, int numFrames, boolean doesRepeat) {
        super(FPSInverse);

        doesFinish = !doesRepeat;
        frames = new Texture[numFrames];

        for (int i = 0; i < numFrames; i++) {
            frames[i] = new Texture("res/Images/" + fileName + "/_" + i + ".png");
        }

        setSize(frames[0].width(), frames[0].height());
        setPQ(width, height);
    }

    public Animation(String fileName, int numFrames, boolean doesRepeat, double secondsPerFrame) {
        super(secondsPerFrame);

        this.doesFinish = !doesRepeat;
        frames = new Texture[numFrames];

        for (int i = 0; i < numFrames; i++) {
            frames[i] = new Texture("res/Images/" + fileName + "/_" + i + ".png");
        }

        setSize(frames[0].width(), frames[0].height());
        setPQ(width, height);
    }

    public void addImage(String fileName, int numFrames, float x, float y){
        for (int i = 0; i < frames.length; i++)
            frames[i].addImage("res/Images/" + fileName + "/_" + i % numFrames + ".png", x, y);
    }

    public Texture getTexture(){
        double newTime = getTime();
        if (newTime - startTime > time) {
            pointer++;
            startTime = newTime;

            if (pointer >= frames.length){
                if (doesFinish)
                    return null;
                pointer = 0;
            }
        }

        return frames[pointer];
    }

    public void setOriginalSize(){
        setSize(frames[0].width(), frames[0].height());
    }

    public void free() {for (Texture t: frames) t.free();}

    public void reset() {
        pointer = 0;
        startTime = getTime();
    }
}
