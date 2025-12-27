package com.hyperion.template.teavm;

import com.github.xpenatan.gdx.backends.teavm.TeaApplicationConfiguration;
import com.github.xpenatan.gdx.backends.teavm.TeaApplication;
import com.hyperion.template.MyGdxGame;

/**
 * Launches the TeaVM/HTML application.
 */
public class TeaVMLauncher {
    public static void main(String[] args) {
        TeaApplicationConfiguration config = new TeaApplicationConfiguration("canvas");
        //// If width and height are each greater than 0, then the app will use a fixed size.
        //config.width = 640;
        //config.height = 480;
        //// If width and height are both 0, then the app will use all available space.
        config.width = 1280;
        config.height = 720;
        new TeaApplication(new MyGdxGame(), config);
    }
}
