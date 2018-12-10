package com.dmr.deathmarch;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;


public class GIFscreen implements Screen {
    private SpriteBatch batch;
    private Texture ttrSplash;
    private DeathMarch game;
    private Stage stage;
    private Animation<TextureRegion> animation;
    float elapsed;

    public GIFscreen(DeathMarch game) {
        stage = new Stage(new ScreenViewport());
        this.game = game;
        batch = new SpriteBatch();
        ttrSplash = new Texture("logo_bird.png");
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        elapsed += Gdx.graphics.getDeltaTime();


        animation = com.holidaystudios.tools.GifDecoder.loadGIFAnimation(Animation.PlayMode.LOOP, Gdx.files.internal("images/yo.gif").read());


        stage.draw();

        batch.begin();


        //batch.draw(ttrSplash, 400, 0, Gdx.graphics.getWidth()- 800, Gdx.graphics.getHeight());
        batch.draw(animation.getKeyFrame(elapsed), 375.0f, 425.0f);
        batch.end();


        if(Gdx.input.isTouched()){
            game.setScreen(new GamesMasterScreen(game));
            //dispose();
        }
    }

    @Override
    public void hide() { }

    @Override
    public void pause() { }

    @Override
    public void resume() { }

    @Override
    public void show() {

    }

    @Override
    public void resize(int width, int height) { }

    @Override
    public void dispose() {
        ttrSplash.dispose();
        batch.dispose();
    }
}