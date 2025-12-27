package com.hyperion.template.ui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;

public class UiFactory {

    public static TextButton textButton(
            String text,
            TextButton.TextButtonStyle style,
            float scale,
            Runnable runnable
    ) {
        TextButton button = new TextButton(text, style);
        button.getLabel().setAlignment(Align.left);
        button.pad(20);
        button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                runnable.run();
            }
        });
        if (scale != 1) {
            button.setTransform(true);
            button.setScale(scale);
        }
        return button;
    }

}
