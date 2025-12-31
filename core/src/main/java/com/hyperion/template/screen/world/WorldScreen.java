package com.hyperion.template.screen.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
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
    private static final int START_SPEED = 100;

    private final SpriteBatch batch = new SpriteBatch();

    private final Camera cam = new OrthographicCamera();

    private final Stage stage = new Stage(new FitViewport(WORLD_WIDTH, WORLD_HEIGHT, cam));

    private final Warrior player =
        new Warrior(Paths.WARRIOR_SPRITE_SHEET, 85, 78, PLAYER_OFFSET_X, START_SPEED, GROUND_Y);

    private final Warrior[] enemies = new Warrior[10];
    private int nextEnemyIndex = 0;

    private float nextEnemyCountDown = 1f;

    private final InfiniteBackground background =
        new InfiniteBackground(Paths.WORLD_BACKGROUND, GROUND_Y, WORLD_WIDTH);

    private final InfiniteBackground ground =
        new InfiniteBackground(Paths.WORLD_MAP, 0, WORLD_WIDTH);

    public WorldScreen() {
        stage.addActor(background);
        stage.addActor(ground);
        stage.addActor(player);
        stage.setDebugAll(true);

        for (int i = 0; i < enemies.length; i++) {
            // initialize pool of reusable enemy warriors to reduce object creations
            enemies[i] = new Warrior(Paths.ENEMY_SPRITE_SHEET, 85, 72, 0, 0, GROUND_Y);
        }
    }

    @Override
    public void render(float delta) {

        if (delta > 0.1f) {
            delta = 0.1f;
        }

        if (Gdx.input.isTouched()) {
            player.jump(delta);
        }

        updateEnemies(delta);

        stage.act(delta);

        float viewPortLeft = viewPortLeft();

        // move background with half of player speed for parallax effect
        background.moveBy(player.getSpeedX() / 2 * delta, 0);

        updateCamera(viewPortLeft);
        rearrangeBackgrounds(viewPortLeft);

        stage.draw();
    }

    private void updateCamera(float viewPortLeft) {
        cam.position.set(viewPortLeft + WORLD_WIDTH / 2, WORLD_HEIGHT / 2, 0);
        cam.update();
    }

    private void rearrangeBackgrounds(float cameraLeft) {
        background.rearrange(cameraLeft);
        ground.rearrange(cameraLeft);
    }

    private void updateEnemies(float delta) {

        nextEnemyCountDown -= delta;

        sendEnemy();

        for (Warrior enemy : enemies) {
            if (enemy.hasParent() && enemy.getRight() < viewPortLeft()) {
                enemy.remove();
            }
        }
    }

    private void sendEnemy() {

        if (nextEnemyCountDown > 0) {
            return;
        }

        Warrior nextEnemy = enemies[nextEnemyIndex];

        if (nextEnemy.hasParent()) {
            // if enemy has a parent it's already on stage and not offscreen yet
            return;
        }

        nextEnemy.setX(viewPortRight() + 100);
        nextEnemy.setSpeedX(-START_SPEED);

        stage.addActor(nextEnemy);

        nextEnemyIndex++;

        if (nextEnemyIndex > enemies.length - 1) {
            nextEnemyIndex = 0;
        }

        resetEnemyCountDown();
    }

    private float viewPortLeft() {
        return player.getX() - PLAYER_OFFSET_X;
    }

    private float viewPortRight() {
        return player.getX() - PLAYER_OFFSET_X + WORLD_WIDTH;
    }

    private void resetEnemyCountDown() {
        nextEnemyCountDown = MathUtils.random(0.3f, 2f);
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
