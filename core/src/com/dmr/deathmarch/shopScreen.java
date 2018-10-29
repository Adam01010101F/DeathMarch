package com.dmr.deathmarch;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
//import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.dmr.deathmarch.npc.Goblin;
import com.dmr.deathmarch.weapons.BeamCannon;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Iterator;

import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;

public class shopScreen implements Screen{
    final DeathMarch game;
    public OrthographicCamera camera;
    public Stage stage;
    private Texture pOneTex;
    private Texture pTwoTex;
    private Texture bmTex;
    private Texture lbTex;
    private Player pOne;
    private Player pTwo;
    private LogicModel lm;
    private Direction lastDirection[];
    AssetManager assetManager;
    TiledMap tiledMap;
    TiledMapRenderer tiledMapRenderer;
    private Sprite beamProjectile;
    //BUFFS
    private Sprite heart;
    private Sprite speed;
    private Sprite dmg;
    private Texture heartTex;
    private Texture speedTex;
    private Texture dmgTex;

    //NPC
    private Sprite npc;
    private Texture npcTex;

    private boolean gamePaused;
    private Sprite door;
    private Texture doorTex;

    private OrthogonalTiledMapRenderer renderer;
    private TiledMapTileLayer collisionLayer;
    private TiledMap map;
    private String mapName;
    private int[] background = new int[] {0}, foreground = new int[] {1};
    private String blockedKey = "blocked";
    private float increment;
    //adding text
    FreeTypeFontGenerator generator;
    FreeTypeFontParameter parameters;
    BitmapFont uiText;
    private Array<String> text;
    //for the time
    private long start;
    private long diffTime;

    private Skin skin;

    private Dialog dialog;
    private Music bgm_Music;

    public shopScreen(final DeathMarch game,Player player1, Player player2){
        this.game=game;
        mapName = "maps/shop.tmx";
        map = new TmxMapLoader().load(mapName);
        collisionLayer = (TiledMapTileLayer) map.getLayers().get(1);

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1280, 1200);

        text = new Array<String>();

        skin = new Skin(Gdx.files.internal("skin/pixthulhu-ui.json"));
        pOneTex = new Texture(Gdx.files.internal("player1.png"));
        pTwoTex = new Texture(Gdx.files.internal("player2.png"));
        stage = new Stage(new FitViewport(1280,1280));

        //Player Creation
        pOne = player1;
//        pOne.setPosition(1280/2f - 120/2f, 720/2f);

//        pTwo.setPosition((1280/2f -120/2f), 720/2f);

        //Buffs
        heartTex = new Texture(Gdx.files.internal("Red_orb.png"));
        heart = new Sprite(heartTex);

        speedTex = new Texture(Gdx.files.internal("Green_orb.png"));
        speed = new Sprite(speedTex);

        dmgTex = new Texture(Gdx.files.internal("Blue_orb.png"));
        dmg = new Sprite(dmgTex);

        //NPC
        npcTex = new Texture(Gdx.files.internal("npc.png"));
        npc = new Sprite(npcTex);



        doorTex = new Texture(Gdx.files.internal("Stairs_down.png"));
        door = new Sprite(doorTex);
        //Setting Player 1 Position
        pOne.setX(30);
        pOne.setY(40);

        //Setting Buff position
        speed.setScale(8f);
        speed.setX(550);
        speed.setY(73);

        heart.setScale(8f);
        heart.setX(350);
        heart.setY(73);

        dmg.setScale(8f);
        dmg.setX(750);
        dmg.setY(73);

        npc.setScale(0.5f);
        npc.setX(470);
        npc.setY(140);


        //it's actually stairs but oh well
        door.setScale(3f);
        door.setX(30);
        door.setY(40);

        //text for NPC
        generator = new FreeTypeFontGenerator((Gdx.files.internal("fonts/joystix.ttf")));
        parameters = new FreeTypeFontParameter();
        parameters.size = 20;
        parameters.color= Color.BLACK;


        uiText = generator.generateFont(parameters);


        text.add("1");
        text.add("2");
        int x = 500;
        int y = 50;

        start = TimeUtils.millis();

        generator.dispose();
    }
    @Override
    public void render(float delta){
//        Gdx.gl.glClearColor(0,0,0,0);
//        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        //
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


        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        Gdx.input.setInputProcessor(stage);
        stage.draw();
        Gdx.gl.glClearColor(0,0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);



        camera.update();

        game.batch.setProjectionMatrix(camera.combined);
        tiledMapRenderer.setView(camera);
        tiledMapRenderer.render(background);
        tiledMapRenderer.render(foreground);
        // Load Objects onto Screen
        game.batch.begin();

        pOne.draw(game.batch);


        heart.draw(game.batch);
        speed.draw(game.batch);
        dmg.draw(game.batch);

        npc.draw(game.batch);

        door.draw(game.batch);

       // text bubble



        diffTime = TimeUtils.timeSinceMillis(start);
        if(diffTime<3000){
            uiText.draw(game.batch,"Welcome to my store!",370,150);
        }
        if(diffTime>3000 && diffTime<8000){
            uiText.draw(game.batch,"We only sell the scariest of items here!",200,150);
        }
        if(diffTime>8000 && diffTime<12000){
            uiText.draw(game.batch,"Beware you might not be the same when you leave",200,150);
        }
        //Drawing Text for Items
        uiText.draw(game.batch,"Health Boost",240,50);
        uiText.draw(game.batch,"Speed Boost", 460,50);
        uiText.draw(game.batch,"Damage Boost",660,50);

        
//        uiText.draw(game.batch,"Hello World",500,50);
        if(pOne.getBoundingRectangle().overlaps(door.getBoundingRectangle() )){
            uiText.draw(game.batch,"Press P to exit the store",370,150);
        }

        game.batch.end();

        //Player 1 Keybindings
        if(Gdx.input.isKeyPressed(Input.Keys.W) || Gdx.input.isKeyPressed(Input.Keys.D) ||
                Gdx.input.isKeyPressed(Input.Keys.A)|| Gdx.input.isKeyPressed(Input.Keys.S)){
            pOne.clearDirections();
            if(Gdx.input.isKeyPressed(Input.Keys.W)){
                if(collidesTop(pOne))
                {
                    pOne.setRotation(90);
                    pOne.setY(pOne.getY());
                    pOne.setYDirection(Direction.Up);
                }
                else {
                    pOne.setRotation(90);
                    pOne.setY(pOne.getY() + 150 * Gdx.graphics.getDeltaTime());
                    pOne.setYDirection(Direction.Up);
                }
            }
            else if(Gdx.input.isKeyPressed(Input.Keys.S)){

                if(collidesBottom(pOne))
                {
                    pOne.setRotation(270);
                    pOne.setY(pOne.getY());
                    pOne.setYDirection(Direction.Down);
                }
                else {
                    pOne.setRotation(270);
                    pOne.setY(pOne.getY() - 150 * Gdx.graphics.getDeltaTime());
                    pOne.setYDirection(Direction.Down);
                }
            }
            else if(Gdx.input.isKeyPressed(Input.Keys.D)){
                if(collidesRight(pOne))
                {
                    pOne.setRotation(0);
                    pOne.setX(pOne.getX());
                    pOne.setXDirection(Direction.Right);
                }
                else {

                    pOne.setRotation(0);
                    pOne.setX(pOne.getX() + 150 * Gdx.graphics.getDeltaTime());
                    pOne.setXDirection(Direction.Right);
                }
            }
            else if(Gdx.input.isKeyPressed(Input.Keys.A)){
                if(collidesLeft(pOne)){
                    pOne.setRotation(180);
                    pOne.setX(pOne.getX());
                    pOne.setXDirection(Direction.Left);
                }
                else{

                    pOne.setRotation(180);
                    pOne.setX(pOne.getX()-150*Gdx.graphics.getDeltaTime());
                    pOne.setXDirection(Direction.Left);
                }

            }
            //else if(Gdx.input.isKeyPressed(Input.Keys.P))

        }


        //leaving shop area
        if (Gdx.input.isKeyPressed(Input.Keys.P) && pOne.getBoundingRectangle().overlaps(door.getBoundingRectangle())) {
            bgm_Music.stop();
            game.changeScreen(DeathMarch.APPLICATION);

        }

        //buffs for speed

        //buffing player 1 speed
        if(Gdx.input.isKeyPressed(Input.Keys.SPACE) && pOne.getBoundingRectangle().overlaps(speed.getBoundingRectangle())){
            pOne.buffSpeed();
            System.out.println("Speed Increased to " + pOne.getSpeed());
        }
        if(Gdx.input.isKeyPressed(Input.Keys.SPACE) && pOne.getBoundingRectangle().overlaps(heart.getBoundingRectangle())){
            pOne.buffHealth();
            System.out.println("Health Increased to " + pOne.getHealth());
        }
        if(Gdx.input.isKeyPressed(Input.Keys.SPACE) && pOne.getBoundingRectangle().overlaps(dmg.getBoundingRectangle())){
            pOne.setDmgMulti(2);
            System.out.println("Damage Increased to " + pOne.getDmgMulti());
        }



        //Player Boundaries
//        checkBoundary(pOne);
//        checkBoundary(pTwo);




        stage.clear();


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
        map = new TmxMapLoader().load(mapName);
        tiledMapRenderer = new OrthogonalTiledMapRenderer(map);

        bgm_Music = Gdx.audio.newMusic(Gdx.files.internal("sakura.mp3"));
        bgm_Music.setLooping(true);
        bgm_Music.setVolume(0.5f);
        bgm_Music.play();

    }
    private void checkBoundary(Player player){
        if(player.getX()<0)player.setX(0);
        if(player.getX()>1280-120)player.setX(1280-120);
    }

    private boolean isCellBlocked(float x, float y) {
        System.out.println("X: " + x);
        System.out.println("Y : " + y );

        TiledMapTileLayer.Cell cell = collisionLayer.getCell((int) (x / collisionLayer.getTileWidth()), (int) (y / collisionLayer.getTileHeight()));

        return cell != null && cell.getTile() != null && cell.getTile().getProperties().containsKey(blockedKey);
    }

    public boolean collidesRight(Player player) {
        increment = collisionLayer.getTileWidth();
        increment = player.getBoundingRectangle().getWidth() < increment ? player.getBoundingRectangle().getWidth() / 2 : increment / 2;
        for(float step = 0; step <= player.getBoundingRectangle().getHeight(); step += increment)
            if(isCellBlocked(player.getBoundingRectangle().getX() + player.getBoundingRectangle().getWidth(), player.getBoundingRectangle().getY() + step))
                return true;
        return false;
    }

    public boolean collidesLeft(Player player) {
        increment = collisionLayer.getTileWidth();
        increment = player.getBoundingRectangle().getWidth() < increment ? player.getBoundingRectangle().getWidth() / 2 : increment / 2;
        for(float step = 0; step <= player.getBoundingRectangle().getHeight(); step += increment)
            if(isCellBlocked(player.getBoundingRectangle().getX(), player.getBoundingRectangle().getY() + step))
                return true;
        return false;
    }

    public boolean collidesTop(Player player) {
        increment = collisionLayer.getTileHeight();
        increment = player.getBoundingRectangle().getHeight() < increment ? player.getBoundingRectangle().getHeight() / 2 : increment / 2;
        for(float step = 0; step <= player.getBoundingRectangle().getWidth(); step += increment) {
            if (isCellBlocked(player.getBoundingRectangle().getX() + step, player.getBoundingRectangle().getY() + player.getBoundingRectangle().getHeight()))
                return true;
        }
        return false;

    }

    public boolean collidesBottom(Player player) {
        // calculate the increment for step in #collidesLeft() and #collidesRight()
        increment = collisionLayer.getTileHeight();
        increment = player.getBoundingRectangle().getHeight() < increment ? player.getBoundingRectangle().getHeight() / 2 : increment / 2;
        for(float step = 0; step <= player.getBoundingRectangle().getWidth(); step += increment)
            if(isCellBlocked(player.getBoundingRectangle().getX() + step, player.getBoundingRectangle().getY()))
                return true;
        return false;
    }

    public boolean bulletCollidesRight(Projectile proj) {
        increment = collisionLayer.getTileWidth();
        increment = proj.getBoundingRectangle().getWidth() < increment ? proj.getBoundingRectangle().getWidth() / 2 : increment / 2;
        for(float step = 0; step <= proj.getBoundingRectangle().getHeight(); step += increment)
            if(isCellBlocked(proj.getBoundingRectangle().getX() + proj.getBoundingRectangle().getWidth(), proj.getBoundingRectangle().getY() + step))
                return true;
        return false;
    }

    public boolean bulletCollidesLeft(Projectile proj) {
        increment = collisionLayer.getTileWidth();
        increment = proj.getBoundingRectangle().getWidth() < increment ? proj.getBoundingRectangle().getWidth() / 2 : increment / 2;
        for(float step = 0; step <= proj.getBoundingRectangle().getHeight(); step += increment)
            if(isCellBlocked(proj.getBoundingRectangle().getX(), proj.getBoundingRectangle().getY() + step))
                return true;
        return false;
    }

    public boolean bulletCollidesTop(Projectile proj) {
        increment = collisionLayer.getTileHeight();
        increment = proj.getBoundingRectangle().getHeight() < increment ? proj.getBoundingRectangle().getHeight() / 2 : increment / 2;
        for(float step = 0; step <= proj.getBoundingRectangle().getWidth(); step += increment) {
            if (isCellBlocked(proj.getBoundingRectangle().getX() + step, proj.getBoundingRectangle().getY() + proj.getBoundingRectangle().getHeight()))
                return true;
        }
        return false;

    }

    public boolean bulletCollidesBottom(Projectile proj) {

        increment = collisionLayer.getTileHeight();
        increment = proj.getBoundingRectangle().getHeight() < increment ? proj.getBoundingRectangle().getHeight() / 2 : increment / 2;
        for(float step = 0; step <= proj.getBoundingRectangle().getWidth(); step += increment)
            if(isCellBlocked(proj.getBoundingRectangle().getX() + step, proj.getBoundingRectangle().getY()))
                return true;
        return false;
    }
}

