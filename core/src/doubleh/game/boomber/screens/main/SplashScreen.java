package doubleh.game.boomber.screens.main;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import doubleh.game.boomber.BoomberGame;
import doubleh.game.boomber.screens.BaseScreen;
import doubleh.game.boomber.utils.Constants;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;

/**
 * This screen is for introduction screen
 */
public class SplashScreen extends BaseScreen {

    private Stage stage;

    public SplashScreen() {
        this.stage = new Stage(new FitViewport(Constants.WIDTH, Constants.HEIGHT));
    }

    @Override
    public void show() {

        Gdx.input.setInputProcessor(stage);

        Texture splashTex = BoomberGame.getInstance().assets.get("img/logo.png", Texture.class);
        Image splashImg;

        splashImg = new Image(splashTex);
        splashImg.setSize(splashImg.getWidth() * (Constants.WIDTH * 1.25f / 1024) * (splashImg.getWidth() / Constants.WIDTH), splashImg.getHeight() * (Constants.WIDTH * 1.25f / 1024) * (splashImg.getWidth() / Constants.WIDTH));
        splashImg.setOrigin(splashImg.getWidth() / 2, splashImg.getHeight() / 2);
        splashImg.setPosition(stage.getWidth() / 2 - splashImg.getWidth() / 2, stage.getHeight() + splashImg.getHeight() / 2);
        splashImg.addAction(sequence(alpha(0), scaleTo(.1f, .1f),
                parallel(fadeIn(2f, Interpolation.pow2),
                        scaleTo(2f, 2f, 2.5f, Interpolation.pow5),
                        moveTo(stage.getWidth() / 2 - splashImg.getWidth() / 2, stage.getHeight() / 2 - splashImg.getHeight() / 2, 2f, Interpolation.swing)
                ),
                delay(0.5f), fadeOut(1f), run(() -> BoomberGame.getInstance().setScreen(BoomberGame.getInstance().mainMenuScreen))));

        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                BoomberGame.getInstance().audios.playSound("s_INTRO", 1f, 1f, 0f);
            }
        }, 1f);

        stage.addActor(splashImg);
    }

    private void update(float delta) {
        stage.act(delta);

        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            BoomberGame.getInstance().setScreen(BoomberGame.getInstance().mainMenuScreen);
        }
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1f, 1f, 1f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        update(delta);

        stage.draw();
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
