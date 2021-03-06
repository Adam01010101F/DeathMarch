package com.dmr.deathmarch;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;

public class MainMenuScreen implements Screen {
    final DeathMarch game;
    public OrthographicCamera camera;
    public Stage stage;
    private Music bgm_Music;
    FreeTypeFontGenerator generator;
    FreeTypeFontParameter parameters;
    BitmapFont uiText;
    FreeTypeFontGenerator g;
    FreeTypeFontParameter p;
    BitmapFont textyText;


    public TiledTest testTile;

    public MainMenuScreen(final DeathMarch game) {
        this.game = game;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1280, 720);

        stage = new Stage(new ScreenViewport());

        //b = new SpriteBatch();
        //text for NPC
        generator = new FreeTypeFontGenerator((Gdx.files.internal("fonts/horrendo.ttf")));
        parameters = new FreeTypeFontParameter();
        parameters.size = 80;
        parameters.color= Color.RED;
//        parameters.borderColor = Color.RED;
//        parameters.borderWidth = 3;




        uiText = generator.generateFont(parameters);
        //start = TimeUtils.millis();

        generator.dispose();


        g = new FreeTypeFontGenerator((Gdx.files.internal("fonts/joystix.ttf")));
        p = new FreeTypeFontParameter();
        p.size = 20;
        p.color= Color.GREEN;
        p.borderColor = Color.RED;
        p.borderWidth = 3;

        textyText = g.generateFont(p);

        g.dispose();



    }

    @Override
    public void render(float delta) {
//        Gdx.gl.glClearColor(0.5f,0,0,1);
//        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
//
//        camera.update();
//        game.batch.setProjectionMatrix(camera.combined);
//
//        game.batch.begin();
//        game.font.draw(game.batch, "DEATH MARCH", 570, 600);
//        game.font.draw(game.batch, "Ready Player 1", 500, 200);
//        game.font.draw(game.batch, "Ready Player 2", 640, 200);
//        game.batch.end();
//
//        if(Gdx.input.isTouched()){
//            game.setScreen(new GameScreen(game));
//            dispose();
//        }

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
    public void show(){
        //music
        bgm_Music = Gdx.audio.newMusic(Gdx.files.internal("mainMenu.mp3"));
        bgm_Music.setLooping(true);
        bgm_Music.setVolume(0.5f);
        bgm_Music.play();

        stage.clear();
        Gdx.input.setInputProcessor(stage);

        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();

        // Create a table that fills the screen. Everything else will go inside this table.
        Table table = new Table();
        table.setFillParent(true);
        table.setDebug(true);
        stage.addActor(table);

        Skin skin = new Skin(Gdx.files.internal("skin/pixthulhu-ui.json"));

        TextButton newGame = new TextButton("Start Game", skin);
        TextButton preferences = new TextButton("Settings", skin);
        TextButton exit = new TextButton("Exit", skin);
        TextButton hof = new TextButton("Hall of Fame", skin);
        //

        //table.add(uiText.draw())

        table.add(newGame).fillX().uniformX();
        table.row().pad(10, 0, 10, 0);
        table.add(hof).fillX().uniformX();
        //table.row();
        table.row().pad(10, 0, 10, 0);
        table.add(preferences).fillX().uniformX();
        //table.row();
        table.row().pad(10, 0, 10, 0);
        table.add(exit).fillX().uniformX();

        exit.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                bgm_Music.stop();
                Gdx.app.exit();
            }
        });

        newGame.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                bgm_Music.stop();
                game.changeScreen(DeathMarch.APPLICATION);
            }
        });

        preferences.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor){
                bgm_Music.stop();
                game.changeScreen(DeathMarch.PREFERENCES);
            }

        });
        hof.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.changeScreen(DeathMarch.HOF);
            }
        });
    }
}
