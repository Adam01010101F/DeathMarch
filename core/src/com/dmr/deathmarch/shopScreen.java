package com.dmr.deathmarch;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
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


    private boolean gamePaused;
    private Sprite door;
    private Texture doorTex;

    private OrthogonalTiledMapRenderer renderer;
    private TiledMap map;

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

    public shopScreen(final DeathMarch game,Player player1, Player player2){
        this.game=game;
        System.out.println(player1.getX());
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1280, 720);

        text = new Array<String>();

        skin = new Skin(Gdx.files.internal("skin/pixthulhu-ui.json"));
        pOneTex = new Texture(Gdx.files.internal("player1.png"));
        pTwoTex = new Texture(Gdx.files.internal("player2.png"));
        stage = new Stage(new ScreenViewport());



        //Player Creation
        pOne = player1;
//        pOne.setPosition(1280/2f - 120/2f, 720/2f);
        pTwo = player2;
//        pTwo.setPosition((1280/2f -120/2f), 720/2f);

        //Buffs
        heartTex = new Texture(Gdx.files.internal("Red_orb.png"));
        heart = new Sprite(heartTex);

        speedTex = new Texture(Gdx.files.internal("Green_orb.png"));
        speed = new Sprite(speedTex);

        dmgTex = new Texture(Gdx.files.internal("Blue_orb.png"));
        dmg = new Sprite(dmgTex);




        doorTex = new Texture(Gdx.files.internal("Stairs_down.png"));
        door = new Sprite(doorTex);

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

        // 5 & 12
        door.setScale(8f);
        door.setX(22);
        door.setY(20);

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


        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        Gdx.input.setInputProcessor(stage);
        stage.draw();
        Gdx.gl.glClearColor(0,0.3f, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);



        camera.update();

        game.batch.setProjectionMatrix(camera.combined);

        // Load Objects onto Screen
        game.batch.begin();

        pOne.draw(game.batch);
        pTwo.draw(game.batch);

        heart.draw(game.batch);
        speed.draw(game.batch);
        dmg.draw(game.batch);

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


        game.batch.end();

        //Player 1 Keybindings
        if(Gdx.input.isKeyPressed(Input.Keys.W) || Gdx.input.isKeyPressed(Input.Keys.D) ||
                Gdx.input.isKeyPressed(Input.Keys.A)|| Gdx.input.isKeyPressed(Input.Keys.S)){
            pOne.clearDirections();
//            int condRot = (pOne.getWeapon().getRotation()/90)%2==0 ? -90: 90;
            if(Gdx.input.isKeyPressed(Input.Keys.W)){
                pOne.setRotation(90);
                pOne.setY(pOne.getY()+pOne.getSpeed()*Gdx.graphics.getDeltaTime());
                pOne.setYDirection(Direction.Up);

            }
            if(Gdx.input.isKeyPressed(Input.Keys.S)){
                pOne.setRotation(270);
                pOne.setY(pOne.getY()-pOne.getSpeed()*Gdx.graphics.getDeltaTime());
                pOne.setYDirection(Direction.Down);



            }
            if(Gdx.input.isKeyPressed(Input.Keys.D)){
                pOne.setRotation(0);
                pOne.setX(pOne.getX()+pOne.getSpeed()*Gdx.graphics.getDeltaTime());
                pOne.setXDirection(Direction.Right);


            }
            if(Gdx.input.isKeyPressed(Input.Keys.A)){
                pOne.setRotation(180);
                pOne.setX(pOne.getX()-pOne.getSpeed()*Gdx.graphics.getDeltaTime());
                pOne.setXDirection(Direction.Left);

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
        //leaving shop area
        if (Gdx.input.isKeyPressed(Input.Keys.P) && pOne.getBoundingRectangle().overlaps(door.getBoundingRectangle()) && pTwo.getBoundingRectangle().overlaps(door.getBoundingRectangle())) {
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
            pOne.buffDmg();
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






    }
    private void checkBoundary(Player player){
        if(player.getX()<0)player.setX(0);
        if(player.getX()>1280-120)player.setX(1280-120);
    }
}

