package doubleh.game.boomber.ai;

import com.badlogic.gdx.math.Vector2;
import doubleh.game.boomber.actors.entities.BaseEntity;

public class AIAxisMoving extends BaseAI {

    private boolean isXFirst;
    private BaseEntity target;

    public AIAxisMoving(BaseEntity entity, boolean isXFirst) {
        super(entity);
        this.isXFirst = isXFirst;
    }

    @Override
    public void update(float delta) {
        moveFollowPlayer();
    }

    /**
     * Move follow player
     * Check x first, y after
     */
    private void moveFollowPlayer() {
        if (target != null) {
            Vector2 bossPos = entity.getBody().getPosition();
            Vector2 targetPos = target.getBody().getPosition();

            if (isXFirst) {
                if ((int) bossPos.x - (int) targetPos.x > 0) entity.dir = BaseEntity.Direction.LEFT;
                else {
                    if ((int) bossPos.x - (int) targetPos.x < 0)
                        entity.dir = BaseEntity.Direction.RIGHT;
                    else {
                        if ((int) bossPos.y - (int) targetPos.y > 0)
                            entity.dir = BaseEntity.Direction.DOWN;
                        else if ((int) bossPos.y - (int) targetPos.y < 0)
                            entity.dir = BaseEntity.Direction.UP;
                    }
                }
            } else {
                if ((int) bossPos.y - (int) targetPos.y > 0) entity.dir = BaseEntity.Direction.DOWN;
                else {
                    if ((int) bossPos.y - (int) targetPos.y < 0)
                        entity.dir = BaseEntity.Direction.UP;
                    else {
                        if ((int) bossPos.x - (int) targetPos.x > 0)
                            entity.dir = BaseEntity.Direction.LEFT;
                        else if ((int) bossPos.x - (int) targetPos.x < 0)
                            entity.dir = BaseEntity.Direction.RIGHT;
                    }
                }
            }
        }
    }

    public BaseEntity getTarget() {
        return target;
    }

    public void setTarget(BaseEntity target) {
        this.target = target;
    }

    public boolean isXFirst() {
        return isXFirst;
    }

    public void setXFirst(boolean XFirst) {
        isXFirst = XFirst;
    }
}
