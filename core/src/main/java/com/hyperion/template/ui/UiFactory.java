package com.hyperion.template.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.hyperion.template.assets.MyAssetManager;
import com.hyperion.template.sound.SoundManager;

public class UiFactory {

    private static final TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();

    static {
        buttonStyle.font = MyAssetManager.getFont();
        buttonStyle.overFontColor = Color.PURPLE;
    }

    public static TextButton textButton(
        String text,
        float scale,
        Runnable runnable
    ) {
        TextButton button = new JuicyTextButton(text, buttonStyle, scale);
        button.getLabel().setAlignment(Align.left);
        button.pad(20);
        button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                runnable.run();
            }
        });
        return button;
    }

    public static CheckBox createCheckBox(
        CheckBox.CheckBoxStyle style,
        boolean checked,
        float scale,
        Runnable runnable
    ) {
        CheckBox checkBox = new CheckBox("", style);
        checkBox.setChecked(checked);
        if (scale != 1) {
            checkBox.setTransform(true);
            checkBox.setScale(scale);
        }
        checkBox.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                SoundManager.playSound("sound/Universfield - Button Click.ogg");
                runnable.run();
            }
        });
        return checkBox;
    }

}
