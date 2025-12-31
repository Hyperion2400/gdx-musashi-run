package com.hyperion.template.screen.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.hyperion.template.MyGdxGame;
import com.hyperion.template.assets.Paths;
import com.hyperion.template.audio.AudioManager;
import com.hyperion.template.screen.GameScreen;
import com.hyperion.template.screen.ScreenManager;
import com.hyperion.template.screen.credits.CreditsScreen;
import com.hyperion.template.screen.world.WorldScreen;
import com.hyperion.template.screen.settings.SettingsScreen;
import com.hyperion.template.ui.FontSize;
import com.hyperion.template.ui.UiFactory;

public class MainMenuScreen implements GameScreen {

    private final Stage stage;

    public MainMenuScreen() {

        TextButton startButton = UiFactory.textButton(
            "Start",
            FontSize.LARGE,
            () -> ScreenManager.pushScreen(new WorldScreen())
        );

        TextButton settingsButton = UiFactory.textButton(
            "Settings",
            FontSize.MEDIUM,
            () -> ScreenManager.pushScreen(new SettingsScreen())
        );

        TextButton creditsButton = UiFactory.textButton(
            "Credits",
            FontSize.MEDIUM,
            () -> ScreenManager.pushScreen(new CreditsScreen())
        );

        TextButton quitButton = UiFactory.textButton(
            "Quit",
            FontSize.MEDIUM,
            () -> Gdx.app.exit()
        );

        Table table = new Table();
        table.setFillParent(true);
        table.padLeft(200);
        table.setDebug(false);

        table.left().defaults().height(64).fill().pad(16);
        table.add(startButton);
        table.row();
        table.add(settingsButton);
        table.row();
        table.add(creditsButton);
        table.row();
        table.add(quitButton);

        Image menuBackgroundImg = UiFactory.menuBackground();

        stage = new Stage(new FitViewport(
            MyGdxGame.WIDTH,
            MyGdxGame.HEIGHT,
            new OrthographicCamera()
        ));
        stage.addActor(menuBackgroundImg);
        stage.addActor(table);
        stage.addActor(versionLabel());
    }

    private Actor versionLabel() {
        Label versionLabel = UiFactory.label(MyGdxGame.getVersion(), FontSize.SMALL);
        versionLabel.setPosition(8, 0);
        versionLabel.getColor().a = 0.5f;
        return versionLabel;
    }

    @Override
    public void show() {
        AudioManager.playMusic(getMusicPath());
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
