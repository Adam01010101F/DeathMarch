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
import com.dmr.deathmarch.npc.Bunny;
import com.dmr.deathmarch.npc.Goblin;
import com.dmr.deathmarch.weapons.BeamCannon;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

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
    public static Bunny bunny;
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
    private int CoinCount;

    private Sprite[] coin;
    private Texture coinSkin;

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



    public GameScreen(final DeathMarch game,Player player1,Player player2){

        coin = new Sprite[4];
        this.game = game;
        mapName = "maps/billiards.tmx";
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1280, 1200);
        screenCoords = new Vector3();
        stage = new Stage(new FitViewport(1280,1200));
        shopSkin = new Skin(Gdx.files.internal("skin/pixthulhu-ui.json"));

        pOne = player1;
        pTwo = player2;
        bunny = new Bunny(pTwo.getTexture());

        CoinCount = 0;


        map = new TmxMapLoader().load(mapName);

        shape = new ShapeRenderer();

        collisionLayer = (TiledMapTileLayer) map.getLayers().get(1);


        skin = new Skin(Gdx.files.internal("skin/pixthulhu-ui.json"));

        shopSkin = new Skin(Gdx.files.internal("skin/pixthulhu-ui.json"));

        lbTex = new Texture(Gdx.files.internal("fireball.png"));

        bmTex = new Texture(Gdx.files.internal("BeamCannon.png"));

        pTwoTex = new Texture(Gdx.files.internal("player2.png"));

        coinSkin = new Texture(Gdx.files.internal("coin.png"));

        ArrayList <Tuple> posBotRight = new ArrayList<Tuple>();
        ArrayList <Tuple> posTopRight = new ArrayList<Tuple>();
        ArrayList <Tuple> posBotLeft = new ArrayList<Tuple>();
        ArrayList <Tuple> posTopLeft = new ArrayList<Tuple>();

        posBotLeft.add(new Tuple(150,95)); //0//
        posBotLeft.add(new Tuple(50,250)); //1//
        posBotLeft.add(new Tuple(75,75)); //2//
        posBotLeft.add(new Tuple(150,200)); //3//

        posTopLeft.add(new Tuple(100,650)); //4
        posTopLeft.add(new Tuple(100,800)); //5//
        posTopLeft.add(new Tuple(125,875));//6
        posTopLeft.add(new Tuple(10,850));//7//

        posBotRight.add(new Tuple(800,100)); //8//
        posBotRight.add(new Tuple(900,100));//9
        posBotRight.add(new Tuple(600,250)); //10//
        posBotRight.add(new Tuple(750,300));//11//

        posTopRight.add(new Tuple(750,800));//12//
        posTopRight.add(new Tuple(1000,800));//13//
        posTopRight.add(new Tuple(950,850));//14//
        posTopRight.add(new Tuple(1000,900));//15

        Random rand = new Random();

        //int n = rand.nextInt(15) + 0;


        int PosX = 100;
        int PosY = 100;
        int arrInd = 3;


        for(int x = 0; x<=3; x++){

                coin[x] = new Sprite(coinSkin);

                coin[x].setScale(1/6f);

                int n = rand.nextInt(arrInd) + 0;

                if(x == 0) {
                    coin[x].setPosition(posBotLeft.get(n).getX(), posBotLeft.get(n).getY());
                    System.out.println("the positions used are" + posBotLeft.get(n).getX() + " " + posBotLeft.get(n).getY());
                }
                else if(x == 1) {
                    coin[x].setPosition(posTopLeft.get(n).getX(), posTopLeft.get(n).getY());
                    System.out.println("the positions used are" + posTopLeft.get(n).getX() + " " + posTopLeft.get(n).getY());

                }
                else if(x == 2) {
                    coin[x].setPosition(posBotRight.get(n).getX(), posBotRight.get(n).getY());
                    System.out.println("the positions used are" + posBotRight.get(n).getX() + " " + posBotRight.get(n).getY());

                }
                else if(x == 3) {
                    coin[x].setPosition(posTopRight.get(n).getX(), posTopRight.get(n).getY());
                    System.out.println("the positions used are" + posTopRight.get(n).getX() + " " + posTopRight.get(n).getY());


                }
                //System.out.println("the positions used are" + positions.get(n).getX() + " " + positions.get(n).getY());

//
//            positions.remove(n);
//            arrInd = arrInd -1;


        }






        //System.out.println(coin.getOriginX()+ " " + coin.getOriginY());
//       pOne.setColor(Color.GRAY);


        Gobbi = new Goblin();
        //stage = new Stage(new ScreenViewport());

        //Player Creation
        pOne.setOriginCenter();
        pOne.setPosition(150,150);
        pOne.setScale(1 / 8f);
        pOne.setWeapon(new BeamCannon(bmTex));
        //
        pTwo.setPosition(1000, 900);
        pTwo.setHealth(150);
        pTwo.setScale(2f);
        pTwo.setWeapon(new BeamCannon(bmTex));
        //
        bunny.setOriginCenter();
        bunny.setPosition(350,134);

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
            game.changeScreen(DeathMarch.BUNNIES);

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
        for (Iterator<Projectile> iter = projectiles.iterator(); iter.hasNext(); ) {
            Projectile projectile = iter.next();
            projectile.rotate(-20);
            checkBoundaries(projectile);
            projectile.setPosition(projectile.getX() + 350 * projectile.getxVel() * Gdx.graphics.getDeltaTime()
                    , projectile.getY() + 350 * projectile.getyVel() * Gdx.graphics.getDeltaTime());
//            if(projectile.getX()>1280 | projectile.getY()>720 || projectile.getX() < 0 || projectile.getY() < 0){
//                iter.remove();
//            }
            if (collidesLeft(
                    projectile.getBoundingRectangle().getWidth(),
                    projectile.getBoundingRectangle().getHeight(),
                    projectile.getBoundingRectangle().getX(),
                    projectile.getBoundingRectangle().getY()
            )) {
                if (projectile.getBounceCount() == 20) {
                    iter.remove();
                }

                projectile.bounce(projectile.getxVel(), projectile.getyVel(), 1);

            } else if (collidesRight(
                    projectile.getBoundingRectangle().getWidth(),
                    projectile.getBoundingRectangle().getHeight(),
                    projectile.getBoundingRectangle().getX(),
                    projectile.getBoundingRectangle().getY()
            )) {
                if (projectile.getBounceCount() == 20) {
                    iter.remove();
                }

                projectile.bounce(projectile.getxVel(), projectile.getyVel(), 2);

            } else if (collidesBottom(
                    projectile.getBoundingRectangle().getWidth(),
                    projectile.getBoundingRectangle().getHeight(),
                    projectile.getBoundingRectangle().getX(),
                    projectile.getBoundingRectangle().getY()
            )) {
                if (projectile.getBounceCount() == 20) {
                    iter.remove();
                }

                projectile.bounce(projectile.getxVel(), projectile.getyVel(), 3);

            } else if (collidesTop(
                    projectile.getBoundingRectangle().getWidth(),
                    projectile.getBoundingRectangle().getHeight(),
                    projectile.getBoundingRectangle().getX(),
                    projectile.getBoundingRectangle().getY()
            )) {
                if (projectile.getBounceCount() == 20) {
                    iter.remove();
                }

                projectile.bounce(projectile.getxVel(), projectile.getyVel(), 4);
            }
            /*//Bunny hit Player 2 Hit
            //This was for when Player Bunny had projectiles. Had to test that it could work, shoot itself.
            if (projectile.getBoundingRectangle().overlaps(pTwo.getBoundingRectangle())) {
//                    goblin.takeDamage(beamCannon.getDamage());
                iter.remove();

                System.out.println("Goblin has been hit");


               *//* pTwo.transform();*//*


            }*/

            if (projectile.getBoundingRectangle().overlaps(pTwo.getBoundingRectangle())) {
//                    goblin.takeDamage(beamCannon.getDamage());
                    iter.remove();
                    pOne.addKill();
                    pOne.addHealth();
                    pTwo.takeDmg(5 * pOne.getDmgMulti());
                }
        }

        // Bullet Physics|Destruction
        for (int i = 0; i < bile.size; i++) {
            Projectile proj = bile.get(i);
            proj.rotate(-20);
            checkBoundaries(proj);
            proj.setPosition(proj.getX() + 350 * proj.getxVel() * Gdx.graphics.getDeltaTime()
                    , proj.getY() + 350 * proj.getyVel() * Gdx.graphics.getDeltaTime());

            if (collidesLeft(
                    proj.getBoundingRectangle().getWidth(),
                    proj.getBoundingRectangle().getHeight(),
                    proj.getBoundingRectangle().getX(),
                    proj.getBoundingRectangle().getY()
            )) {
                if(proj.getBounceCount() == 20)
                    bile.removeIndex(i);
                else {
                    proj.bounce(proj.getxVel(), proj.getyVel(), 1);
                }
            } else if (collidesRight(
                    proj.getBoundingRectangle().getWidth(),
                    proj.getBoundingRectangle().getHeight(),
                    proj.getBoundingRectangle().getX(),
                    proj.getBoundingRectangle().getY()
            )) {
                if(proj.getBounceCount() == 20)
                    bile.removeIndex(i);
                else {
                    proj.bounce(proj.getxVel(), proj.getyVel(), 2);
                }

            } else if (collidesBottom(
                    proj.getBoundingRectangle().getWidth(),
                    proj.getBoundingRectangle().getHeight(),
                    proj.getBoundingRectangle().getX(),
                    proj.getBoundingRectangle().getY()
            )) {
                if (proj.getBounceCount() == 20) {
                    bile.removeIndex(i);
                }

                proj.bounce(proj.getxVel(), proj.getyVel(), 3);

            } else if (collidesTop(
                    proj.getBoundingRectangle().getWidth(),
                    proj.getBoundingRectangle().getHeight(),
                    proj.getBoundingRectangle().getX(),
                    proj.getBoundingRectangle().getY()
            )) {
                if (proj.getBounceCount() == 20) {
                    bile.removeIndex(i);
                }

                proj.bounce(proj.getxVel(), proj.getyVel(), 4);

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

        // Goblin AI
        if (collidesTop(bunny.getBoundingRectangle().getWidth(),
                bunny.getBoundingRectangle().getHeight(),
                bunny.getBoundingRectangle().getX(),
                bunny.getBoundingRectangle().getY())) {
            if (!(collidesRight(bunny.getBoundingRectangle().getWidth(),
                    bunny.getBoundingRectangle().getHeight(),
                    bunny.getBoundingRectangle().getX(),
                    bunny.getBoundingRectangle().getY()))) {
                bunny.setX(bunny.getX() + (20) * Gdx.graphics.getDeltaTime());
            }
            else {
                bunny.setX(bunny.getX() - (20) * Gdx.graphics.getDeltaTime());
            }

        }
        else if (collidesRight(bunny.getBoundingRectangle().getWidth(),
                bunny.getBoundingRectangle().getHeight(),
                bunny.getBoundingRectangle().getX(),
                bunny.getBoundingRectangle().getY())) {
            if (!(collidesRight(bunny.getBoundingRectangle().getWidth(),
                    bunny.getBoundingRectangle().getHeight(),
                    bunny.getBoundingRectangle().getX(),
                    bunny.getBoundingRectangle().getY()))) {
                bunny.setY(bunny.getY() + (-20) * Gdx.graphics.getDeltaTime());
            }
            else {
                bunny.setY(bunny.getY() + (20) * Gdx.graphics.getDeltaTime());
            }
        }
        else if (collidesBottom(bunny.getBoundingRectangle().getWidth(),
                bunny.getBoundingRectangle().getHeight(),
                bunny.getBoundingRectangle().getX(),
                bunny.getBoundingRectangle().getY())) {
            if (!(collidesRight(bunny.getBoundingRectangle().getWidth(),
                    bunny.getBoundingRectangle().getHeight(),
                    bunny.getBoundingRectangle().getX(),
                    bunny.getBoundingRectangle().getY()))) {
                bunny.setX(bunny.getX() + (20) * Gdx.graphics.getDeltaTime());
            }
            else {
                bunny.setX(bunny.getX() - (20) * Gdx.graphics.getDeltaTime());
            }
        }
        else if (collidesLeft(bunny.getBoundingRectangle().getWidth(),
                bunny.getBoundingRectangle().getHeight(),
                bunny.getBoundingRectangle().getX(),
                bunny.getBoundingRectangle().getY())) {
            if (!(collidesRight(bunny.getBoundingRectangle().getWidth(),
                    bunny.getBoundingRectangle().getHeight(),
                    bunny.getBoundingRectangle().getX(),
                    bunny.getBoundingRectangle().getY()))) {
                bunny.setY(bunny.getY() + (-20) * Gdx.graphics.getDeltaTime());
            }
            else {
                bunny.setY(bunny.getY() + (20) * Gdx.graphics.getDeltaTime());
            }
        } else {
//            System.out.println("Touched AI");

            float gX = bunny.getX();
            float gY = bunny.getY();
            float difX = pOne.getX() - gX;
            float difY = pOne.getY() - gY;
            float distance1 = (float) Math.sqrt((difX*difX)-(difY*difY));
            if (Math.abs(distance1) > 0) {
                float acc1;
                float acc2;
                if (difX >= 0) {
                    acc1 = 1;
                } else {
                    acc1 = -1;
                }
                if (difY >= 0) {
                    acc2 = 1;
                } else {
                    acc2 = -1;
                }
//                if (distance1 != 0) {
                    System.out.println(" gY:("+gX+", "+gY+")"+ " pOne:("+pOne.getX()+", "+pOne.getY()+")"
                            +" \ndifX: " + difX + " difY: " + difY + " dist: " + distance1);
//                            +"\nBunny Acc-> X:" + acc1 +" Y:" + acc2);
                bunny.setX(gX + ((20 * acc1) * Gdx.graphics.getDeltaTime()));
                bunny.setY(gY + ((20 * acc2) * Gdx.graphics.getDeltaTime()));

//                }
//                goblin.checkGob(pOne);
                float angle = (float) Math.toDegrees(Math.atan2(pOne.getY() - bunny.getY(), pOne.getX() - bunny.getX()));
                if (angle < 0) {
                    angle = angle + 360;
                }
                bunny.setRotation(angle);

            }
        }

        game.batch.setProjectionMatrix(camera.combined);
        stage.getBatch().setProjectionMatrix(camera.combined);

        // Load Objects onto Screen
        game.batch.begin();
        //sets up the timer
        Vector3 posCamara = camera.position;
        uiText.draw(game.batch, "Time: " + (500 - (System.currentTimeMillis() - startTime) / 1000), posCamara.x - 100, posCamara.y + 580);
        uiText.draw(game.batch, "Player Health: " + Math.round(pOne.getHealth()), posCamara.x - 600, posCamara.y + 580);
        uiText.draw(game.batch, "Player Points: " + CoinCount, posCamara.x - 600, posCamara.y + 560);
//        uiText.draw(game.batch, "Zombie Health: " + Math.round(pTwo.getHealth()), posCamara.x + 200, posCamara.y + 580);
//        uiText.draw(game.batch, "Zombie Points: " + Math.round(pTwo.getKills()), posCamara.x + 200, posCamara.y + 560);


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
        game.font.draw(game.batch,"P2 x:("+bunny.getX()+", "+bunny.getY()+")", 100, 250);
        game.font.draw(game.batch, "Projectiles: " + projectiles.size, 100, 200);
        game.font.draw(game.batch, "Projectiles: " + bile.size, 100, 100);
        pOne.draw(game.batch);
        pTwo.draw(game.batch);
        bunny.draw(game.batch);

        stairs.draw(game.batch);
//        game.batch.draw(bmTex, pOne.getX(), pOne.getY()-8);
//        pOne.getWeapon().draw(game.batch);
        boolean run = true;

        for(int i = 0; i<=3; i++){
            coin[i].draw(game.batch);
        }


        for (int y = 0; y <= 3; y++) {
                if (coin[y].getBoundingRectangle().overlaps(pOne.getBoundingRectangle())) {
                    coin[y].setPosition(10000, 10000);
                    CoinCount = CoinCount + 1;
                    System.out.println("coin count : " + CoinCount);
                }
        }

        if (CoinCount == 4) {
            dispose();
            game.changeScreen(DeathMarch.GIF);
        }

        for (Sprite gob : goblins) {
            game.batch.draw(pTwoTex, gob.getX(), gob.getY());
        }
        for (Sprite beam : projectiles) {
            beam.draw(game.batch);
        }
        for (Sprite beam : bile) {
            beam.draw(game.batch);
        }
        game.batch.end();

        for(Projectile proj: projectiles){
            shape.begin(ShapeRenderer.ShapeType.Filled);
            shape.setColor(Color.BLACK);
            shape.rect(proj.getX(), proj.getY(), proj.getWidth(), proj.getHeight());
            shape.end();
        }

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

        if (500 - (System.currentTimeMillis() - startTime) / 1000 == 0) {
            init();
            stage.dispose();
            dispose();
            game.changeScreen(DeathMarch.GAMESMASTER);
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
    }

//    private void checkBoundaries(Player player) {
//        //Handles Y coords
//        if(player.getY()+270<0){                                                   //Bottom of the Screen
//            screenCoords = new Vector3(player.getX(), stage.getHeight(),0);
//            stage.getViewport().project(screenCoords);
//            player.setY(screenCoords.y-150);
//            System.out.println("Bot->Player: "+ player.getY() + " Player+Off: " + (player.getY()-150)
//                    +" ScreenCoords: " + screenCoords.y +" ScreenCoords+Off: " + (screenCoords.y-150));
//        } else if(player.getY()+160>=stage.getViewport().getWorldHeight()){        //Top of the Screen
//            screenCoords = new Vector3(player.getX(), 0, 0);
//            stage.getViewport().project(screenCoords);
//            player.setY(screenCoords.y-270);
//            System.out.println("Top->Player: " + player.getY() + " Player+Off: "+ (player.getY()-270)
//                    +" ScreenCoords: " + screenCoords.y + " ScreenCoords+Off" + (screenCoords.y-270));
//        }
//        //Handles X coords
//        if(player.getX()+270<0){                                                  //Left of the Screen
//            screenCoords = new Vector3(1280, player.getY(),0);
//            stage.getViewport().unproject(screenCoords);
//            player.setX(screenCoords.x-270);
//            System.out.println("Left->Player: "+ player.getX() + " Player+Off: " + (player.getX()-150)
//                    +" ScreenCoords: " + screenCoords.x +" ScreenCoords+Off: " + (screenCoords.x-150));
//
//        } else if(player.getX()+270>=stage.getWidth()){                            //Right of the screen
//            screenCoords = new Vector3(0, player.getY(),0);
//            stage.getViewport().unproject(screenCoords);
//            player.setX(screenCoords.x-270);
//            System.out.println("Right->Player: " + player.getX() + " Player+Off: "+ (player.getX()-270)
//                    +" ScreenCoords: " + screenCoords.x + " ScreenCoords+Off" + (screenCoords.x-270));
//
//        }
//    }

    private void checkBoundaries(Sprite boundSprite) {
        //Handles Y coords
        if(boundSprite.getY()+270<0){                                                   //Bottom of the Screen
            screenCoords = new Vector3(boundSprite.getX(), stage.getHeight(),0);
            stage.getViewport().project(screenCoords);
            boundSprite.setY(screenCoords.y-150);
            System.out.println("Bot->Sprite: "+ boundSprite.getY() + " Sprite+Off: " + (boundSprite.getY()-150)
                    +" ScreenCoords: " + screenCoords.y +" ScreenCoords+Off: " + (screenCoords.y-150));
        } else if(boundSprite.getY()+160>=stage.getViewport().getWorldHeight()){        //Top of the Screen
            screenCoords = new Vector3(boundSprite.getX(), 0, 0);
            stage.getViewport().project(screenCoords);
            boundSprite.setY(screenCoords.y-270);
            System.out.println("Top->Sprite: " + boundSprite.getY() + " Sprite+Off: "+ (boundSprite.getY()-270)
                    +" ScreenCoords: " + screenCoords.y + " ScreenCoords+Off" + (screenCoords.y-270));
        }
        //Handles X coords
        else if(boundSprite.getX()+270<0){                                                  //Left of the Screen
            screenCoords = new Vector3(1280, boundSprite.getY(),0);
            stage.getViewport().unproject(screenCoords)
            ;            boundSprite.setX(screenCoords.x-270);
            System.out.println("Left->Sprite: "+ boundSprite.getX() + " Sprite+Off: " + (boundSprite.getX()-150)
                    +" ScreenCoords: " + screenCoords.x +" ScreenCoords+Off: " + (screenCoords.x-150));

        } else if(boundSprite.getX()+270>=stage.getWidth()){                            //Right of the screen
            screenCoords = new Vector3(0, boundSprite.getY(),0);
            stage.getViewport().unproject(screenCoords);
            boundSprite.setX(screenCoords.x-270);
            System.out.println("Right->Sprite: " + boundSprite.getX() + " Sprite+Off: "+ (boundSprite.getX()-270)
                    +" ScreenCoords: " + screenCoords.x + " ScreenCoords+Off" + (screenCoords.x-270));

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
            //stage.dispose();
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