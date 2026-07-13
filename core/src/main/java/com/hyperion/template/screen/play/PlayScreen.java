package com.hyperion.template.screen.play;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.hyperion.template.assets.Paths;
import com.hyperion.template.audio.AudioManager;
import com.hyperion.template.screen.GameScreen;
import com.hyperion.template.screen.ScreenManager;
import com.hyperion.template.screen.menu.MainMenuScreen;
import com.hyperion.template.system.ActionSystem;
import com.hyperion.template.system.DifficultySystem;
import com.hyperion.template.system.ScoreSystem;
import com.hyperion.template.system.SpawnSystem;
import com.hyperion.template.ui.FontSize;
import com.hyperion.template.ui.UiFactory;
import com.hyperion.template.world.InfiniteBackground;
import com.hyperion.template.world.Warrior;

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
    private final DifficultySystem difficultySystem = new DifficultySystem(START_SPEED);
    private final ScoreSystem scoreSystem = new ScoreSystem(PLAYER_OFFSET_X, scoreLabel);
    private final SpawnSystem spawnSystem = new SpawnSystem(worldStage);

    private final Warrior player = new Warrior(
        Warrior.MARTIAL_HERO_1, 88, PLAYER_OFFSET_X, START_SPEED, 1, GROUND_Y, 1, 75, 4);

    private final Warrior[] enemies = new Warrior[6];

    private final InfiniteBackground background =
        new InfiniteBackground(Paths.WORLD_BACKGROUND, GROUND_Y, WORLD_WIDTH);

    private final InfiniteBackground ground =
        new InfiniteBackground(Paths.WORLD_MAP, 0, WORLD_WIDTH);

    private long deathTimestamp = 0; // used to measure time before going back to main menu after death
    private final int outroDuration = 3000;

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
                Warrior.MARTIAL_HERO_2, 85, 0, START_SPEED, -1, GROUND_Y, 1, 65, 2);
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
            "You can jump on top of enemies, but avoid their blades.", FontSize.SMALL);
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

            scoreSystem.updateScore(player.getX());
            player.setSpeedX(difficultySystem.calculateSpeed(scoreSystem.getScore()));

            actionSystem.update(delta, player, enemies);

            if (player.isDead()) {
                endGame();
            }
        } else {
            updateOutro();
        }

        updatePositions(delta);

        worldStage.draw();
        uiStage.draw();
    }

    private void endGame() {
        deathTimestamp = TimeUtils.millis();
        scoreSystem.updateHighScore();
        AudioManager.playSound(Paths.GAME_OVER);
    }

    private void updateOutro() {
        float outroProgression = 1f * (TimeUtils.millis() - deathTimestamp) / outroDuration;
        // turn screen red after death because it looks cool
        worldStage.getBatch().setColor(1f, 1f - outroProgression, 1f - outroProgression, 1f);
        if (outroProgression > 1) {
            ScreenManager.pushScreen(new MainMenuScreen());
        }
    }

    private void updatePositions(float delta) {
        worldStage.act(delta);

        float viewPortLeft = viewPortLeft();

        // move background in relation to ground for parallax effect
        background.moveBy(player.getSpeedX() / 4 * delta, 0);

        background.rearrange(viewPortLeft);
        ground.rearrange(viewPortLeft);

        spawnSystem.update(delta, enemies, viewPortLeft, viewPortRight(), scoreSystem.getScore());
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
