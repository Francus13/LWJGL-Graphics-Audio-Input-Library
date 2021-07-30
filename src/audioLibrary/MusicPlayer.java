package audioLibrary;

import static org.lwjgl.openal.AL11.*;

public class MusicPlayer extends Player{
    public MusicPlayer(float gain){
        super(gain);
        alSourcei(id, AL_LOOPING, AL_TRUE);
    }

    public void setLooping(boolean looping){
        if (looping)
            alSourcei(id, AL_LOOPING, AL_TRUE);
        else
            alSourcei(id, AL_LOOPING, AL_FALSE);
    }
}
