package com.hyperion.template.entity;

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
import com.hyperion.template.audio.AudioManager;

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
    private final int hitFrame; // the frame of the animation that represents the hit

    private final Rectangle attackBox;
    private final Rectangle startAttackBox;

    private float maxSpeedX;
    private float speedX;
    private float speedY = 0;
    private float animationTime = 0;
    private boolean isAttacking = false;
    private boolean damagedDealt = false; // whether current attack has already dealth damage
    private boolean isDead = false;
    private int health;

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
        attackBox = new Rectangle(0, 0, attackRange, getHeight());
        startAttackBox = new Rectangle(0, 0, attackRange, getHeight());

        TextureAtlas atlas = MyAssetManager.getTextureAtlas(Paths.SPRITE_SHEET);

        runAnimation = createAnimation(atlas, character, "run", Animation.PlayMode.LOOP);
        jumpAnimation = createAnimation(atlas, character, "jump", Animation.PlayMode.LOOP);
        fallAnimation = createAnimation(atlas, character, "fall", Animation.PlayMode.LOOP);
        attackAnimation = createAnimation(atlas, character, "attack1", Animation.PlayMode.NORMAL);
        deathAnimation = createAnimation(atlas, character, "death", Animation.PlayMode.NORMAL);
    }

    private Animation<TextureRegion> createAnimation(
        TextureAtlas atlas,
        String character,
        String animation,
        Animation.PlayMode playMode
    ) {

        TextureRegion region = atlas.findRegion(character + "_" + animation);
        TextureRegion[][] splitRegions = region.split(REGION_WIDTH, region.getRegionHeight());

        // all regions have exactly one row
        // otherwise we'd have to iterate through the two-dimensional array to pick all frames
        TextureRegion[] frames = splitRegions[0];

        var anim = new Animation<>(0.1f, frames);
        anim.setPlayMode(playMode);
        return anim;
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

        updateAttackBoxes(delta);
    }

    private void updateAttackBoxes(float delta) {

        float x = getX() + (getRight() - getX()) / 2;
        int speedOffset = (int) (25 * speedX * delta); // anticipate movement

        attackBox.x = startAttackBox.x = x;

        if (direction == 1) {
            startAttackBox.x += speedOffset;
        } else {
            attackBox.x -= attackRange;
            startAttackBox.x -= attackRange + speedOffset;
        }

        attackBox.y = startAttackBox.y = getY();
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {

        // if speed is negative (running left) use negative width to flip texture
        float posX = (direction == 1 ? getX() : getX() + REGION_WIDTH) - offsetX;

        TextureRegion region;

        if (isDead) {
            region = deathAnimation.getKeyFrame(animationTime);
        } else if (isAttacking) {
            region = attackAnimation.getKeyFrame(animationTime);
        } else if (getY() == groundY) {
            region = runAnimation.getKeyFrame(animationTime);
        } else if (speedY > 0) {
            region = jumpAnimation.getKeyFrame(animationTime);
        } else {
            region = fallAnimation.getKeyFrame(animationTime);
        }

        batch.draw(region, posX, getY(), direction * REGION_WIDTH, region.getRegionHeight());
    }

    @Override
    public void drawDebug(ShapeRenderer shapes) {
        super.drawDebug(shapes);
        shapes.setColor(Color.RED);
        //shapes.rect(attackBox.x, attackBox.y, attackBox.width, attackBox.height);
        shapes.setColor(Color.GREEN);
        shapes.rect(
            startAttackBox.x,
            startAttackBox.y,
            startAttackBox.width,
            startAttackBox.height
        );
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


        if (getY() > groundY || isAttacking) {
            return;
        }

        animationTime = 0;
        isAttacking = true;
        AudioManager.playSound(Paths.SWORD_SLASH);
    }

    public void takeHit() {

        health--;

        if (health <= 0) {
            die();
        }
    }

    public void dealDamage() {
        damagedDealt = true;
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
        damagedDealt = false;
        health = maxHealth;
    }

    public float getSpeedX() {
        return speedX;
    }

    public void setSpeedX(float newSpeedX) {
        maxSpeedX = newSpeedX;
        speedX = maxSpeedX;
    }

    public Rectangle getBounds() {
        return new Rectangle(getX(), getY(), getWidth(), getHeight());
    }

    /**
     * The bounds in which a target is considered to be hit if an attack occurs.
     */
    public Rectangle getAttackBox() {
        return attackBox;
    }

    /**
     * The bounds in which an attack should be started to hit a moving target.
     */
    public Rectangle getStartAttackBox() {
        return startAttackBox;
    }

    public boolean isHitFrame() {
        var hitFrameStartTime = hitFrame * attackAnimation.getFrameDuration();
        var hitFrameEndTime = (hitFrame + 1) * attackAnimation.getFrameDuration();

        return isAttacking
            && animationTime >= hitFrameStartTime
            && animationTime < hitFrameEndTime;
    }

    public boolean isAttacking() {
        return isAttacking;
    }

    public boolean isDamagedDealt() {
        return damagedDealt;
    }

    public boolean isDead() {
        return isDead;
    }

}
