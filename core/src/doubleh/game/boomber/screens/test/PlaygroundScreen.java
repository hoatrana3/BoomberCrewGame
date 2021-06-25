package doubleh.game.boomber.screens.test;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import doubleh.game.boomber.screens.BaseScreen;

/**
 * This screen just a playground to have fun with scebe2d world, it doesnt have any meaning in our game
 */
public class PlaygroundScreen extends BaseScreen {

    public PlaygroundScreen() {
    }

    @Override
    public void show() {
    }

    private void update(float delta) {
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1f, 1f, 1f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        update(delta);
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
    }
}

