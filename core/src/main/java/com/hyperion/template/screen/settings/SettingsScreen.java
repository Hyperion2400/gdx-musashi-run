package com.hyperion.template.screen.settings;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.hyperion.template.MyGdxGame;
import com.hyperion.template.assets.MyAssetManager;
import com.hyperion.template.assets.Paths;
import com.hyperion.template.screen.GameScreen;
import com.hyperion.template.screen.ScreenManager;
import com.hyperion.template.screen.menu.MainMenuScreen;
import com.hyperion.template.sound.SoundManager;
import com.hyperion.template.state.SettingsManager;
import com.hyperion.template.ui.UiFactory;

public class SettingsScreen implements GameScreen {

    private final Stage stage;
    private final CheckBox fullscreenCheckBox;
    private final CheckBox soundCheckBox;
    private final CheckBox musicCheckBox;

    public SettingsScreen() {

        stage = new Stage(new FitViewport(
                MyGdxGame.WIDTH,
                MyGdxGame.HEIGHT,
                new OrthographicCamera()
        ));

        Image menuBackgroundImg = new Image(MyAssetManager.getTexture(Paths.MENU_BACKGROUND));
        menuBackgroundImg.setWidth(MyGdxGame.WIDTH);
        menuBackgroundImg.setHeight(MyGdxGame.HEIGHT);
        menuBackgroundImg.getColor().a = 0.5f;
        stage.addActor(menuBackgroundImg);

        Table rootTable = new Table();
        rootTable.setFillParent(true);
        rootTable.pad(80, 140, 80, 140);
        rootTable.setDebug(false);
        stage.addActor(rootTable);

        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = MyAssetManager.getFont();

        Label header = new Label("Settings", labelStyle);

        Image checkboxOffImg = new Image(MyAssetManager.getTexture(Paths.CHECKBOX_OFF));
        Image checkboxOnImg = new Image(MyAssetManager.getTexture(Paths.CHECKBOX_ON));

        CheckBox.CheckBoxStyle checkBoxStyle = new CheckBox.CheckBoxStyle();
        checkBoxStyle.font = MyAssetManager.getFont();
        checkBoxStyle.checkboxOff = checkboxOffImg.getDrawable();
        checkBoxStyle.checkboxOn = checkboxOnImg.getDrawable();

        Label fullscreenLabel = new Label("Fullscreen", labelStyle);
        fullscreenLabel.setFontScale(0.75f);
        fullscreenLabel.setHeight(48);
        fullscreenCheckBox = UiFactory.createCheckBox(
                checkBoxStyle, Gdx.graphics.isFullscreen(),
                0.75f,
                this::updateFullscreen
        );
        fullscreenCheckBox.padLeft(16);
        fullscreenCheckBox.padBottom(16);

        Label musicLabel = new Label("Music", labelStyle);
        musicLabel.setFontScale(0.75f);
        musicLabel.setHeight(48);
        musicCheckBox = UiFactory.createCheckBox(
                checkBoxStyle, SettingsManager.isMusicEnabled(),
                0.75f,
                this::updateMusic
        );
        musicCheckBox.padLeft(16);
        musicCheckBox.padBottom(16);

        Label soundLabel = new Label("Sound", labelStyle);
        soundLabel.setFontScale(0.75f);
        soundLabel.setHeight(48);
        soundCheckBox = UiFactory.createCheckBox(
                checkBoxStyle, SettingsManager.isSoundEnabled(),
                0.75f,
                this::updateSound
        );
        soundCheckBox.padLeft(16);
        soundCheckBox.padBottom(16);

        TextButton backButton = UiFactory.textButton(
                "Back",
                1,
                () -> ScreenManager.pushScreen(new MainMenuScreen())
        );

        rootTable.top().left().defaults().left().height(80);
        rootTable.add(header).width(600);
        rootTable.add().width(400);
        rootTable.row();
        rootTable.add().height(0).expandY();
        if (Gdx.app.getType() == Application.ApplicationType.Desktop) {
            rootTable.row();
            rootTable.add(fullscreenLabel).padLeft(240);
            rootTable.add(fullscreenCheckBox).width(80);
        }
        rootTable.row();
        rootTable.add(musicLabel).padLeft(240);
        rootTable.add(musicCheckBox).width(80);
        rootTable.row();
        rootTable.add(soundLabel).padLeft(240);
        rootTable.add(soundCheckBox).width(80);
        rootTable.row();
        rootTable.add().height(0).expandY();
        rootTable.row();
        rootTable.add();
        rootTable.add(backButton).right().bottom();
    }

    private void updateFullscreen() {
        SettingsManager.setFullScreen(fullscreenCheckBox.isChecked());
    }

    private void updateMusic() {
        boolean isMusic = musicCheckBox.isChecked();
        SettingsManager.setMusicEnabled(isMusic);
        if (!isMusic) {
            SoundManager.stopMusic();
        } else {
            SoundManager.playMusic(getMusicPath());
        }
    }

    private void updateSound() {
        SettingsManager.setSoundEnabled(soundCheckBox.isChecked());
    }

    @Override
    public void render(float delta) {

        stage.act(Gdx.graphics.getDeltaTime());

        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void dispose() {
        stage.dispose();
    }

    @Override
    public void setInputProcessor() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public String getMusicPath() {
        return Paths.MENU_MUSIC;
    }
}
