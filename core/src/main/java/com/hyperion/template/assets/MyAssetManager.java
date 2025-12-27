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

    // singleton instance
    public static final AssetManager INSTANCE = new AssetManager();

    private static final List<String> textureFiles = List.of(
        "main_menu_background.png"
    );

    private MyAssetManager() {
    }

    public static void loadAssets() {

        // TEXTURES
        var textureParams = new TextureLoader.TextureParameter();
        textureParams.minFilter = Texture.TextureFilter.Linear;
        textureParams.magFilter = Texture.TextureFilter.Linear;
        textureFiles.forEach(path -> INSTANCE.load(
            "texture/" + path,
            Texture.class,
            textureParams
        ));

        loadFonts();
    }

    private static void loadFonts() {
        // set the loaders for the generator and the fonts themselves
        FileHandleResolver resolver = new InternalFileHandleResolver();
        INSTANCE.setLoader(BitmapFont.class, ".fnt", new BitmapFontLoader(resolver));

        INSTANCE.load("ui/patrick_hand_64.fnt", BitmapFont.class, fontParams());
    }

    private static BitmapFontLoader.BitmapFontParameter fontParams() {
        BitmapFontLoader.BitmapFontParameter parameter = new BitmapFontLoader.BitmapFontParameter();
        parameter.minFilter = Texture.TextureFilter.Linear;
        return parameter;
    }

}
