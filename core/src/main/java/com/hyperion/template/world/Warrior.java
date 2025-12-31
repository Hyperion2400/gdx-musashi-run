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

    private final int groundY;

    private float speedX;
    private float speedY = 0;

    private final Animation<TextureRegion> runAnimation;
    private final Animation<TextureRegion> jumpAnimation;
    private final Animation<TextureRegion> fallAnimation;

    private float animationTime = 0;

    public Warrior(String spriteSheetPath, int x, int speedX, int groundY) {

        setPosition(x, groundY);
        this.speedX = speedX;
        this.groundY = groundY;

        setSize(30, 50);

        TextureAtlas atlas = MyAssetManager.getTextureAtlas(spriteSheetPath);

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

        if (getY() > groundY) {
            speedY -= GRAVITY * delta;
        } else if (getY() < groundY) {
            speedY = 0;
            setY(groundY);
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {

        // if speed is negative (running left) use negative width to flip texture
        int width = speedX >= 0 ? TILE_SIZE : -TILE_SIZE;
        float posX = (speedX >= 0 ? getX() : getX() + TILE_SIZE) - getOriginX();
        float posY = getY() - getOriginY();

        TextureRegion region;

        if (getY() == groundY) {
            region = runAnimation.getKeyFrame(animationTime, true);
        } else if (speedY > 0) {
            region = jumpAnimation.getKeyFrame(animationTime, true);
        } else {
            region = fallAnimation.getKeyFrame(animationTime, true);
        }

        batch.draw(region, posX, posY, width, TILE_SIZE);
    }

    public void jump(float delta) {
        if (getY() == groundY) {
            speedY = 350;
        } else if (getY() > groundY) {
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
