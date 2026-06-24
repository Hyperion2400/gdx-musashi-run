package com.hyperion.template.system;

import com.badlogic.gdx.Gdx;
import com.hyperion.template.world.Warrior;

public class ActionSystem {

    public void update(float delta, Warrior player, Warrior[] enemies) {

        if (player.isDead()) {
            return;
        }

        if (Gdx.input.isTouched()) {
            player.jump(delta);
        }

        for (Warrior enemy : enemies) {

            if (enemy.isDead()) {
                continue;
            }

            if (!enemy.isAttacking()
                && enemy.getStartAttackBox().overlaps(player.getBounds())) {
                enemy.attack();
            }

            if (enemy.isHitFrame()
                && !enemy.isDamagedDealt()
                && enemy.getAttackBox().overlaps(player.getBounds())) {
                player.takeHit();
                enemy.dealDamage();
            }
        }
    }
}
