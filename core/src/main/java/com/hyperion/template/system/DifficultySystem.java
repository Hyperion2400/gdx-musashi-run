package com.hyperion.template.system;

public class DifficultySystem {

    private final int START_SPEED;

    public DifficultySystem(int startSpeed) {
        this.START_SPEED = startSpeed;
    }

    public float calculateSpeed(int score) {
        if (score < 100) {
            return START_SPEED;
        } else if (score < 1000) {
            float progressionFactor = score / 250 * 0.25f; // add 25% base speed every 250 points
            return START_SPEED * (1 + progressionFactor);
        } else {
            // terminal speed
            return START_SPEED * 2;
        }
    }

}
