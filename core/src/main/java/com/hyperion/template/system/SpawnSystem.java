package com.hyperion.template.system;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.hyperion.template.world.Warrior;

import java.util.ArrayDeque;
import java.util.Deque;

public class SpawnSystem {

    private enum Interval {
        SHORT(0.5f),
        MEDIUM(1f),
        LONG(1.5f);

        public final float time;

        Interval(float time) {
            this.time = time;
        }
    }

    private final Stage stage;

    private int nextEnemyIndex = 0;
    private int maxShortIntervalsInARow = 2;
    private float timeTillNextEnemy = Interval.LONG.time;
    private final Deque<Interval> lastIntervals = new ArrayDeque<>();

    public SpawnSystem(Stage stage) {
        this.stage = stage;
    }

    public void update(
        float delta,
        Warrior[] enemies,
        float viewPortLeft,
        float viewPortRight,
        int score
    ) {

        timeTillNextEnemy -= delta;

        if (timeTillNextEnemy < 0) {
            maxShortIntervalsInARow = Math.min(2 + score / 500, 8); // allow more short intervals in a row each 500 points
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
        Interval nextInterval = nextInterval();
        lastIntervals.push(nextInterval);
        if (lastIntervals.size() > maxShortIntervalsInARow) {
            lastIntervals.removeLast();
        }

        timeTillNextEnemy = nextInterval.time;
    }

    private Interval nextInterval() {
        // make sure we don't get the same interval too many times in a row
        if (lastIntervalsMatch(Interval.SHORT, maxShortIntervalsInARow - 1)) {
            return new Interval[]{Interval.MEDIUM, Interval.LONG}[MathUtils.random(0, 1)];
        } else if (lastIntervalsMatch(Interval.LONG, 3)) {
            return new Interval[]{Interval.SHORT, Interval.MEDIUM}[MathUtils.random(0, 1)];
        } else {
            return Interval.values()[MathUtils.random(0, Interval.values().length - 1)];
        }
    }

    private boolean lastIntervalsMatch(Interval interval, int limit) {
        return lastIntervals.stream().limit(limit).allMatch(i -> i == Interval.SHORT);
    }
}
