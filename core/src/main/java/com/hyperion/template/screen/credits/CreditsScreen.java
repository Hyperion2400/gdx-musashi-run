package com.hyperion.template.screen.credits;

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
import com.hyperion.template.screen.menu.MainMenuScreen;
import com.hyperion.template.ui.UiFactory;

public class CreditsScreen implements GameScreen {

    private final Stage stage;

    public CreditsScreen() {

        stage = new Stage(new FitViewport(
            MyGdxGame.WIDTH,
            MyGdxGame.HEIGHT,
            new OrthographicCamera()
        ));

        Image menuBackgroundImg = new Image(MyAssetManager.getTexture("texture/menu_background.png"));
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

        Label header = new Label("Credits", labelStyle);

        Label leftLabel = new Label(
            """
                Programming
                Music
                Sound
                """,
            labelStyle
        );
        leftLabel.setFontScale(0.5f);
        leftLabel.setHeight(32);

        Label rightLabel = new Label(
            """
                Hyperion2400
                Retro Energy by Universfield
                Button Click by Universfield
                """,
            labelStyle
        );
        rightLabel.setFontScale(0.5f);
        rightLabel.setHeight(32);

        TextButton backButton = UiFactory.textButton(
            "Back",
            1,
            () -> ScreenManager.pushScreen(new MainMenuScreen())
        );

        rootTable.top().left().defaults().left().height(80);
        rootTable.add(header).width(400);
        rootTable.add().width(600);
        rootTable.row();
        rootTable.add().height(0).expandY();
        rootTable.row();
        rootTable.add(leftLabel).padLeft(100);
        rootTable.add(rightLabel);
        rootTable.row();
        rootTable.add().height(0).expandY();
        rootTable.row();
        rootTable.add();
        rootTable.add(backButton).right().bottom();
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
