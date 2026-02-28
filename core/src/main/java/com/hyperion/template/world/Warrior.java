package com.hyperion.template.world;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.hyperion.template.assets.MyAssetManager;
import com.hyperion.template.assets.Paths;

public class Warrior extends Actor {

    public static final String MARTIAL_HERO_1 = "martial_hero_1";
    public static final String MARTIAL_HERO_2 = "martial_hero_2";

    private static final int REGION_WIDTH = 200;
    private static final int GRAVITY = 1000;

    private final Animation<TextureRegion> runAnimation;
    private final Animation<TextureRegion> jumpAnimation;
    private final Animation<TextureRegion> fallAnimation;
    private final Animation<TextureRegion> attackAnimation;
    private final Animation<TextureRegion> deathAnimation;

    // positioning of the sprite within the texture due to horizontal whitespace
    private final int offsetX;

    private final int groundY;
    private final int attackRange;
    private final int maxHealth;
    private final int direction; // 1 if right, -1 if left
    private final int hitFrame;

    private final Rectangle attackBox = new Rectangle();
    private final Rectangle hitBox = new Rectangle();

    private float maxSpeedX;
    private float speedX;
    private float speedY = 0;
    private float animationTime = 0;
    private boolean isAttacking = false;
    private boolean isDead = false;
    private int health;
    private int energy = 3;

    public Warrior(
        String character,
        int offsetX,
        int x,
        int maxSpeedX,
        int direction,
        int groundY,
        int maxHealth,
        int attackRange,
        int hitFrame
    ) {

        this.offsetX = offsetX;
        setPosition(x, groundY);
        this.maxSpeedX = maxSpeedX;
        this.speedX = maxSpeedX;
        this.direction = direction;
        this.groundY = groundY;
        this.maxHealth = maxHealth;
        this.health = maxHealth;
        this.attackRange = attackRange;
        this.hitFrame = hitFrame;

        setSize(30, 48);

        TextureAtlas atlas = MyAssetManager.getTextureAtlas(Paths.SPRITE_SHEET);

        runAnimation = createAnimation(atlas, character, "run");
        jumpAnimation = createAnimation(atlas, character, "jump");
        fallAnimation = createAnimation(atlas, character, "fall");
        attackAnimation = createAnimation(atlas, character, "attack1");
        deathAnimation = createAnimation(atlas, character, "death");
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

        moveBy(direction * speedX * delta, speedY * delta);

        if (getY() > groundY) {
            speedY -= GRAVITY * delta;
        } else if (getY() < groundY) {
            speedY = 0;
            setY(groundY);
        }

        if (isAttacking && animationTime > attackAnimation.getAnimationDuration()) {
            isAttacking = false;
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {

        // if speed is negative (running left) use negative width to flip texture
        float posX = (direction == 1 ? getX() : getX() + REGION_WIDTH) - offsetX;

        TextureRegion region;

        if (isDead) {
            region = deathAnimation.getKeyFrame(animationTime, false);
        } else if (isAttacking) {
            region = attackAnimation.getKeyFrame(animationTime, false);
        } else if (getY() == groundY) {
            region = runAnimation.getKeyFrame(animationTime, true);
        } else if (speedY > 0) {
            region = jumpAnimation.getKeyFrame(animationTime, true);
        } else {
            region = fallAnimation.getKeyFrame(animationTime, true);
        }

        batch.draw(region, posX, getY(), direction * REGION_WIDTH, region.getRegionHeight());
    }

    @Override
    public void drawDebug(ShapeRenderer shapes) {
        super.drawDebug(shapes);
        shapes.setColor(Color.RED);
        drawAttackBox(shapes);
        shapes.setColor(Color.GREEN);
    }

    private void drawAttackBox(ShapeRenderer shapes) {
        if (direction == 1) {
            shapes.rect(getRight(), getY(), attackRange, getHeight());
        } else {
            shapes.rect(getX(), getY(), -attackRange, getHeight());
        }
    }

    public void jump(float delta) {

        if (isDead) {
            return;
        }

        if (getY() == groundY) {
            speedY = 350;
        } else if (getY() > groundY) {
            // touching longer prolongs jump
            speedY += GRAVITY / 2 * delta;
        }

        isAttacking = false;
    }

    public void attack() {


        if (getY() > groundY || isAttacking || energy <= 0) {
            return;
        }

        animationTime = 0;
        isAttacking = true;
        energy --;
    }

    public void takeHit() {

        health--;

        if (health <= 0) {
            die();
        }
    }

    private void die() {

        if (isDead) {
            return;
        }

        animationTime = 0;
        isDead = true;
        speedX = 0;
    }

    public void reset() {
        speedX = maxSpeedX;
        animationTime = 0;
        isDead = false;
        isAttacking = false;
        health = maxHealth;
        energy = 3;
    }

    public float getSpeedX() {
        return speedX;
    }

    public Rectangle getBounds() {
        return new Rectangle(getX(), getY(), getWidth(), getHeight());
    }

    public Rectangle getAttackBox() {
        if (direction == 1) {
            attackBox.x = getRight();
        } else {
            attackBox.x = getX() - attackRange;
        }

        attackBox.y = getY();
        attackBox.width = attackRange;
        attackBox.height = getHeight();

        return attackBox;
    }

    public Rectangle getStartAttackBox(float delta) {

        int speedOffset = (int) (20 * speedX * delta); // anticipate movement

        if (direction == 1) {
            attackBox.x = getRight() + speedOffset;
        } else {
            attackBox.x = getX() - attackRange - speedOffset;
        }

        attackBox.y = getY();
        attackBox.width = attackRange;
        attackBox.height = getHeight();

        return attackBox;
    }

    public boolean isAttacking() {
        return isAttacking;
    }

    public boolean isHitFrame() {
        return isAttacking && animationTime >= hitFrame * attackAnimation.getFrameDuration() && animationTime < (hitFrame + 1) * attackAnimation.getFrameDuration();
    }

    public boolean isDead() {
        return isDead;
    }

}
