package com.hyperion.template.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.hyperion.template.MyGdxGame;
import com.hyperion.template.audio.AudioManager;

/**
 * Renders and transitions screens.
 */
public class ScreenManager {

    private static MyGdxGame game;
    private static ShapeRenderer shapeRenderer;

    private static float transitionDuration = 0.8f;
    private static boolean fadingIn = false;
    private static boolean fadingOut = false;
    private static float overlayOpacity = 1;

    private static GameScreen nextScreen = null;

    private ScreenManager() {
    }

    public static void create(MyGdxGame game) {
        ScreenManager.game = game;

        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setColor(Color.BLACK);
    }

    public static void pushScreen(GameScreen screen) {
        transitionTo(screen, 0.6f);
    }

    private static void transitionTo(GameScreen screen, float transitionDuration) {

        ScreenManager.transitionDuration = transitionDuration;

        // update projection matrix in case window has been resized
        shapeRenderer.getProjectionMatrix()
            .setToOrtho2D(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        shapeRenderer.updateMatrices();

        if (fadingIn || fadingOut) {
            return;
        }

        if (game.getScreen() == null) {
            fadingIn = true;
            overlayOpacity = 1;
            game.setScreen(screen);
        } else {
            fadingOut = true;
            overlayOpacity = 0;

            if (game.getScreen() instanceof GameScreen gameScreen) {
                gameScreen.clearInputProcessor();
            }

            nextScreen = screen;
        }
    }


    public static void render() {

        if (!fadingIn && !fadingOut) {
            return;
        }

        update();
        renderOverlay();
    }

    private static void update() {
        if (fadingIn) {
            overlayOpacity -= Gdx.graphics.getDeltaTime() / (transitionDuration / 2);

            if (overlayOpacity <= 0) {
                fadingIn = false;
                overlayOpacity = 0;

                if (game.getScreen() instanceof GameScreen gameScreen) {
                    gameScreen.setInputProcessor();
                }
            }
        } else if (fadingOut) {
            overlayOpacity += Gdx.graphics.getDeltaTime() / (transitionDuration / 2);


            if (overlayOpacity < 1) {
                // if next screen has a music path and it differs from current, fade out music as well
                if (nextScreen.getMusicPath() != null
                    && !nextScreen.getMusicPath().equals(AudioManager.getCurrentMusicPath())) {
                    AudioManager.setVolume(1 - overlayOpacity);
                }
            } else {
                fadingIn = true;
                fadingOut = false;
                overlayOpacity = 1;

                Screen lastScreen = game.getScreen();
                game.setScreen(nextScreen);
                nextScreen = null;
                lastScreen.dispose();
            }
        }
    }

    private static void renderOverlay() {
        Gdx.gl.glEnable(GL20.GL_BLEND);

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.getColor().a = overlayOpacity;
        shapeRenderer.rect(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        shapeRenderer.end();

        Gdx.gl.glDisable(GL20.GL_BLEND);
    }

    public static void dispose() {
        shapeRenderer.dispose();
    }

}
