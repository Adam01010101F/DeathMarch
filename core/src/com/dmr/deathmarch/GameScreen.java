package com.dmr.deathmarch;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
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
    private Player pTwo;
    private Goblin Gobbi;
    private BeamCannon beamCannon;
    private Array<Projectile> projectiles;
    private long lastBeamShot;
    private Array<Goblin> goblins;
    private LogicModel lm;
    private Box2DDebugRenderer dbr;
    private Direction lastDirection[];
    private Sprite beamProjectile;

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
        Gobbi = new Goblin();
        beamCannon = new BeamCannon();

        pOne.x = 1280/2 - 64/2;
        pOne.y = 720/2;
        pOne.width = 50;
        pOne.height = 50;

        pTwo = new Player("Adam", false, pTwoTex);
        pTwo.setPosition((1280/2 -120/2), 720/2);

//        pTwo.x = 1280/2 - 16/2;
//        pTwo.y = 720/2;
//        pTwo.width = 120;
//        pTwo.height = 120;
        lastDirection = new Direction[2];       // Tracks the direction of the user.
        projectiles = new Array<Projectile>();
        goblins = new Array<Goblin>();
        createGoblin();
    }
    @Override
    public void show() {

    }
    //TODO: Fix textures. They judder because they aren't perfectly centered.
    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0,0.3f, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();


        // Bullet Physics|Destruction
        for(Iterator<Projectile> iter = projectiles.iterator(); iter.hasNext();){
            Projectile projectile = iter.next();
            projectile.setPosition(projectile.getX() +350 * projectile.getxVel() * Gdx.graphics.getDeltaTime()
                    , projectile.getY() + 350 * projectile.getyVel() * Gdx.graphics.getDeltaTime());
            if(projectile.getX()>1280 | projectile.getY()>720 || projectile.getX() < 0 || projectile.getY() < 0){
                iter.remove();
            }
            for(Goblin goblin: goblins){
                if(projectile.getBoundingRectangle().overlaps(goblin)){
                    goblin.takeDamage(beamCannon.getDamage());
                }
            }
        }

        // Goblin Destruction
        for(Iterator<Goblin> iter = goblins.iterator(); iter.hasNext();){
            Goblin goblin = iter.next();
            if(goblin.isDead() == true){
                System.out.print("Goblin is dead.");
                iter.remove();
            }
        }

        game.batch.setProjectionMatrix(camera.combined);

        // Load Objects onto Screen
        game.batch.begin();
        game.font.draw(game.batch, "P2 x: "+pTwo.getX()+" y: "+pTwo.getY(), 100, 150);
        game.font.draw(game.batch, "Projectiles: " + projectiles.size, 100, 200);
        game.batch.draw(pOneTex, pOne.x, pOne.y);
        pTwo.draw(game.batch);
        game.batch.draw(bmTex, pOne.x, pOne.y-8);
        for(Rectangle goblin: goblins){
            game.batch.draw(pTwoTex, goblin.x, goblin.y);
        }
        for (Sprite beam: projectiles){
            beam.draw(game.batch);
        }
        game.batch.end();

        //Player 1 Keybindings
        if(Gdx.input.isKeyPressed(Input.Keys.W) || Gdx.input.isKeyPressed(Input.Keys.D) ||
                Gdx.input.isKeyPressed(Input.Keys.A)|| Gdx.input.isKeyPressed(Input.Keys.S)){
            lastDirection[0] = Direction.None;
            lastDirection[1] = Direction.None;
            if(Gdx.input.isKeyPressed(Input.Keys.D)){
                pOne.x += 150 * Gdx.graphics.getDeltaTime();
                lastDirection[0] = Direction.Right;
            }
            if(Gdx.input.isKeyPressed(Input.Keys.A)){
                pOne.x -= 150 * Gdx.graphics.getDeltaTime();
                lastDirection[0] = Direction.Left;
            }
            if(Gdx.input.isKeyPressed(Input.Keys.W)){
                pOne.y += 150 * Gdx.graphics.getDeltaTime();
                lastDirection[1] = Direction.Up;
            }
            if(Gdx.input.isKeyPressed(Input.Keys.S)){
                pOne.y -= 150 *Gdx.graphics.getDeltaTime();
                lastDirection[1] = Direction.Down;
            }

        }
        if(Gdx.input.isKeyPressed(Input.Keys.ENTER)){
            if(TimeUtils.nanoTime() - beamCannon.getLastBeamShot() > beamCannon.getCooldown()){
//                beamCannon.setLastBeamShot(TimeUtils.nanoTime());
                projectiles.add(beamCannon.shoot(pOne, lbTex, lastDirection));
            }
        }

        //Player 2 Keybindings
        if(Gdx.input.isKeyPressed(Input.Keys.UP)){
            pTwo.setRotation(90);
            pTwo.setPosition(pTwo.getX(), pTwo.getY()+150*Gdx.graphics.getDeltaTime());
        }
        if(Gdx.input.isKeyPressed(Input.Keys.DOWN)){
            pTwo.setRotation(270);
            pTwo.setPosition(pTwo.getX(), pTwo.getY()-150*Gdx.graphics.getDeltaTime());
        }
        if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)){
            pTwo.setRotation(0);
            pTwo.setPosition(pTwo.getX()+150*Gdx.graphics.getDeltaTime(), pTwo.getY());
        }
        if(Gdx.input.isKeyPressed(Input.Keys.LEFT)){
            pTwo.setRotation(180);
            pTwo.setPosition(pTwo.getX()-150*Gdx.graphics.getDeltaTime(), pTwo.getY());
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

    public void createGoblin(){
        Goblin goblin = new Goblin();
        goblin.x = 1280/2 - 16/2;
        goblin.y = 720/2;
        goblins.add(goblin);
    }
}
