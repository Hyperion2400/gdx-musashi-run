package com.hyperion.template;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.ScreenUtils;
import com.hyperion.template.assets.MyAssetManager;
import com.hyperion.template.screen.ScreenManager;
import com.hyperion.template.screen.menu.MainMenuScreen;

/**
 * {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms.
 */
public class MyGdxGame extends Game {

    public final static String VERSION = "v0.0.1";
    public final static String TITLE = "MyGdxGame";

    @Override
    public void create() {

        ScreenManager.INSTANCE.create();

        MyAssetManager.loadAssets();
        MyAssetManager.INSTANCE.finishLoading();

        ScreenManager.INSTANCE.transitionTo((new MainMenuScreen()));
    }

    @Override
    public void render() {

        ScreenUtils.clear(Color.BLACK);

        super.render();
        ScreenManager.INSTANCE.render();
    }

    @Override
    public void dispose() {
        super.dispose();
        MyAssetManager.INSTANCE.dispose();
        ScreenManager.INSTANCE.dispose();
    }

}
