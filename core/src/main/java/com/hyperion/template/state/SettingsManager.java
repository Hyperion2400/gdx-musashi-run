package com.hyperion.template.state;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.hyperion.template.MyGdxGame;

public class SettingsManager {

    public static final String PATH = MyGdxGame.TITLE + "/settings";
    public static final String FULLSCREEN = "fullscreen";
    public static final String MUSIC = "music";
    public static final String SOUND = "sound";

    private static boolean musicEnabled;
    private static boolean soundEnabled;

    private SettingsManager() {
    }

    public static void create() {

        Preferences prefs = Gdx.app.getPreferences(SettingsManager.PATH);

        setFullScreen(prefs.getBoolean(SettingsManager.FULLSCREEN, false));
        setMusicEnabled(prefs.getBoolean(SettingsManager.MUSIC, true));
        setSoundEnabled(prefs.getBoolean(SettingsManager.SOUND, true));
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

    public static boolean isMusicEnabled() {
        return musicEnabled;
    }

    public static void setMusicEnabled(boolean musicEnabled) {
        SettingsManager.musicEnabled = musicEnabled;
        putBoolean(MUSIC, musicEnabled);
    }

    public static void setSoundEnabled(boolean soundEnabled) {
        SettingsManager.soundEnabled = soundEnabled;
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
