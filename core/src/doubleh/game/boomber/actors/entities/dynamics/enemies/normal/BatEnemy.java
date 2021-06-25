package doubleh.game.boomber.actors.entities.dynamics.enemies.normal;

import com.badlogic.gdx.ai.steer.behaviors.Arrive;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import doubleh.game.boomber.actors.entities.dynamics.EnemyEntity;
import doubleh.game.boomber.actors.others.AnimationActor;
import doubleh.game.boomber.ai.AISteeringBehaviour;
import doubleh.game.boomber.modes.BaseMode;
import doubleh.game.boomber.utils.GameUtils;
import doubleh.game.boomber.utils.LightBuilder;

import static doubleh.game.boomber.utils.B2dBuilder.createCircleFixture;
import static doubleh.game.boomber.utils.Constants.*;

public class BatEnemy extends EnemyEntity {
    private final float SENSOR_RADIUS_MULTIPLE = 7f;

    private float sensorRadius;
    private AISteeringBehaviour aiSteeringBehaviour;
    private Vector2 posBackToMap;
    private boolean isActive;

    public BatEnemy(BaseMode mode, Texture texture, Vector2 position, Vector2 bodySize, float density) {
        super(mode, texture, position, bodySize, BodyDef.BodyType.DynamicBody, density, "BAT ENEMY", true);

        animation = new AnimationActor(mode, position, new Vector2(.5f, 0.5f), bodySize.x * PPM * 1.5f, "img/enemy/normal/bat/batenemy.atlas", "front", 10f, 0f, Animation.PlayMode.LOOP_PINGPONG, "BAT ENEMY");

        createCircleFixture(this.body, boxSize.x * SENSOR_RADIUS_MULTIPLE, density, "ENEMY SENSOR", true, CATEGORY_ENEMY, MASK_ENEMY, GROUP_NEVER_COLLIDE, true);
        LightBuilder.createPointLight(mode.rayHandler, body, new Color(.5f, .5f, .5f, .4f), 120, 15f, 0f).setXray(true);

        speed = MIN_SPEED;
        dir = Direction.LEFT;
        lives = 3;
        sensorRadius = boxSize.x * SENSOR_RADIUS_MULTIPLE;

        this.posBackToMap = new Vector2(0, 0);
        this.body.setLinearVelocity(new Vector2(0, 0));
        this.isActive = false;

        animation.setFlipX(true);

        aiSteeringBehaviour = new AISteeringBehaviour(body,
                true,
                boxSize.x * PPM,
                70,
                1500,
                90,
                10);
    }

    public boolean isDetectedPlayer() {
        return detectedPlayer;
    }

    @Override
    protected void updateWhenExist(float delta) {
        if (!detectedPlayer) {
            if (posBackToMap.isZero()) {
                if (isActive) {
                    posBackToMap.x = MathUtils.random(mode.mapLevel.currentLevel.lvlWidth);
                    posBackToMap.y = MathUtils.random(mode.mapLevel.currentLevel.lvlHeight);
                }
            } else {
                Vector2 dir = new Vector2(posBackToMap.x, posBackToMap.y).sub(body.getPosition()).nor();
                this.body.setLinearVelocity(MathUtils.lerp(this.body.getLinearVelocity().x, dir.x * MIN_SPEED, 0.02f), MathUtils.lerp(body.getLinearVelocity().y, dir.y * MIN_SPEED, 0.02f));
            }
        } else {
            if (!GameUtils.isCirclerOverlapCircle(this.body.getPosition(), sensorRadius, target.getBody().getPosition(), target.getBoxSize().x / 2)) {
                setDetectedPlayer(false, null);
            } else {
                posBackToMap = new Vector2(0, 0);
                if (target != null) {
                    aiSteeringBehaviour.setSteeringBehavior(
                            new Arrive<>(aiSteeringBehaviour, new AISteeringBehaviour(target.getBody(), true, target.getBoxSize().x * PPM))
                                    .setTimeToTarget(0.001f) //
                                    .setArrivalTolerance(0.0001f) //
                                    .setDecelerationRadius(3)
                    );

                    isActive = true;
                } else
                    this.body.setLinearVelocity(MathUtils.lerp(this.body.getLinearVelocity().x, 0, 0.5f), MathUtils.lerp(body.getLinearVelocity().y, 0, 0.5f));

                aiSteeringBehaviour.getSteeringBehavior().setEnabled(true);
                aiSteeringBehaviour.update(delta);
            }
        }
    }

    @Override
    protected void updateWhenNotExist(float delta) {
    }
}
