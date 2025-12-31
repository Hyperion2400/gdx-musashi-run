package com.hyperion.template.world;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.hyperion.template.assets.MyAssetManager;
import com.hyperion.template.assets.Paths;

public class Warrior extends Actor {

    private static final int TILE_SIZE = 200;
    private static final int GRAVITY = 1000;

    private final int floorY;

    private float speedX;
    private float speedY = 0;

    private final Animation<TextureRegion> runAnimation;
    private final Animation<TextureRegion> jumpAnimation;
    private final Animation<TextureRegion> fallAnimation;

    private float animationTime = 0;

    public Warrior(int x, int speedX, int floorY, boolean flip) {

        setPosition(x, floorY);
        this.speedX = speedX;
        this.floorY = floorY;

        setOrigin(80, 78);

        TextureAtlas atlas = MyAssetManager.getTextureAtlas(Paths.WARRIOR_SPRITE_SHEET);

        TextureRegion[][] runRegions = atlas.findRegion("Run").split(TILE_SIZE, TILE_SIZE);

        TextureRegion[] runFrames = new TextureRegion[runRegions.length * runRegions[0].length];
        int index = 0;

        for (TextureRegion[] regionRow : runRegions) {
            for (TextureRegion region : regionRow) {
                runFrames[index++] = region;
            }
        }

        runAnimation = new Animation<>(0.1f, runFrames);

        TextureRegion[][] jumpRegions = atlas.findRegion("Jump").split(TILE_SIZE, TILE_SIZE);

        TextureRegion[] jumpFrames = new TextureRegion[jumpRegions.length * jumpRegions[0].length];
        index = 0;

        for (TextureRegion[] regionRow : jumpRegions) {
            for (TextureRegion region : regionRow) {
                jumpFrames[index++] = region;
            }
        }

        jumpAnimation = new Animation<>(0.1f, jumpFrames);

        TextureRegion[][] fallRegions = atlas.findRegion("Fall").split(TILE_SIZE, TILE_SIZE);

        TextureRegion[] fallFrames = new TextureRegion[fallRegions.length * fallRegions[0].length];
        index = 0;

        for (TextureRegion[] regionRow : fallRegions) {
            for (TextureRegion region : regionRow) {
                fallFrames[index++] = region;
            }
        }

        fallAnimation = new Animation<>(0.1f, fallFrames);
    }

    @Override
    public void act(float delta) {

        super.act(delta);

        animationTime += delta;

        moveBy(speedX * delta, speedY * delta);

        if (getY() > floorY) {
            speedY -= GRAVITY * delta;
        } else if (getY() < floorY) {
            speedY = 0;
            setY(floorY);
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (getY() == floorY) {
            batch.draw(
                runAnimation.getKeyFrame(animationTime, true),
                (getX() - getOriginX()),
                (getY() - getOriginY()),
                TILE_SIZE,
                TILE_SIZE
            );
        } else if (speedY > 0) {
            batch.draw(
                jumpAnimation.getKeyFrame(animationTime, true),
                (getX() - getOriginX()),
                (getY() - getOriginY()),
                TILE_SIZE * getScaleX(),
                TILE_SIZE * getScaleY()
            );
        } else {
            batch.draw(
                fallAnimation.getKeyFrame(animationTime, true),
                (getX() - getOriginX()),
                (getY() - getOriginY()),
                TILE_SIZE,
                TILE_SIZE
            );
        }
    }

    public void jump(float delta) {
        if (getY() == floorY) {
            speedY = 350;
        } else if (getY() > floorY) {
            // touching longer prolongs jump
            speedY += GRAVITY / 2 * delta;
        }
    }

    public void setSpeedX(float speedX) {
        this.speedX = speedX;
    }

    public float getSpeedX() {
        return speedX;
    }
}
