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
    private Player pOne;
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

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1280, 720);

        lbTex = new Texture(Gdx.files.internal("laserBeam.png"));
        bmTex = new Texture(Gdx.files.internal("BeamCannon.png"));
        pOneTex = new Texture(Gdx.files.internal("player1.png"));
        pTwoTex = new Texture(Gdx.files.internal("player2.png"));
        Gobbi = new Goblin();

        //Player Creation
        pOne = new Player("Shredder", false, pOneTex);
        pOne.setPosition(1280/2f - 120/2f, 720/2f);
        pOne.setWeapon(new BeamCannon(bmTex));
//        pOne.scale(1.5f);
        pTwo = new Player("Donatello", false, pTwoTex);
        pTwo.setPosition((1280/2f -120/2f), 720/2f);

        // Ghetto Managers
        lastDirection = new Direction[2];       // Tracks the direction of the user.
        projectiles = new Array<Projectile>();
        goblins = new Array<Goblin>();

        createGoblin();
    }
    @Override
    public void show() {

    }

    //TODO: Fix textures. They judder because they aren't perfectly centered in png file.
    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0,0.3f, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();


        // Bullet Physics|Destruction
        for(Iterator<Projectile> iter = projectiles.iterator(); iter.hasNext();){
            Projectile projectile = iter.next();
            projectile.setPosition(projectile.getX() +350 * projectile.getxVel() * Gdx.graphics.getDeltaTime()
                    , projectile.getY() + 350 *projectile.getyVel() * Gdx.graphics.getDeltaTime());
            if(projectile.getX()>1280 | projectile.getY()>720 || projectile.getX() < 0 || projectile.getY() < 0){
                iter.remove();
            }
            //TODO:: Give ownership of projectile to count score.
            for(Goblin goblin: goblins){
                if(projectile.getBoundingRectangle().overlaps(goblin)){
                    goblin.takeDamage(beamCannon.getDamage());
                }
            }
        }

        // Goblin Destruction
        for(Iterator<Goblin> iter = goblins.iterator(); iter.hasNext();){
            Goblin goblin = iter.next();
            float playerX = pOne.getX();
            float playerY = pOne.getY();
            float x = playerX - goblin.getX();
            float y = playerY - goblin.getY();
            float distance = (float) Math.sqrt((x*x) - (y*y));
            if(goblin.isDead()){
                System.out.print("Goblin is dead.");
                iter.remove();
            }
            else
            {
                goblin.x = ((150*(x/distance)) * Gdx.graphics.getDeltaTime());
                goblin.y = ((150*(y/distance)) * Gdx.graphics.getDeltaTime());
                goblin.setRotation();
            }
        }

        game.batch.setProjectionMatrix(camera.combined);

        // Load Objects onto Screen
        game.batch.begin();
        game.font.draw(game.batch, "P2 x: "+pTwo.getX()+" y: "+pTwo.getY(), 100, 150);
        game.font.draw(game.batch, "Projectiles: " + projectiles.size, 100, 200);
        pOne.draw(game.batch);
        pTwo.draw(game.batch);
//        game.batch.draw(bmTex, pOne.getX(), pOne.getY()-8);
        pOne.getWeapon().draw(game.batch);
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
            pOne.clearDirections();
//            int condRot = (pOne.getWeapon().getRotation()/90)%2==0 ? -90: 90;
            if(Gdx.input.isKeyPressed(Input.Keys.W)){
                pOne.setRotation(90);
                pOne.setY(pOne.getY()+150*Gdx.graphics.getDeltaTime());
                pOne.setYDirection(Direction.Up);
                System.out.println(pOne.getWeapon().getRotation());

                if(((pOne.getWeapon().getRotation()/90)%2) == 0) {
                    pOne.getWeapon().rotate(90);
                }
                pOne.getWeapon().setPosition(pOne.getX()+64, pOne.getY()+92);
            }
            if(Gdx.input.isKeyPressed(Input.Keys.S)){
                pOne.setRotation(270);
                pOne.setY(pOne.getY()-150*Gdx.graphics.getDeltaTime());
                pOne.setYDirection(Direction.Down);
                System.out.println(pOne.getWeapon().getRotation());

                if(((pOne.getWeapon().getRotation()/90)%2) == 0) {
                    pOne.getWeapon().rotate(90);
                }
                pOne.getWeapon().setPosition(pOne.getX()-8, pOne.getY());
            }
            if(Gdx.input.isKeyPressed(Input.Keys.D)){
                pOne.setRotation(0);
                pOne.setX(pOne.getX()+150*Gdx.graphics.getDeltaTime());
                pOne.setXDirection(Direction.Right);
                System.out.println(pOne.getWeapon().getRotation());
                if(((pOne.getWeapon().getRotation()/90)%2) != 0) {

                    pOne.getWeapon().rotate(-90);
                }
                pOne.getWeapon().setPosition(pOne.getX(), pOne.getY());
            }
            if(Gdx.input.isKeyPressed(Input.Keys.A)){
                pOne.setRotation(180);
                pOne.setX(pOne.getX()-150*Gdx.graphics.getDeltaTime());
                pOne.setXDirection(Direction.Left);
                System.out.println(pOne.getWeapon().getRotation());
                if(((pOne.getWeapon().getRotation()/90)%2) != 0) {
                    pOne.getWeapon().rotate(-90);
                }
                pOne.getWeapon().setPosition(pOne.getX(), pOne.getY());
            }

        }
        if(Gdx.input.isKeyPressed(Input.Keys.ENTER)){
            if(TimeUtils.nanoTime() - pOne.getWeapon().getLastShot() > pOne.getWeapon().getCooldown()){
                projectiles.add(pOne.getWeapon().shoot(pOne, lbTex, pOne.getLastDirection()));
            }
        }

        //Player 2 Keybindings
        if(Gdx.input.isKeyPressed(Input.Keys.UP) || Gdx.input.isKeyPressed(Input.Keys.RIGHT) ||
                Gdx.input.isKeyPressed(Input.Keys.LEFT)|| Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            pTwo.clearDirections();
            if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
                pTwo.setRotation(90);
                pTwo.setY(pTwo.getY() + 150 * Gdx.graphics.getDeltaTime());
                pTwo.setYDirection(Direction.Up);
            }
            if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
                pTwo.setRotation(270);
                pTwo.setY(pTwo.getY() - 150 * Gdx.graphics.getDeltaTime());
                pTwo.setYDirection(Direction.Down);
            }
            if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
                pTwo.setRotation(0);
                pTwo.setX(pTwo.getX() + 150 * Gdx.graphics.getDeltaTime());
                pTwo.setXDirection(Direction.Right);
            }
            if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
                pTwo.setRotation(180);
                pTwo.setX(pTwo.getX() - 150 * Gdx.graphics.getDeltaTime());
                pTwo.setXDirection(Direction.Left);
            }
        }
        if(Gdx.input.isKeyPressed(Input.Keys.SPACE)){
            if(TimeUtils.nanoTime() - beamCannon.getLastShot() > beamCannon.getCooldown()){
//                beamCannon.setLastBeamShot(TimeUtils.nanoTime());
                projectiles.add(beamCannon.shoot(pTwo, lbTex, pTwo.getLastDirection()));
            }
        }


        //Player Boundaries
        if(pOne.getX()<0){pOne.setX(0);}
        if(pOne.getX()>1280-120){pOne.setX(1280-120);}

        checkBoundary(pOne);
        checkBoundary(pTwo);
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

    private void createGoblin(){
        Goblin goblin = new Goblin();
        goblin.x = 1280/2f - 16/2f;
        goblin.y = 720/2f;
        goblins.add(goblin);
    }

    private void checkBoundary(Player player){
        if(player.getX()<0)player.setX(0);
        if(player.getX()>1280-120)player.setX(1280-120);
    }
}
