package doubleh.game.boomber;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import doubleh.game.boomber.managers.AudioManager;
import doubleh.game.boomber.screens.main.*;
import doubleh.game.boomber.screens.test.AITestScreen;
import doubleh.game.boomber.screens.test.PhysicTestScreen;
import doubleh.game.boomber.screens.test.PlaygroundScreen;
import doubleh.game.boomber.utils.Constants;

/**
 * This is core main class for game
 */
public class BoomberGame extends Game {

    private static BoomberGame instance = new BoomberGame();

    public SpriteBatch batch;

    public BitmapFont font;

    public AssetManager assets;
    public AudioManager audios;

    public static Skin skin;

    public static Image imgFadeTransition;

    public LoadingScreen loadingScreen;
    public SplashScreen splashScreen;
    public MainMenuScreen mainMenuScreen;
    public PlaygroundScreen playgroundScreen;
    public PhysicTestScreen physicTestScreen;
    public AITestScreen aiTestScreen;
    public GameScreen gameScreen;
    public GameOverScreen gameOverScreen;

    public BoomberGame() {
    }

    public static BoomberGame getInstance() {
        return instance;
    }

    @Override
    public void create() {
        Constants.WIDTH = Gdx.graphics.getWidth();
        Constants.HEIGHT = Gdx.graphics.getHeight();

        assets = new AssetManager();
        batch = new SpriteBatch();
        audios = new AudioManager();

        initFonts();

        loadingScreen = new LoadingScreen();
        splashScreen = new SplashScreen();
        mainMenuScreen = new MainMenuScreen();
        playgroundScreen = new PlaygroundScreen();
        physicTestScreen = new PhysicTestScreen();
        aiTestScreen = new AITestScreen();
        gameScreen = new GameScreen();
        gameOverScreen = new GameOverScreen();

        this.setScreen(loadingScreen);

        Gdx.graphics.getGL20().glEnable(GL20.GL_BLEND);
    }

    @Override
    public void render() {
        super.render();

        if (assets.getProgress() == 1f) audios.updateVolume();
    }

    @Override
    public void dispose() {
        super.dispose();
        try {
            batch.dispose();
            font.dispose();
            assets.dispose();

            loadingScreen.dispose();
            splashScreen.dispose();
            mainMenuScreen.dispose();
            playgroundScreen.dispose();
            aiTestScreen.dispose();
            physicTestScreen.dispose();
            gameScreen.dispose();
            gameOverScreen.dispose();
        } catch (NullPointerException e) {
            // TODO: Failed in dispose
        }
    }

    /**
     * Init fonts for game
     */
    private void initFonts() {
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/UTM Mobifone KT.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter params = new FreeTypeFontGenerator.FreeTypeFontParameter();

        params.size = Constants.WIDTH / 50;
        params.color = Color.WHITE;
        font = generator.generateFont(params);
        font.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
    }
}

/**
 * TODO: UI, Audio, Ground have effect, change tiles in level of boss
 */
