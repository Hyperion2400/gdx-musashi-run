package com.hyperion.template.assets;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.BitmapFontLoader;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.TextureLoader;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

import java.util.List;

public class MyAssetManager {

    // private singleton instance that only this class operates on directly
    private static final AssetManager INSTANCE = new AssetManager();

    private static final String FONT_PATH = "ui/patrick_hand_64.fnt";

    private static final List<String> textureFiles = List.of(
        "main_menu_background.png"
    );

    private MyAssetManager() {
    }

    public static void loadAssets() {

        loadTextures();
        loadFonts();

        INSTANCE.finishLoading();
    }

    private static void loadTextures() {
        // TEXTURES
        var textureParams = new TextureLoader.TextureParameter();
        textureParams.minFilter = Texture.TextureFilter.Linear;
        textureParams.magFilter = Texture.TextureFilter.Linear;
        textureFiles.forEach(path -> INSTANCE.load(
            "texture/" + path,
            Texture.class,
            textureParams
        ));
    }

    private static void loadFonts() {
        // set the loaders for the generator and the fonts themselves
        FileHandleResolver resolver = new InternalFileHandleResolver();
        INSTANCE.setLoader(BitmapFont.class, ".fnt", new BitmapFontLoader(resolver));

        INSTANCE.load(FONT_PATH, BitmapFont.class, fontParams());
    }

    private static BitmapFontLoader.BitmapFontParameter fontParams() {
        BitmapFontLoader.BitmapFontParameter parameter = new BitmapFontLoader.BitmapFontParameter();
        parameter.minFilter = Texture.TextureFilter.Linear;
        return parameter;
    }

    public static Texture getTexture(String fileName) {
        return INSTANCE.get(fileName, Texture.class);
    }

    public static BitmapFont getFont() {
        return INSTANCE.get(FONT_PATH, BitmapFont.class);
    }

    public static void dispose() {
        INSTANCE.dispose();
    }
}
