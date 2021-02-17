package audio;

import org.lwjgl.openal.*;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.lwjgl.openal.AL10.*;
import static org.lwjgl.openal.ALC10.*;
import static org.lwjgl.system.MemoryUtil.NULL;
import static runner.Driver.getTime;
import static runner.Driver.MUSIC_FADE_TIME;

public class AudioManager {
    public static final int VOLUME_SCALE = 100;

    private static final List<Player> players = new ArrayList<>();

    private static MusicPlayer musicPlayer;

    private static boolean update = false;
    private static double startTime;
    private static Music nextMusic;
    private static boolean hasToUpdateMusic;


    private static long device;
    private static long context;
    
    private static float masterGain = 1;
    private static float musicGain = 1;
    private static float soundGain = 1;

    public static void initAudioManager(){
        device = alcOpenDevice((ByteBuffer) null);
        if (device == NULL){
            System.err.println("Failed to connect to a default device");
            System.exit(1);
        }

        context = alcCreateContext(device, (IntBuffer) null);
        if (context == NULL){
            System.err.println("Failed to create an OpenAL context with the default device");
            System.exit(1);
        }
        alcMakeContextCurrent(context);

        AL.createCapabilities(ALC.createCapabilities(device));

        alGetError();

        musicPlayer = new MusicPlayer(musicGain);
    }

    public static void updateMusic(){
        if (update) {
            float timeElapsed = (float) (getTime() - startTime);

            if (timeElapsed < MUSIC_FADE_TIME)
                musicPlayer.setGain((1 - timeElapsed / MUSIC_FADE_TIME) * musicGain);
            else {
                if (hasToUpdateMusic){
                    if (nextMusic != null)
                        musicPlayer.play(nextMusic.id());
                    hasToUpdateMusic = false;
                }

                if (timeElapsed < MUSIC_FADE_TIME * 2)
                    musicPlayer.setGain((timeElapsed / MUSIC_FADE_TIME - 1) * musicGain);
                else {
                    musicPlayer.setGain(musicGain);
                    update = false;
                }
            }
        }
    }

    public static void play(Music music){
        update = true;
        hasToUpdateMusic = true;
        nextMusic = music;
        startTime = getTime();
    }

    public static void play(Sound sound){
        Player player = null;
        for (Player p: players)
            if (p.isNotPlaying()) {
                player = p;
                break;
            }
        if (player == null){
            player = new Player(soundGain);
            players.add(player);
        }

        player.play(sound.id());
    }

    public static void play(Sound sound, int numTimes){
        Player player = null;
        for (Player p: players)
            if (p.isNotPlaying()) {
                player = p;
                break;
            }
        if (player == null){
            player = new Player(soundGain);
            players.add(player);
        }

        int[] soundIds = new int[numTimes];
        Arrays.fill(soundIds, sound.id());

        player.play(soundIds);
    }

    public static void play(Sound[] sounds){
        Player player = null;
        for (Player p: players)
            if (p.isNotPlaying()) {
                player = p;
                break;
            }
        if (player == null){
            player = new Player(soundGain);
            players.add(player);
        }

        int[] soundIds = new int[sounds.length];
        for (int i = 0; i < sounds.length; i++)
            soundIds[i] = sounds[i].id();

        player.play(soundIds);
    }
    
    public static int getMasterVolume() {return (int) (masterGain * VOLUME_SCALE);}
    public static int getMusicVolume() {return (int) (musicGain * VOLUME_SCALE);}
    public static int getSoundVolume() {return (int) (soundGain * VOLUME_SCALE);}

    public static void setMasterVolume(int volume) {masterGain = (float) volume / VOLUME_SCALE; alListenerf(AL_GAIN, masterGain);}

    public static void setSoundVolume(int volume){
        soundGain = (float) volume / VOLUME_SCALE;

        for (Player player: players)
            player.setGain(soundGain);
    }

    public static void setMusicVolume(int volume){
        musicGain = (float) volume / VOLUME_SCALE;
        musicPlayer.setGain(musicGain);
    }

    public static void free(Sound sound) {alDeleteBuffers(sound.id());}

    public static void free(Music music) {alDeleteBuffers(music.id());}

    public static void terminateAudioManager(){
        for (Player player: players)
            player.free();

        alcDestroyContext(context);
        alcCloseDevice(device);
    }
}
