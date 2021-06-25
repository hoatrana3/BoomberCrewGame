package doubleh.game.boomber.screens.test;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ai.steer.behaviors.Arrive;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import doubleh.game.boomber.ai.AISteeringBehaviour;
import doubleh.game.boomber.listeners.TestPhysicContactListener;
import doubleh.game.boomber.screens.BaseScreen;
import doubleh.game.boomber.utils.CameraStylist;
import doubleh.game.boomber.utils.Constants;

import java.util.ArrayList;
import java.util.List;

import static doubleh.game.boomber.utils.B2dBuilder.*;
import static doubleh.game.boomber.utils.Constants.*;

/**
 * This screen just a playground to have fun with AI (background is box2d), just a test screen
 */
public class AITestScreen extends BaseScreen {

    private World tWorld;
    private Box2DDebugRenderer tB2dr;
    private Body tPlayer, tEnemy;
    private OrthographicCamera physicCamera;

    private AISteeringBehaviour sPlayer, sEnemy;

    private Fixture fixturePlayer, fixtureEnemy;

    private List<Fixture> fixtures;

    public AITestScreen() {
        tWorld = new World(new Vector2(0, 0f), true);
        tB2dr = new Box2DDebugRenderer();

        fixtures = new ArrayList<Fixture>();

        physicCamera = new OrthographicCamera();
        physicCamera.setToOrtho(false, 32, 18);

        tWorld.setContactListener(new TestPhysicContactListener());
    }

    @Override
    public void show() {
        tPlayer = createBody(tWorld, 0, 2, BodyDef.BodyType.DynamicBody, false);
        tEnemy = createBody(tWorld, 4, 7, BodyDef.BodyType.DynamicBody, false);

        fixturePlayer = createCircleFixture(tPlayer, 0.5f, 1f, "", false, CATEGORY_PLAYER, MASK_PLAYER, GROUP_ALWAYS_COLLIDE, false);
        fixtureEnemy = createCircleFixture(tEnemy, 0.5f, 1f, "", false, CATEGORY_ENEMY, MASK_ENEMY, GROUP_ALWAYS_COLLIDE, false);

        for (int i = 0; i < 25; i += 5)
            for (int j = 0; j < 25; j += 5)
                lazyCreateTestBoxObject(i, j, BodyDef.BodyType.StaticBody);

        Body wall = createBody(tWorld, 0, 0, BodyDef.BodyType.StaticBody, false);

        Vector2[] vertices = new Vector2[5];
        vertices[0] = new Vector2(-5f, -5f);
        vertices[1] = new Vector2(4 * 5f + 5f, -5f);
        vertices[2] = new Vector2(4 * 5f + 5f, 4 * 5f + 5f);
        vertices[3] = new Vector2(-5f, 4 * 5f + 5f);
        vertices[4] = new Vector2(-5f, -5f);

        fixtures.add(createChainShapeFixture(wall, vertices, 1f, "Test", false, CATEGORY_BRICK, MASK_BRICK, GROUP_ALWAYS_COLLIDE, false));

        sPlayer = new AISteeringBehaviour(tPlayer, true, 1);
        sEnemy = new AISteeringBehaviour(tEnemy, true, 1);

        Arrive<Vector2> arrive = new Arrive<>(sEnemy, sPlayer)
                .setTimeToTarget(0.1f) //
                .setArrivalTolerance(0.001f) //
                .setDecelerationRadius(3);

        sEnemy.setSteeringBehavior(arrive);
    }

    /**
     * Create multiple object in word - for lazy
     *
     * @param x x position of object in physic world
     * @param y y position of object in physic world
     */
    private void lazyCreateTestBoxObject(int x, int y, BodyDef.BodyType type) {
        Body tempBody = createBody(tWorld, x, y, type, true);
        fixtures.add(createBoxFixture(tempBody, 1f, 1f, 1f, "Test", false, CATEGORY_BRICK, MASK_BRICK, GROUP_ALWAYS_COLLIDE, false));
    }

    private void update(float delta) {
        tWorld.step(delta, 6, 2);

        if (Constants.USE_LEARP) CameraStylist.learpOnTarget(physicCamera, tPlayer.getPosition());
        else CameraStylist.lockOnTarget(physicCamera, tPlayer.getPosition());

        physicCamera.update();

        sEnemy.update(delta);

        handleInputs();
    }

    private void handleInputs() {
        Vector2 oldPos = tPlayer.getPosition();

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            sPlayer.getBody().setLinearVelocity(-8f, 0);
        } else {
            if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
                sPlayer.getBody().setLinearVelocity(8f, 0);
            } else {
                if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
                    sPlayer.getBody().setLinearVelocity(0, 8f);
                } else {
                    if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
                        sPlayer.getBody().setLinearVelocity(0, -8f);
                    } else {
                        if (Constants.USE_LEARP)
                            sPlayer.getBody().setLinearVelocity(MathUtils.lerp(tPlayer.getLinearVelocity().x, 0, LEARP_FACTOR), MathUtils.lerp(tPlayer.getLinearVelocity().y, 0, LEARP_FACTOR));
                        else sPlayer.getBody().setLinearVelocity(0, 0);
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
