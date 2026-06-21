package com.hyperion.template.screen.play;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.hyperion.template.assets.Paths;
import com.hyperion.template.audio.AudioManager;
import com.hyperion.template.entity.Warrior;
import com.hyperion.template.screen.GameScreen;
import com.hyperion.template.screen.ScreenManager;
import com.hyperion.template.screen.menu.MainMenuScreen;
import com.hyperion.template.settings.PreferencesManager;
import com.hyperion.template.system.ActionSystem;
import com.hyperion.template.system.SpawnSystem;
import com.hyperion.template.ui.FontSize;
import com.hyperion.template.ui.UiFactory;
import com.hyperion.template.world.InfiniteBackground;

public class PlayScreen implements GameScreen {

    private static final int WORLD_WIDTH = 640;
    private static final int WORLD_HEIGHT = 360;
    private static final int PLAYER_OFFSET_X = 128;
    private static final int GROUND_Y = 96;
    private static final int START_SPEED = 100;

    private final SpriteBatch batch = new SpriteBatch();

    private final Camera cam = new OrthographicCamera();

    private final Label scoreLabel = scoreLabel();
    private final Stage worldStage = new Stage(new FitViewport(WORLD_WIDTH, WORLD_HEIGHT, cam));
    private final Stage uiStage =
        new Stage(new FitViewport(WORLD_WIDTH, WORLD_HEIGHT, new OrthographicCamera()));

    private final ActionSystem actionSystem = new ActionSystem();
    private final SpawnSystem spawnSystem = new SpawnSystem(worldStage);

    private final Warrior player = new Warrior(
        Warrior.MARTIAL_HERO_1, 88, PLAYER_OFFSET_X, START_SPEED, 1, GROUND_Y, 1, 60, 4);

    private final Warrior[] enemies = new Warrior[6];

    private final InfiniteBackground background =
        new InfiniteBackground(Paths.WORLD_BACKGROUND, GROUND_Y, WORLD_WIDTH);

    private final InfiniteBackground ground =
        new InfiniteBackground(Paths.WORLD_MAP, 0, WORLD_WIDTH);

    private int score;
    private final int highScore = PreferencesManager.getHighScore();
    private long deathTimestamp = 0; // used to measure time before going back to main menu after death

    public PlayScreen() {
        worldStage.addActor(background);
        worldStage.addActor(ground);
        worldStage.addActor(player);
        worldStage.addActor(hintLabel1());
        worldStage.addActor(hintLabel2());

        uiStage.addActor(scoreLabel);

        for (int i = 0; i < enemies.length; i++) {
            // initialize pool of reusable enemy warriors to reduce object creations
            enemies[i] = new Warrior(
                Warrior.MARTIAL_HERO_2, 85, 0, START_SPEED, -1, GROUND_Y, 1, 50, 2);
        }
    }

    private Label scoreLabel() {
        Label label = UiFactory.label("0", FontSize.SMALL);
        label.setPosition(16, WORLD_HEIGHT - 40);
        return label;
    }

    private Label hintLabel1() {
        Label label = UiFactory.label("Tap to jump. Hold to jump higher.", FontSize.SMALL);
        label.setPosition(WORLD_WIDTH / 2f, 32);
        label.getColor().a = 0.8f;
        return label;
    }

    private Label hintLabel2() {
        Label label = UiFactory.label(
            "You can touch enemies, but avoid their blades.",
            FontSize.SMALL
        );
        label.setPosition(3 * WORLD_WIDTH, 32);
        label.getColor().a = 0.8f;
        return label;
    }

    @Override
    public void render(float delta) {

        if (delta > 0.1f) {
            delta = 0.1f;
        }

        if (!player.isDead()) {

            updateScore();
            updateDifficulty();

            actionSystem.update(delta, player, enemies);

            if (player.isDead()) {
                deathTimestamp = TimeUtils.millis();
                if (score > highScore) {
                    PreferencesManager.setHighScore(score);
                }
            }
        } else {
            if (TimeUtils.millis() - deathTimestamp > 3000) {
                ScreenManager.pushScreen(new MainMenuScreen());
            }
        }

        updatePositions(delta);

        worldStage.draw();
        uiStage.draw();
    }

    private void updatePositions(float delta) {
        worldStage.act(delta);

        float viewPortLeft = viewPortLeft();

        // move background with half of player speed for parallax effect
        background.moveBy(player.getSpeedX() / 2 * delta, 0);

        background.rearrange(viewPortLeft);
        ground.rearrange(viewPortLeft);

        spawnSystem.update(delta, enemies, viewPortLeft, viewPortRight(), score);
        updateCamera(viewPortLeft);
    }

    private void updateScore() {
        score = (int) (player.getX() - PLAYER_OFFSET_X) / 10; // just based on player position
        scoreLabel.setText(String.valueOf(score));
        if (score > highScore) {
            scoreLabel.setColor(Color.GOLD); // change color to signify new high score
        }
    }

    private void updateDifficulty() {
        if (score < 100) {
            player.setSpeedX(START_SPEED); // start speed
        } else if (score > 100 && score < 400) {
            float progressionFactor = score / 100 * 0.1f; // add 1% speed every 100 points
            player.setSpeedX(START_SPEED * (1 + progressionFactor));
        } else if (score > 500) {
            // terminal speed. going beyond 1.5 makes me dizzy.
            // rather use max number of allowed short spawn intervals in a row to increase difficulty
            player.setSpeedX(START_SPEED * 1.5f);
        }
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
