package doubleh.game.boomber.screens.main;

import com.badlogic.gdx.audio.Music;
import doubleh.game.boomber.BoomberGame;
import doubleh.game.boomber.managers.ModeManager;
import doubleh.game.boomber.screens.BaseScreen;

/**
 * This screen is main game play screen of our game
 */
public class GameScreen extends BaseScreen {
    public ModeManager modeManager;

    public enum gameState {
    }

    public gameState state;

    public GameScreen() {
    }

    @Override
    public void show() {
        modeManager = new ModeManager();
        modeManager.load(0);

        BoomberGame.getInstance().audios.stopMusic("m_MAINMENU");

        BoomberGame.getInstance().audios.loopMusic("m_INGAME");
        BoomberGame.getInstance().audios.get("m_INGAME", Music.class).setVolume(1f);
    }

    @Override
    public void render(float delta) {
        modeManager.render(delta);
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
    }
}
