package com.hyperion.template.screen.play;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.ScreenUtils;
import com.hyperion.template.screen.GameScreen;
import com.hyperion.template.audio.AudioManager;

public class PlayScreen implements GameScreen {

    public PlayScreen() {
    }

    @Override
    public void show() {
        AudioManager.playMusic(getMusicPath());
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

    @Override
    public String getMusicPath() {
        return null;
    }

}
