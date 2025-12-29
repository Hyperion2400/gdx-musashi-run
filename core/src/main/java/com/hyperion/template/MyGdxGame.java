package com.hyperion.template;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.ScreenUtils;
import com.hyperion.template.assets.MyAssetManager;
import com.hyperion.template.screen.ScreenManager;
import com.hyperion.template.screen.menu.MainMenuScreen;
import com.hyperion.template.state.SettingsManager;

/**
 * {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms.
 */
public class MyGdxGame extends Game {

    public static final String TITLE = "MyGdxGame";

    public static final int WIDTH = 1280;
    public static final int HEIGHT = 720;

    private static String version;

    @Override
    public void create() {

        loadProjectVersion();

        SettingsManager.create();
        ScreenManager.create(this);

        MyAssetManager.loadAssets();

        ScreenManager.pushScreen((new MainMenuScreen()));
    }

    private void loadProjectVersion() {

        // needs project_version file to be created in build.gradle
        if (Gdx.files.internal("project_version").exists()) {
            version = Gdx.files.internal("project_version").readString();
            Gdx.graphics.setTitle(TITLE + " " + version);
        }
    }

    @Override
    public void render() {

        ScreenUtils.clear(Color.BLACK);

        super.render();
        ScreenManager.render();
    }

    @Override
    public void dispose() {
        super.dispose();
        MyAssetManager.dispose();
        ScreenManager.dispose();
    }

    public static String getVersion() {
        return version;
    }
}
