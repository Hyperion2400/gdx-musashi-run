package com.hyperion.template.screen.settings;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.hyperion.template.MyGdxGame;
import com.hyperion.template.assets.Paths;
import com.hyperion.template.audio.AudioManager;
import com.hyperion.template.screen.GameScreen;
import com.hyperion.template.screen.ScreenManager;
import com.hyperion.template.screen.menu.MainMenuScreen;
import com.hyperion.template.settings.PreferencesManager;
import com.hyperion.template.ui.FontSize;
import com.hyperion.template.ui.UiFactory;

public class SettingsScreen implements GameScreen {

    private final Stage stage;

    private CheckBox fullscreenCheckBox;
    private CheckBox soundCheckBox;
    private CheckBox musicCheckBox;

    public SettingsScreen() {

        Image menuBackgroundImg = UiFactory.menuBackground();
        menuBackgroundImg.getColor().a = 0.5f;

        Table table = settingsTable();

        stage = new Stage(new FitViewport(
            MyGdxGame.WIDTH, MyGdxGame.HEIGHT, new OrthographicCamera()));
        stage.addActor(menuBackgroundImg);
        stage.addActor(table);
    }

    private Table settingsTable() {

        fullscreenCheckBox = UiFactory.checkBox(
            Gdx.graphics.isFullscreen(), FontSize.MEDIUM, this::changeFullscreenSetting);
        fullscreenCheckBox.padLeft(20);
        fullscreenCheckBox.padBottom(20);

        musicCheckBox = UiFactory.checkBox(
            PreferencesManager.isMusicEnabled(), FontSize.MEDIUM, this::changeMusicSetting);
        musicCheckBox.padLeft(20);
        musicCheckBox.padBottom(20);

        soundCheckBox = UiFactory.checkBox(
            PreferencesManager.isSoundEnabled(), FontSize.MEDIUM, this::changeSoundSetting);
        soundCheckBox.padLeft(20);
        soundCheckBox.padBottom(20);

        TextButton backButton = UiFactory.textButton(
            "Back", FontSize.LARGE, () -> ScreenManager.pushScreen(new MainMenuScreen()));

        Table table = UiFactory.fullSizeTable();

        int checkBoxCellSize = 80;
        // by how much the main content is offset inside the table
        int contentIndent = 240;

        // HEADER
        table.top().left().defaults().left().height(80);
        table.add(UiFactory.label("Settings", FontSize.LARGE)).width(600);
        table.add().width(400);

        // FILLER
        table.row();
        table.add().height(0).expandY();

        // FULLSCREEN SETTING
        if (Gdx.app.getType() == Application.ApplicationType.Desktop) {
            table.row();
            table.add(UiFactory.label("Fullscreen", FontSize.MEDIUM)).padLeft(contentIndent);
            table.add(fullscreenCheckBox).width(checkBoxCellSize);
        }

        // MUSIC SETTING
        table.row();
        table.add(UiFactory.label("Music", FontSize.MEDIUM)).padLeft(contentIndent);
        table.add(musicCheckBox).width(checkBoxCellSize);

        // SOUND SETTING
        table.row();
        table.add(UiFactory.label("Sound", FontSize.MEDIUM)).padLeft(contentIndent);
        table.add(soundCheckBox).width(checkBoxCellSize);

        // FILLER
        table.row();
        table.add().height(0).expandY();

        // BACK BUTTON
        table.row();
        table.add();
        table.add(backButton).right().bottom();

        return table;
    }

    private void changeFullscreenSetting() {
        PreferencesManager.setFullScreen(fullscreenCheckBox.isChecked());
    }

    private void changeMusicSetting() {
        boolean isMusic = musicCheckBox.isChecked();
        PreferencesManager.setMusicEnabled(isMusic);
        if (!isMusic) {
            AudioManager.stopMusic();
        } else {
            AudioManager.playMusic(getMusicPath());
        }
    }

    private void changeSoundSetting() {
        PreferencesManager.setSoundEnabled(soundCheckBox.isChecked());
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
