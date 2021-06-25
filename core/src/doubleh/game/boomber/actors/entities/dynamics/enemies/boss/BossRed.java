package doubleh.game.boomber.actors.entities.dynamics.enemies.boss;

import box2dLight.PointLight;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import doubleh.game.boomber.BoomberGame;
import doubleh.game.boomber.modes.BaseMode;
import doubleh.game.boomber.utils.Constants;
import doubleh.game.boomber.utils.GameFactory;
import doubleh.game.boomber.utils.LightBuilder;

import static doubleh.game.boomber.utils.Constants.PPM;

public class BossRed extends BaseBoss {
    private PointLight lightRadiance;

    private static final float RADIANCE_TIME_TO_RIP = 4f;
    private static final float INVI_TIME = 5f;
    private static final float FIRE_WALKING_TIME = 10f;

    private boolean isRadianceOn;
    private boolean isFireWalking;

    private float timeRadianceToRipPlayer;
    private float timeToFireWarlking;

    private enum Skill {
        RADIANCE_CHANGING, INVISIBLE, FIRE_WALKING;

        public static Skill fromInteger(int index) {
            switch (index) {
                case 0:
                    return RADIANCE_CHANGING;
                case 1:
                    return INVISIBLE;
                case 2:
                    return FIRE_WALKING;
                default:
                    return null;
            }
        }

        public static Skill randomSkill() {
            return Skill.fromInteger(MathUtils.random(2));
        }
    }

    public BossRed(BaseMode mode, Texture texture, Vector2 position) {
        super(mode, texture, position, new Vector2(1.94f, 1.94f),
                new Vector2(1f, 1.4f), 1.94f * 2f,
                Constants.MIN_SPEED - 2f, 1.94f * PPM * 1.65f,
                "BOSS RED", "img/enemy/boss/red/boss-red.atlas", 30);

        lightRadiance = LightBuilder.createPointLight(mode.rayHandler, body, new Color(0.98f, .41568f, .41568f, 1f), 120, 0, 5f);
        lightRadiance.setXray(true);

        isRadianceOn = false;
        timeRadianceToRipPlayer = RADIANCE_TIME_TO_RIP;

        isFireWalking = false;
        timeToFireWarlking = FIRE_WALKING_TIME;
    }

    @Override
    public void update(float delta) {
        super.update(delta);

        fireWalkingUpdate(delta);
        radianceUpdate(delta);
    }

    @Override
    protected boolean checkTargetInEffect() {
        return target.isRedEffect();
    }

    @Override
    protected void setTargetInEffect(boolean isIn) {
        target.setRedEffect(isIn);
    }

    @Override
    protected void actWithSkill() {
        switch (Skill.randomSkill()) {
            case RADIANCE_CHANGING:
                isRadianceOn = !isRadianceOn;
                BoomberGame.getInstance().audios.playSound("s_BRED_RAD", 1, 1, 0);
                break;
            case INVISIBLE:
                goToInvisible();
                BoomberGame.getInstance().audios.playSound("s_BRED_INVI", 1, 1, 0);
                break;
            case FIRE_WALKING:
            default:
                isFireWalking = true;
                BoomberGame.getInstance().audios.playSound("s_BRED_WALK", 1, 1, 0);
                break;
        }
    }

    @Override
    protected void doRandomWhenNormalMove() {
        if (MathUtils.random((isFireWalking) ? 20 : 50) == 1) {
            if (!isFireWalking)
                GameFactory.buildAndAddNewExplosion(mode, new Vector2((int) this.body.getPosition().x, (int) (this.body.getPosition().y - this.boxSize.y / 2)), 0.9f, "BOSS RED EXPLOSION", "img/explosion/explosion_boss_red.atlas", 2f);
            else {
                GameFactory.buildAndAddNewExplosion(mode, new Vector2((int) this.body.getPosition().x, (int) (this.body.getPosition().y - this.boxSize.y / 2)), 0.9f, "BOSS RED EXPLOSION", "img/explosion/explosion_boss_red.atlas", 2f);
                GameFactory.buildAndAddNewExplosion(mode, new Vector2((int) this.body.getPosition().x + 1, (int) (this.body.getPosition().y - this.boxSize.y / 2)), 0.9f, "BOSS RED EXPLOSION", "img/explosion/explosion_boss_red.atlas", 2f);
                GameFactory.buildAndAddNewExplosion(mode, new Vector2((int) this.body.getPosition().x - 1, (int) (this.body.getPosition().y - this.boxSize.y / 2)), 0.9f, "BOSS RED EXPLOSION", "img/explosion/explosion_boss_red.atlas", 2f);
                GameFactory.buildAndAddNewExplosion(mode, new Vector2((int) this.body.getPosition().x, (int) (this.body.getPosition().y + 1 - this.boxSize.y / 2)), 0.9f, "BOSS RED EXPLOSION", "img/explosion/explosion_boss_red.atlas", 2f);
                GameFactory.buildAndAddNewExplosion(mode, new Vector2((int) this.body.getPosition().x, (int) (this.body.getPosition().y - 1 - this.boxSize.y / 2)), 0.9f, "BOSS RED EXPLOSION", "img/explosion/explosion_boss_red.atlas", 2f);
            }
        }
    }

    @Override
    public void detach() {
        super.detach();

        lightRadiance.remove();
    }

    private void radianceUpdate(float delta) {
        lightRadiance.setDistance(MathUtils.lerp(lightRadiance.getDistance(), (isRadianceOn) ? 10f : 0f, 0.025f));

        if (detectedPlayer) {
            if (isRadianceOn) timeRadianceToRipPlayer -= delta;
        } else timeRadianceToRipPlayer = RADIANCE_TIME_TO_RIP;

        if (timeRadianceToRipPlayer <= 0) {
            target.beAttacked(false, this);
            timeRadianceToRipPlayer = RADIANCE_TIME_TO_RIP;
        }
    }

    private void fireWalkingUpdate(float delta) {
        if (isFireWalking) {
            timeToFireWarlking -= delta;

            if (timeToFireWarlking <= 0) {
                isFireWalking = false;
                timeToFireWarlking = FIRE_WALKING_TIME;
            }
        }
    }

    private void goToInvisible() {
        animation.addAction(Actions.sequence(Actions.run(new Thread(() -> speed *= 1.5f)), Actions.alpha(0.015f, 1.5f, Interpolation.pow2), Actions.delay(INVI_TIME), Actions.alpha(1f, 2.5f, Interpolation.pow2), Actions.run(new Thread(() -> speed /= 1.5f))));
    }

    @Override
    public void updateWhenNotExist(float delta) {
        super.updateWhenNotExist(delta);

        isFireWalking = isRadianceOn = false;
    }
}
