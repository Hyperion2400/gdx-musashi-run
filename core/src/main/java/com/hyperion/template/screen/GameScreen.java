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

    /**
     * @return The path of the music resource that is supposed to be played in this screen.
     * If null, then the current music is continued to be played
     */
    default String getMusicPath() {
        return null;
    }

}
