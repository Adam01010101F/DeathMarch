package com.dmr.deathmarch;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;


public class HOFScreen implements Screen {
    final DeathMarch game;
    private OrthographicCamera camera;
    public HOFScreen(final DeathMarch game){
        this.game = game;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1280, 720);


    }

}
