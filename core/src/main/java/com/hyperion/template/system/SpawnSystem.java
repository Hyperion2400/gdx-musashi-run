package com.hyperion.template.system;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.hyperion.template.entity.Warrior;

public class SpawnSystem {

    private final Stage stage;

    private float timeTillNextEnemy = 1f;
    private int nextEnemyIndex = 0;

    public SpawnSystem(Stage stage) {
        this.stage = stage;
    }

    public void update(
        float delta,
        Warrior[] enemies,
        float viewPortLeft,
        float viewPortRight
    ) {

        timeTillNextEnemy -= delta;

        if (timeTillNextEnemy < 0) {
            spawnEnemy(enemies, viewPortRight);
        }

        for (Warrior enemy : enemies) {
            if (enemy.hasParent() && enemy.getRight() < viewPortLeft) {
                enemy.remove();
            }
        }
    }

    private void spawnEnemy(Warrior[] enemies, float viewPortRight) {


        Warrior nextEnemy = enemies[nextEnemyIndex];

        if (nextEnemy.hasParent()) {
            // if enemy has a parent it's already on stage and not offscreen yet
            return;
        }

        nextEnemy.setX(viewPortRight + 100);
        nextEnemy.reset();

        stage.addActor(nextEnemy);

        nextEnemyIndex++;

        if (nextEnemyIndex > enemies.length - 1) {
            nextEnemyIndex = 0;
        }

        resetTimeTillNextEnemy();
    }

    private void resetTimeTillNextEnemy() {
        timeTillNextEnemy = MathUtils.random(0.3f, 2f);
    }
}
