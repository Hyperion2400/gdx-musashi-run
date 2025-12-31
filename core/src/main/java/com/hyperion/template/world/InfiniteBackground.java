package com.hyperion.template.world;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.hyperion.template.assets.MyAssetManager;

/**
 * Image that repeats horizontally. Needs to be rearranged if camera moves.
 */
public class InfiniteBackground extends Actor {

    private final Texture texture;
    private final int drawCount;

    public InfiniteBackground(String texturePath, int posY, int worldWidth) {
        this.texture = MyAssetManager.getTexture(texturePath);
        setY(posY);
        this.drawCount = worldWidth / texture.getWidth() + 2;
    }

    @Override
    public void act(float delta) {
        super.act(delta);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        for (int i = 0; i < drawCount; i++) {
            batch.draw(
                texture,
                getX() + i * texture.getWidth(),
                getY(),
                texture.getWidth(),
                texture.getHeight()
            );
        }
    }

    /**
     * If camera has moved more than one texture width past the background, jump to the right.
     */
    public void rearrange(float cameraLeft) {
        if (getX() + texture.getWidth() < cameraLeft) {
            moveBy(texture.getWidth(), 0);
        }
    }
}
