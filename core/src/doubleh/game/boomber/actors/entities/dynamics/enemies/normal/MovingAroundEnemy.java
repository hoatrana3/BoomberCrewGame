package doubleh.game.boomber.actors.entities.dynamics.enemies.normal;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import doubleh.game.boomber.actors.entities.dynamics.EnemyEntity;
import doubleh.game.boomber.actors.others.AnimationActor;
import doubleh.game.boomber.ai.AIRandomMoving;
import doubleh.game.boomber.modes.BaseMode;
import doubleh.game.boomber.utils.Constants;
import doubleh.game.boomber.utils.GameUtils;

import static doubleh.game.boomber.utils.Constants.PPM;

public class MovingAroundEnemy extends EnemyEntity {
    private AIRandomMoving aiRandomMoving;

    public MovingAroundEnemy(BaseMode mode, Texture texture, Vector2 position, Vector2 bodySize, float density) {
        super(mode, texture, position, bodySize, BodyDef.BodyType.DynamicBody, density, "MVA ENEMY", false);

        animation = new AnimationActor(mode, position, new Vector2(.5f, 0.5f), bodySize.x * PPM * 1.15f, "img/enemy/normal/mva/mvaenemy.atlas", "left", 20f, 0f, Animation.PlayMode.LOOP_PINGPONG, "MOVING AROUND ENEMY");

        speed = Constants.MIN_SPEED - 2f;
        dir = Direction.LEFT;
        this.lives = 1;

        animation.setFlipX(true);

        aiRandomMoving = new AIRandomMoving(this);
    }

    @Override
    protected void updateWhenExist(float delta) {
        aiRandomMoving.update(delta);

        GameUtils.lazyDoWithDirection(animation, dir, body, speed, "left", "left");
    }

    @Override
    protected void updateWhenNotExist(float delta) {
    }
}
