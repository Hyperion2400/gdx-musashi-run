package com.hyperion.template.teavm;

import com.github.xpenatan.gdx.backends.teavm.TeaApplication;
import com.github.xpenatan.gdx.backends.teavm.TeaApplicationConfiguration;
import com.hyperion.template.MyGdxGame;

/**
 * Launches the TeaVM/HTML application.
 */
public class TeaVMLauncher {
    public static void main(String[] args) {
        TeaApplicationConfiguration config = new TeaApplicationConfiguration("canvas");
        //// If width and height are each greater than 0, then the app will use a fixed size.
        // config.width = MyGdxGame.WIDTH;
        // config.height = MyGdxGame.HEIGHT;
        //// If width and height are both 0, then the app will use all available space.
        // I choose 0, so that the game takes up all available space in fullscreen mode.
        // It's hosted on itch.io where I can set the size to 1280x720 when it's not in fullscreen.
        config.width = 0;
        config.height = 0;
        new TeaApplication(new MyGdxGame(), config);
    }
}
