package com.dmr.deathmarch;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
//import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.*;
import com.dmr.deathmarch.npc.Goblin;
import com.dmr.deathmarch.weapons.BeamCannon;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Iterator;

import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;

public class GameScreen implements Screen {
    public DeathMarch game;
    private long startTime;
    private OrthographicCamera camera;
    private Texture playerTex;
    private Texture pTwoTex;
    private Texture bmTex;
    private Texture lbTex;
    public static Player pOne;
    public static Player pTwo;
    private Goblin Gobbi;
    private BeamCannon beamCannon;
    public static Array<Projectile> projectiles;
    public static Array<Projectile> bile;
    private long lastBeamShot;
    public static Array<Goblin> goblins;
    private LogicModel lm;
    //private Box2DDebugRenderer dbr;
    public static Direction lastDirection[];
    private Stage stage;
    AssetManager assetManager;
    private TiledMap map;
    private TiledMapRenderer tiledMapRenderer;
    private Sprite beamProjectile;
    private Table table;
    private TextButton buttonPlay;
    private TextButton buttonQuit;
    private Rectangle npc;
    private Sprite stairs;
    private Texture npcTex;
    private boolean gamePaused;
    Skin shopSkin;
    private OrthographicCamera cam;
    private OrthogonalTiledMapRenderer renderer;
    private TiledMapTileLayer collisionLayer;
    private MapObjects objects;
    private String blockedKey = "blocked";
    private float increment;
    private Skin skin;

    private Vector3 screenCoords;

    private Dialog dialog;
    private int[] background = new int[]{0}, foreground = new int[]{1};
    private ShapeRenderer shape;
    //adding dialogue for npc
    FreeTypeFontGenerator generator;
    FreeTypeFontParameter parameters;
    BitmapFont uiText;

    //for the time
    private long start;
    private long diffTime;
    private String mapName;

    //Music
    private Music bgm_Music;
    private Sound sound;


    public GameScreen(final DeathMarch game, Player player1, Player player2) {
        this.game = game;
        mapName = "maps/billiards.tmx";
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1280, 1280);
        screenCoords = new Vector3();
        stage = new Stage(new FitViewport(1280, 1280));
        shopSkin = new Skin(Gdx.files.internal("skin/pixthulhu-ui.json"));

        pOne = player1;
        pTwo = player2;

        map = new TmxMapLoader().load(mapName);
        shape = new ShapeRenderer();
        collisionLayer = (TiledMapTileLayer) map.getLayers().get(1);

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

        skin = new Skin(Gdx.files.internal("skin/pixthulhu-ui.json"));
        shopSkin = new Skin(Gdx.files.internal("skin/pixthulhu-ui.json"));

        lbTex = new Texture(Gdx.files.internal("laserBeam.png"));

        bmTex = new Texture(Gdx.files.internal("BeamCannon.png"));

        pTwoTex = new Texture(Gdx.files.internal("player2.png"));
        Gobbi = new Goblin();
        //stage = new Stage(new ScreenViewport());

        //Player Creation
        //pOne = player1;
        pOne.setOriginCenter();
        pOne.setPosition(100, 100);
//        System.out.println(pOne.getOriginX()+ " " + pOne.getOriginY());
//        pOne.setColor(Color.GRAY);
        pOne.setScale(1 / 8f);
        pOne.setWeapon(new BeamCannon(bmTex));
        //pTwo = player2;
        pTwo.setPosition(1000, 900);
//        pTwo.setColor(Color.PURPLE);
//        pTwo.setScale(3/4f);
        pTwo.setHealth(150);
        //
        pTwo.setWeapon(new BeamCannon(bmTex));

        // Ghetto Managers
        //npc
        npcTex = new Texture(Gdx.files.internal("Stairs_up.png"));
        stairs = new Sprite(npcTex);

        sound = Gdx.audio.newSound(Gdx.files.internal("blaster.mp3"));


//        pTwo.x = 1280/2 - 16/2;
//        pTwo.y = 720/2;
//        pTwo.width = 120;
//        pTwo.height = 120;

        //doors
        stairs.setX(1150);
        stairs.setY(27);
        stairs.setScale(3f);


        lastDirection = new Direction[2];       // Tracks the direction of the user.
        projectiles = new Array<Projectile>();
        bile = new Array<Projectile>();
        goblins = new Array<Goblin>();


        //text for NPC
        generator = new FreeTypeFontGenerator((Gdx.files.internal("fonts/joystix.ttf")));
        parameters = new FreeTypeFontParameter();
        parameters.size = 20;
        parameters.color = Color.BLACK;
//        parameters.borderColor = Color.WHITE;
//        parameters.borderWidth = 0.5f;


        uiText = generator.generateFont(parameters);
        start = TimeUtils.millis();

        generator.dispose();

        startTime = System.currentTimeMillis();
    }


    public void create() {


    }

    @Override
    public void show() {

        startTime = System.currentTimeMillis();


        map = new TmxMapLoader().load(mapName);

        //float unitScale = 2f;
        map = new TmxMapLoader().load("maps/billiards.tmx");

        tiledMapRenderer = new OrthogonalTiledMapRenderer(map);
        bgm_Music = Gdx.audio.newMusic(Gdx.files.internal("battle1.mp3"));
        bgm_Music.setLooping(true);
        bgm_Music.setVolume(1.3f);
        bgm_Music.play();


    }

    //TODO: Fix textures. They judder because they aren't perfectly centered in png file.
    @Override
    public void render(float delta) {


        stage.clear();
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        Gdx.input.setInputProcessor(stage);

        // ---GAME CONTROLS---
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            bgm_Music.stop();
            //camera = new OrthographicCamera();
            //camera.setToOrtho(false, 1280, 720);
            //camera.update();
            game.changeScreen(DeathMarch.MENU);

        }
        // ------------------

        stage.draw();
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


        //table
        tiledMapRenderer.setView(camera);
        tiledMapRenderer.render(background);
        tiledMapRenderer.render(foreground);


        camera.update();
        // Bullet Physics|Destruction
        for (int i = 0; i < projectiles.size; i++) {
            Projectile proj = projectiles.get(i);
            proj.setPosition(proj.getX() + 350 * proj.getxVel() * Gdx.graphics.getDeltaTime()
                    , proj.getY() + 350 * proj.getyVel() * Gdx.graphics.getDeltaTime());
            if (collidesLeft(
                    proj.getBoundingRectangle().getWidth(),
                    proj.getBoundingRectangle().getHeight(),
                    proj.getBoundingRectangle().getX(),
                    proj.getBoundingRectangle().getY()
            )) {
                projectiles.removeIndex(i);
            } else if (collidesRight(
                    proj.getBoundingRectangle().getWidth(),
                    proj.getBoundingRectangle().getHeight(),
                    proj.getBoundingRectangle().getX(),
                    proj.getBoundingRectangle().getY()
            )) {
                projectiles.removeIndex(i);

            } else if (collidesBottom(
                    proj.getBoundingRectangle().getWidth(),
                    proj.getBoundingRectangle().getHeight(),
                    proj.getBoundingRectangle().getX(),
                    proj.getBoundingRectangle().getY()
            )) {
                projectiles.removeIndex(i);

            } else if (collidesTop(
                    proj.getBoundingRectangle().getWidth(),
                    proj.getBoundingRectangle().getHeight(),
                    proj.getBoundingRectangle().getX(),
                    proj.getBoundingRectangle().getY()
            )) {
                projectiles.removeIndex(i);

            } else {
                // do nothing
            }
            //TODO:: Give ownership of projectile to count score.
            for (Projectile biles : projectiles) {
                if (biles.getBoundingRectangle().overlaps(pTwo.getBoundingRectangle())) {
//                    goblin.takeDamage(beamCannon.getDamage());
                    projectiles.removeIndex(i);

                    System.out.println("Goblin takes damage");
                    pOne.addKill();
                    pOne.addHealth();
                    pTwo.takeDmg(5 * pOne.getDmgMulti());

                }
            }
        }



        // Bullet Physics|Destruction
        for(int i = 0; i < bile.size; i++) {
            Projectile proj = bile.get(i);
            proj.setPosition(proj.getX() + 350 * proj.getxVel() * Gdx.graphics.getDeltaTime()
                    , proj.getY() + 350 * proj.getyVel() * Gdx.graphics.getDeltaTime());
            if (collidesLeft(
                    proj.getBoundingRectangle().getWidth(),
                    proj.getBoundingRectangle().getHeight(),
                    proj.getBoundingRectangle().getX(),
                    proj.getBoundingRectangle().getY()
            )) {
                bile.removeIndex(i);
            } else if (collidesRight(
                    proj.getBoundingRectangle().getWidth(),
                    proj.getBoundingRectangle().getHeight(),
                    proj.getBoundingRectangle().getX(),
                    proj.getBoundingRectangle().getY()
            )) {
                bile.removeIndex(i);

            } else if (collidesBottom(
                    proj.getBoundingRectangle().getWidth(),
                    proj.getBoundingRectangle().getHeight(),
                    proj.getBoundingRectangle().getX(),
                    proj.getBoundingRectangle().getY()
            )) {
                bile.removeIndex(i);

            } else if (collidesTop(
                    proj.getBoundingRectangle().getWidth(),
                    proj.getBoundingRectangle().getHeight(),
                    proj.getBoundingRectangle().getX(),
                    proj.getBoundingRectangle().getY()
            )) {
                bile.removeIndex(i);

            } else {
                // do nothing
            }
            //TODO:: Give ownership of projectile to count score.
            for (Projectile biles : bile) {
                if (biles.getBoundingRectangle().overlaps(pOne.getBoundingRectangle())) {
//                    goblin.takeDamage(beamCannon.getDamage());
                    bile.removeIndex(i);

                    System.out.println("Player takes damage");
                    pTwo.addKill();
                    pTwo.addHealth(2);
                    pOne.takeDmg(5);
                }
            }

        }


//        // Bullet Physics|Destruction
//        for(Iterator<Projectile> iter = projectiles.iterator(); iter.hasNext();){
//                Projectile projectile = iter.next();
//                projectile.setPosition(projectile.getX() +350 * projectile.getxVel() * Gdx.graphics.getDeltaTime()
//                        , projectile.getY() + 350 * projectile.getyVel() * Gdx.graphics.getDeltaTime());
//                if(bulletCollidesLeft(projectile)){
//                    iter.remove();
//                }
//                else if(bulletCollidesRight(projectile)){
//                    iter.remove();
//                }
//                else if(bulletCollidesBottom(projectile)){
//                    iter.remove();
//                }
//                else if(bulletCollidesTop(projectile)){
//                    iter.remove();
//                }
//                else {
//                    // do nothing
//                }
//
//
//            //TODO:: Give ownership of projectile to count score.
//            for(Projectile proj: projectiles){
//                if(proj.getBoundingRectangle().overlaps(pTwo.getBoundingRectangle())){
////                    goblin.takeDamage(beamCannon.getDamage());
//                    iter.remove();
//                    System.out.println("Goblin takes damage");
//                    pOne.addKill();
//                }
//            }
//        }

        // Goblin Destruction
        Player goblin = pTwo;
        if (collidesTop(goblin.getBoundingRectangle().getWidth(),
                goblin.getBoundingRectangle().getHeight(),
                goblin.getBoundingRectangle().getX(),
                goblin.getBoundingRectangle().getY())) {
            if (!(collidesRight(goblin.getBoundingRectangle().getWidth(),
                    goblin.getBoundingRectangle().getHeight(),
                    goblin.getBoundingRectangle().getX(),
                    goblin.getBoundingRectangle().getY()))) {
                goblin.setX(goblin.getX() + (90) * Gdx.graphics.getDeltaTime());
            }
            else {
                goblin.setX(goblin.getX() - (90) * Gdx.graphics.getDeltaTime());
            }

        }
        else if (collidesRight(goblin.getBoundingRectangle().getWidth(),
                goblin.getBoundingRectangle().getHeight(),
                goblin.getBoundingRectangle().getX(),
                goblin.getBoundingRectangle().getY())) {
            if (!(collidesRight(goblin.getBoundingRectangle().getWidth(),
                    goblin.getBoundingRectangle().getHeight(),
                    goblin.getBoundingRectangle().getX(),
                    goblin.getBoundingRectangle().getY()))) {
                goblin.setY(goblin.getY() + (-90) * Gdx.graphics.getDeltaTime());
            }
            else {
                goblin.setY(goblin.getY() + (90) * Gdx.graphics.getDeltaTime());
            }
        }
        else if (collidesBottom(goblin.getBoundingRectangle().getWidth(),
                goblin.getBoundingRectangle().getHeight(),
                goblin.getBoundingRectangle().getX(),
                goblin.getBoundingRectangle().getY())) {
            if (!(collidesRight(goblin.getBoundingRectangle().getWidth(),
                    goblin.getBoundingRectangle().getHeight(),
                    goblin.getBoundingRectangle().getX(),
                    goblin.getBoundingRectangle().getY()))) {
                goblin.setX(goblin.getX() + (90) * Gdx.graphics.getDeltaTime());
            }
            else {
                goblin.setX(goblin.getX() - (90) * Gdx.graphics.getDeltaTime());
            }
        }
        else if (collidesLeft(goblin.getBoundingRectangle().getWidth(),
                goblin.getBoundingRectangle().getHeight(),
                goblin.getBoundingRectangle().getX(),
                goblin.getBoundingRectangle().getY())) {
            if (!(collidesRight(goblin.getBoundingRectangle().getWidth(),
                    goblin.getBoundingRectangle().getHeight(),
                    goblin.getBoundingRectangle().getX(),
                    goblin.getBoundingRectangle().getY()))) {
                goblin.setY(goblin.getY() + (-90) * Gdx.graphics.getDeltaTime());
        } else {
            float gX = goblin.getX();
            float gY = goblin.getY();
            float x = pOne.getX() - gX;
            float y = pOne.getY() - gY;
            float distance1 = (float) Math.sqrt((x*x)-(y*y));
            if (0 < distance1) {
                float acc1 = (x / distance1);
                float acc2 = (y / distance1);
                if (x > 0) {
                    acc1 = 1;
                } else if(x < 0){
                    acc1 = -1;
                }
                else {
                    acc1 = 0;
                }
                if (y > 0) {
                    acc2 = 1;
                } else if(y < 0){
                    acc2 = -1;
                }
                else
                {
                    acc2 = 0;
                }
                if (distance1 != 0) {
                    goblin.setX(gX + ((0 * acc1) * Gdx.graphics.getDeltaTime()));
                    goblin.setY(gY + ((20 * acc2) * Gdx.graphics.getDeltaTime()));

                }
                goblin.checkGob(pOne);
                float angle = (float) Math.toDegrees(Math.atan2(pOne.getY() - goblin.getY(), pOne.getX() - goblin.getX()));
                if (angle < 0) {
                    angle = angle + 360;
                }
                goblin.setRotation(angle);

            }
        }

        game.batch.setProjectionMatrix(camera.combined);


        // Load Objects onto Screen
        game.batch.begin();
        //sets up the timer
        Vector3 posCamara = camera.position;
        uiText.draw(game.batch, "Time: " + (300 - (System.currentTimeMillis() - startTime) / 1000), posCamara.x - 100, posCamara.y + 580);
        uiText.draw(game.batch, "Soldier Health: " + Math.round(pOne.getHealth()), posCamara.x - 600, posCamara.y + 580);
        uiText.draw(game.batch, "Soldier Points: " + Math.round(pOne.getKills()), posCamara.x - 600, posCamara.y + 560);
        uiText.draw(game.batch, "Zombie Health: " + Math.round(pTwo.getHealth()), posCamara.x + 200, posCamara.y + 580);
        uiText.draw(game.batch, "Zombie Points: " + Math.round(pTwo.getKills()), posCamara.x + 200, posCamara.y + 560);


        //NPC dialogue conditional statement
        diffTime = TimeUtils.timeSinceMillis(start);
        if (diffTime < 3000) {
            uiText.draw(game.batch, "Welcome to Death March!", 370, 40);
        }
        if (diffTime > 3000 && diffTime < 6000) {
            uiText.draw(game.batch, "Let's see if you survive HAHAHA!", 370, 40);
        }

        if (pOne.getBoundingRectangle().overlaps(stairs.getBoundingRectangle())) {
            uiText.draw(game.batch, "Press P to enter the store", 370, 150);
        }


        game.font.draw(game.batch, "P1 x: " + pOne.getX() + " y: " + pOne.getY(), 100, 150);
        game.font.draw(game.batch, "Projectiles: " + projectiles.size, 100, 200);
        game.font.draw(game.batch, "Projectiles: " + bile.size, 100, 100);
        pOne.draw(game.batch);
        pTwo.draw(game.batch);
        stairs.draw(game.batch);
//        game.batch.draw(bmTex, pOne.getX(), pOne.getY()-8);
//        pOne.getWeapon().draw(game.batch);

        //for (Sprite goblin : goblins) {
        //    game.batch.draw(pTwoTex, goblin.getX(), goblin.getY());
        //}
        for (Sprite beam : projectiles) {
            beam.draw(game.batch);
        }
        for (Sprite beam : bile) {
            beam.draw(game.batch);
        }
        game.batch.end();


        //shape.begin(ShapeRenderer.ShapeType.Filled);
        //shape.setColor(Color.BLACK);
        //shape.rect(pOne.getBoundingRectangle().getX(), pOne.getBoundingRectangle().getY(), pOne.getBoundingRectangle().getWidth(), pOne.getBoundingRectangle().getHeight());
        //shape.end();

        //Player 1 Keybindings
        if (Gdx.input.isKeyPressed(Input.Keys.W) || Gdx.input.isKeyPressed(Input.Keys.D) ||
                Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.S)) {
            pOne.clearDirections();
            if (Gdx.input.isKeyPressed(Input.Keys.W)) {
                if (collidesTop(
                        pOne.getBoundingRectangle().getWidth(),
                        pOne.getBoundingRectangle().getHeight(),
                        pOne.getBoundingRectangle().getX(),
                        pOne.getBoundingRectangle().getY())) {
                    pOne.setRotation(90);
                    pOne.setY(pOne.getY());
                    pOne.setYDirection(Direction.Up);
                } else {
                    pOne.setRotation(90);
                    pOne.setY(pOne.getY() + pOne.getSpeed() * Gdx.graphics.getDeltaTime());
                    pOne.setYDirection(Direction.Up);
                }
            }
            if (Gdx.input.isKeyPressed(Input.Keys.S)) {

                if (collidesBottom(
                        pOne.getBoundingRectangle().getWidth(),
                        pOne.getBoundingRectangle().getHeight(),
                        pOne.getBoundingRectangle().getX(),
                        pOne.getBoundingRectangle().getY()
                )) {
                    pOne.setRotation(270);
                    pOne.setY(pOne.getY());
                    pOne.setYDirection(Direction.Down);
                } else {
                    pOne.setRotation(270);
                    pOne.setY(pOne.getY() - pOne.getSpeed() * Gdx.graphics.getDeltaTime());
                    pOne.setYDirection(Direction.Down);
                }
            }
            if (Gdx.input.isKeyPressed(Input.Keys.D)) {
                if (collidesRight(
                        pOne.getBoundingRectangle().getWidth(),
                        pOne.getBoundingRectangle().getHeight(),
                        pOne.getBoundingRectangle().getX(),
                        pOne.getBoundingRectangle().getY()
                )) {
                    pOne.setRotation(0);
                    pOne.setX(pOne.getX());
                    pOne.setXDirection(Direction.Right);
                } else {

                    pOne.setRotation(0);
                    pOne.setX(pOne.getX() + pOne.getSpeed() * Gdx.graphics.getDeltaTime());
                    pOne.setXDirection(Direction.Right);
                }
            }
            if (Gdx.input.isKeyPressed(Input.Keys.A)) {
                if (collidesLeft(
                        pOne.getBoundingRectangle().getWidth(),
                        pOne.getBoundingRectangle().getHeight(),
                        pOne.getBoundingRectangle().getX(),
                        pOne.getBoundingRectangle().getY()
                )) {
                    pOne.setRotation(180);
                    pOne.setX(pOne.getX());
                    pOne.setXDirection(Direction.Left);
                } else {

                    pOne.setRotation(180);
                    pOne.setX(pOne.getX() - pOne.getSpeed() * Gdx.graphics.getDeltaTime());
                    pOne.setXDirection(Direction.Left);
                }

            }

        }
        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            if (TimeUtils.nanoTime() - pOne.getWeapon().getLastShot() > pOne.getWeapon().getCooldown()) {
                sound.play(1f);
                projectiles.add(pOne.getWeapon().shoot(pOne, lbTex, pOne.getLastDirection()));
            }
        }

        //enter Store
        if (Gdx.input.isKeyPressed(Input.Keys.P) && pOne.getBoundingRectangle().overlaps(stairs.getBoundingRectangle())) {
            bgm_Music.stop();
            game.changeScreen(DeathMarch.SHOP);
        }


        //Player 2 Keybindings
        if (Gdx.input.isKeyPressed(Input.Keys.UP) || Gdx.input.isKeyPressed(Input.Keys.RIGHT) ||
                Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.DOWN)) {

            pTwo.clearDirections();
            if (Gdx.input.isKeyPressed(Input.Keys.UP)) {

                if (collidesTop(
                        pTwo.getBoundingRectangle().getWidth(),
                        pTwo.getBoundingRectangle().getHeight(),
                        pTwo.getBoundingRectangle().getX(),
                        pTwo.getBoundingRectangle().getY()
                )) {
                    pTwo.setRotation(90);
                    pTwo.setY(pTwo.getY());
                    pOne.setYDirection(Direction.Up);
                } else {
                    pTwo.setRotation(90);
                    pTwo.setY(pTwo.getY() + 200 * Gdx.graphics.getDeltaTime());
                    pTwo.setYDirection(Direction.Up);
                }

            } else if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
                if (collidesBottom(
                        pTwo.getBoundingRectangle().getWidth(),
                        pTwo.getBoundingRectangle().getHeight(),
                        pTwo.getBoundingRectangle().getX(),
                        pTwo.getBoundingRectangle().getY()
                )) {
                    pTwo.setRotation(270);
                    pTwo.setY(pTwo.getY());
                    pTwo.setYDirection(Direction.Down);
                } else {
                    pTwo.setRotation(270);
                    pTwo.setY(pTwo.getY() - 200 * Gdx.graphics.getDeltaTime());
                    pTwo.setYDirection(Direction.Down);
                }
            } else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
                if (collidesRight(
                        pTwo.getBoundingRectangle().getWidth(),
                        pTwo.getBoundingRectangle().getHeight(),
                        pTwo.getBoundingRectangle().getX(),
                        pTwo.getBoundingRectangle().getY())) {
                    pTwo.setRotation(0);
                    pTwo.setX(pTwo.getX());
                    pTwo.setXDirection(Direction.Right);
                } else {

                    pTwo.setRotation(0);
                    pTwo.setX(pTwo.getX() + 200 * Gdx.graphics.getDeltaTime());
                    pTwo.setXDirection(Direction.Right);
                }
            } else if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
                if (collidesLeft(
                        pTwo.getBoundingRectangle().getWidth(),
                        pTwo.getBoundingRectangle().getHeight(),
                        pTwo.getBoundingRectangle().getX(),
                        pTwo.getBoundingRectangle().getY())) {
                    pTwo.setRotation(180);
                    pTwo.setX(pTwo.getX());
                    pTwo.setXDirection(Direction.Left);
                } else {

                    pTwo.setRotation(180);
                    pTwo.setX(pTwo.getX() - 200 * Gdx.graphics.getDeltaTime());
                    pTwo.setXDirection(Direction.Left);
                }
            }

        }
        if (Gdx.input.isKeyPressed(Input.Keys.ENTER)) {
            if (TimeUtils.nanoTime() - pTwo.getWeapon().getLastShot() > pTwo.getWeapon().getCooldown()) {
                bile.add(pTwo.getWeapon().shoot(pTwo, lbTex, pTwo.getLastDirection()));
            }
        }

        if (300 - (System.currentTimeMillis() - startTime) / 1000 == 0) {
            init();
            game.changeScreen(DeathMarch.MENU);
        }


        if (pTwo.isDead() && !pOne.isDead()) {
            bgm_Music.stop();
            game.changeScreen(DeathMarch.WIN);
            //init();

        }

        if (pOne.isDead()) {

            bgm_Music.stop();
            game.changeScreen(DeathMarch.LOSE);
            init();
        }

        checkBoundaries(pOne);
    }}

    private void checkBoundaries(Player player) {
        //Handles Y coords
        if(player.getY()+270<=0){   //Bottom of the Screen
            screenCoords = new Vector3(player.getX(), stage.getHeight(),0);
            stage.getViewport().project(screenCoords);
            player.setY(screenCoords.y);
            System.out.println("LowerBound Touched");
        } else if(player.getY()+160>=stage.getViewport().getWorldHeight()){ //Top of the Screen
            System.out.println("UpperBound Touched");
            screenCoords = new Vector3(player.getX(), 0, 0);
            stage.getViewport().project(screenCoords);
            player.setY(screenCoords.y-270);
        }
        //Handles X coords
        if(player.getX()+270<=0){       //Left of the Screen
            screenCoords = new Vector3(1280, player.getY(),0);
            stage.getViewport().unproject(screenCoords);
            player.setX(screenCoords.x-270);
        } else if(player.getX()+270>=stage.getWidth()){ //Right of the screen
            screenCoords = new Vector3(0, player.getY(),0);
            stage.getViewport().unproject(screenCoords);
            player.setX(screenCoords.x-270);
        }
    }

    @Override
        public void resize ( int width, int height){
            stage.getViewport().update(width, height, true);
        }

        @Override
        public void pause () {

        }

        @Override
        public void resume () {

        }

        @Override
        public void hide () {

        }

        @Override
        public void dispose () {
            stage.dispose();
            //camera.setToOrtho(false,1280,720);
            bgm_Music.dispose();


        }

    public static void init(){

        //game = new DeathMarch();
        lastDirection = new Direction[2];       // Tracks the direction of the user.
        projectiles = new Array<Projectile>();
        bile = new Array<Projectile>();
        goblins = new Array<Goblin>();

        //startTime = System.currentTimeMillis();
        pOne.resetHealth();
        pOne.resetKills();
        pTwo.resetHealth();
        pTwo.setHealth(150);
        pTwo.resetKills();
        pOne.setPosition(100,100);
        pTwo.setPosition(1000,900);

    }

    private boolean checkColl(float x , float y, Sprite p, Array<Goblin> g)
    {
        Sprite q = p;
        q.setX(q.getX() + x);
        q.setY(q.getY() + y);
        for(Iterator<Goblin> iter = g.iterator(); iter.hasNext();) {
            Goblin goblin = iter.next();
            if(p.getBoundingRectangle().overlaps(goblin.getBoundingRectangle()))
            {
                return false;
            }
        }
        return true;
    }
    private void createGoblin(){
        Goblin goblin = new Goblin();
        goblin.setX(pTwo.getX());
        goblin.setY(pTwo.getY());
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

    private boolean isCellBlocked(float x, float y) {
//        System.out.println("X: " + x);
//        System.out.println("Y : " + y );

        TiledMapTileLayer.Cell cell = collisionLayer.getCell((int) (x / collisionLayer.getTileWidth()), (int) (y / collisionLayer.getTileHeight()));

        return cell != null && cell.getTile() != null && cell.getTile().getProperties().containsKey(blockedKey);
    }

    public boolean collidesRight(float boundingWidth, float boundingHeight, float boundingX, float boundingY) {
        increment = collisionLayer.getTileWidth();
        increment = boundingWidth < increment ? boundingWidth / 2 : increment / 2;
        for(float step = 0; step <= boundingHeight; step += increment)
            if(isCellBlocked(boundingX + boundingWidth, boundingY + step)){
                return true;
            }
        return false;
    }

    public boolean collidesLeft(float boundingWidth, float boundingHeight, float boundingX, float boundingY) {
        increment = collisionLayer.getTileWidth();
        increment = boundingWidth < increment ? boundingWidth / 2 : increment / 2;
        for(float step = 0; step <= boundingHeight; step += increment)
            if(isCellBlocked(boundingX, boundingY + step)){
                return true;
            }
        return false;
    }

    public boolean collidesTop(float boundingWidth, float boundingHeight, float boundingX, float boundingY) {
        increment = collisionLayer.getTileHeight();
        increment = boundingHeight < increment ? boundingHeight / 2 : increment / 2;
        for(float step = 0; step <= boundingWidth; step += increment) {
            if (isCellBlocked(boundingX + step, boundingY + boundingHeight)){
                return true;
            }
        }
        return false;
    }

    public boolean collidesBottom(float boundingWidth, float boundingHeight, float boundingX, float boundingY) {
        // calculate the increment for step in #collidesLeft() and #collidesRight()
        increment = collisionLayer.getTileHeight();
        increment = boundingHeight < increment ? boundingHeight / 2 : increment / 2;
        for(float step = 0; step <= boundingWidth; step += increment)
            if(isCellBlocked(boundingX + step, boundingY)){
                return true;
            }
        return false;
    }


}