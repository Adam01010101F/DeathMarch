package com.dmr.deathmarch;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

public class GameScreen implements Screen {
    final DeathMarch game;
    private Texture p1Tex;
    private Texture p2Tex;
    private Rectangle p1;
    private Rectangle p2;
    private Sound useWeapon;

    public GameScreen(final DeathMarch game){
        this.game = game;

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
