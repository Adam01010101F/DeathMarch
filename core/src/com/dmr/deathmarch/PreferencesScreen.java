package com.dmr.deathmarch;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;


public class PreferencesScreen implements Screen {
    private DeathMarch game;
    private static final String PREF_MUSIC_VOLUME = "volume";
    private static final String PREF_MUSIC_ENABLED = "music.enabled";
    private static final String PREF_SOUND_ENABLED = "sound.enabled";
    private static final String PREF_SOUND_VOL = "sound";
    private static final String PREFS_NAME = "b2dtut";

    private AppPreferences game1;

    Label titleLabel;
    Label volumeMusicLabel;
    Label volumeSoundLabel;
    Label musicOnOffLabel;
    Label soundOnOffLabel;

    Stage stage;

    public PreferencesScreen(final DeathMarch game) {
        this.game = game;
        stage = new Stage(new ScreenViewport());

    }

    @Override

    public void show() {

        stage.clear();
        Gdx.input.setInputProcessor(stage);


        // Create a table that fills the screen. Everything else will go inside
        // this table.

        Table table = new Table();

        table.setFillParent(true);

        //table.setDebug(true);

        stage.addActor(table);


        // temporary until we have asset manager in

        Skin skin = new Skin(Gdx.files.internal("skin/pixthulhu-ui.json"));


        // music volume

        final Slider volumeMusicSlider = new Slider(0f, 1f, 0.1f, false, skin);

        volumeMusicSlider.setValue(game.getPreferences().getMusicVolume());

        volumeMusicSlider.addListener(new EventListener() {

            @Override

            public boolean handle(Event event) {

                game.getPreferences().setMusicVolume(volumeMusicSlider.getValue());

                // updateVolumeLabel();

                return false;

            }

        });
        // sound volume

        final Slider soundMusicSlider = new Slider(0f, 1f, 0.1f, false, skin);

        soundMusicSlider.setValue(game.getPreferences().getSoundVolume());

        soundMusicSlider.addListener(new EventListener() {

            @Override

            public boolean handle(Event event) {

                game.getPreferences().setSoundVolume(soundMusicSlider.getValue());

                // updateVolumeLabel();

                return false;

            }

        });


        // music on/off

        final CheckBox musicCheckbox = new CheckBox(null, skin);

        musicCheckbox.setChecked(game.getPreferences().isMusicEnabled());

        musicCheckbox.addListener(new EventListener() {

            @Override

            public boolean handle(Event event) {

                boolean enabled = musicCheckbox.isChecked();

                game.getPreferences().setMusicEnabled(enabled);

                return false;

            }

        });
        // sound on/off

        final CheckBox soundEffectsCheckbox = new CheckBox(null, skin);

        soundEffectsCheckbox.setChecked(game.getPreferences().isSoundEffectsEnabled());

        soundEffectsCheckbox.addListener(new EventListener() {

            @Override

            public boolean handle(Event event) {

                boolean enabled = soundEffectsCheckbox.isChecked();

                game.getPreferences().setSoundEffectsEnabled(enabled);

                return false;

            }

        });


        // return to main screen button

        final TextButton backButton = new TextButton("Back", skin);

        backButton.addListener(new ChangeListener() {

            @Override

            public void changed(ChangeEvent event, Actor actor) {
                game.changeScreen(DeathMarch.MENU);


            }

        });
        titleLabel = new Label("Preferences", skin);

        volumeMusicLabel = new Label("Music Volume", skin);

        volumeSoundLabel = new Label("Sound Volume", skin);

        musicOnOffLabel = new Label("Music", skin);

        soundOnOffLabel = new Label("Sound Effect", skin);


        table.add(titleLabel).colspan(2);

        table.row().pad(10, 0, 0, 10);

        table.add(volumeMusicLabel).left();

        table.add(volumeMusicSlider);
        table.row().pad(10, 0, 0, 10);

        table.add(musicOnOffLabel).left();

        table.add(musicCheckbox);

        table.row().pad(10, 0, 0, 10);

        table.add(volumeSoundLabel).left();

        table.add(soundMusicSlider);

        table.row().pad(10, 0, 0, 10);

        table.add(soundOnOffLabel).left();

        table.add(soundEffectsCheckbox);

        table.row().pad(10, 0, 0, 10);

        table.add(backButton).colspan(2);


    }

    @Override
    public void render(float delta) {
        // TODO Auto-generated method stub


        // clear the screen ready for next set of images to be drawn

        Gdx.gl.glClearColor(0f, 0f, 0f, 1);

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


        // tell our stage to do actions and draw itself

        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));

        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        // TODO Auto-generated method stub

        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {
        // TODO Auto-generated method stub
    }

    @Override
    public void resume() {
        // TODO Auto-generated method stub
    }

    @Override
    public void hide() {
        // TODO Auto-generated method stub
    }

    @Override
    public void dispose() {
        // TODO Auto-generated method stub

    }
}