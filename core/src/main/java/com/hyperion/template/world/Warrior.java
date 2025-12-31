package com.hyperion.template.world;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.hyperion.template.assets.MyAssetManager;
import com.hyperion.template.assets.Paths;

public class Warrior extends Actor {

    public static final String MARTIAL_HERO_1 = "martial_hero_1";
    public static final String MARTIAL_HERO_2 = "martial_hero_2";

    private static final int REGION_WIDTH = 200;
    private static final int GRAVITY = 1000;

    // positioning of the sprite within the texture due to horizontal whitespace
    private final int offsetX;

    private final int groundY;

    private float speedX;
    private float speedY = 0;

    private final Animation<TextureRegion> runAnimation;
    private final Animation<TextureRegion> jumpAnimation;
    private final Animation<TextureRegion> fallAnimation;

    private float animationTime = 0;

    public Warrior(String character, int offsetX, int x, int speedX, int groundY) {

        this.offsetX = offsetX;
        setPosition(x, groundY);
        this.speedX = speedX;
        this.groundY = groundY;

        setSize(30, 48);

        TextureAtlas atlas = MyAssetManager.getTextureAtlas(Paths.SPRITE_SHEET);

        runAnimation = createAnimation(atlas, character, "run");
        jumpAnimation = createAnimation(atlas, character, "jump");
        fallAnimation = createAnimation(atlas, character, "fall");
    }

    private Animation<TextureRegion> createAnimation(
        TextureAtlas atlas,
        String character,
        String animation
    ) {

        TextureRegion region = atlas.findRegion(character + "_" + animation);
        TextureRegion[][] splitRegions = region.split(REGION_WIDTH, region.getRegionHeight());

        // all regions have exactly one row
        // otherwise we'd have to iterate through the two-dimensional array to pick all frames
        TextureRegion[] frames = splitRegions[0];

        return new Animation<>(0.1f, frames);
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
        int width = speedX >= 0 ? REGION_WIDTH : -REGION_WIDTH;
        float posX = (speedX >= 0 ? getX() : getX() + REGION_WIDTH) - offsetX;

        TextureRegion region;

        if (getY() == groundY) {
            region = runAnimation.getKeyFrame(animationTime, true);
        } else if (speedY > 0) {
            region = jumpAnimation.getKeyFrame(animationTime, true);
        } else {
            region = fallAnimation.getKeyFrame(animationTime, true);
        }

        batch.draw(region, posX, getY(), width, region.getRegionHeight());
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
