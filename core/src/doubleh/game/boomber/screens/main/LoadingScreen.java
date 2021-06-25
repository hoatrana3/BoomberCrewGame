package doubleh.game.boomber.screens.main;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import doubleh.game.boomber.BoomberGame;
import doubleh.game.boomber.screens.BaseScreen;
import doubleh.game.boomber.utils.Constants;

/**
 * This screen is for loading assets game
 */
public class LoadingScreen extends BaseScreen {

    private ShapeRenderer shapeRenderer;
    private float progress;

    public LoadingScreen() {
    }

    @Override
    public void show() {
        Constants.WIDTH = Gdx.graphics.getWidth();
        Constants.HEIGHT = Gdx.graphics.getHeight();

        shapeRenderer = new ShapeRenderer();

        progress = 0f;

        BoomberGame.getInstance().font.setColor(Color.BLACK);

        queueAssets();
    }

    private void loadSkin() {
        BoomberGame.skin = new Skin();
        BoomberGame.skin.addRegions(BoomberGame.getInstance().assets.get("ui/uiskin.atlas", TextureAtlas.class));
        BoomberGame.skin.add("default-font", BoomberGame.getInstance().font);
        BoomberGame.skin.load(Gdx.files.internal("ui/uiskin.json"));
    }

    private void update(float delta) {
        progress = MathUtils.lerp(progress, BoomberGame.getInstance().assets.getProgress(), .1f);
        if (BoomberGame.getInstance().assets.update() && progress >= BoomberGame.getInstance().assets.getProgress() - .001f) {
            BoomberGame.getInstance().assets.finishLoading();
            loadSkin();
            Constants.FINISHED_LOADING = true;
            BoomberGame.getInstance().setScreen(BoomberGame.getInstance().splashScreen);
        }
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1f, 1f, 1f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        update(delta);

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(new Color(44f / 255f, 44f / 255f, 44f / 255f, 1f));
        shapeRenderer.rect(0, Constants.HEIGHT / 2f - 4, Constants.WIDTH, 8);

        shapeRenderer.setColor(new Color(201f / 255f, 28f / 255f, 8f / 255f, 1f));
        shapeRenderer.rect(0, Constants.HEIGHT / 2f - 4, progress * Constants.WIDTH, 8);
        shapeRenderer.end();

        BoomberGame.getInstance().batch.begin();
        BoomberGame.getInstance().font.draw(BoomberGame.getInstance().batch, "Loading game assets, please wait", 40, Constants.HEIGHT / 2 - 30);
        BoomberGame.getInstance().batch.end();
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
        shapeRenderer.dispose();
    }

    private void queueAssets() {
        BoomberGame.getInstance().assets.load("img/namegame.png", Texture.class);
        BoomberGame.getInstance().assets.load("img/logo.png", Texture.class);
        BoomberGame.getInstance().assets.load("img/mainmenubackground.png", Texture.class);
        BoomberGame.getInstance().assets.load("img/black.jpg", Texture.class);
        BoomberGame.getInstance().assets.load("img/tutorial.png", Texture.class);
        BoomberGame.getInstance().assets.load("img/credits.png", Texture.class);

        BoomberGame.getInstance().assets.load("img/items/bomb.png", Texture.class);
        BoomberGame.getInstance().assets.load("img/items/fire.png", Texture.class);
        BoomberGame.getInstance().assets.load("img/items/lightning.png", Texture.class);
        BoomberGame.getInstance().assets.load("img/items/hospital.png", Texture.class);

        BoomberGame.getInstance().assets.load("ui/uiskin.atlas", TextureAtlas.class);
        BoomberGame.getInstance().assets.load("img/boom/boom.atlas", TextureAtlas.class);
        BoomberGame.getInstance().assets.load("img/explosion/explosion.atlas", TextureAtlas.class);
        BoomberGame.getInstance().assets.load("img/explosion/explosion_boss_violet.atlas", TextureAtlas.class);
        BoomberGame.getInstance().assets.load("img/explosion/explosion_boss_red.atlas", TextureAtlas.class);
        BoomberGame.getInstance().assets.load("img/explosion/explosion_enemy_normal.atlas", TextureAtlas.class);

        BoomberGame.getInstance().assets.load("img/badges/badges.atlas", TextureAtlas.class);

        BoomberGame.getInstance().assets.load("img/character/ninja/ninja.atlas", TextureAtlas.class);
        BoomberGame.getInstance().assets.load("img/character/combatant/combatant.atlas", TextureAtlas.class);
        BoomberGame.getInstance().assets.load("img/character/knight/knight.atlas", TextureAtlas.class);
        BoomberGame.getInstance().assets.load("img/character/mummydog/mummydog.atlas", TextureAtlas.class);

        BoomberGame.getInstance().assets.load("img/portal/portal.atlas", TextureAtlas.class);

        BoomberGame.getInstance().assets.load("img/enemy/normal/mva/mvaenemy.atlas", TextureAtlas.class);
        BoomberGame.getInstance().assets.load("img/enemy/normal/firer/fireenemy.atlas", TextureAtlas.class);
        BoomberGame.getInstance().assets.load("img/enemy/normal/boom/boomenemy.atlas", TextureAtlas.class);
        BoomberGame.getInstance().assets.load("img/enemy/normal/bat/batenemy.atlas", TextureAtlas.class);

        BoomberGame.getInstance().assets.load("img/enemy/boss/violet/boss-violet.atlas", TextureAtlas.class);
        BoomberGame.getInstance().assets.load("img/enemy/boss/red/boss-red.atlas", TextureAtlas.class);

        BoomberGame.getInstance().audios.load();
    }
}
