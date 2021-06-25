package doubleh.game.boomber.modes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import doubleh.game.boomber.BoomberGame;
import doubleh.game.boomber.utils.Constants;
import doubleh.game.boomber.utils.GameUtils;

import static doubleh.game.boomber.utils.Constants.MAX_LEVEL;

public class LevelMode extends BaseMode {

    public static int initialLevelIndex = 1;
    public static int lastLevelIndexGot = initialLevelIndex;
    public static boolean isFinallyWin = false;

    public LevelMode() {
        super(initialLevelIndex);
        lastLevelIndexGot = initialLevelIndex;
        isFinallyWin = false;
    }

    @Override
    public void setGLClearColor() {
        Gdx.gl.glClearColor(0.15f, 0.15f, 0.15f, 1f);
    }

    @Override
    public void handleInput() {
        super.handleInput();

        if (Gdx.input.isKeyJustPressed(Input.Keys.C)) {
            doWhenWon();
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.V)) {
            player.animation.addAction(
                    Actions.sequence(
                            Actions.fadeOut(1f, Interpolation.pow2),
                            Actions.run(() -> {
                                callSortEntities();

                                GameUtils.lazySetupFadeImageTransition(new Vector2(-Constants.WIDTH * 100, -Constants.HEIGHT * 100), new Vector2(Constants.WIDTH * 10000, Constants.HEIGHT * 10000),
                                        0, 1, 1,
                                        () -> {
                                            mapLevel.changeLevel(--currentLevelIndex);
                                            if (currentLevelIndex < 1) currentLevelIndex = 1;
                                            reload();
                                        });
                            })
                    )
            );
        }
    }

    @Override
    public void update(float delta) {
        super.update(delta);

        if (!GameUtils.isStillExistEnemiesInGame(this)) {
            portal.getBody().getFixtureList().get(0).setSensor(true);
            portal.light.setDistance((mapLevel.currentLevel.inNight) ? 6f : 2.5f);

            won = true;
        }

        if (timeCountdown <= 0) {
            player.setLives(0);
            player.beAttacked(true, null);
        }
    }

    @Override
    public void renderInformation() {
        super.renderInformation();

        if (!lost) {
            String lvlInfo = "LEVEL " + currentLevelIndex;
            String timeInfo = "TIME " + timeCountdown;
            String liveInfo = "LIVES " + player.getLives();

            BitmapFont font = BoomberGame.getInstance().font;
            SpriteBatch batch = BoomberGame.getInstance().batch;

            batch.begin();

            font.draw(batch, lvlInfo, font.getSpaceWidth() * 6, Constants.HEIGHT - Constants.HEIGHT / 22f);
            font.draw(batch, timeInfo, Constants.WIDTH / 2f - timeInfo.length() * font.getSpaceWidth() * 1.25f, Constants.HEIGHT - Constants.HEIGHT / 22f);
            font.draw(batch, liveInfo, Constants.WIDTH - (liveInfo.length() + 1) * font.getSpaceWidth() * 3, Constants.HEIGHT - Constants.HEIGHT / 22f);

            batch.end();
        }
    }

    @Override
    public void doWhenWon() {
        super.doWhenWon();

        BoomberGame.getInstance().audios.playSound("s_WIN_LEVEL", 1, 1, 0);

        isPaused = true;
        player.animation.addAction(
                Actions.sequence(
                        Actions.fadeOut(0.25f, Interpolation.pow2),
                        Actions.run(() -> {
                            callSortEntities();

                            GameUtils.lazySetupFadeImageTransition(new Vector2(-Constants.WIDTH * 100, -Constants.HEIGHT * 100), new Vector2(Constants.WIDTH * 10000, Constants.HEIGHT * 10000),
                                    0, 1, 1,
                                    () -> {
                                        mapLevel.changeLevel(++currentLevelIndex);
                                        if (currentLevelIndex > MAX_LEVEL) {
                                            currentLevelIndex = MAX_LEVEL;
                                            isFinallyWin = true;

                                            BoomberGame.getInstance().setScreen(BoomberGame.getInstance().gameOverScreen);
                                        }
                                        else {
                                            lastLevelIndexGot = currentLevelIndex;
                                            reload();
                                        }
                                    });
                        })
                )
        );
    }

    @Override
    public void doWhenLost() {
        super.doWhenLost();

        isFinallyWin = false;
    }
}
