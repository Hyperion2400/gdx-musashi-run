package com.hyperion.template.system;

public class DifficultySystem {

    private final int START_SPEED;

    public DifficultySystem(int startSpeed) {
        this.START_SPEED = startSpeed;
    }

    public float calculateSpeed(int score) {
        if (score < 100) {
            return START_SPEED;
        } else if (score > 100 && score < 800) {
            float progressionFactor = score / 100 * 0.1f; // add 1% speed every 100 points
            return START_SPEED * (1 + progressionFactor);
        } else {
            // terminal speed
            return START_SPEED * 1.8f;
        }
    }

}
