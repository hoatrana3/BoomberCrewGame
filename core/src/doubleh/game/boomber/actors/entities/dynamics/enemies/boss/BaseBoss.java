package doubleh.game.boomber.actors.entities.dynamics.enemies.boss;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import doubleh.game.boomber.actors.entities.dynamics.EnemyEntity;
import doubleh.game.boomber.actors.others.AnimationActor;
import doubleh.game.boomber.ai.AIAxisMoving;
import doubleh.game.boomber.ai.AICombination;
import doubleh.game.boomber.ai.AIRandomMoving;
import doubleh.game.boomber.modes.BaseMode;
import doubleh.game.boomber.utils.GameUtils;

import static doubleh.game.boomber.utils.B2dBuilder.createCircleFixture;
import static doubleh.game.boomber.utils.Constants.*;

public abstract class BaseBoss extends EnemyEntity {
    private float timeToThinkHowToAct;

    private boolean isFinishUsingSkills;

    private float sensorRadius;

    private AICombination aiCombination;

    public BaseBoss(BaseMode mode, Texture texture, Vector2 position, Vector2 bodySize, Vector2 offsetDraw, float sensorRadius, float speed, float widthTarget, String actorName, String atlasPath, int lives) {
        super(mode, texture, position, bodySize, BodyDef.BodyType.DynamicBody, 100000f, actorName, false);

        createCircleFixture(this.body, sensorRadius, density, "ENEMY SENSOR", true, CATEGORY_ENEMY, MASK_ENEMY, GROUP_NEVER_COLLIDE, true);


        this.animation = new AnimationActor(mode, position, offsetDraw, widthTarget, atlasPath, "walk", 5f, 0f, Animation.PlayMode.LOOP, actorName);

        this.speed = speed;

        this.sensorRadius = sensorRadius;

        dir = Direction.LEFT;
        act = Action.ONLY_MOVE;

        detectedPlayer = false;

        isFinishUsingSkills = true;

        body.setLinearVelocity(speed, 0f);
        this.lives = lives;

        aiCombination = new AICombination(this,
                new AIRandomMoving(this),
                new AIAxisMoving(this, true));
    }

    @Override
    public void update(float delta) {
        super.update(delta);

        switch (act) {
            case ONLY_MOVE:
                doMoving(delta);

                break;
            case ONLY_ATTACK:
                doReadySkill();
                break;

            case DIE:
                doBeforeDie();
                break;

            case HURT:
            case MOVE_AND_ATTACK:
            default:
                act = Action.ONLY_MOVE;
                break;
        }
    }

    @Override
    protected void updateWhenExist(float delta) {
        if (isFinishUsingSkills) {
            timeToThinkHowToAct += delta;

            if (timeToThinkHowToAct > MathUtils.random(4, 10)) {
                timeToThinkHowToAct = 0;

                if (isTouchingPlayer) act = Action.ONLY_ATTACK;
                else if (detectedPlayer && body.getLinearVelocity().isZero())
                    act = Action.ONLY_ATTACK;
                else act = Action.randomAction();
            }
        } else {
            if (animation.isFinished()) {
                if (animation.getCurrentRegionName().equalsIgnoreCase("attack")) {
                    animation.setPlayMode(Animation.PlayMode.LOOP_PINGPONG);

                    actWithSkill();

                    isFinishUsingSkills = true;
                    act = Action.ONLY_MOVE;
                    dir = Direction.IDLE;
                }
            }
        }
    }

    @Override
    public void updateWhenNotExist(float delta) {
        act = Action.DIE;
        if (checkTargetInEffect()) setTargetInEffect(false);
    }

    /**
     * Do some ready for using skill and changed animation
     */
    private void doReadySkill() {
        isFinishUsingSkills = false;
        getBody().setLinearVelocity(0, 0);

        if (!animation.getCurrentRegionName().equalsIgnoreCase("attack")) {
            boolean isBeforeFlipX = animation.isFlipX();

            animation.setToRegions("attack");
            animation.setKeyToLock(-1);
            animation.setPlayMode(Animation.PlayMode.NORMAL);

            if (isBeforeFlipX) {
                animation.callFlip(true, false);
                animation.setKeyToLock(-1);
            }
        }
    }

    private void doMoving(float delta) {
        doRandomWhenNormalMove();

        if (!isTouchingPlayer) {
            if (!detectedPlayer) {
                aiCombination.removeFromRun(1);
                aiCombination.addToRun(0);
            } else {
                if (!GameUtils.isCirclerOverlapCircle(body.getPosition(), sensorRadius, target.getBody().getPosition(), target.getBoxSize().x / 2)) {
                    if (checkTargetInEffect()) setTargetInEffect(false);
                    setDetectedPlayer(false, target);
                } else {
                    if (!checkTargetInEffect()) setTargetInEffect(true);

                    if (((AIAxisMoving) aiCombination.get(1)).getTarget() == null)
                        ((AIAxisMoving) aiCombination.get(1)).setTarget(target);
                    ((AIAxisMoving) aiCombination.get(1)).setXFirst((MathUtils.random(1, 25) == 1) == (!((AIAxisMoving) aiCombination.get(1)).isXFirst()));

                    aiCombination.removeFromRun(0);
                    aiCombination.addToRun(1);
                }
            }

            aiCombination.update(delta);
            GameUtils.lazyDoWithDirection(animation, dir, body, speed, "idle", "walk");
        }
    }

    protected boolean checkTargetInEffect() {
        return false;
    }

    protected abstract void setTargetInEffect(boolean isIn);

    protected abstract void actWithSkill();

    protected abstract void doRandomWhenNormalMove();
}
