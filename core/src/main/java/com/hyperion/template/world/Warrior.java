package com.hyperion.template.world;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.hyperion.template.assets.MyAssetManager;

public class Warrior extends Actor {

    private static final int TEXTURE_SIZE = 200;
    private static final int GRAVITY = 1000;

    // positioning of the sprite within the texture
    private final int offsetX;
    private final int offsetY;
    private final int groundY;

    private float speedX;
    private float speedY = 0;

    private final Animation<TextureRegion> runAnimation;
    private final Animation<TextureRegion> jumpAnimation;
    private final Animation<TextureRegion> fallAnimation;

    private float animationTime = 0;

    public Warrior(
        String spriteSheetPath,
        int offsetX,
        int offsetY,
        int x,
        int speedX,
        int groundY
    ) {

        this.offsetX = offsetX;
        this.offsetY = offsetY;
        setPosition(x, groundY);
        this.speedX = speedX;
        this.groundY = groundY;

        setSize(30, 48);

        TextureAtlas atlas = MyAssetManager.getTextureAtlas(spriteSheetPath);

        TextureRegion[][] runRegions = atlas.findRegion("Run").split(TEXTURE_SIZE, TEXTURE_SIZE);

        TextureRegion[] runFrames = new TextureRegion[runRegions.length * runRegions[0].length];
        int index = 0;

        for (TextureRegion[] regionRow : runRegions) {
            for (TextureRegion region : regionRow) {
                runFrames[index++] = region;
            }
        }

        runAnimation = new Animation<>(0.1f, runFrames);

        TextureRegion[][] jumpRegions = atlas.findRegion("Jump").split(TEXTURE_SIZE, TEXTURE_SIZE);

        TextureRegion[] jumpFrames = new TextureRegion[jumpRegions.length * jumpRegions[0].length];
        index = 0;

        for (TextureRegion[] regionRow : jumpRegions) {
            for (TextureRegion region : regionRow) {
                jumpFrames[index++] = region;
            }
        }

        jumpAnimation = new Animation<>(0.1f, jumpFrames);

        TextureRegion[][] fallRegions = atlas.findRegion("Fall").split(TEXTURE_SIZE, TEXTURE_SIZE);

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
        int width = speedX >= 0 ? TEXTURE_SIZE : -TEXTURE_SIZE;
        float posX = (speedX >= 0 ? getX() : getX() + TEXTURE_SIZE) - offsetX;
        float posY = getY() - offsetY;

        TextureRegion region;

        if (getY() == groundY) {
            region = runAnimation.getKeyFrame(animationTime, true);
        } else if (speedY > 0) {
            region = jumpAnimation.getKeyFrame(animationTime, true);
        } else {
            region = fallAnimation.getKeyFrame(animationTime, true);
        }

        batch.draw(region, posX, posY, width, TEXTURE_SIZE);
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
