package doubleh.game.boomber.ai;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import doubleh.game.boomber.actors.entities.BaseEntity;

public class AIFollowPath extends BaseAI {

    private Array<Vector2> path;

    public AIFollowPath(BaseEntity entity) {
        super(entity);
    }

    @Override
    public void update(float delta) {
        Vector2 targetPos = path.get(0);
        Vector2 currentPos = entity.getBody().getPosition();

        Vector2 intPos = new Vector2((int) currentPos.x, (int) currentPos.y);
        Vector2 roundPos = new Vector2(Math.round(currentPos.x), Math.round(currentPos.y));

        if (intPos.x == targetPos.x && roundPos.x == targetPos.x) {
            if (intPos.y > targetPos.y) entity.dir = BaseEntity.Direction.DOWN;
            else if (intPos.y < targetPos.y) entity.dir = BaseEntity.Direction.UP;
            else {
                path.removeIndex(0);
            }
        } else {
            if (intPos.y == targetPos.y && roundPos.y == targetPos.y) {
                if (intPos.x > targetPos.x) entity.dir = BaseEntity.Direction.LEFT;
                else if (intPos.x < targetPos.x) entity.dir = BaseEntity.Direction.RIGHT;
                else {
                    path.removeIndex(0);
                }
            }
        }
    }

    public void setPath(Array<Vector2> path) {
        this.path = path;
    }

    public Vector2 getCurrentWantingPoint() {
        return path.get(0);
    }
}
