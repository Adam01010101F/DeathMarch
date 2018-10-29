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
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.dmr.deathmarch.npc.Goblin;
import com.dmr.deathmarch.weapons.BeamCannon;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Iterator;

import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;

public class GameScreen implements Screen {
    final DeathMarch game;
    private long startTime;
    private OrthographicCamera camera;
    private Texture playerTex;
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
    //private Box2DDebugRenderer dbr;
    private Direction lastDirection[];
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

    private Dialog dialog;
    private int[] background = new int[] {0}, foreground = new int[] {1};
    private ShapeRenderer shape;
    //adding dialogue for npc
    FreeTypeFontGenerator generator;
    FreeTypeFontParameter parameters;
    BitmapFont uiText;

    //for the time
    private long start;
    private long diffTime;
    private String mapName;


    public GameScreen(final DeathMarch game,Player player1,Player player2){
        this.game = game;
        mapName = "maps/demoMap.tmx";
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1280, 1200);

        stage = new Stage(new FitViewport(1280,1280));
        shopSkin = new Skin(Gdx.files.internal("skin/pixthulhu-ui.json"));

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
        pOne = player1;
        pOne.setOriginCenter();
        pOne.setPosition(100,100);
//        System.out.println(pOne.getOriginX()+ " " + pOne.getOriginY());
        pOne.setColor(Color.GRAY);
        pOne.setScale(1/8f);
        pOne.setWeapon(new BeamCannon(bmTex));
        pTwo = player2;
        pTwo.setPosition(200,200);
        pTwo.setColor(Color.PURPLE);
        pTwo.setScale(3/4f);
        //
        pTwo.setWeapon(new BeamCannon(bmTex));

        // Ghetto Managers
        //npc
        npcTex = new Texture(Gdx.files.internal("Stairs_up.png"));
        stairs = new Sprite(npcTex);


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
        goblins = new Array<Goblin>();


        //text for NPC
        generator = new FreeTypeFontGenerator((Gdx.files.internal("fonts/joystix.ttf")));
        parameters = new FreeTypeFontParameter();
        parameters.size = 20;
        parameters.color= Color.BLACK;


        uiText = generator.generateFont(parameters);
        start = TimeUtils.millis();

        generator.dispose();

        createGoblin();
        startTime = System.currentTimeMillis();
    }


    public void create(){

    }
    @Override
    public void show() {

        map = new TmxMapLoader().load(mapName);

        //float unitScale = 2f;
        map = new TmxMapLoader().load("maps/demoMap.tmx");

        tiledMapRenderer = new OrthogonalTiledMapRenderer(map);

    }

    //TODO: Fix textures. They judder because they aren't perfectly centered in png file.
    @Override
    public void render(float delta) {

        stage.clear();
        stage.act(Math.min(Gdx.graphics.getDeltaTime(),1/30f));
        Gdx.input.setInputProcessor(stage);

        // ---GAME CONTROLS---
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            game.changeScreen(DeathMarch.MENU);
        }
        // ------------------
        stage.draw();
        Gdx.gl.glClearColor(0,0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //table
        tiledMapRenderer.setView(camera);
        tiledMapRenderer.render(background);
        tiledMapRenderer.render(foreground);

        camera.update();


        // Bullet Physics|Destruction
        for(Iterator<Projectile> iter = projectiles.iterator(); iter.hasNext();){
            Projectile projectile = iter.next();
            projectile.setPosition(projectile.getX() +350 * projectile.getxVel() * Gdx.graphics.getDeltaTime()
                    , projectile.getY() + 350 *projectile.getyVel() * Gdx.graphics.getDeltaTime());
//            if(projectile.getX()>1280 | projectile.getY()>720 || projectile.getX() < 0 || projectile.getY() < 0){
//                iter.remove();
//            }
            if(bulletCollidesLeft(projectile)){
                iter.remove();
            }
            else if(bulletCollidesRight(projectile)){
                iter.remove();
            }
            else if(bulletCollidesBottom(projectile)){
                iter.remove();
            }
            else if(bulletCollidesTop(projectile)){
                iter.remove();
            }
            else{
                // do nothing
            }


            //TODO:: Give ownership of projectile to count score.
            for(Goblin goblin: goblins){
                if(goblin.getBoundingRectangle().overlaps(projectile.getBoundingRectangle())){
//                    goblin.takeDamage(beamCannon.getDamage());
                    iter.remove();
                    System.out.println("Goblin takes damage");
                    pOne.addKill();
                }
            }
        }

        // Goblin Destruction
        for(Iterator<Goblin> iter = goblins.iterator(); iter.hasNext();){
            Goblin goblin = iter.next();
            float x = pOne.getX() - goblin.getX();
            float y = pOne.getY() - goblin.getY();
            float distance1 = (float) Math.sqrt((x*x) - (y*y));
            float x2 = pTwo.getX() - goblin.getX();
            float y2 = pTwo.getY() - goblin.getY();
            float distance2 = (float) Math.sqrt((x2*x2) - (y2*y2));
            if(goblin.isDead()){
                iter.remove();
                System.out.println("Goblin is dead :D");
                pOne.addBigKill();
            }
            else
            {
                float gX = goblin.getX();
                float gY = goblin.getY();
                if(distance2 > distance1)
                {
                    float acc1 = (x / distance1);
                    float acc2 = (y / distance1);
                    if(x >= 0)
                    {
                        acc1 = 1;
                    }
                    else
                    {
                        acc1 = -1;
                    }
                    if(y  >= 0)
                    {
                        acc2 = 1;
                    }
                    else
                    {
                        acc2 = -1;
                    }
                    if(distance1 != 0) {
                        goblin.setX(gX + ((20 * acc1) * Gdx.graphics.getDeltaTime()));
                        goblin.setY(gY + ((20 * acc2) * Gdx.graphics.getDeltaTime()));

                    }
                    goblin.checkGob(pOne);
                    float angle = (float) Math.toDegrees(Math.atan2(pOne.getY() - goblin.getY(), pOne.getX() - goblin.getX()));
                    if(angle  < 0)
                    {
                        angle = angle + 360;
                    }
                    goblin.setRotation(angle);

                }
                else {
                    if(distance2 != 0) {
                        float acc1 = (x2 / distance2);
                        float acc2 = (y2 / distance2);
                        if(x2 >= 0)
                        {
                            acc1 = 1;
                        }
                        else
                        {
                            acc1 = -1;
                        }
                        if(y2 >= 0)
                        {
                            acc2 = 1;
                        }
                        else
                        {
                            acc2 = -1;
                        }
                        goblin.setX(gX + ((20 * acc1) * Gdx.graphics.getDeltaTime()));
                        goblin.setY(gY + ((20 * acc2) * Gdx.graphics.getDeltaTime()));
                    }
                    goblin.checkGob(pTwo);
                    float angle = (float) Math.toDegrees(Math.atan2(pTwo.getY() - goblin.getY(), pTwo.getX() - goblin.getX()));
                    if(angle < 0)
                    {
                        angle = angle + 360;
                    }
                    goblin.setRotation(angle);

                }
            }
//            checkBoundary(goblin);
        }

        game.batch.setProjectionMatrix(camera.combined);

        // Load Objects onto Screen
        game.batch.begin();
        //sets up the timer
        Vector3 posCamara = camera.position;
        uiText.draw(game.batch, "Time: " + (300 - (System.currentTimeMillis() - startTime )/1000), posCamara.x - 100, posCamara.y + 580);
        uiText.draw(game.batch, "Player 1 Health: " + Math.round(pOne.getHealth()), posCamara.x - 600, posCamara.y + 580);
        uiText.draw(game.batch, "Player 2 Health: " + Math.round(pTwo.getHealth()), posCamara.x + 200, posCamara.y + 580);


        //NPC dialogue conditional statement
        diffTime = TimeUtils.timeSinceMillis(start);
        if(diffTime<3000){
            uiText.draw(game.batch,"Welcome to Death March!",370,150);
        }
        if(diffTime>3000 && diffTime<6000){
            uiText.draw(game.batch,"Let's see if you survive HAHAHA!",370,150);
        }

        if(pOne.getBoundingRectangle().overlaps(stairs.getBoundingRectangle() )){
            uiText.draw(game.batch,"Press P to enter the store",370,150);
        }




        game.font.draw(game.batch, "P2 x: "+pTwo.getX()+" y: "+pTwo.getY(), 100, 150);
        game.font.draw(game.batch, "Projectiles: " + projectiles.size, 100, 200);
        pOne.draw(game.batch);
        pTwo.draw(game.batch);
        stairs.draw(game.batch);
//        game.batch.draw(bmTex, pOne.getX(), pOne.getY()-8);
//        pOne.getWeapon().draw(game.batch);

        for(Sprite goblin: goblins){
            game.batch.draw(pTwoTex, goblin.getX(), goblin.getY());
        }
        for (Sprite beam: projectiles){
            beam.draw(game.batch);
        }
        game.batch.end();


        //shape.begin(ShapeRenderer.ShapeType.Filled);
        //shape.setColor(Color.BLACK);
        //shape.rect(pOne.getBoundingRectangle().getX(), pOne.getBoundingRectangle().getY(), pOne.getBoundingRectangle().getWidth(), pOne.getBoundingRectangle().getHeight());
        //shape.end();

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
                    pOne.setY(pOne.getY() + pOne.getSpeed() * Gdx.graphics.getDeltaTime());
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
                    pOne.setY(pOne.getY() - pOne.getSpeed() * Gdx.graphics.getDeltaTime());
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
                    pOne.setX(pOne.getX() + pOne.getSpeed() * Gdx.graphics.getDeltaTime());
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
                    pOne.setX(pOne.getX()-pOne.getSpeed()*Gdx.graphics.getDeltaTime());
                    pOne.setXDirection(Direction.Left);
                }

            }

        }
        if(Gdx.input.isKeyPressed(Input.Keys.ENTER)){
            if(TimeUtils.nanoTime() - pOne.getWeapon().getLastShot() > pOne.getWeapon().getCooldown()){
                projectiles.add(pOne.getWeapon().shoot(pOne.getBoundingRectangle(), lbTex, pOne.getLastDirection()));
            }
        }

        //enter Store
        if (Gdx.input.isKeyPressed(Input.Keys.P) && pOne.getBoundingRectangle().overlaps(stairs.getBoundingRectangle())) {
            game.changeScreen(DeathMarch.SHOP);
        }


        //Player 2 Keybindings
        if(Gdx.input.isKeyPressed(Input.Keys.UP) || Gdx.input.isKeyPressed(Input.Keys.RIGHT) ||
                Gdx.input.isKeyPressed(Input.Keys.LEFT)|| Gdx.input.isKeyPressed(Input.Keys.DOWN)) {

            pTwo.clearDirections();
            if (Gdx.input.isKeyPressed(Input.Keys.UP)) {

                if(collidesTop(pTwo))
                {
                    pTwo.setRotation(90);
                    pTwo.setY(pTwo.getY());
                    pOne.setYDirection(Direction.Up);
                }
                else{
                    pTwo.setRotation(90);
                    pTwo.setY(pTwo.getY() + 200 * Gdx.graphics.getDeltaTime());
                    pTwo.setYDirection(Direction.Up);
                }

            }
            else if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
                if(collidesBottom(pTwo)){
                    pTwo.setRotation(270);
                    pTwo.setY(pTwo.getY());
                    pTwo.setYDirection(Direction.Down);
                }
                else{
                    pTwo.setRotation(270);
                    pTwo.setY(pTwo.getY() - 200 * Gdx.graphics.getDeltaTime());
                    pTwo.setYDirection(Direction.Down);
                }
            }
            else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
                if(collidesRight(pTwo)){
                    pTwo.setRotation(0);
                    pTwo.setX(pTwo.getX());
                    pTwo.setXDirection(Direction.Right);
                }
                else{

                    pTwo.setRotation(0);
                    pTwo.setX(pTwo.getX() + 200 * Gdx.graphics.getDeltaTime());
                    pTwo.setXDirection(Direction.Right);
                }
            }
            else if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
                if(collidesLeft(pTwo)){
                    pTwo.setRotation(180);
                    pTwo.setX(pTwo.getX());
                    pTwo.setXDirection(Direction.Left);
                }
                else{

                    pTwo.setRotation(180);
                    pTwo.setX(pTwo.getX() - 200 * Gdx.graphics.getDeltaTime());
                    pTwo.setXDirection(Direction.Left);
                }
            }

        }
        if(Gdx.input.isKeyPressed(Input.Keys.SPACE)){
            if(TimeUtils.nanoTime() - pTwo.getWeapon().getLastShot() > pTwo.getWeapon().getCooldown()){
                projectiles.add(pTwo.getWeapon() .shoot(pTwo.getWeapon(), lbTex, pTwo.getLastDirection()));
            }
        }

        if(300 - (System.currentTimeMillis() - startTime )/1000 == 0){
            //startTime = System.currentTimeMillis()
            game.changeScreen(DeathMarch.MENU);

            //game.setScreen(new GameScreen(game));
        }

        if(pOne.isDead()|| pTwo.isDead()){
            game.changeScreen(DeathMarch.MENU);
        }


        }


        //Player Boundaries
//        checkBoundary(pOne);
//        checkBoundary(pTwo);



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
        goblin.setX(1280/2f - 16/2f);
        goblin.setY(720/2f);
        goblins.add(goblin);
    }

    private void checkBoundary(Player player){
        if(player.getBoundingRectangle().getX()<0){
            System.out.println(player.getX() + " " + player.getBoundingRectangle().getX());

            player.setX(player.getBoundingRectangle().getX()-95);
            System.out.println(player.getX() + " " + player.getBoundingRectangle().getX());

        }
        if(player.getBoundingRectangle().getX()>1280-120){
            player.setX(player.getBoundingRectangle().getX());
            System.out.println(player.getX() + " " + player.getBoundingRectangle().getX());

        }
        if(player.getBoundingRectangle().getY()<0)player.setY(0);
        if(player.getBoundingRectangle().getY()>720-120)player.setY(720-120);
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