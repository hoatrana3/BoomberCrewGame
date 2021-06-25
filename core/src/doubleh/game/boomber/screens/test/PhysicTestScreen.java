package doubleh.game.boomber.screens.test;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import doubleh.game.boomber.listeners.TestPhysicContactListener;
import doubleh.game.boomber.screens.BaseScreen;
import doubleh.game.boomber.utils.CameraStylist;
import doubleh.game.boomber.utils.Constants;

import static doubleh.game.boomber.utils.B2dBuilder.createBody;
import static doubleh.game.boomber.utils.B2dBuilder.createBoxFixture;
import static doubleh.game.boomber.utils.Constants.*;

/**
 * This screen just a playground to have fun with box2d light, just a test screen
 */
public class PhysicTestScreen extends BaseScreen {

    private World tWorld;
    private Box2DDebugRenderer tB2dr;
    private Body tPlayer, tEnemy, tGround;
    private OrthographicCamera physicCamera;
    private TestPhysicContactListener contactListener;

    private Fixture fixturePlayer;

    public PhysicTestScreen() {
        physicCamera = new OrthographicCamera();
        physicCamera.setToOrtho(false, 32, 18);
        tWorld = new World(new Vector2(0, 0f), true);
        tB2dr = new Box2DDebugRenderer();
        contactListener = new TestPhysicContactListener();

        tWorld.setContactListener(contactListener);

    }

    @Override
    public void show() {
        tPlayer = createBody(tWorld, 0, 2, BodyDef.BodyType.DynamicBody, true);
        tEnemy = createBody(tWorld, 4, 7, BodyDef.BodyType.StaticBody, true);

        createBoxFixture(tPlayer, 1f, 1f, 1f, "Player", true, CATEGORY_PLAYER, MASK_PLAYER, GROUP_NEVER_COLLIDE, false);
        createBoxFixture(tEnemy, 1f, 1f, 1f, "Enemy", true, CATEGORY_BOOM, MASK_BOOM, GROUP_NEVER_COLLIDE, false);

        for (int i = 0; i < 30; i += 10)
            for (int j = 0; j < 30; j += 10)
                lazyCreateTestBoxObject(i, j, BodyDef.BodyType.StaticBody);

        for (int i = 3; i < 30; i += 10)
            for (int j = 3; j < 30; j += 10)
                lazyCreateTestBoxObject(i, j, BodyDef.BodyType.StaticBody);

    }

    /**
     * Create multiple object in word - for lazy
     *
     * @param x
     * @param y
     */
    private void lazyCreateTestBoxObject(int x, int y, BodyDef.BodyType type) {
        Body tempBody = createBody(tWorld, x, y, type, true);
        createBoxFixture(tempBody, 1f, 1f, 1f, "Test", true, CATEGORY_BRICK, MASK_BRICK, GROUP_ALWAYS_COLLIDE, false);
    }

    private void update(float delta) {
        tWorld.step(delta, 6, 2);

        if (Constants.USE_LEARP) CameraStylist.learpOnTarget(physicCamera, tPlayer.getPosition());
        else CameraStylist.lockOnTarget(physicCamera, tPlayer.getPosition());

        physicCamera.update();

        handleInputs();
    }

    private void handleInputs() {
        Vector2 oldPos = tPlayer.getPosition();

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            tPlayer.setLinearVelocity(-8f, 0);
        } else {
            if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
                tPlayer.setLinearVelocity(8f, 0);
            } else {
                if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
                    tPlayer.setLinearVelocity(0, 8f);
                } else {
                    if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
                        tPlayer.setLinearVelocity(0, -8f);
                    } else {
                        if (Constants.USE_LEARP)
                            tPlayer.setLinearVelocity(MathUtils.lerp(tPlayer.getLinearVelocity().x, 0, LEARP_FACTOR), MathUtils.lerp(tPlayer.getLinearVelocity().y, 0, LEARP_FACTOR));
                        else tPlayer.setLinearVelocity(0, 0);
                    }
                }
            }
        }
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        update(delta);

        tB2dr.render(tWorld, physicCamera.combined);
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
        tWorld.dispose();
        tB2dr.dispose();
    }
}
