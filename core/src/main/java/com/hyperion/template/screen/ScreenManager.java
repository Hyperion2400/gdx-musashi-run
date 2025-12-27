package com.hyperion.template.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.hyperion.template.MyGdxGame;

/**
 * Renders and transitions screens.
 */
public class ScreenManager {

    // private singleton instance that only this class operates on
    private static final ScreenManager INSTANCE = new ScreenManager();

    private MyGdxGame game;
    private ShapeRenderer shapeRenderer;

    private float transitionDuration = 0.8f;
    private boolean fadingIn = false;
    private boolean fadingOut = false;
    private float transitionOverlayAlpha = 1;

    private GameScreen nextScreen = null;

    private ScreenManager() {
    }

    public static void create(MyGdxGame game) {
        INSTANCE.game = game;

        INSTANCE.shapeRenderer = new ShapeRenderer();
        INSTANCE.shapeRenderer.setColor(Color.BLACK);
    }

    public static void pushScreen(GameScreen screen) {
        INSTANCE.transitionTo(screen, 0.8f);
    }

    private void transitionTo(GameScreen screen, float transitionDuration) {

        this.transitionDuration = transitionDuration;

        // update projection matrix in case window has been resized
        shapeRenderer.getProjectionMatrix()
            .setToOrtho2D(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        shapeRenderer.updateMatrices();

        if (fadingIn || fadingOut) {
            return;
        }

        if (game.getScreen() == null) {
            fadingIn = true;
            transitionOverlayAlpha = 1;
            game.setScreen(screen);
        } else {
            fadingOut = true;
            transitionOverlayAlpha = 0;

            if (game.getScreen() instanceof GameScreen gameScreen) {
                gameScreen.clearInputProcessor();
            }

            nextScreen = screen;
        }
    }


    public static void render() {

        if (!INSTANCE.fadingIn && !INSTANCE.fadingOut) {
            return;
        }

        INSTANCE.update();
        INSTANCE.renderOverlay();
    }

    private void update() {
        if (fadingIn) {
            transitionOverlayAlpha -= Gdx.graphics.getDeltaTime() / (transitionDuration / 2);

            if (transitionOverlayAlpha <= 0) {
                fadingIn = false;
                transitionOverlayAlpha = 0;

                if (game.getScreen() instanceof GameScreen gameScreen) {
                    gameScreen.setInputProcessor();
                }
            }
        } else if (fadingOut) {
            transitionOverlayAlpha += Gdx.graphics.getDeltaTime() / (transitionDuration / 2);

            if (transitionOverlayAlpha >= 1) {
                fadingIn = true;
                fadingOut = false;
                transitionOverlayAlpha = 1;

                Screen lastScreen = game.getScreen();
                game.setScreen(nextScreen);
                nextScreen = null;
                lastScreen.dispose();
            }
        }
    }

    private void renderOverlay() {
        Gdx.gl.glEnable(GL20.GL_BLEND);

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.getColor().a = transitionOverlayAlpha;
        shapeRenderer.rect(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        shapeRenderer.end();

        Gdx.gl.glDisable(GL20.GL_BLEND);
    }

    public static void dispose() {
        INSTANCE.shapeRenderer.dispose();
    }

}
