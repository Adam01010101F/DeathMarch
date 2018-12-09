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
	private FirstTimeMenu bunnyscreen;
	private GamesMasterScreen gms;
	private GIFscreen gifscreen;
	//private EndScreen endScreen;
	private AppPreferences preferences;
	private TileMapScreen tileMap;
	private shopScreen s;
	private HOFScreen HallOfFame;
	private LoseScreen loseScreen;
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
	public final static int WIN = 7;
	public final static int LOSE = 8;
	public final static int BUNNIES = 9;
	public final static int GIF = 10;
	public final static int GAMESMASTER = 11;


	public DeathMarch() {


	}

	public AppPreferences getPreferences(){

		return this.preferences;

	}

	@Override
	public void create () {
		batch = new SpriteBatch();
		font = new BitmapFont();

		playerTex = new Texture(Gdx.files.internal("icon.png"));
		playerTex1 = new Texture(Gdx.files.internal("bunnies/bunny3.png"));
		player1 = new Player("Shredder", false, playerTex);
		player2 = new Player("Donatello", false, playerTex1);

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
		//bgm_Music.dispose();
		font.dispose();
	}

	public void changeScreen(int screen){

		switch(screen){

			case GAMESMASTER:
				if(gms == null) gms = new GamesMasterScreen(this);
				System.out.println("this is gifscreen");
				this.setScreen(gms);

				break;


			case GIF:
				if(gifscreen == null) gifscreen = new GIFscreen(this);
				System.out.println("this is gifscreen");
				this.setScreen(gifscreen);

				break;


			case BUNNIES:

				if(bunnyscreen == null) bunnyscreen = new FirstTimeMenu(this);
				System.out.println("this is screen 9 - bunnies");
				this.setScreen(bunnyscreen);

				break;



			case MENU:

				if(menuScreen == null) menuScreen = new MainMenuScreen(this);
				System.out.println("this is MainMenuScreen");

				this.setScreen(menuScreen);

                break;

            case PREFERENCES:

                if(preferencesScreen == null) preferencesScreen = new PreferencesScreen(this);
				System.out.println("this is preferences screen");

                this.setScreen(preferencesScreen);

                break;

            case APPLICATION:

                // always make new game screen so game can't start midway

                if(mainScreen == null){

                    mainScreen = new GameScreen(this,player1,player2);
					System.out.println("this is game screen");


				}

                this.setScreen(mainScreen);

                break;

            case HOF:
                //System.out.println(nameScreen.getInputName());
				HallOfFame = new HOFScreen(this, player1);
				System.out.println("this is hall of fame screen");

				this.setScreen(HallOfFame);
                break;


            case SHOP:

				if(s == null) s = new shopScreen(this,player1,player2);
				System.out.println("this is shop screen");

				this.setScreen(s);

				break;

			case LOSE:

				if(loseScreen == null) loseScreen = new LoseScreen(this,player1);
				System.out.println("this is lose screen");

				this.setScreen(loseScreen);

				break;


			case WIN:

				if(nameScreen == null) nameScreen = new NameScreen(this,player1);
				System.out.println("this is win screen");

				this.setScreen(nameScreen);

				break;

		}

	}

//	public void create2(){
//		setScreen(new MainMenuScreen(this));
//	}
//	public void init(DeathMarch game){
//		create2();
//		//game.changeScreen(DeathMarch.MENU);
//	}

}
