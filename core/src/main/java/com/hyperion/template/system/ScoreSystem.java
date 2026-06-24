package com.hyperion.template.system;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.hyperion.template.settings.PreferencesManager;

public class ScoreSystem {

    private int score = 0;
    private final int highScore = PreferencesManager.getHighScore();
    private final int PLAYER_OFFSET_X;
    private final Label scoreLabel;

    public ScoreSystem(int playerOffsetX, Label scoreLabel) {
        this.PLAYER_OFFSET_X = playerOffsetX;
        this.scoreLabel = scoreLabel;
    }

    public void updateScore(float playerX) {

        score = (int) ((playerX - PLAYER_OFFSET_X) / 10); // just based on player position

        scoreLabel.setText(String.valueOf(score));
        if (isHighScore()) {
            scoreLabel.setColor(Color.GOLD); // change color to signify new high score
        }
    }

    public boolean isHighScore() {
        return score > highScore;
    }

    public void updateHighScore() {
        if (isHighScore()) {
            PreferencesManager.setHighScore(score);
        }
    }

    public int getScore() {
        return score;
    }
}
