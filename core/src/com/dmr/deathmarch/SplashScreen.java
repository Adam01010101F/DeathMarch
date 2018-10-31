package com.dmr.deathmarch;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class SplashScreen implements Screen {
    private SpriteBatch batch;
    private Texture ttrSplash;
    private DeathMarch game;

    public SplashScreen(DeathMarch game) {
        this.game = game;
        batch = new SpriteBatch();
        ttrSplash = new Texture("logo_bird.png");
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        batch.draw(ttrSplash, 400, 0, Gdx.graphics.getWidth()- 800, Gdx.graphics.getHeight());
        batch.end();

        if(Gdx.input.isTouched()){
            game.setScreen(new FirstTimeMenu(game));
            dispose();
        }
    }

    @Override
    public void hide() { }

    @Override
    public void pause() { }

    @Override
    public void resume() { }

    @Override
    public void show() { }

    @Override
    public void resize(int width, int height) { }

    @Override
    public void dispose() {
        ttrSplash.dispose();
        batch.dispose();
    }
}