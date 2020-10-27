package audio;

import org.lwjgl.BufferUtils;
import org.lwjgl.system.libc.LibCStdlib;

import java.nio.IntBuffer;
import java.nio.ShortBuffer;

import static org.lwjgl.openal.AL11.*;
import static org.lwjgl.stb.STBVorbis.stb_vorbis_decode_filename;

public class Sound {
    public static final Sound testSound = new Sound("Shotgun Shot");

    private final int id;

    public Sound(String fileName){
        IntBuffer channels = BufferUtils.createIntBuffer(1);
        IntBuffer sampleRate = BufferUtils.createIntBuffer(1);

        ShortBuffer data = stb_vorbis_decode_filename("res/Audio/Sounds/" + fileName + ".ogg", channels, sampleRate);

        if (data == null){
            System.err.println("Could not load sound data: " + fileName);
            System.exit(1);
        }

        int c = channels.get();
        int format = -1;
        if (c == 2)
            format = AL_FORMAT_STEREO16;
        else if (c == 1)
            format = AL_FORMAT_MONO16;

        id = alGenBuffers();
        alBufferData(id, format, data, sampleRate.get());

        if (alGetError() != AL_NO_ERROR){
            System.err.println("Failure to retrieve audio data or create sound: " + fileName);
            System.exit(1);
        }

        LibCStdlib.free(data);
    }

    public int id() {return id;}
}
