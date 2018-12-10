package com.dmr.deathmarch;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;

public class GamesMasterScreen implements Screen {
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

    public GamesMasterScreen(final DeathMarch game) {
        this.game = game;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1280, 720);

        stage = new Stage(new ScreenViewport());

        //b = new SpriteBatch();
        //text for NPC
        generator = new FreeTypeFontGenerator((Gdx.files.internal("fonts/joystix.ttf")));
        parameters = new FreeTypeFontParameter();
        parameters.size = 120;
        parameters.color= Color.YELLOW;
//        parameters.borderColor = Color.RED;
//        parameters.borderWidth = 3;




        uiText = generator.generateFont(parameters);
        //start = TimeUtils.millis();

        generator.dispose();


        g = new FreeTypeFontGenerator((Gdx.files.internal("fonts/joystix.ttf")));
        p = new FreeTypeFontParameter();
        p.size = 20;
        p.color= Color.GREEN;
        //p.borderColor = Color.RED;
        //p.borderWidth = 3;

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


        game.batch.begin();


        //Vector3 posCamara = camera.position;
        //renderBackground();
        uiText.draw(game.batch, "GamesMaster",  +95 , 1000 );
        textyText.draw(game.batch,"Made by the DEATH MARCHERS for BIRDSTUFF INC.",255, 100);


        game.batch.end();



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
        //game.batch.dispose();
        stage.dispose();
    }

    @Override
    public void show(){
        //music
//        bgm_Music = Gdx.audio.newMusic(Gdx.files.internal("mainMenu.mp3"));
//        bgm_Music.setLooping(true);
//        bgm_Music.setVolume(0.5f);
//        bgm_Music.play();

        stage.clear();
        Gdx.input.setInputProcessor(stage);

        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();

        Table testTable = new Table();
        testTable.setBackground(new TextureRegionDrawable(new TextureRegion(new Texture("images/8bit.jpg"))));
        testTable.setFillParent(true);
        testTable.setDebug(true);
        stage.addActor(testTable);

        // Create a table that fills the screen. Everything else will go inside this table.
        Table table = new Table();
        table.setFillParent(true);
        table.setDebug(true);
        stage.addActor(table);

        Skin skin = new Skin(Gdx.files.internal("skin3/star-soldier-ui.json"));

        TextButton zombieGame = new TextButton("DeathMarch", skin);
        TextButton bunnyGame = new TextButton("Brazy Billiards", skin);
        TextButton sysexit = new TextButton("Exit", skin);
        //TextButton hof = new TextButton("Hall of Fame", skin);
        //

        //table.add(uiText.draw())

        table.add(zombieGame).fillX().uniformX();
        table.row().pad(10, 0, 10, 0);
        //table.add(hof).fillX().uniformX();
        //table.row();
        table.row().pad(10, 0, 10, 0);
        table.add(bunnyGame).fillX().uniformX();
        //table.row();
        table.row().pad(10, 0, 10, 0);
        table.add(sysexit).fillX().uniformX();

        sysexit.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                //bgm_Music.stop();
                Gdx.app.exit();
            }
        });

        zombieGame.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                //bgm_Music.stop();
                game.changeScreen(DeathMarch.MENU);
            }
        });

        bunnyGame.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor){
                //bgm_Music.stop();
                game.changeScreen(DeathMarch.BUNNIES);
            }

        });
    }
}

