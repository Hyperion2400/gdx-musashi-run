package com.hyperion.template.screen.play;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.hyperion.template.assets.Paths;
import com.hyperion.template.audio.AudioManager;
import com.hyperion.template.entity.Warrior;
import com.hyperion.template.screen.GameScreen;
import com.hyperion.template.screen.ScreenManager;
import com.hyperion.template.screen.menu.MainMenuScreen;
import com.hyperion.template.system.ActionSystem;
import com.hyperion.template.system.SpawnSystem;
import com.hyperion.template.world.InfiniteBackground;

public class PlayScreen implements GameScreen {

    private static final int WORLD_WIDTH = 640;
    private static final int WORLD_HEIGHT = 360;
    private static final int PLAYER_OFFSET_X = 128;
    private static final int GROUND_Y = 96;
    private static final int START_SPEED = 100;

    private final SpriteBatch batch = new SpriteBatch();

    private final Camera cam = new OrthographicCamera();

    private final Stage stage = new Stage(new FitViewport(WORLD_WIDTH, WORLD_HEIGHT, cam));

    private final ActionSystem actionSystem = new ActionSystem();
    private final SpawnSystem spawnSystem = new SpawnSystem(stage);

    private final Warrior player = new Warrior(
        Warrior.MARTIAL_HERO_1, 88, PLAYER_OFFSET_X, START_SPEED, 1, GROUND_Y, 1, 60, 4);

    private final Warrior[] enemies = new Warrior[6];

    private final InfiniteBackground background =
        new InfiniteBackground(Paths.WORLD_BACKGROUND, GROUND_Y, WORLD_WIDTH);

    private final InfiniteBackground ground =
        new InfiniteBackground(Paths.WORLD_MAP, 0, WORLD_WIDTH);

    private long deathTimestamp = 0; // used to measure time before going back to main menu after death

    public PlayScreen() {
        stage.addActor(background);
        stage.addActor(ground);
        stage.addActor(player);

        for (int i = 0; i < enemies.length; i++) {
            // initialize pool of reusable enemy warriors to reduce object creations
            enemies[i] = new Warrior(
                Warrior.MARTIAL_HERO_2, 85, 0, START_SPEED, -1, GROUND_Y, 1, 50, 2);
        }
    }

    @Override
    public void render(float delta) {

        if (delta > 0.1f) {
            delta = 0.1f;
        }

        if (!player.isDead()) {

            actionSystem.update(delta, player, enemies);

            if (player.isDead()) {
                deathTimestamp = TimeUtils.millis();
            }
        } else {
            if (TimeUtils.millis() - deathTimestamp > 3000) {
                ScreenManager.pushScreen(new MainMenuScreen());
            }
        }

        updatePositions(delta);

        stage.draw();
    }

    private void updatePositions(float delta) {
        stage.act(delta);

        float viewPortLeft = viewPortLeft();

        // move background with half of player speed for parallax effect
        background.moveBy(player.getSpeedX() / 2 * delta, 0);

        background.rearrange(viewPortLeft);
        ground.rearrange(viewPortLeft);

        spawnSystem.update(delta, enemies, viewPortLeft, viewPortRight());
        updateCamera(viewPortLeft);
    }

    private float viewPortLeft() {
        return player.getX() - PLAYER_OFFSET_X;
    }

    private float viewPortRight() {
        return player.getX() - PLAYER_OFFSET_X + WORLD_WIDTH;
    }

    private void updateCamera(float viewPortLeft) {
        cam.position.set(viewPortLeft + WORLD_WIDTH / 2, WORLD_HEIGHT / 2, 0);
        cam.update();
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
