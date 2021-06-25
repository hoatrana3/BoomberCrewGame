package doubleh.game.boomber.actors.entities.dynamics.enemies.normal;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.utils.Array;
import doubleh.game.boomber.actors.entities.dynamics.EnemyEntity;
import doubleh.game.boomber.actors.others.AnimationActor;
import doubleh.game.boomber.ai.AIAStarPathFinding;
import doubleh.game.boomber.ai.AICombination;
import doubleh.game.boomber.ai.AIFollowPath;
import doubleh.game.boomber.ai.AIRandomMoving;
import doubleh.game.boomber.modes.BaseMode;
import doubleh.game.boomber.utils.Constants;
import doubleh.game.boomber.utils.GameFactory;
import doubleh.game.boomber.utils.GameUtils;

import static doubleh.game.boomber.utils.B2dBuilder.createCircleFixture;
import static doubleh.game.boomber.utils.Constants.*;

public class BoomEnemy extends EnemyEntity {
    private static final float TIME_TO_BOOM = 3.5f;

    private float timeToBoom;

    private AICombination aiCombination;

    public BoomEnemy(BaseMode mode, Texture texture, Vector2 position, Vector2 bodySize, float density) {
        super(mode, texture, position, bodySize, BodyDef.BodyType.DynamicBody, density, "BOOM ENEMY", false);

        this.animation = new AnimationActor(mode, position, new Vector2(.5f, 0.6f), bodySize.x * PPM * 1.25f, "img/enemy/normal/boom/boomenemy.atlas", "left", 8f, 0f, Animation.PlayMode.LOOP_PINGPONG, "BOOM ENEMY");

        createCircleFixture(this.body, boxSize.x * 3f, density, "ENEMY SENSOR", true, CATEGORY_ENEMY, MASK_ENEMY, GROUP_NEVER_COLLIDE, true);

        speed = Constants.MIN_SPEED - 2f;
        dir = Direction.IDLE;
        lives = 1;

        timeToBoom = TIME_TO_BOOM;

        animation.setFlipX(true);

        aiCombination = new AICombination(this,
                new AIRandomMoving(this),
                new AIAStarPathFinding(this),
                new AIFollowPath(this)
        );
    }

    @Override
    protected void updateWhenExist(float delta) {
        if (!isTouchingPlayer) {
            if (!detectedPlayer) {
                aiCombination.removeFromRun(1);
                aiCombination.removeFromRun(2);
                aiCombination.addToRun(0);
            } else {
                if (((AIAStarPathFinding) aiCombination.get(1)).getTarget() == null)
                    ((AIAStarPathFinding) aiCombination.get(1)).setTarget(target);

                aiCombination.removeFromRun(0);
                aiCombination.addToRun(1);

                if (((AIAStarPathFinding) aiCombination.get(1)).pathFound) {
                    timeToBoom -= delta;

                    aiCombination.removeFromRun(0);

                    if (body.getLinearVelocity().isZero()) {
                        Vector2 wanting = ((AIFollowPath) aiCombination.get(2)).getCurrentWantingPoint();
                        Direction currentDir = dir;

                        switch (currentDir) {
                            case DOWN:
                            case UP:
                                if ((int) wanting.x > (int) body.getPosition().x)
                                    currentDir = Direction.RIGHT;
                                else currentDir = Direction.LEFT;

                                break;
                            case LEFT:
                            case RIGHT:
                                if ((int) wanting.y > (int) body.getPosition().y)
                                    currentDir = Direction.UP;
                                else currentDir = Direction.DOWN;

                                break;
                        }

                        GameUtils.lazyDoWithDirection(animation, currentDir, body, speed + 10f, "left", "left");

                        return;
                    }

                    Array<Vector2> wayPoints = GameUtils.generateWayPointsFromPathFound(((AIAStarPathFinding) aiCombination.get(1)).getPath());

                    if (((AIAStarPathFinding) aiCombination.get(1)).pathChanged) {
                        ((AIFollowPath) aiCombination.get(2)).setPath(wayPoints);
                    }

                    aiCombination.addToRun(2);
                } else {
                    aiCombination.addToRun(0);
                }

                speed = MIN_SPEED + 1f;
            }

            aiCombination.update(delta);

            GameUtils.lazyDoWithDirection(animation, dir, body, speed, "left", "left");
        } else {
            timeToBoom = -100;
        }

        if (timeToBoom <= 0) {
            beAttacked(true, null);
        }
    }

    @Override
    protected void updateWhenNotExist(float delta) {
        if (timeToBoom > -1000) {
            Vector2 boomPos = body.getPosition();
            float radius = 0.9f;
            float timeToEffect = 4f;

            GameFactory.createFourDirectionSingleLineExplosion(mode, boomPos, radius, "BOOM ENEMY EXPLOSION", "img/explosion/explosion_enemy_normal.atlas", timeToEffect, 3f);
            GameFactory.createSquareExpandExplosion(mode, boomPos, radius, 1, "BOOM ENEMY EXPLOSION", "img/explosion/explosion_enemy_normal.atlas", timeToEffect);

            timeToBoom = -1999;
        }
    }
}
