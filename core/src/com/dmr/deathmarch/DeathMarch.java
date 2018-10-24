package com.dmr.deathmarch;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class DeathMarch extends Game {
	SpriteBatch batch;
	public BitmapFont font;
	public Stage stage;

	TiledTest test1;

	private SplashScreen splashScreen;
	private PreferencesScreen preferencesScreen;
	private MainMenuScreen menuScreen;
	private GameScreen mainScreen;
	//private EndScreen endScreen;
	private AppPreferences preferences;
	private TileMapScreen tileMap;

	public final static int MENU = 0;
	public final static int PREFERENCES = 1;
	public final static int APPLICATION = 2;
	public final static int ENDGAME = 3;
	public final static int APP2 = 4;


	public AppPreferences getPreferences(){

		return this.preferences;

	}

	@Override
	public void create () {
		batch = new SpriteBatch();
		font = new BitmapFont();

//		this.setScreen(new MainMenuScreen(this));

		splashScreen = new SplashScreen(this);
		setScreen(splashScreen);
		preferences = new AppPreferences();
	}

	@Override
	public void render () {
		super.render(); //Needed otherwise you will get a black screen.
//		Gdx.gl.glClearColor(0f, 0f, 0f, 1);
//		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
//		stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
//		stage.draw();
	}

	@Override
	public void dispose () {
		batch.dispose();
		font.dispose();
	}
	public void changeScreen(int screen){

		switch(screen){

			case MENU:

				if(menuScreen == null) menuScreen = new MainMenuScreen(this);

				this.setScreen(menuScreen);

				break;

			case PREFERENCES:

				if(preferencesScreen == null) preferencesScreen = new PreferencesScreen(this);

				this.setScreen(preferencesScreen);

				break;

			case APPLICATION:

				// always make new game screen so game can't start midway

				if(mainScreen == null){

					mainScreen = new GameScreen(this);

				}

				this.setScreen(mainScreen);

				break;


			case APP2:

				if(mainScreen == null){
					//test1.create();
					tileMap = new TileMapScreen(this);
				}
				this.setScreen(tileMap);

				break;
		}

	}

}
