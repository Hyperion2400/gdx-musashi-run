package com.hyperion.template.assets;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.BitmapFontLoader;
import com.badlogic.gdx.assets.loaders.TextureLoader;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

/**
 * Wraps AssetManager and loads assets.
 */
public class MyAssetManager {

    private static final AssetManager assetManager = new AssetManager();

    private MyAssetManager() {
    }

    public static void loadAssets() {

        loadTextures();

        assetManager.load(Paths.SPRITE_SHEET, TextureAtlas.class);

        loadFont();

        loadAudio();

        assetManager.finishLoading();
    }

    private static void loadTextures() {

        assetManager.load(Paths.MENU_BACKGROUND, Texture.class);
        assetManager.load(Paths.WORLD_BACKGROUND, Texture.class);
        assetManager.load(Paths.WORLD_MAP, Texture.class);

        // use TextureFilter.Linear to reduce scaling artifacts but not for pixel art
        var parameter = new TextureLoader.TextureParameter();
        parameter.minFilter = Texture.TextureFilter.Linear;
        parameter.magFilter = Texture.TextureFilter.Linear;

        assetManager.load(Paths.CHECKBOX_OFF, Texture.class, parameter);
        assetManager.load(Paths.CHECKBOX_ON, Texture.class, parameter);
    }

    private static void loadFont() {
        BitmapFontLoader.BitmapFontParameter parameter = new BitmapFontLoader.BitmapFontParameter();
        // prevents pixelation when scaling down
        parameter.minFilter = Texture.TextureFilter.Linear;
        // prevents pixelation when scaling up
        parameter.magFilter = Texture.TextureFilter.Linear;

        assetManager.load(Paths.FONT, BitmapFont.class, parameter);
    }

    private static void loadAudio() {
        assetManager.load(Paths.MENU_MUSIC, Music.class);
        assetManager.load(Paths.BUTTON_CLICK, Sound.class);
        assetManager.load(Paths.SWORD_SLASH, Sound.class);
    }

    public static Texture getTexture(String path) {
        return assetManager.get(path, Texture.class);
    }

    public static TextureAtlas getTextureAtlas(String path) {
        return assetManager.get(path, TextureAtlas.class);
    }

    public static BitmapFont getFont() {
        return assetManager.get(Paths.FONT, BitmapFont.class);
    }

    public static Music getMusic(String path) {
        return assetManager.get(path, Music.class);
    }

    public static Sound getSound(String path) {
        return assetManager.get(path, Sound.class);
    }

    public static void dispose() {
        assetManager.dispose();
    }

}
