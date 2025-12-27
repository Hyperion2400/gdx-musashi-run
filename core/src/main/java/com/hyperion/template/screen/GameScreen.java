package com.hyperion.template.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;

public interface GameScreen extends Screen {

    void setInputProcessor();

    default void clearInputProcessor() {
        Gdx.input.setInputProcessor(null);
    }

    @Override
    default void show() {
    }

    @Override
    default void pause() {
    }

    @Override
    default void resume() {
    }

    @Override
    default void hide() {
    }

}
