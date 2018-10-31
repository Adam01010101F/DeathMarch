package com.dmr.deathmarch;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class LoseScreen implements Screen {

    final DeathMarch game;
    public OrthographicCamera camera;
    public Stage stage;
    private int score;
    private TextureRegion backgroundTexture;

    public LoseScreen(final DeathMarch game, Player player) {
        this.game = game;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1280, 720);
        this.score = player.getKills();
        stage = new Stage(new ScreenViewport());
        backgroundTexture = new TextureRegion(new Texture("background.png"), 0, 0, 1280, 720);

    }
    @Override
    public void render(float delta) {

        Gdx.gl.glClearColor(0f, 0f, 0f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();


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

        Label gameOver = new Label("You Died.",skin);
        gameOver.setFontScale(1.75f);
        gameOver.setStyle(new Label.LabelStyle(new BitmapFont(Gdx.files.internal("fonts/spooky.fnt")

        ),Color.RED));
        Label playerScore = new Label("Your Score: " + this.score,skin);
        playerScore.setFontScale(1.75f);
        playerScore.setStyle(new Label.LabelStyle(new BitmapFont((Gdx.files.internal("fonts/spooky.fnt"))),Color.RED));
//        TextButton gameOver = new TextButton("You Died.",skin);
//        TextButton playerScore = new TextButton("Your Score: " + player.getKills(),skin);
        TextButton mainMenuButton = new TextButton("Back to Main Menu", skin);

        table.add(gameOver).fillX().uniform();
        table.row().pad(10,0,10,0);
        table.add(playerScore).fillX().uniform();
        table.row().pad(10,0,10,0);
        table.add(mainMenuButton).fillX().uniform();
        table.row().pad(10,0,10,0);


        mainMenuButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.changeScreen(DeathMarch.MENU);
            }
        });
    }

}
