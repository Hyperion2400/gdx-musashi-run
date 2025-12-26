package com.hyperion.template;

import com.badlogic.gdx.Game;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class GdxGameTemplate extends Game {
    @Override
    public void create() {
        setScreen(new FirstScreen());
    }
}