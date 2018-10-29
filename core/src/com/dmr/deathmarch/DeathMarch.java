package com.dmr.deathmarch;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
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
	public Player player1;
	public Player player2;
	private SplashScreen splashScreen;
	private PreferencesScreen preferencesScreen;
	private MainMenuScreen menuScreen;
	private GameScreen mainScreen;
	private NameScreen nameScreen;
	//private EndScreen endScreen;
	private AppPreferences preferences;
	private TileMapScreen tileMap;
	private shopScreen s;
	private HOFScreen HallOfFame;
	private Texture playerTex;
	private Music bgm_Music;
	private Texture playerTex1;

	public final static int MENU = 0;
	public final static int PREFERENCES = 1;
	public final static int APPLICATION = 2;
	public final static int ENDGAME = 3;
	public final static int APP2 = 4;
	public final static int SHOP = 5;
	public final static int HOF = 6;



	public DeathMarch() {


	}

	public AppPreferences getPreferences(){

		return this.preferences;

	}

	@Override
	public void create () {
		batch = new SpriteBatch();
		font = new BitmapFont();

		playerTex = new Texture(Gdx.files.internal("survivor-shoot_rifle_0.png"));
		playerTex1 = new Texture(Gdx.files.internal("zombie.png"));
		player1 = new Player("Shredder", false, playerTex, 23, 38, 293, 191);
		player2 = new Player("Donatello", false, playerTex1, 0, 0, 45, 37);

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
		bgm_Music.dispose();
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

                    mainScreen = new GameScreen(this,player1,player2);

                }

                this.setScreen(mainScreen);

                break;

            case HOF:
                System.out.println(nameScreen.getInputName());
                if(HallOfFame == null){
                    HallOfFame = new HOFScreen(this, player1);
                }
                this.setScreen(HallOfFame);
                break;


            case SHOP:

				if(s == null) s = new shopScreen(this,player1,player2);

				this.setScreen(s);

				break;
		}

	}

}
