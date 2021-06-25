package doubleh.game.boomber.ai;

import com.badlogic.gdx.math.MathUtils;
import doubleh.game.boomber.actors.entities.BaseEntity;

public class AIRandomMoving extends BaseAI {

    private float timeToRethink;

    public AIRandomMoving(BaseEntity entity) {
        super(entity);

        timeToRethink = 2;
    }

    public void update(float delta) {
        timeToRethink += delta;

        if (timeToRethink > 2f) {
            timeToRethink = 0;
            moveAround();
        }

        if (entity.getBody().getLinearVelocity().isZero()) {
            int choice = MathUtils.random(1);

            if (choice == 0)
                entity.dir = BaseEntity.Direction.getRandomDirFromOtherAxis(entity.dir);
            else entity.dir = BaseEntity.Direction.getOpposite(entity.dir);
        }
    }

    /**
     * Random moving way for stupid enemy
     */
    private void moveAround() {
        BaseEntity.Direction last = entity.dir;

        new Thread(() -> {
            while (entity.dir == last) {
                entity.dir = BaseEntity.Direction.fromInteger(MathUtils.random(3));
            }
        }).start();
    }
}
