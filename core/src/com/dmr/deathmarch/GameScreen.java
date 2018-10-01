package com.dmr.deathmarch;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

public class GameScreen implements Screen {
    final DeathMarch game;
    private OrthographicCamera camera;
    private Texture pOneTex;
    private Texture pTwoTex;
    private Rectangle pOne;
    private Rectangle pTwo;
    private Sound useWeapon;

    public GameScreen(final DeathMarch game){
        this.game = game;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1280, 720);

        pOneTex = new Texture(Gdx.files.internal("player1.png"));
        pTwoTex = new Texture(Gdx.files.internal("player2.png"));
        pOne = new Rectangle();
        pTwo = new Rectangle();

        pOne.x = 1280/2 - 64/2;
        pOne.y = 720/2;
        pOne.width = 100;
        pOne.height = 100;

        pTwo.x = 1280/2 - 32/2;
        pTwo.y = 720/2;
        pTwo.width = 120;
        pTwo.height = 120;


    }
    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0,0, 0, 0.3f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();
        game.font.draw(game.batch, "P1 x: "+pOne.x+" y: "+pOne.y, 100, 150);
        game.batch.draw(pOneTex, pOne.x, pOne.y);
        game.batch.draw(pTwoTex, pTwo.x, pTwo.y);
        game.batch.end();

        //Player 1 Keybindings
        if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)){
            pOne.x += 350 * Gdx.graphics.getDeltaTime();
        }
        if(Gdx.input.isKeyPressed(Input.Keys.LEFT)){
            pOne.x -= 350 * Gdx.graphics.getDeltaTime();
        }
        if(Gdx.input.isKeyPressed(Input.Keys.UP)){
            pOne.y += 350 * Gdx.graphics.getDeltaTime();
        }
        if(Gdx.input.isKeyPressed(Input.Keys.DOWN)){
            pOne.y -= 350 *Gdx.graphics.getDeltaTime();
        }
        //Player Boundaries
        if(pOne.x<0){pOne.x = 0;}
        if(pOne.x>1280-64){pOne.x = 1280-64;}
    }

    @Override
    public void resize(int width, int height) {

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

    }
}
