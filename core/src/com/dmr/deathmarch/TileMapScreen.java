package com.dmr.deathmarch;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

public class TileMapScreen implements Screen {
    DeathMarch game;
    private OrthographicCamera camera ;
    private OrthogonalTiledMapRenderer renderer ;
    private TiledMap map;

    SpriteBatch batch;

    float unitScale = 1/30f;

    public TileMapScreen(DeathMarch game) {
        this.game = game;
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        renderer.setView(camera);
        renderer.render();

        batch.begin();

        batch.end();
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void show() {

        TiledMap map = new TmxMapLoader().load("demoMap.tmx");
        renderer = new OrthogonalTiledMapRenderer(map,unitScale);

        camera = new OrthographicCamera();

        camera.setToOrtho(false,40,40);
        renderer.setView(camera);
        camera.update();

        batch = new SpriteBatch();
    }

    @Override
    public void hide() {
        dispose();
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void dispose() {
        System.out.println("Disposed");
        batch.dispose();
        renderer.dispose();
    }
}
