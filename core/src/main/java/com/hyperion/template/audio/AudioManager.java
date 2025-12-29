package com.hyperion.template.audio;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.hyperion.template.assets.MyAssetManager;
import com.hyperion.template.settings.SettingsManager;

public class AudioManager {

    private static final float MAX_MUSIC_VOLUME = 1f;

    private static String currentMusicPath;
    private static Music music;

    private AudioManager() {
    }

    public static void playMusic(String musicPath) {
        if (!SettingsManager.isMusicEnabled()) {
            return;
        }

        if (musicPath == null) {
            return;
        }

        if (!musicPath.equals(currentMusicPath)) {
            stopMusic();
            currentMusicPath = musicPath;
            music = MyAssetManager.getMusic(musicPath);
            music.setLooping(true);
        }

        music.setVolume(MAX_MUSIC_VOLUME);

        if (!music.isPlaying()) {
            music.play();
        }
    }

    public static String getCurrentMusicPath() {
        return currentMusicPath;
    }

    public static void setVolume(float volume) {
        if (music != null) {
            music.setVolume(Math.max(volume * MAX_MUSIC_VOLUME, MAX_MUSIC_VOLUME));
        }
    }

    public static void stopMusic() {
        if (music != null) {
            music.stop();
        }
    }

    public static void playSound(String soundPath) {
        if (SettingsManager.isSoundEnabled()) {
            Sound sound = MyAssetManager.getSound(soundPath);
            sound.play();
        }
    }
}
