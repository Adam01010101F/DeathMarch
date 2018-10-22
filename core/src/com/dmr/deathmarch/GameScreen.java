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
    private Box2DDebugRenderer dbr;
    private Direction lastDirection[];
    private Sprite beamProjectile;
    private Table table;
    private TextButton buttonPlay;
    private TextButton buttonQuit;
    private Stage stage;
    private Rectangle npc;
    private Texture npcTex;
    private boolean gamePaused;


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
        stage = new Stage(new ScreenViewport());

        pOne.x = 1280/2 - 64/2;
        pOne.y = 720/2;
        pOne.width = 50;
        pOne.height = 50;

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

        //table
        Skin shopSkin = new Skin(Gdx.files.internal("skin/pixthulhu-ui.json"));

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
        game.font.draw(game.batch, "P1 x: "+pOne.x+" y: "+pOne.y, 100, 150);
        game.font.draw(game.batch, "Projectiles: " + projectiles.size, 100, 200);
        game.batch.draw(pOneTex, pOne.x, pOne.y);
        //npc to draw so it appears, constantly draws over and over.
        game.batch.draw(npcTex, npc.x, npc.y);
        game.batch.draw(bmTex, pOne.x, pOne.y-8);
        for(Rectangle goblin: goblins){
            game.batch.draw(pTwoTex, goblin.x, goblin.y);
        }
        for (Sprite beam: projectiles){
            beam.draw(game.batch);
        }
        game.batch.end();

        //Player 1 Key bindings
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
        }}
        if(Gdx.input.isKeyPressed(Input.Keys.ENTER)){
            if(TimeUtils.nanoTime() - beamCannon.getLastBeamShot() > beamCannon.getCooldown()){
                beamCannon.setLastBeamShot(TimeUtils.nanoTime());
                projectiles.add(beamCannon.shoot(pOne, lbTex, lastDirection));
            }
        }
        // TABLE/WINDOW
        if (Gdx.input.isKeyPressed(Input.Keys.P)) {
            if (gamePaused == false)
                gamePaused = true;
            else
                gamePaused = false;
        }
        if(gamePaused == true) {
            table.setVisible(true);
            stage.draw();
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
