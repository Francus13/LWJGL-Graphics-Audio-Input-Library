package audio;

import static org.lwjgl.openal.AL10.*;
import static org.lwjgl.openal.AL11.*;

public class MusicPlayer extends Player{
    public MusicPlayer(float gain){
        super(gain);
        alSourcei(id, AL_LOOPING, AL_TRUE);
    }
}
