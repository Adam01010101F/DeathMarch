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

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

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
}
