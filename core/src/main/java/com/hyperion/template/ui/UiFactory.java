package com.hyperion.template.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.hyperion.template.MyGdxGame;
import com.hyperion.template.assets.MyAssetManager;
import com.hyperion.template.assets.Paths;
import com.hyperion.template.audio.AudioManager;

public class UiFactory {

    private static final Label.LabelStyle labelStyle = new Label.LabelStyle();
    private static final TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
    private static final CheckBox.CheckBoxStyle checkBoxStyle = new CheckBox.CheckBoxStyle();

    static {
        labelStyle.font = MyAssetManager.getFont();

        buttonStyle.font = MyAssetManager.getFont();
        buttonStyle.overFontColor = Color.PURPLE;

        Image checkboxOffImg = new Image(MyAssetManager.getTexture(Paths.CHECKBOX_OFF));
        Image checkboxOnImg = new Image(MyAssetManager.getTexture(Paths.CHECKBOX_ON));

        checkBoxStyle.font = MyAssetManager.getFont();
        checkBoxStyle.checkboxOff = checkboxOffImg.getDrawable();
        checkBoxStyle.checkboxOn = checkboxOnImg.getDrawable();
    }

    public static Image menuBackground() {
        Image menuBackgroundImg = new Image(MyAssetManager.getTexture(Paths.MENU_BACKGROUND));
        menuBackgroundImg.setWidth(MyGdxGame.WIDTH);
        menuBackgroundImg.setHeight(MyGdxGame.HEIGHT);
        return menuBackgroundImg;
    }

    /**
     * Table takes up the entire screen except for some padding.
     */
    public static Table fullSizeTable() {
        Table table = new Table();
        table.setFillParent(true);
        table.pad(80, 140, 80, 140);
        table.setDebug(false);
        return table;
    }

    public static Label label(String version, FontSize fontSize) {
        Label label = new Label(version, labelStyle);

        if (fontSize.scale != 1) {
            label.setFontScale(fontSize.scale);
            label.setHeight(fontSize.scale * MyAssetManager.BITMAP_FONT_SIZE);
        }

        return label;
    }

    public static TextButton textButton(String text, FontSize fontSize, Runnable runnable) {
        TextButton button = new JuicyTextButton(text, buttonStyle, fontSize.scale);
        button.getLabel().setAlignment(Align.left);
        //button.pad(20);
        button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                runnable.run();
            }
        });
        return button;
    }

    public static CheckBox checkBox(boolean isChecked, FontSize fontSize, Runnable runnable) {
        CheckBox checkBox = new CheckBox("", checkBoxStyle);
        checkBox.setChecked(isChecked);
        if (fontSize.scale != 1) {
            checkBox.setTransform(true);
            checkBox.setScale(fontSize.scale);
        }
        checkBox.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                AudioManager.playSound(Paths.BUTTON_CLICK);
                runnable.run();
            }
        });
        return checkBox;
    }

}
