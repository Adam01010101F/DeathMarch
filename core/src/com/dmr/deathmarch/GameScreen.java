package com.dmr.deathmarch;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import com.dmr.deathmarch.npc.Goblin;
import com.dmr.deathmarch.weapons.BeamCannon;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Iterator;

public class GameScreen implements Screen {
    final DeathMarch game;
    private OrthographicCamera camera;
    private Texture pOneTex;
    private Texture pTwoTex;
    private Texture bmTex;
    private Texture lbTex;
    private Rectangle pOne;
    private Goblin pTwo;
    private BeamCannon beamCannon;
    private Array<Rectangle> projectiles;
    private long lastBeamShot;
    private Array<Goblin> goblins;
    private LogicModel lm;
    private Box2DDebugRenderer dbr;
    private Direction lastDirection[];

    public GameScreen(final DeathMarch game){
        this.game = game;
//        lm = new LogicModel();
//        dbr = new Box2DDebugRenderer();

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1280, 720);

        lbTex = new Texture(Gdx.files.internal("laserBeam.png"));
        bmTex = new Texture(Gdx.files.internal("BeamCannon.png"));
        pOneTex = new Texture(Gdx.files.internal("player1.png"));
        pTwoTex = new Texture(Gdx.files.internal("player2.png"));
        pOne = new Rectangle();
        pTwo = new Goblin();
        beamCannon = new BeamCannon();

        pOne.x = 1280/2 - 64/2;
        pOne.y = 720/2;
        pOne.width = 50;
        pOne.height = 50;

//        pTwo.x = 1280/2 - 16/2;
//        pTwo.y = 720/2;
//        pTwo.width = 120;
//        pTwo.height = 120;
        lastDirection = new Direction[2];       // Tracks the direction of the user.
        projectiles = new Array<Rectangle>();
        goblins = new Array<Goblin>();
        createGoblin();
    }
    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
//        lm.logicStep(delta);
        Gdx.gl.glClearColor(0,0.3f, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
//        dbr.render(lm.world, camera.combined);

        camera.update();

        lastDirection[0] = Direction.None;
        lastDirection[1] = Direction.None;

        // Bullet Physics|Destruction
        for(Iterator<Rectangle> iter = projectiles.iterator(); iter.hasNext();){
            Rectangle projectile = iter.next();
            projectile.x += 350 * Gdx.graphics.getDeltaTime();
            if(projectile.x>1280){
                iter.remove();
            }
            for(Goblin goblin: goblins){
                if(projectile.overlaps(goblin)){
                    goblin.takeDamage(beamCannon.getDamage());
                }
            }
        }

        // Goblin Destruction
        for(Iterator<Goblin> iter = goblins.iterator(); iter.hasNext();){
            Goblin goblin = iter.next();
            if(goblin.isDead() == true){
                iter.remove();
            }
        }

        game.batch.setProjectionMatrix(camera.combined);

        // Load Objects onto Screen
        game.batch.begin();
        game.font.draw(game.batch, "P1 x: "+pOne.x+" y: "+pOne.y, 100, 150);
        game.font.draw(game.batch, "Enum" + lastDirection[0]+" " +lastDirection[1], 100, 200);
        game.batch.draw(pOneTex, pOne.x, pOne.y);
        for(Rectangle goblin: goblins){
            game.batch.draw(pTwoTex, goblin.x, goblin.y);
        }
        for (Rectangle beam: projectiles){
            game.batch.draw(lbTex, beam.x, beam.y);
        }
        game.batch.end();

        //Player 1 Key bindings
        if(Gdx.input.isKeyPressed(Input.Keys.D)){
            pOne.x += 350 * Gdx.graphics.getDeltaTime();
            lastDirection[1] = Direction.Right;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.A)){
            pOne.x -= 350 * Gdx.graphics.getDeltaTime();
            lastDirection[1] = Direction.Left;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.W)){
            pOne.y += 350 * Gdx.graphics.getDeltaTime();
            lastDirection[0] = Direction.Up;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.S)){
            pOne.y -= 350 *Gdx.graphics.getDeltaTime();
            lastDirection[0] = Direction.Down;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.ENTER)){
            if(TimeUtils.nanoTime() - beamCannon.getLastBeamShot() > beamCannon.getCooldown()){
            projectiles.add(beamCannon.shoot(pOne, lastDirection));}
            beamCannon.setLastBeamShot(TimeUtils.nanoTime());
        }

        //Player Boundaries
        if(pOne.x<0){pOne.x = 0;}
        if(pOne.x>1280-120){pOne.x = 1280-120;}
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

//    public void shootBeam(){
//        Rectangle laserBeam = new Rectangle();
//        laserBeam.x = pOne.x;
//        laserBeam.y = pOne.y;
//        laserBeam.height = 32;
//        laserBeam.width = 64;
//        laserBeams.add(laserBeam);
//        lastBeamShot = TimeUtils.nanoTime();
//    }

    public void createGoblin(){
        Goblin goblin = new Goblin();
        goblin.x = 1280/2 - 16/2;
        goblin.y = 720/2;
        goblins.add(goblin);
    }
}
