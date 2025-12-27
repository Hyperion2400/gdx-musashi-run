package com.hyperion.template.screen.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.ScreenUtils;
import com.hyperion.template.screen.GameScreen;

public class GameLevelScreen implements GameScreen {

    public GameLevelScreen() {
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.RED);
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void dispose() {
    }

    @Override
    public void setInputProcessor() {
    }

}
