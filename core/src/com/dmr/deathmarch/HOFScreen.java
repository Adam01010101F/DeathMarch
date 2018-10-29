package com.dmr.deathmarch;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.google.gson.*;

public class HOFScreen implements Screen {
    final DeathMarch game;

    private OrthographicCamera camera;
    private Boolean enteredUser;
    private Stage stage;
    Table masterTable;
    Label.LabelStyle hofStyle;

    Famer famers[] = new Famer[10];

    HOFScreen(final DeathMarch game, Player player) {
        this.game = game;

        enteredUser=false;
        stage = new Stage(new ScreenViewport());


        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1024, 576);

        masterTable = new Table();
        hofStyle = new Label.LabelStyle(new BitmapFont(), Color.GOLD);


        Gson gson = new Gson();
        if (Gdx.files.local("hof.json").exists()) {
            FileHandle file = Gdx.files.internal("hof.json");
            System.out.println("File Exists.");
            famers = gson.fromJson(file.readString(), Famer[].class);
            for (int i = 0; i<famers.length;i++) {
                if(player.getKills()>0&&!enteredUser) {
                    famers[i] = new Famer(player.getName(), player.getKills());
                    break;
                }
            }
        } else {
            FileHandle file = Gdx.files.local("hof.json");
            for (int i = 0; i<famers.length;i++) {
                if(player.getKills()>0&&!enteredUser) {
                    famers[i] = new Famer(player.getName(), player.getKills());
                    enteredUser=true;
                }
                famers[i] = new Famer();
            }
            file.writeString(gson.toJson(famers), false);
        }

    }


    @Override
    public void show() {
        stage.clear();
        Gdx.input.setInputProcessor(stage);

        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();

        Skin skin = new Skin(Gdx.files.internal("skin/pixthulhu-ui.json"));

//        Table table = new Table();
        masterTable.setFillParent(true);
        masterTable.setDebug(true);
        stage.addActor(masterTable);


        for(int i=0; i<famers.length ;i++){
            masterTable.row().colspan(2).expandX().fillX();
            masterTable.add(new Label((i+1)+". " + famers[i].getName() +" " +
                    famers[i].getPoints(), skin)).width(200).height(100);
        }
        System.out.print("It gets here.");

    }

    @Override
    public void render(float delta) {
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
}


class Famer {
    String name;
    int points;

    Famer() {
        name = "";
        points = 0;
    }

    Famer(String name, int points) {
        this.name = name;
        this.points = points;
    }

    public void setName(String name) {
        this.name = name;
    }
    public void setPoints(int points){
        this.points = points;
    }
    public String getName(){return name;}
    public int getPoints(){return points;}

    
    public boolean isGreaterThan(Famer famer){
        if(this.points>famer.points)
            return true;
        return false;
    }
    @Override
    public String toString(){
        String rtnS= "Name: " + name + " points: " + points;
        return rtnS;
    }
}
