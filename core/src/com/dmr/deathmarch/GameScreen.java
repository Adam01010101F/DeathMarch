package com.dmr.deathmarch;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import org.w3c.dom.css.Rect;

import java.util.Iterator;

public class GameScreen implements Screen {
    final DeathMarch game;
    private OrthographicCamera camera;
    private Texture pOneTex;
    private Texture pTwoTex;
    private Texture lbTex;
    private Rectangle pOne;
    private Rectangle pTwo;
    private Array<Rectangle> laserBeams;
    private long lastBeamShot;

    public GameScreen(final DeathMarch game){
        this.game = game;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1280, 720);

        lbTex = new Texture(Gdx.files.internal("laserBeam.png"));
        pOneTex = new Texture(Gdx.files.internal("player1.png"));
        pTwoTex = new Texture(Gdx.files.internal("player2.png"));
        pOne = new Rectangle();
        pTwo = new Rectangle();

        pOne.x = 1280/2 - 64/2;
        pOne.y = 720/2;
        pOne.width = 100;
        pOne.height = 100;

        pTwo.x = 1280/2 - 16/2;
        pTwo.y = 720/2;
        pTwo.width = 120;
        pTwo.height = 120;

        laserBeams = new Array<Rectangle>();
    }
    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0,0, 0, 0.3f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();

        for(Iterator<Rectangle> iter = laserBeams.iterator(); iter.hasNext();){
            Rectangle beam = iter.next();
            beam.x += 350*Gdx.graphics.getDeltaTime();
            if(beam.x>1280){
                iter.remove();
            }
            if(beam.overlaps(pTwo)){
                iter.remove();
            }
        }
        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();
        game.font.draw(game.batch, "P1 x: "+pOne.x+" y: "+pOne.y, 100, 150);
        game.batch.draw(pOneTex, pOne.x, pOne.y);
        game.batch.draw(pTwoTex, pTwo.x, pTwo.y);
        for (Rectangle beam: laserBeams){
            game.batch.draw(lbTex, beam.x, beam.y);
        }
        game.batch.end();

        //Player 1 Keybindings
        if(Gdx.input.isKeyPressed(Input.Keys.D)){
            pOne.x += 350 * Gdx.graphics.getDeltaTime();
        }
        if(Gdx.input.isKeyPressed(Input.Keys.A)){
            pOne.x -= 350 * Gdx.graphics.getDeltaTime();
        }
        if(Gdx.input.isKeyPressed(Input.Keys.W)){
            pOne.y += 350 * Gdx.graphics.getDeltaTime();
        }
        if(Gdx.input.isKeyPressed(Input.Keys.S)){
            pOne.y -= 350 *Gdx.graphics.getDeltaTime();
        }
        if(Gdx.input.isKeyPressed(Input.Keys.ENTER)){
            if(TimeUtils.nanoTime() - lastBeamShot >1000000000){
            shootBeam();}
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

    public void shootBeam(){
        Rectangle laserBeam = new Rectangle();
        laserBeam.x = pOne.x;
        laserBeam.y = pOne.y;
        laserBeam.height = 32;
        laserBeam.width = 64;
        laserBeams.add(laserBeam);
        lastBeamShot = TimeUtils.nanoTime();
    }
}
