package doubleh.game.boomber.actors.entities.dynamics;

import box2dLight.PointLight;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import doubleh.game.boomber.BoomberGame;
import doubleh.game.boomber.actors.entities.BaseEntity;
import doubleh.game.boomber.actors.others.AnimationActor;
import doubleh.game.boomber.modes.BaseMode;
import doubleh.game.boomber.utils.Constants;
import doubleh.game.boomber.utils.GameUtils;
import doubleh.game.boomber.utils.LightBuilder;

import static doubleh.game.boomber.utils.Constants.*;

/**
 * This class is player entity in the game
 */
public class PlayerEntity extends DynamicEntity {
    private int boomCapacity;
    private int boomExplosionLength;

    public int maxGotCapacity;
    public int maxGotLength;
    public float maxGotSpeed;

    private PointLight lightAround, lightFar;
    private int lives;

    private boolean lightOn;

    private boolean undying;

    private boolean isVioletEffect;
    private boolean isRedEffect;

    public PlayerEntity(BaseMode mode, Texture texture, Vector2 position, Vector2 bodySize, BodyDef.BodyType type, float density, String actorName, boolean isSensor) {
        super(mode, texture, position, bodySize, type, density, "PLAYER", actorName, false, CATEGORY_PLAYER, MASK_PLAYER, GROUP_NOT_ACTIVED, isSensor, true);

        boomCapacity = maxGotCapacity = ((USE_CHEAT) ? MAX_BOOM_CAPACITY : MIN_BOOM_CAPACITY);
        boomExplosionLength = maxGotLength = ((USE_CHEAT) ? MAX_EXPLOSION_LENGTH : MIN_EXPLOSION_LENGTH);
        speed = maxGotSpeed = ((USE_CHEAT) ? MAX_SPEED : MIN_SPEED);

        undying = true;
        lightOn = false;

        isVioletEffect = false;
        isRedEffect = false;

        this.animation = new AnimationActor(mode, position, getOffsetDrawUpToCharacter(), bodySize.x * PPM * 1.25f, "img/character/" + CHARACTOR_NAME + "/" + CHARACTOR_NAME + ".atlas", "idle", 7f, 0f, Animation.PlayMode.LOOP, actorName);

        lightAround = LightBuilder.createPointLight(mode.rayHandler, body, new Color(.8f, .7f, .5f, .5f), 120, 0f, 2f);
        lightFar = LightBuilder.createPointLight(mode.rayHandler, body, new Color(.8f, .7f, .5f, .5f), 120, 0f, 30);

        lightFar.setXray(true);

        lightAround.setContactFilter(Constants.CATEGORY_LIGHT, MASK_LIGHT, GROUP_NEVER_COLLIDE);

        dir = Direction.IDLE;
        lives = (USE_CHEAT) ? 10000 : 3;

        animation.addAction(
                Actions.sequence(
                        Actions.repeat(2, Actions.sequence(Actions.alpha(0),
                                Actions.alpha(1, 0.5f, Interpolation.pow2),
                                Actions.alpha(0, 0.5f, Interpolation.pow2))),
                        Actions.alpha(1, 0.5f, Interpolation.pow2),
                        Actions.run(new Thread(() -> {
                            undying = false;
                        }))
                )
        );
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        if (!this.animation.getCurrentRegionName().contains("die")) {

            Vector2 oldPos = body.getPosition();

            float newSpeed = speed;

            if (Gdx.input.isKeyPressed(Input.Keys.F)) {
                newSpeed *= 1.5f;
            }

            if (Gdx.input.isKeyPressed(Input.Keys.S)) {
                newSpeed = MIN_SPEED / 1.5f;
            }

            if (isVioletEffect || isRedEffect) newSpeed /= 1.5f;

            if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
                dir = Direction.LEFT;
            } else {
                if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
                    dir = Direction.RIGHT;
                } else {
                    if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
                        dir = Direction.UP;
                    } else {
                        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
                            dir = Direction.DOWN;
                        } else {
                            dir = Direction.IDLE;
                        }
                    }
                }
            }

            GameUtils.lazyDoWithDirection(animation, dir, body, newSpeed, "idle", (newSpeed > MIN_SPEED + 2f) ? "run" : "walk");

            if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
                if (((boomCapacity > 0 && (!isVioletEffect)) || ((boomCapacity > maxGotCapacity / 2) && isVioletEffect))
                        || ((boomCapacity > 0 && (!isRedEffect)) || ((boomCapacity > maxGotCapacity / 2) && isRedEffect))) {
                    Vector2 calculateBoomPos = oldPos;

                    calculateBoomPos = new Vector2((float) Math.floor(calculateBoomPos.x), (float) Math.floor(calculateBoomPos.y));

                    BoomEntity newBoom = new BoomEntity(mode, null, calculateBoomPos, new Vector2(1f, 1f), BodyDef.BodyType.StaticBody, 10f, "BOOM", true);
                    newBoom.setExplosionLength(boomExplosionLength);
                    newBoom.setPlayer(this);

                    mode.addNewEntity(newBoom);
                    BoomberGame.getInstance().audios.playSound("s_PLACE_BOOM", 1f, 1f, 0f);

                    boomCapacity--;
                }
            }

            if (Gdx.input.isKeyJustPressed(Input.Keys.E)) {
                setLightOn(!lightOn);
                BoomberGame.getInstance().audios.playSound("s_LIGHT", 1, 1, 0);
            }
        }
    }

    @Override
    public void update(float delta) {
        super.update(delta);

        if (exist)
            animation.setPosition((body.getPosition().x + animation.getOffset().x - boxSize.x / 2) * PPM, (body.getPosition().y + animation.getOffset().y - boxSize.y / 2) * PPM);
        animation.act(delta);

        if (animation.isFinished()) exist = false;

        updateLight();
    }

    private void updateLight() {
        if (isVioletEffect) {
            lightFar.setSoftnessLength(0);
            lightAround.setSoftnessLength(0);

            if (lightAround.getDistance() > 0f)
                lightAround.setDistance(lightAround.getDistance() - 0.02f);
            if (lightFar.getDistance() > 0f)
                lightFar.setDistance(lightFar.getDistance() - 0.02f);
        } else if (lightOn) {
            lightFar.setSoftnessLength(30);
            lightAround.setSoftnessLength(2);

            if (lightAround.getDistance() < 9f)
                lightAround.setDistance(lightAround.getDistance() + 0.02f);
            if (lightFar.getDistance() < 12f)
                lightFar.setDistance(lightFar.getDistance() + 0.02f);
        }
    }

    @Override
    public void detach() {
        super.detach();

        lightAround.remove();
    }

    public void resetItemsEffect() {
        if (!USE_CHEAT) {
            boomCapacity = maxGotCapacity = MIN_BOOM_CAPACITY;
            boomExplosionLength = maxGotLength = MIN_EXPLOSION_LENGTH;
            speed = maxGotSpeed = MIN_SPEED;
        }
    }

    public int getBoomCapacity() {
        return boomCapacity;
    }

    public void setBoomCapacity(int boomCapacity) {
        this.boomCapacity = boomCapacity;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public int getBoomExplosionLength() {
        return boomExplosionLength;
    }

    public void setBoomExplosionLength(int boomExplosionLength) {
        this.boomExplosionLength = boomExplosionLength;
    }

    public int getLives() {
        return lives;
    }

    public void setLives(int lives) {
        this.lives = lives;
    }

    public void setLightOn(boolean isOn) {
        lightOn = isOn;

        if (lightOn) {
            if (!isVioletEffect) {
                lightAround.setDistance(9f);
                lightFar.setDistance(12f);
            }
        } else {
            lightAround.setDistance(0f);
            lightFar.setDistance(0f);
        }
    }

    public boolean isVioletEffect() {
        return isVioletEffect;
    }

    public void setVioletEffect(boolean silent) {
        isVioletEffect = silent;
    }

    public boolean isRedEffect() {
        return isRedEffect;
    }

    public void setRedEffect(boolean redEffect) {
        isRedEffect = redEffect;
    }

    @Override
    public void beAttacked(boolean force, BaseEntity attacker) {
        if (!undying) {
            if (lives > 1) {
                lives--;
                undying = true;
                resetItemsEffect();

                if (attacker instanceof EnemyEntity) {
                    ((EnemyEntity) attacker).effectOnPlayer(this);
                }

                animation.addAction(
                        Actions.sequence(
                                Actions.repeat(2, Actions.sequence(Actions.alpha(0),
                                        Actions.alpha(1, 0.5f, Interpolation.pow2),
                                        Actions.alpha(0, 0.5f, Interpolation.pow2))),
                                Actions.alpha(1, 0.5f, Interpolation.pow2),
                                Actions.run(new Thread(() -> {
                                    undying = false;
                                }))
                        )
                );
            } else {
                GameUtils.lazySetRegionsForAnimation(this.animation, "die");
                this.animation.setPlayMode(Animation.PlayMode.NORMAL);
                this.animation.setOffset(new Vector2(this.animation.getOffset().x - 0.3f, this.animation.getOffset().y - 0.25f));

                this.animation.addAction(Actions.sequence(
                        Actions.parallel(
                                Actions.fadeOut(3f, Interpolation.pow2),
                                Actions.moveTo(this.animation.getX(), this.animation.getY() + 30, 3f, Interpolation.pow2)
                        ),
                        Actions.run(() -> mode.entityToDelete.add(this))
                        )
                );

                this.body.setLinearVelocity(0, 0);

                for (Fixture fixture : body.getFixtureList()) fixture.setSensor(true);

                mode.lost = true;
                exist = false;
            }
        }
    }

    private Vector2 getOffsetDrawUpToCharacter() {
        switch (CHARACTOR_NAME) {
            case "ninja":
                return new Vector2(.5f, 0.85f);
            case "knight":
                return new Vector2(.5f, 1.05f);
            case "combatant":
                return new Vector2(.5f, 0.85f);
            case "mummydog":
                return new Vector2(.5f, 0.85f);
            default:
                return new Vector2(.5f, 0.5f);
        }
    }
}
