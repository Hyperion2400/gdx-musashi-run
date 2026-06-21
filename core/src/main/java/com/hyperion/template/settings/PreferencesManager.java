package com.hyperion.template.settings;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.hyperion.template.MyGdxGame;

public class PreferencesManager {

    public static final String PATH = MyGdxGame.TITLE + "/settings";

    public static final String FULLSCREEN = "fullscreen";
    public static final String HIGH_SCORE = "high_score";
    public static final String MUSIC = "music";
    public static final String SOUND = "sound";

    private static boolean musicEnabled;
    private static boolean soundEnabled;

    private PreferencesManager() {
    }

    public static void create() {

        Preferences prefs = Gdx.app.getPreferences(PreferencesManager.PATH);

        setFullScreen(prefs.getBoolean(PreferencesManager.FULLSCREEN, false));
        setMusicEnabled(prefs.getBoolean(PreferencesManager.MUSIC, true));
        setSoundEnabled(prefs.getBoolean(PreferencesManager.SOUND, true));
    }

    public static void setFullScreen(boolean fullScreen) {

        if (Gdx.app.getType() != Application.ApplicationType.Desktop) {
            return;
        }

        putBoolean(FULLSCREEN, fullScreen);

        if (fullScreen) {
            Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
        } else {
            Gdx.graphics.setWindowedMode(MyGdxGame.WIDTH, MyGdxGame.HEIGHT);
        }
    }

    public static int getHighScore() {
        Preferences prefs = Gdx.app.getPreferences(PATH);
        return prefs.getInteger(HIGH_SCORE);
    }

    public static void setHighScore(int highScore) {
        Preferences prefs = Gdx.app.getPreferences(PATH);
        prefs.putInteger(HIGH_SCORE, highScore);
        prefs.flush();
    }

    public static boolean isMusicEnabled() {
        return musicEnabled;
    }

    public static void setMusicEnabled(boolean musicEnabled) {
        PreferencesManager.musicEnabled = musicEnabled;
        putBoolean(MUSIC, musicEnabled);
    }

    public static void setSoundEnabled(boolean soundEnabled) {
        PreferencesManager.soundEnabled = soundEnabled;
        putBoolean(SOUND, soundEnabled);
    }

    public static boolean isSoundEnabled() {
        return soundEnabled;
    }

    private static void putBoolean(String key, boolean value) {
        Preferences prefs = Gdx.app.getPreferences(PATH);
        prefs.putBoolean(key, value);
        prefs.flush();
    }
}
