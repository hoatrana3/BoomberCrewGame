package doubleh.game.boomber.actors.entities.dynamics.enemies.normal;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import doubleh.game.boomber.actors.entities.dynamics.EnemyEntity;
import doubleh.game.boomber.actors.others.AnimationActor;
import doubleh.game.boomber.modes.BaseMode;
import doubleh.game.boomber.utils.GameFactory;

import static doubleh.game.boomber.utils.Constants.PPM;

public class FirerEnemy extends EnemyEntity {
    private static final float COOLDOWN_TIME_TO_FIRE = 4f;

    private float coolDownTime;

    public FirerEnemy(BaseMode mode, Texture texture, Vector2 position, Vector2 bodySize, float density, boolean isLeft) {
        super(mode, texture, position, bodySize, BodyDef.BodyType.StaticBody, density, "FIRER ENEMY", false);

        this.animation = new AnimationActor(mode, position, new Vector2(.5f, 0.5f), bodySize.x * PPM * 1.15f, "img/enemy/normal/firer/fireenemy.atlas", "firer", 10f, 0f, Animation.PlayMode.LOOP, "FIRER ENEMY");

        speed = 0;
        dir = (isLeft) ? Direction.LEFT : Direction.RIGHT;
        lives = 10000;

        coolDownTime = 0;

        if (!isLeft) {
            animation.setFlipX(true);
            animation.callFlip(true, false);
        }

        animation.setKeyToLock(0);
    }

    @Override
    protected void updateWhenExist(float delta) {
        coolDownTime -= delta;

        if (coolDownTime <= 0) {
            if (MathUtils.random(40) == 1) {
                animation.setKeyToLock(-1);
            }

            if (animation.getKeyFrameIndex() == 11) {
                Vector2 enemyPos = new Vector2(body.getPosition().x + ((dir == Direction.LEFT) ? (-1) : 1), body.getPosition().y);
                Vector2 targetPos = new Vector2(enemyPos.x + ((dir == Direction.LEFT) ? (-3) : 4), enemyPos.y);

                GameFactory.createExplosionBetweenTwoPoints(mode, "FIRER EXPLOSION", "img/explosion/explosion_enemy_normal.atlas", boxSize, enemyPos, targetPos, 0.9f, 1.5f);
                animation.setKeyToLock(0);

                coolDownTime = COOLDOWN_TIME_TO_FIRE;
            }
        }
    }

    @Override
    protected void updateWhenNotExist(float delta) {
    }
}
