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
import com.hyperion.template.assets.Paths;
import com.hyperion.template.screen.GameScreen;
import com.hyperion.template.screen.ScreenManager;
import com.hyperion.template.screen.menu.MainMenuScreen;
import com.hyperion.template.ui.FontSize;
import com.hyperion.template.ui.UiFactory;

public class CreditsScreen implements GameScreen {

    private final Stage stage;

    public CreditsScreen() {
        Image menuBackgroundImg = UiFactory.menuBackground();
        menuBackgroundImg.getColor().a = 0.5f;

        Table table = creditsTable();

        stage = new Stage(new FitViewport(
            MyGdxGame.WIDTH,
            MyGdxGame.HEIGHT,
            new OrthographicCamera()
        ));

        stage.addActor(menuBackgroundImg);
        stage.addActor(table);
    }

    private Table creditsTable() {
        Label leftContent = UiFactory.label(
            """
                Programming
                Tileset
                Sprite sheets
                Font
                Music
                Sound""",
            FontSize.SMALL
        );

        Label rightContent = UiFactory.label(
            """
                Hyperion2400
                craftpix.net
                LuizMelo
                Patrick Wagesreiter
                Universfield
                Universfield""",
            FontSize.SMALL
        );

        TextButton backButton = UiFactory.textButton(
            "Back",
            FontSize.LARGE,
            () -> ScreenManager.pushScreen(new MainMenuScreen())
        );

        Table table = UiFactory.fullSizeTable();

        // HEADER
        table.top().left().defaults().left();
        table.add(UiFactory.label("Credits", FontSize.LARGE)).width(500);
        table.add().width(500);

        // FILLER
        table.row();
        table.add().expandY();

        // CONTENT
        table.row();
        table.add(leftContent).padLeft(200);
        table.add(rightContent);

        // FILLER
        table.row();
        table.add().expandY();

        // BACK BUTTON
        table.row();
        table.add();
        table.add(backButton).right().bottom();

        return table;
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
