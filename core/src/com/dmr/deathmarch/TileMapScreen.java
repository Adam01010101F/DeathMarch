package com.dmr.deathmarch;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

public class TileMapScreen implements Screen {
    DeathMarch game;
    private OrthographicCamera camera ;
    private OrthogonalTiledMapRenderer renderer ;
    private TiledMap map;
    private Player player;
    private Texture playerTex = new Texture(Gdx.files.internal("survivor-shoot_rifle_0.png"));
    private Player pOne;
    private int[] background = new int[] {0}, foreground = new int[] {1};
    SpriteBatch batch;

    float unitScale = 1/30f;

    public TileMapScreen(DeathMarch game) {

        pOne = new Player("Shredder", false, playerTex, 23, 38, 293, 191);
        pOne.setOriginCenter();
        this.game = game;
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        camera.position.set(player.getX() + player.getWidth() / 2, player.getY() + player.getHeight() / 2, 0);
        camera.update();

        renderer.setView(camera);

        renderer.render(background);
        renderer.render(foreground);
        //renderer.getSpriteBatch().begin();
       // player.draw(renderer.getSpriteBatch());
       // renderer.getSpriteBatch().end();


//        camera.update();
//        renderer.setView(camera);
//        renderer.render();
//
//        batch.begin();
//
//        batch.end();
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void show() {

        TiledMap map = new TmxMapLoader().load("maps/demoMap.tmx");
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
