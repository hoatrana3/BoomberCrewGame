package doubleh.game.boomber.actors.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import doubleh.game.boomber.actors.entities.dynamics.BaseItemEntity;
import doubleh.game.boomber.actors.others.AnimationActor;
import doubleh.game.boomber.modes.BaseMode;
import doubleh.game.boomber.utils.GameFactory;
import doubleh.game.boomber.utils.GameUtils;

/**
 * This class is base class for every entity
 */
public abstract class BaseEntity extends Actor {
    public BaseMode mode;

    protected Body body;
    protected boolean exist;
    protected float density;
    protected Vector2 boxSize;
    protected int realZIndex;

    public Texture texture;
    public AnimationActor animation;
    public float speed;

    public Direction dir;

    public enum Direction {
        UP, DOWN, LEFT, RIGHT, IDLE;

        public static Direction fromInteger(int index) {
            switch (index) {
                case 0:
                    return UP;
                case 1:
                    return DOWN;
                case 2:
                    return LEFT;
                case 3:
                    return RIGHT;
                case 4:
                    return IDLE;
                default:
                    return null;
            }
        }

        public static Direction getOpposite(Direction direction) {
            switch (direction) {
                case DOWN:
                    return UP;
                case UP:
                    return DOWN;
                case RIGHT:
                    return LEFT;
                case LEFT:
                    return RIGHT;
                case IDLE:
                    return IDLE;
                default:
                    return null;
            }
        }

        public static Direction getRandomDirFromOtherAxis(Direction direction) {
            switch (direction) {
                case DOWN:
                case UP:
                    return fromInteger(MathUtils.random(2, 3));
                case RIGHT:
                case LEFT:
                    return fromInteger(MathUtils.random(0, 1));
                case IDLE:
                    return fromInteger(MathUtils.random(0, 3));
                default:
                    return null;
            }
        }
    }

    public enum Action {
        ONLY_MOVE, ONLY_ATTACK, HURT, DIE, MOVE_AND_ATTACK;

        public static Action fromInteger(int index) {
            switch (index) {
                case 0:
                    return ONLY_MOVE;
                case 1:
                    return ONLY_ATTACK;
                case 2:
                    return MOVE_AND_ATTACK;
                case 3:
                    return HURT;
                case 4:
                    return DIE;
                default:
                    return null;
            }
        }

        public static Action randomAction() {
            return Action.fromInteger(MathUtils.random(2));
        }
    }

    public BaseEntity(BaseMode mode, float density, Vector2 boxSize) {
        this.mode = mode;
        this.density = density;
        this.boxSize = boxSize;

        exist = true;
    }

    public Body getBody() {
        return body;
    }

    public void setBody(Body body) {
        this.body = body;
    }

    public boolean isExist() {
        return exist;
    }

    public void setExist(boolean exist) {
        this.exist = exist;
    }

    public float getDensity() {
        return density;
    }

    public void setDensity(float density) {
        this.density = density;
    }

    public Vector2 getBoxSize() {
        return boxSize;
    }

    public void setBoxSize(Vector2 boxSize) {
        this.boxSize = boxSize;
    }

    public void update(float delta) {
    }

    @Override
    public boolean equals(Object o) {
        return (
                (o.getClass() == this.getClass()) && ((BaseEntity) o).getName().equals(this.getName())
                        && ((BaseEntity) o).exist == this.exist && ((BaseEntity) o).body == this.body && ((BaseEntity) o).density == this.density
        );
    }

    public void detach() {
        if (body != null) {
            if (GameUtils.isStaticBlock(getName())) {
                if ((int) body.getPosition().x == (int) mode.portalPos.x && (int) body.getPosition().y == (int) mode.portalPos.y) {
                    mode.portal.setVisible(true);
                } else {
                    BaseItemEntity item = GameFactory.generateRandomItem(mode, new Vector2(body.getPosition().x - 0.5f, body.getPosition().y - 0.5f));
                    if (item != null) mode.addNewEntity(item);
                }

                mode.getCurrentGraph().reformatNodeAtCoordToFloor((int) body.getPosition().x, (int) body.getPosition().y);
            }

            for (int i = 0; i < body.getFixtureList().size; ++i) {
                Fixture fixture = body.getFixtureList().get(i);

                if (fixture != null) {
                    body.destroyFixture(fixture);
                    fixture = null;
                }
            }

            World world = body.getWorld();
            world.destroyBody(body);

            body = null;
        }
    }

    public int getRealZIndex() {
        return realZIndex;
    }

    public void setRealZIndex(int realZIndex) {
        this.realZIndex = realZIndex;
    }

    public void beAttacked(boolean force, BaseEntity attacker) {

    }

    public void doBeforeDie() {

    }
}
