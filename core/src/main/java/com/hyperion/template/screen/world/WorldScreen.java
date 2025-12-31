package com.hyperion.template.screen.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.hyperion.template.assets.Paths;
import com.hyperion.template.audio.AudioManager;
import com.hyperion.template.screen.GameScreen;
import com.hyperion.template.world.InfiniteBackground;
import com.hyperion.template.world.Warrior;

public class WorldScreen implements GameScreen {

    private static final int WORLD_WIDTH = 640;
    private static final int WORLD_HEIGHT = 360;
    private static final int PLAYER_OFFSET_X = 128;
    private static final int GROUND_Y = 96;
    private static final int START_SPEED = 200;

    private final SpriteBatch batch = new SpriteBatch();

    private final Camera cam = new OrthographicCamera();

    private final Stage stage = new Stage(new FitViewport(WORLD_WIDTH, WORLD_HEIGHT, cam));

    private final Warrior player = new Warrior(PLAYER_OFFSET_X, START_SPEED, GROUND_Y, false);

    private final InfiniteBackground background =
        new InfiniteBackground(Paths.WORLD_BACKGROUND, GROUND_Y, WORLD_WIDTH);

    private final InfiniteBackground ground =
        new InfiniteBackground(Paths.WORLD_MAP, 0, WORLD_WIDTH);

    public WorldScreen() {
        stage.addActor(background);
        stage.addActor(ground);
        stage.addActor(player);
    }

    @Override
    public void render(float delta) {

        if (Gdx.input.isTouched()) {
            player.jump(delta);
        }

        stage.act(delta);

        float cameraLeft = player.getX() - PLAYER_OFFSET_X;

        // move background with half of player speed for parallax effect
        background.moveBy(player.getSpeedX() / 2 * delta, 0);

        updateCamera(cameraLeft);
        rearrangeBackgrounds(cameraLeft);

        stage.draw();

    }

    private void updateCamera(float cameraLeft) {
        cam.position.set(cameraLeft + WORLD_WIDTH / 2, WORLD_HEIGHT / 2, 0);
        cam.update();
    }

    private void rearrangeBackgrounds(float cameraLeft) {
        background.rearrange(cameraLeft);
        ground.rearrange(cameraLeft);
    }

    @Override
    public void show() {
        AudioManager.playMusic(getMusicPath());
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void dispose() {
        batch.dispose();
    }

    @Override
    public void setInputProcessor() {
    }

    @Override
    public String getMusicPath() {
        return null;
    }

}
