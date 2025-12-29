package com.hyperion.template.ui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.hyperion.template.assets.Paths;
import com.hyperion.template.audio.AudioManager;

/**
 * TextButton with simple click animation and sound effects.
 */
public class JuicyTextButton extends TextButton {

    private static final float AFTER_CLICK_SCALE = 1.2f;
    private static final float SCALE_BACK_SPEED = 1f;

    private final float defaultScale;

    public JuicyTextButton(String text, TextButtonStyle style, float defaultScale) {
        super(text, style);

        this.defaultScale = defaultScale;

        setTransform(true);
        setScale(defaultScale);

        addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                AudioManager.playSound(Paths.BUTTON_CLICK);
                setScale(defaultScale * AFTER_CLICK_SCALE);
            }
        });

    }

    @Override
    public void act(float delta) {

        if (getScaleX() > defaultScale) {
            setScaleX(getScaleX() - SCALE_BACK_SPEED * delta);
        }

        if (getScaleY() > defaultScale) {
            setScaleY(getScaleY() - SCALE_BACK_SPEED * delta);
        }

        if (getScaleX() < defaultScale) {
            setScaleX(defaultScale);
        }

        if (getScaleY() < defaultScale) {
            setScaleY(defaultScale);
        }

        super.act(delta);
    }

}
