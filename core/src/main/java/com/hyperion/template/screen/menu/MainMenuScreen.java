package com.hyperion.template.screen.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.hyperion.template.MyGdxGame;
import com.hyperion.template.assets.MyAssetManager;
import com.hyperion.template.screen.GameScreen;
import com.hyperion.template.screen.ScreenManager;
import com.hyperion.template.screen.game.GameLevelScreen;
import com.hyperion.template.sound.SoundManager;
import com.hyperion.template.ui.UiFactory;

public class MainMenuScreen implements GameScreen {

    private final Stage stage;

    public MainMenuScreen() {

        stage = new Stage(new FitViewport(
            MyGdxGame.WIDTH,
            MyGdxGame.HEIGHT,
            new OrthographicCamera()
        ));

        Image backgroundImg = new Image(MyAssetManager.getTexture("texture/menu_background.png"));

        backgroundImg.setWidth(MyGdxGame.WIDTH);
        backgroundImg.setHeight(MyGdxGame.HEIGHT);
        stage.addActor(backgroundImg);

        Label.LabelStyle smallLabelStyle = new Label.LabelStyle();
        smallLabelStyle.font = MyAssetManager.getFont();

        Label versionLabel = new Label(MyGdxGame.VERSION, smallLabelStyle);
        versionLabel.setPosition(8, 0);
        versionLabel.getColor().a = 0.5f;
        versionLabel.setFontScale(0.5f);
        versionLabel.setHeight(32);
        stage.addActor(versionLabel);

        TextButton startButton = UiFactory.textButton(
            "Start",
            1,
            () -> ScreenManager.pushScreen(new GameLevelScreen())
        );

        TextButton creditsButton = UiFactory.textButton(
            "Credits",
            0.75f,
            () -> Gdx.app.exit()
        );

        TextButton quitButton = UiFactory.textButton(
            "Quit",
            0.75f,
            () -> Gdx.app.exit()
        );

        Table rootTable = new Table();
        rootTable.setFillParent(true);
        rootTable.padLeft(200);
        rootTable.setDebug(false);
        stage.addActor(rootTable);

        rootTable.left().defaults().height(64).fill().pad(16);
        rootTable.add(startButton);
        rootTable.row().height(48);
        rootTable.add(creditsButton);
        rootTable.row().height(48);
        rootTable.add(quitButton);

    }

    @Override
    public void show() {
        SoundManager.playMusic(getMusicPath());
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
        return "music/Universfield - Retro Energy.ogg";
    }

}
