package audioLibrary;

import static org.lwjgl.openal.AL11.*;

public class Player {

    protected final int id;
    private float gain;

    public Player(float gain){
        id = alGenSources();

        if (alGetError() != AL_NO_ERROR){
            System.err.println("Failure to create an audio player");
            return;
        }

        setGain(gain);
    }

    public void play(int bufferId){
        alSourcei(id, AL_BUFFER, bufferId);
        alSourcePlay(id);
    }

    public void play(int[] bufferIds){
        alSourceQueueBuffers(id, bufferIds); System.out.println(alGetSourcei(id, AL_BUFFERS_QUEUED));
        alSourcePlay(id);
    }

    public float getGain() {return gain;}
    public void setGain(float gain) {this.gain = gain; alSourcef(id, AL_GAIN, gain);}
    public void stop() {alSourceUnqueueBuffers(id); alSourceStop(id);}
    public void pause() {if (alGetSourcei(id, AL_SOURCE_STATE) == AL_PLAYING) alSourcePause(id);}
    public void start() {if (alGetSourcei(id, AL_SOURCE_STATE) == AL_PAUSED) alSourcePlay(id);}
    public void restart() {start(); alSourcePlay(id);}

    public boolean isStopped() {return alGetSourcei(id, AL_SOURCE_STATE) == AL_STOPPED;}

    public void free() {alDeleteSources(id);}
}
