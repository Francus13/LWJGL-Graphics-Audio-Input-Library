package graphics;

import static runner.Timer.getTime;
import static runner.Driver.FPSInverse;

public class Animation extends Renderable{

    private String fileName;
    private int numFrames;
    private Texture[] frames;
    private Texture frame;
    private int pointer;
    private boolean doesFinish;

    public Animation(String fileName, int numFrames, boolean doesFinish) {
        super(FPSInverse);

        this.doesFinish = doesFinish;

        if (doesFinish){
            this.fileName = fileName;
            this.numFrames = numFrames;
            frame = new Texture("res/Images/" + fileName + "/_0.png");
            setSize(frame.width(), frame.height());
        }

        else {
            frames = new Texture[numFrames];

            for (int i = 0; i < numFrames; i++) {
                frames[i] = new Texture("res/Images/" + fileName + "/_" + i + ".png");
            }

            setSize(frames[0].width(), frames[0].height());
        }
    }

    public Animation(String fileName, int numFrames, double secondsPerFrame, boolean doesFinish) {
        super(secondsPerFrame);

        this.doesFinish = doesFinish;

        if (doesFinish){
            this.fileName = fileName;
            this.numFrames = numFrames;
            frame = new Texture("res/Images/" + fileName + "/_0.png");
            setSize(frame.width(), frame.height());
        }

        else {
            frames = new Texture[numFrames];

            for (int i = 0; i < numFrames; i++) {
                frames[i] = new Texture("res/Images/" + fileName + "/_" + i + ".png");
            }

            setSize(frames[0].width(), frames[0].height());
        }
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

            if (doesFinish){
                if (pointer == numFrames)
                    return null;
                frame.addImage("res/Images/" + fileName + "/_" + pointer + ".png", 0, 0);
                return frame;
            }

            else if (pointer == frames.length)
                pointer = 0;
            return frames[pointer];
        }

        if (doesFinish)
            return frame;
        return frames[pointer];
    }

    public void setOriginalSize(){
        setSize(frames[0].width(), frames[0].height());
    }

    public void reset() {
        pointer = 0;
        startTime = getTime();
    }
}
