package audioLibrary;

import org.lwjgl.openal.AL;
import org.lwjgl.openal.ALC;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.lwjgl.openal.AL11.*;
import static org.lwjgl.openal.ALC11.*;
import static org.lwjgl.system.MemoryUtil.NULL;
import static runner.Driver.MUSIC_FADE_TIME;
import static runner.Driver.getTime;


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
                    if(nextMusic != null){
                        musicPlayer.stop();
                        musicPlayer.play(nextMusic.id());
                    }
                    else
                        musicPlayer.stop();

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

    public static MusicPlayer play(Music music, boolean looping){
        update = true;
        hasToUpdateMusic = true;
        nextMusic = music;
        startTime = getTime();
        musicPlayer.setLooping(looping);

        return musicPlayer;
    }

    public static void stopMusic(){
        update = true;
        hasToUpdateMusic = true;
        nextMusic = null;
        startTime = getTime();
    }

    public static Player play(Sound sound){
        Player player = getPlayer();

        player.play(sound.id());
        return player;
    }

    public static Player play(Sound sound, int numTimes){
        Player player = getPlayer();

        int[] soundIds = new int[numTimes];
        Arrays.fill(soundIds, sound.id());

        player.play(soundIds);
        return player;
    }

    public static Player play(Sound[] sounds){
        Player player = getPlayer();

        int[] soundIds = new int[sounds.length];
        for (int i = 0; i < sounds.length; i++)
            soundIds[i] = sounds[i].id();

        player.play(soundIds);
        return player;
    }

    private static Player getPlayer(){
        Player player = null;
        for (Player p: players)
            if (p.isStopped()) {
                player = p;
                break;
            }
        if (player == null){
            player = new Player(soundGain);
            players.add(player);
        }
        return player;
    }

    public static void pauseAudio(){
        musicPlayer.pause();
        for (Player player: players)
            player.pause();
    }

    public static void playPausedAudio(){
        musicPlayer.start();
        for (Player player: players)
            player.start();
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
        musicPlayer.free();
        for (Player player: players)
            player.free();

        alcDestroyContext(context);
        alcCloseDevice(device);
    }
}
