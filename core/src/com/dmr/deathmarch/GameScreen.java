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
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
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
    TiledMap tiledMap;
    TiledMapRenderer tiledMapRenderer;
    private Sprite beamProjectile;
    private Table table;
    private TextButton buttonPlay;
    private TextButton buttonQuit;
    private Rectangle npc;
    private Texture npcTex;
    private boolean gamePaused;
    Skin shopSkin;

    private OrthographicCamera cam;
    private OrthogonalTiledMapRenderer renderer;
    private TiledMap map;


    private Skin skin;

    private Dialog dialog;



    public GameScreen(final DeathMarch game){
        this.game = game;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1280, 720);

        stage = new Stage(new ScreenViewport());
        shopSkin = new Skin(Gdx.files.internal("skin/pixthulhu-ui.json"));

        shape = new ShapeRenderer();






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

        skin = new Skin(Gdx.files.internal("skin/pixthulhu-ui.json"));
        shopSkin = new Skin(Gdx.files.internal("skin/pixthulhu-ui.json"));

        lbTex = new Texture(Gdx.files.internal("laserBeam.png"));

        bmTex = new Texture(Gdx.files.internal("BeamCannon.png"));
        playerTex = new Texture(Gdx.files.internal("survivor-shoot_rifle_0.png"));
        pTwoTex = new Texture(Gdx.files.internal("player2.png"));
        Gobbi = new Goblin();
        stage = new Stage(new ScreenViewport());

        //Player Creation
        pOne = new Player("Shredder", false, playerTex, 23, 38, 293, 191);
        pOne.setOriginCenter();
//        System.out.println(pOne.getOriginX()+ " " + pOne.getOriginY());
        pOne.setColor(Color.GRAY);
//        pOne.setSize(pOne.getBoundingRectangle().width, pOne.getBoundingRectangle().height);
        pOne.setScale(1/3f);
//        pOne.setPosition(1280/2f, 720/2f);
//        pOne.setBounds( pOne.getWidth()/3f, pOne.getHeight()/3f);
        pOne.setWeapon(new BeamCannon(bmTex));
        System.out.println(pOne.getHeight()+" " + pOne.getWidth()+ " " + pOne.getBoundingRectangle().height + " "
                + pOne.getBoundingRectangle().width);
        pTwo = new Player("Donatello", false, playerTex, 23, 38, 293, 191);
        pTwo.setColor(Color.PURPLE);
        pTwo.setScale(1/3f);
        pTwo.setWeapon(new BeamCannon(bmTex));

        // Ghetto Managers
        //npc
        npcTex = new Texture(Gdx.files.internal("npc.png"));
        npc = new Rectangle();


//        pTwo.x = 1280/2 - 16/2;
//        pTwo.y = 720/2;
//        pTwo.width = 120;
//        pTwo.height = 120;

        //npc
        npc.x = 1100;
        npc.y = 10;
        npc.width = 150;
        npc.height = 150;

        lastDirection = new Direction[2];       // Tracks the direction of the user.
        projectiles = new Array<Projectile>();
        goblins = new Array<Goblin>();

        createGoblin();
        startTime = System.currentTimeMillis();
    }


    public void create(){

    }
    @Override
    public void show() {
        //TiledMap map = new TmxMapLoader().load("demoMap.tmx");





    }

    //TODO: Fix textures. They judder because they aren't perfectly centered in png file.
    @Override
    public void render(float delta) {
        stage.clear();
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        Gdx.input.setInputProcessor(stage);
        stage.draw();
        Gdx.gl.glClearColor(1,1, 1, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //table

        //window
           /* Window window = new Window("ShopKeeper", shopSkin );
            window.setPosition(400,200);
            window.pack();*/

        //table
        stage.clear();
        Gdx.input.setInputProcessor(stage);

        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1/30f));
        stage.draw();

        //constrains table size
        Container<Table> tableContainer = new Container<Table>();
        float sw = Gdx.graphics.getWidth();
        float sh = Gdx.graphics.getHeight();

        float cw = sw * 0.7f;
        float ch = sh * 0.5f;

        //creates container restraints
        tableContainer.setSize(cw, ch);
        tableContainer.setPosition((sw - cw) / 2.0f, (sh - ch) / 2.0f);
        tableContainer.fillX();

        //creates table
        Table table = new Table();
        table.setFillParent(true);
        table.setDebug(true);
        stage.addActor(table);

        //creates subTable so that exit button is center
        Table subTable = new Table();
        table.setFillParent(true);
        table.setDebug(true);
        stage.addActor(subTable);


        //sets size of table
        table.setSize(800,480);
        subTable.setSize(800,480);

        //label for table includes items and title
        Label topLabel = new Label("Shop Keeper",shopSkin);
        Label grenades = new Label("Grenades",shopSkin);
        Label health = new Label("Health Booster",shopSkin);
        //buttons to buy itesm
        TextButton buyg = new TextButton("Buy",shopSkin);
        TextButton buyh = new TextButton("Buy",shopSkin);
        //button to exit
        TextButton buttonExit = new TextButton("Exit" , shopSkin);
        table.add(buttonExit);
        //adds the rows for Title Label
        table.row().colspan(2).expandX().fillX();
        table.add(topLabel).fillX();
        table.row().colspan(2).expandX().fillX();
        //adds the rows for grenades
        table.row().colspan(2).expandX().fillX();
        table.add(grenades).expandX().fillX();
        //the following commented changes size of button.
        //table.add(buyg).width(Value.percentWidth(.25F,table));
        table.add(buyg).expandX().fillX();
        //adds the rows for health booster
        table.row().colspan(2).expandX().fillX();
        table.add(health).expandX().fillX();
        table.add(buyh).expandX().fillX();
        //adds the row and separate table for exit button
        table.row().colspan(2).expandX().fillX();
        table.add(subTable);
        subTable.pad(16);
        subTable.row().fillX().expandX();

        subTable.add(buttonExit).width(cw/3.0f);






        /*TextButton buttonQuit = new TextButton("Quit", shopSkin);
        table.add(buttonQuit);
        table.row();*/

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
                if(goblin.getBoundingRectangle().overlaps(projectile.getBoundingRectangle())){
//                    goblin.takeDamage(beamCannon.getDamage());
                    iter.remove();
                    System.out.println("Goblin takes damage");
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
            }
            else
            {
                float gX = goblin.getX();
                float gY = goblin.getY();
                if(distance2 > distance1)
                {
                    goblin.setX(goblin.getX() + ((40*(x/distance1)) * Gdx.graphics.getDeltaTime()));
                    goblin.setY(gY + ((40*(y/distance1)) * Gdx.graphics.getDeltaTime()));
                    float angle = (float) Math.toDegrees(Math.atan2(pOne.getY() - goblin.getY(), pOne.getX() - goblin.getX()));
                    if(angle  < 0)
                    {
                        angle = angle + 360;
                    }
                    goblin.setRotation(angle);
                }
                else {
                    goblin.setX(gX + ((40*(x2/distance2)) * Gdx.graphics.getDeltaTime()));
                    goblin.setY(gY + ((40*(y2/distance2)) * Gdx.graphics.getDeltaTime()));
                    float angle = (float) Math.toDegrees(Math.atan2(pTwo.getY() - goblin.getY(), pTwo.getX() - goblin.getX()));
                    if(angle < 0)
                    {
                        angle = angle + 360;
                    }
                    goblin.setRotation(angle);
                }
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
//        pOne.getWeapon().draw(game.batch);
        game.batch.draw(npcTex, npc.x, npc.y);
        for(Sprite goblin: goblins){
            game.batch.draw(pTwoTex, goblin.getX(), goblin.getY());
        }
        for (Sprite beam: projectiles){
            beam.draw(game.batch);
        }
        game.batch.end();
        shape.begin(ShapeRenderer.ShapeType.Filled);
        shape.setColor(Color.BLACK);
        shape.rect(pOne.getX(), pOne.getY(),
                pOne.getWidth(), pOne.getHeight());
//        shape.rect(pTwo.getBoundingRectangle().x,pTwo.getBoundingRectangle().y,
//                pTwo.getBoundingRectangle().width,pTwo.getBoundingRectangle().height);
//        shape.rect(pTwo.getX(),pTwo.getY(),
//                pTwo.getWidth(),pTwo.getHeight());
        shape.setColor(Color.RED);
        shape.rect(pOne.getBoundingRectangle().x,pOne.getBoundingRectangle().y,
                pOne.getBoundingRectangle().width,pOne.getBoundingRectangle().height);

        shape.end();

        //Player 1 Keybindings
        if(Gdx.input.isKeyPressed(Input.Keys.W) || Gdx.input.isKeyPressed(Input.Keys.D) ||
                Gdx.input.isKeyPressed(Input.Keys.A)|| Gdx.input.isKeyPressed(Input.Keys.S)){
            pOne.clearDirections();
            if(Gdx.input.isKeyPressed(Input.Keys.W)){
                pOne.setRotation(90);
                pOne.setY(pOne.getY()+150*Gdx.graphics.getDeltaTime());
                pOne.setYDirection(Direction.Up);
//                if(((pOne.getWeapon().getRotation()/90)%2) == 0) {
//                    pOne.getWeapon().rotate(90);
//                }
//                pOne.getWeapon().setPosition(pOne.getX()+64, pOne.getY()+92);
            }
            else if(Gdx.input.isKeyPressed(Input.Keys.S)){
                pOne.setRotation(270);
                pOne.setY(pOne.getY()-150*Gdx.graphics.getDeltaTime());
                pOne.setYDirection(Direction.Down);
//                if(((pOne.getWeapon().getRotation()/90)%2) == 0) {
//                    pOne.getWeapon().rotate(90);
//                }
//                pOne.getWeapon().setPosition(pOne.getX()-8, pOne.getY());
            }
            else if(Gdx.input.isKeyPressed(Input.Keys.D)){
                pOne.setRotation(0);
                pOne.setX(pOne.getX()+150*Gdx.graphics.getDeltaTime());
                pOne.setXDirection(Direction.Right);

//                if(((pOne.getWeapon().getRotation()/90)%2) != 0) {
//                    pOne.getWeapon().rotate(-90);
//                }
//                pOne.getWeapon().setPosition(pOne.getX()+64, pOne.getY()+8);
            }
            else if(Gdx.input.isKeyPressed(Input.Keys.A)){
                pOne.setRotation(180);
                pOne.setX(pOne.getX()-150*Gdx.graphics.getDeltaTime());
                pOne.setXDirection(Direction.Left);

//                if(((pOne.getWeapon().getRotation()/90)%2) != 0) {
//                    pOne.getWeapon().rotate(-90);
//                }
//                pOne.getWeapon().setPosition(pOne.getX(), pOne.getY()+64);
            }

        }
        if(Gdx.input.isKeyPressed(Input.Keys.ENTER)){
            if(TimeUtils.nanoTime() - pOne.getWeapon().getLastShot() > pOne.getWeapon().getCooldown()){
                projectiles.add(pOne.getWeapon().shoot(pOne.getBoundingRectangle(), lbTex, pOne.getLastDirection()));
            }
        }

        // TABLE/WINDOW
        if (Gdx.input.isKeyPressed(Input.Keys.P)) {
            if (!gamePaused)
                gamePaused = true;
            else
                gamePaused = false;
        }
        if(gamePaused) {
            table.setVisible(true);
            stage.draw();
        }

        //Player 2 Keybindings
        if(Gdx.input.isKeyPressed(Input.Keys.UP) || Gdx.input.isKeyPressed(Input.Keys.RIGHT) ||
                Gdx.input.isKeyPressed(Input.Keys.LEFT)|| Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            pTwo.clearDirections();
            if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
                pTwo.setRotation(90);
                pTwo.setY(pTwo.getY() + 200 * Gdx.graphics.getDeltaTime());
                pTwo.setYDirection(Direction.Up);
            }
            else if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
                pTwo.setRotation(270);
                pTwo.setY(pTwo.getY() - 200 * Gdx.graphics.getDeltaTime());
                pTwo.setYDirection(Direction.Down);
            }
            else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
                pTwo.setRotation(0);
                pTwo.setX(pTwo.getX() + 200 * Gdx.graphics.getDeltaTime());
                pTwo.setXDirection(Direction.Right);
            }
            else if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
                pTwo.setRotation(180);
                pTwo.setX(pTwo.getX() - 200 * Gdx.graphics.getDeltaTime());
                pTwo.setXDirection(Direction.Left);
            }
        }
        if(Gdx.input.isKeyPressed(Input.Keys.SPACE)){
            if(TimeUtils.nanoTime() - pTwo.getWeapon().getLastShot() > pTwo.getWeapon().getCooldown()){
                projectiles.add(pTwo.getWeapon() .shoot(pTwo.getWeapon(), lbTex, pTwo.getLastDirection()));
            }
        }

        if((pOne.isDead()&&pTwo.isDead()) || (System.currentTimeMillis() - startTime == 300000l)){

        }
        //Player Boundaries
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
}