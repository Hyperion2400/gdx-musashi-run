package com.hyperion.template.assets;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.BitmapFontLoader;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.TextureLoader;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

import java.util.List;

/**
 * Wraps AssetManager and loads assets.
 */
public class MyAssetManager {

    private static final AssetManager assetManager = new AssetManager();

    private static final String FONT_PATH = "font/patrick_hand_64.fnt";

    private static final List<String> textureFiles = List.of(
        "menu_background.png",
        "checkbox_off.png",
        "checkbox_on.png"
    );

    private static final List<String> musicFiles = List.of(
        "Universfield - Retro Energy.ogg"
    );

    private static final List<String> soundFiles = List.of(
        "Universfield - Button Click.ogg"
    );

    private MyAssetManager() {
    }

    public static void loadAssets() {

        loadTextures();
        loadFonts();

        musicFiles.forEach(path -> assetManager.load("music/" + path, Music.class));
        soundFiles.forEach(path -> assetManager.load("sound/" + path, Sound.class));

        assetManager.finishLoading();
    }

    private static void loadTextures() {
        // TEXTURES
        var textureParams = new TextureLoader.TextureParameter();
        textureParams.minFilter = Texture.TextureFilter.Linear;
        textureParams.magFilter = Texture.TextureFilter.Linear;
        textureFiles.forEach(path -> assetManager.load(
            "texture/" + path,
            Texture.class,
            textureParams
        ));
    }

    private static void loadFonts() {
        // set the loaders for the generator and the fonts themselves
        FileHandleResolver resolver = new InternalFileHandleResolver();
        assetManager.setLoader(BitmapFont.class, ".fnt", new BitmapFontLoader(resolver));

        assetManager.load(FONT_PATH, BitmapFont.class, fontParams());
    }

    private static BitmapFontLoader.BitmapFontParameter fontParams() {
        BitmapFontLoader.BitmapFontParameter parameter = new BitmapFontLoader.BitmapFontParameter();
        // prevents pixelation when scaling down
        parameter.minFilter = Texture.TextureFilter.Linear;
        // prevents pixelation when scaling up
        parameter.magFilter = Texture.TextureFilter.Linear;
        return parameter;
    }

    public static Texture getTexture(String path) {
        return assetManager.get(path, Texture.class);
    }

    public static BitmapFont getFont() {
        return assetManager.get(FONT_PATH, BitmapFont.class);
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
