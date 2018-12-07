package com.dmr.deathmarch;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
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

import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.List;

public class HOFScreen implements Screen {
    final DeathMarch game;
    final static int hofSize = 10;
    private OrthographicCamera camera;
    private Boolean enteredUser;
    private Stage stage;
    List<Famer> famers;
    Table masterTable;


    HOFScreen(final DeathMarch game, Player player) {
        this.game = game;

        enteredUser=false;
        stage = new Stage(new ScreenViewport());

        Type listType = new TypeToken<List<Famer>>() {}.getType();
        famers = new LinkedList<Famer>();

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1024, 576);

        masterTable = new Table();
        System.out.println("Does it die here. At Beginning");
        Gson gson = new Gson();
        if (Gdx.files.local("hof.json").exists()) {
            FileHandle file = Gdx.files.local("hof.json");
            System.out.println("File Exists.");
            famers = gson.fromJson(file.readString(), listType);

            for (int i = 0; i<hofSize;i++) {
                if(!enteredUser && player.getKills()>famers.get(i).getPoints()) {
                    famers.add(i, new Famer(player.getName(), player.getKills()));
                    enteredUser = true;
                }
            }
            file.writeString(gson.toJson(famers), false);
        } else {
            FileHandle file = Gdx.files.local("hof.json");

            for (int i = 0; i<hofSize;++i) {
                famers.add(i, new Famer());
                if(!enteredUser && player.getKills()>famers.get(i).getPoints()) {
                    famers.add(i, new Famer(player.getName(), player.getKills()));
                    enteredUser=true;
                }
            }

            file.writeString(gson.toJson(famers), false);
        }
        while(famers.size()>hofSize){
            famers.remove(famers.size()-1);
        }
        System.out.println("Does it die here. At End!");


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
//        masterTable.setDebug(true);
        stage.addActor(masterTable);
        Label title  = new Label("HALL OF FAME", skin);
        title.setFontScale(1.75f);
        title.setStyle(new Label.LabelStyle(new BitmapFont((Gdx.files.internal("fonts/spooky.fnt"))),Color.RED));

        masterTable.row().colspan(2).expandX().fillX();
        masterTable.add(title).colspan(2).expandX().width(450).height(150);


        for(int i=0; i<hofSize;i++){
            masterTable.row().colspan(2).expandX().fillX();
            masterTable.add(new Label((i+1)+". " + famers.get(i).getName() +" " +
                    famers.get(i).getPoints(), skin)).width(200).height(100);
    }
        System.out.print("It gets here.");

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();

        // ---GAME CONTROLS---
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            //game.setScreen(new MainMenuScreen(game));
            //game.init(game);
            GameScreen.init();
            game.changeScreen(DeathMarch.MENU);

        }
        // ------------------



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
