package com.dmr.deathmarch;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class shopScreen implements Screen{
    final DeathMarch game;
    public OrthographicCamera camera;
    public Stage stage;

    public shopScreen(final DeathMarch game){
        this.game=game;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1280, 720);

        stage = new Stage(new ScreenViewport());
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

        Gdx.gl.glClearColor(0f, 0f, 0f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();


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

        buttonExit.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.changeScreen(DeathMarch.APPLICATION);
            }
        });
//
//        newGame.addListener(new ChangeListener() {
//            @Override
//            public void changed(ChangeEvent event, Actor actor) {
//                game.changeScreen(DeathMarch.APPLICATION);
//            }
//        });
//
//        preferences.addListener(new ChangeListener(){
//            @Override
//            public void changed(ChangeEvent event, Actor actor){
//                game.changeScreen(DeathMarch.PREFERENCES);
//            }

//        });
//        newGame2.addListener(new ChangeListener() {
//            @Override
//            public void changed(ChangeEvent event, Actor actor) {
//                game.changeScreen(DeathMarch.APP2);
//            }
//        });


    }
}

