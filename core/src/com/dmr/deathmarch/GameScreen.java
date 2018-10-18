package com.dmr.deathmarch;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
//import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
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
    private Array<Projectile> projectiles;
    private long lastBeamShot;
    private Array<Goblin> goblins;
    private LogicModel lm;
    //private Box2DDebugRenderer dbr;
    private Direction lastDirection[];
    private Stage stage;
    AssetManager assetManager;
    TiledMap tiledMap;
    TiledMapRenderer tiledMapRenderer;

    private OrthographicCamera cam;
    private OrthogonalTiledMapRenderer renderer;
    private TiledMap map;

    private Table table;

    private Skin skin;

    private Dialog dialog;



    public GameScreen(final DeathMarch game){
        this.game = game;
//        lm = new LogicModel();
//        dbr = new Box2DDebugRenderer();

          camera = new OrthographicCamera();
          camera.setToOrtho(false, 1280, 720);

          stage = new Stage(new ScreenViewport());








        //TiledMap map = new TmxMapLoader().load("demoMap.tmx");

//        // only needed once
//        assetManager.setLoader(TiledMap.class, new TmxMapLoader(new InternalFileHandleResolver()));
//        assetManager.load("level1.tmx", TiledMap.class);
//
//// once the asset manager is done loading
//        TiledMap map = assetManager.get("level1.tmx");
//
//        float unitScale = 1 / 16f;
//        OrthogonalTiledMapRenderer renderer = new OrthogonalTiledMapRenderer(map, unitScale);

        //OrthographicCamera camera = new OrthographicCamera();
        //camera.setToOrtho(true);

        Skin skin = new Skin(Gdx.files.internal("skin/pixthulhu-ui.json"));


        lbTex = new Texture(Gdx.files.internal("laserBeam.png"));
        bmTex = new Texture(Gdx.files.internal("BeamCannon.png"));
        pOneTex = new Texture(Gdx.files.internal("player1.png"));
        pTwoTex = new Texture(Gdx.files.internal("player2.png"));
        pOne = new Rectangle();
        pTwo = new Goblin();
        beamCannon = new BeamCannon();

//        Window pause = new Window("Paused", skin);
//        //pause.setMoveable(false); //So the user can't move the window
//        pause.add(new TextButton("Unpause", skin)); //Add a new text button that unpauses the game.
//        pause.pack(); //Important! Correctly scales the window after adding new elements.
//        float newWidth = 400, newHeight = 200;
//        pause.setBounds((Gdx.graphics.getWidth() - newWidth ) / 2,
//                (Gdx.graphics.getHeight() - newHeight ) / 2, newWidth , newHeight ); //Center on screen.
//        stage.addActor(pause);



        pOne.x = 1280/2 - 64/2;
        pOne.y = 720/2;
        pOne.width = 50;
        pOne.height = 50;

//        pTwo.x = 1280/2 - 16/2;
//        pTwo.y = 720/2;
//        pTwo.width = 120;
//        pTwo.height = 120;
        lastDirection = new Direction[2];       // Tracks the direction of the user.
        projectiles = new Array<Projectile>();
        goblins = new Array<Goblin>();
        createGoblin();
    }


    public void create(){

    }
    @Override
    public void show() {
        //TiledMap map = new TmxMapLoader().load("demoMap.tmx");





    }

    @Override
    public void render(float delta) {


        stage.clear();
        Gdx.input.setInputProcessor(stage);
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();





//        lm.logicStep(delta);
        Gdx.gl.glClearColor(0,0.3f, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
//        dbr.render(lm.world, camera.combined);

        camera.update();


        // Bullet Physics|Destruction
        for(Iterator<Projectile> iter = projectiles.iterator(); iter.hasNext();){
            Projectile projectile = iter.next();
            projectile.x += 350* projectile.getxVel()* Gdx.graphics.getDeltaTime();
            projectile.y += 350 * projectile.getyVel() * Gdx.graphics.getDeltaTime();
            if(projectile.x>1280 | projectile.y>720 || projectile.x < 0 || projectile.y < 0){
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
        game.font.draw(game.batch, "Projectiles: " + delta/100, 100, 200);
        game.batch.draw(pOneTex, pOne.x, pOne.y);
        for(Rectangle goblin: goblins){
            game.batch.draw(pTwoTex, goblin.x, goblin.y);
        }
        for (Rectangle beam: projectiles){
            game.batch.draw(lbTex, beam.x, beam.y);
        }
        game.batch.end();

        //Player 1 Key bindings
        if(Gdx.input.isKeyPressed(Input.Keys.W) || Gdx.input.isKeyPressed(Input.Keys.D) ||
                Gdx.input.isKeyPressed(Input.Keys.A)|| Gdx.input.isKeyPressed(Input.Keys.S)){
            lastDirection[0] = Direction.None;
            lastDirection[1] = Direction.None;
            if(Gdx.input.isKeyPressed(Input.Keys.D)){
                pOne.x += 350 * Gdx.graphics.getDeltaTime();
                lastDirection[0] = Direction.Right;
            }
            if(Gdx.input.isKeyPressed(Input.Keys.A)){
                pOne.x -= 350 * Gdx.graphics.getDeltaTime();
                lastDirection[0] = Direction.Left;
            }
            if(Gdx.input.isKeyPressed(Input.Keys.W)){
                pOne.y += 350 * Gdx.graphics.getDeltaTime();
                lastDirection[1] = Direction.Up;
            }
            if(Gdx.input.isKeyPressed(Input.Keys.S)){
                pOne.y -= 350 *Gdx.graphics.getDeltaTime();
                lastDirection[1] = Direction.Down;
            }}
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

    public void showDialog() {


        dialog = new Dialog("Quit?", skin) {

            @Override
            protected void result(Object object) {
                boolean exit = (Boolean) object;
                if (exit) {
                    Gdx.app.exit();
                } else {
                    remove();
                }
            }

        };
        dialog.button("Yes", true);
        dialog.button("No", false);
        dialog.key(Input.Keys.ENTER, true);
        dialog.key(Input.Keys.ESCAPE, false);
        dialog.show(stage);
    }
}