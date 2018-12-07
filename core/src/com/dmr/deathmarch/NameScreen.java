package com.dmr.deathmarch;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class NameScreen implements Screen {

    private TextField nameField;
    final DeathMarch game;
    public OrthographicCamera camera;
    public Stage stage;
    Player p1;

    public NameScreen(final DeathMarch game, Player winner) {
        p1 = winner;
        this.game = game;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1280, 720);

        stage = new Stage(new ScreenViewport());

    }

    @Override
    public void render(float delta) {

        Gdx.gl.glClearColor(0f, 0f, 0f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();

        // ---GAME CONTROLS---
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            //game.setScreen(new MainMenuScreen(game));
            //game.init(game);
            game.changeScreen(DeathMarch.MENU);
        }
        // ------------------


    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        stage.dispose();
    }

    @Override
    public void show() {
        stage.clear();
        Gdx.input.setInputProcessor(stage);

        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();

        // Create a table that fills the screen. Everything else will go inside this table.
        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        Skin skin = new Skin(Gdx.files.internal("skin/pixthulhu-ui.json"));

        Label winnerText = new Label("Winner Winner Chicken Dinner", skin);
        winnerText.setFontScale(1.75f);
        winnerText.setStyle(new Label.LabelStyle(new BitmapFont((Gdx.files.internal("fonts/spooky.fnt"))), Color.GREEN));
        Label enterName = new Label("ENTER YOUR NAME", skin);
        enterName.setFontScale(1.75f);
        enterName.setStyle(new Label.LabelStyle(new BitmapFont((Gdx.files.internal("fonts/spooky.fnt"))), Color.WHITE));
        nameField = new TextField("",skin);
        TextButton submitButton = new TextButton("Submit", skin);
        table.add(winnerText).fillX().uniformX();
        table.row().pad(10, 0, 10, 0);
        table.add(enterName).fillX().uniformX();
        table.row().pad(10, 0, 10, 0);
        table.add(nameField).fillX().uniform();
        table.row().pad(10,0,10,0);
        table.add(submitButton).fillX().uniform();
        table.row().pad(10,0,10,0);


        submitButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                p1.setName(getName());
                //dispose();
                //stage.clear();
                game.changeScreen(DeathMarch.HOF);
                //GameScreen.init();
            }
        });
    }

    public String getName(){
        return nameField.getText();
    }

}
