package doubleh.game.boomber.utils;

import com.badlogic.gdx.math.*;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.Array;
import doubleh.game.boomber.BoomberGame;
import doubleh.game.boomber.actors.entities.BaseEntity;
import doubleh.game.boomber.actors.entities.dynamics.EnemyEntity;
import doubleh.game.boomber.actors.others.AnimationActor;
import doubleh.game.boomber.modes.BaseMode;
import doubleh.game.boomber.tiledmap.TiledSmoothableGraphPath;
import doubleh.game.boomber.tiledmap.flat.FlatTiledNode;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public final class GameUtils {

    public static class ActorComparator implements Comparator<Actor> {
        @Override
        public int compare(Actor e1, Actor e2) {
            if (e1 instanceof BaseEntity && e2 instanceof BaseEntity) {
                if (((BaseEntity) e1).getRealZIndex() == Constants.Z_UNIVERSE) return 1;
                if (((BaseEntity) e2).getRealZIndex() == Constants.Z_UNIVERSE) return -1;

                if (((BaseEntity) e1).getRealZIndex() == Constants.Z_ON_GROUND) return -1;
                if (((BaseEntity) e2).getRealZIndex() == Constants.Z_ON_GROUND) return 1;

                if (e1.getY() > e2.getY()) return -1;

                if (e1.getY() == e2.getY()) {
                    return Integer.compare(((BaseEntity) e1).getRealZIndex(), ((BaseEntity) e2).getRealZIndex());
                }

                if (e1.getY() < e2.getY()) return 1;
            } else {
                if (e1 instanceof BaseEntity) return -1;
                else if (e2 instanceof BaseEntity) return 1;
            }

            return 0;
        }
    }

    /**
     * Checking two entites bounds are overlaping
     *
     * @param e1 entity one
     * @param e2 entity two
     * @return is overlaping?
     */
    public static boolean isTwoEntitesBoundOverlap(BaseEntity e1, boolean isOneCircle, BaseEntity e2, boolean isTwoCircle) {
        if (isOneCircle && isTwoCircle)
            return isCirclerOverlapCircle(new Vector2(e1.getX(), e1.getY()), e1.getWidth(), new Vector2(e2.getX(), e2.getY()), e2.getWidth());
        if (isOneCircle)
            return isCircleOverlapRectangle(new Vector2(e1.getX(), e1.getY()), e1.getWidth(), new Vector2(e2.getX(), e2.getY()), new Vector2(e2.getWidth(), e2.getHeight()));
        if (isTwoCircle)
            return isCircleOverlapRectangle(new Vector2(e2.getX(), e2.getY()), e2.getWidth(), new Vector2(e1.getX(), e1.getY()), new Vector2(e1.getWidth(), e1.getHeight()));
        return isRectangleOverlapRectangle(new Vector2(e1.getX(), e1.getY()), new Vector2(e1.getWidth(), e1.getHeight()), new Vector2(e2.getX(), e2.getY()), new Vector2(e2.getWidth(), e2.getHeight()));
    }

    /**
     * Checking two entites are in same cell
     *
     * @param e1 entity one
     * @param e2 entity two
     * @return is in same cell?
     */
    public static boolean isTwoEntitiesInSameCell(BaseEntity e1, BaseEntity e2) {
        Vector2 pos1 = new Vector2(Math.round(e1.getX()), Math.round(e1.getY()));
        Vector2 pos2 = new Vector2(Math.round(e2.getX()), Math.round(e2.getY()));

        return (pos1.x == pos2.x && pos1.y == pos2.y);
    }


    /**
     * Check whether two circle with given infomation is overlap
     *
     * @return overlap???
     */
    public static boolean isCirclerOverlapCircle(Vector2 pos1, float radius1, Vector2 pos2, float radius2) {
        Circle circle1 = new Circle(pos1.x, pos1.y, radius1);
        Circle circle2 = new Circle(pos2.x, pos2.y, radius2);

        return (circle1.overlaps(circle2));
    }

    /**
     * Check whether two rectangle with given infomation is overlap
     *
     * @return overlap???
     */
    public static boolean isRectangleOverlapRectangle(Vector2 pos1, Vector2 size1, Vector2 pos2, Vector2 size2) {
        Rectangle rectangle1 = new Rectangle(pos1.x, pos1.y, size1.x, size1.y);
        Rectangle rectangle2 = new Rectangle(pos2.x, pos2.y, size2.x, size2.y);

        return (rectangle1.overlaps(rectangle2));
    }

    /**
     * Check whether a circle and a rectangle with given infomation is overlap
     *
     * @return overlap???
     */
    public static boolean isCircleOverlapRectangle(Vector2 cirPos, float cirRadius, Vector2 recPos, Vector2 recSize) {
        Circle circle = new Circle(cirPos.x, cirPos.y, cirRadius);
        Rectangle rectangle = new Rectangle(recPos.x, recPos.y, recSize.x, recSize.y);

        return Intersector.overlaps(circle, rectangle);
    }

    /**
     * Get list of overlaping entites on a entity
     *
     * @param entity entity to check
     * @param actors all actors (entites) in stage
     * @return list of other entites overlaping inout entity
     */
    public static List<BaseEntity> getListEntitesOverlapAnEntity(BaseEntity entity, Array<Actor> actors) {
        List<BaseEntity> list = new ArrayList<BaseEntity>();

        for (Actor actor : actors) {
            if (actor instanceof BaseEntity && !entity.equals(actor) && isTwoEntitesBoundOverlap((BaseEntity) actor, false, entity, false)) {
                list.add((BaseEntity) actor);
            }
        }

        return list;
    }

    /**
     * Check if an entity if overlaped by one entity has specific name
     *
     * @param entity entity to check
     * @param actors all actors (entites) in stage
     * @param name   name want to check
     * @return is overlaped by an entity with name?
     */
    public static boolean isAEntityOverlapingAnyOtherWithName(BaseEntity entity, Array<Actor> actors, String name) {
        List<BaseEntity> list = getListEntitesOverlapAnEntity(entity, actors);

        for (Actor actor : actors)
            if (actor.getName().equals(name)) return true;

        list.clear();

        return false;
    }

    /**
     * Using scene2d to get nearest entity having name with from points
     *
     * @param from   from point
     * @param to     to point
     * @param actors all actors (entities) in stage
     * @param name   name want to find
     * @return the entites found or null
     */
    public static BaseEntity getNearestEntityOnLineScene2d(Vector2 from, Vector2 to, Array<Actor> actors, String name) {
        boolean followVertical = (from.x == to.x);
        BaseEntity result = null;
        float minDistance = Float.MAX_VALUE;

        if (from.x > to.x) {
            float temp = from.x;
            from.x = to.x;
            to.x = temp;
        }

        if (from.y > to.y) {
            float temp = from.y;
            from.y = to.y;
            to.y = temp;
        }

        for (Actor actor : actors) {
            if (actor.getName().equals(name)) {
                if (followVertical) {
                    Vector2 actorPos = ((BaseEntity) actor).getBody().getPosition();

                    if (actorPos.x == from.x && actorPos.y >= from.y && actorPos.y <= to.y && Math.abs(actorPos.y - from.y) < minDistance) {
                        minDistance = Math.abs(actorPos.y - from.y);

                        result = (BaseEntity) actor;
                    }
                } else {
                    Vector2 actorPos = ((BaseEntity) actor).getBody().getPosition();

                    if (actorPos.y == from.y && actorPos.x >= from.x && actorPos.x <= to.x && Math.abs(actorPos.x - from.x) < minDistance) {
                        minDistance = Math.abs(actorPos.x - from.x);

                        result = (BaseEntity) actor;
                    }
                }
            }
        }

        return result;
    }

    /**
     * Get the body is laying at position in box2d world
     *
     * @param world world to check
     * @param x     x position
     * @param y     y position
     * @return the body found or null
     */
    public static Body getBodyAtCoord(World world, int x, int y) {
        Array<Body> bodies = new Array<Body>();
        world.getBodies(bodies);

        for (Body body : bodies) {
            if ((int) body.getPosition().x == x && (int) body.getPosition().y == y) {
                return body;
            }
        }

        return null;
    }

    /**
     * Get the body having a name is laying at position in box2d world
     *
     * @param world    world to check
     * @param x        x position
     * @param y        y position
     * @param userData userData of body want to find
     * @return the body found or null
     */
    public static Body getBodyAtCoord(World world, int x, int y, String userData) {
        Array<Body> bodies = new Array<Body>();
        world.getBodies(bodies);

        for (Body body : bodies) {
            if (body.getUserData().equals(userData) && Math.floor(body.getPosition().x) == x && Math.floor(body.getPosition().y) == y) {
                return body;
            }
        }

        return null;
    }

    /**
     * Using box2d to get nearest entity having name with from points
     *
     * @param mode mode to find
     * @param from from point
     * @param to   to point
     * @param name name want to find
     * @return the entites found or null
     */
    public static BaseEntity getNearestEntityOnLineBox2d(BaseMode mode, Vector2 from, Vector2 to, String name) {
        boolean followVertical = (from.x == to.x);

        Vector2 dir = (to.sub(from)).nor();

        if (followVertical) {
            for (int i = (int) ((dir.y > 0) ? from.y : to.y); i < (int) ((dir.y > 0) ? to.y : from.y); i += dir.y) {
                Body body = getBodyAtCoord(mode.world, (int) from.x, i, name);

                if (body != null) return mode.mapEntites.get(body);
            }
        }

        return null;
    }

    public static void lazySetupFadeImageTransition(Vector2 pos, Vector2 size, float fromAlpha, float toAlpha, float duration, Runnable onCompleted) {
        BoomberGame.imgFadeTransition.setPosition(pos.x, pos.y);
        BoomberGame.imgFadeTransition.setSize(size.x, size.y);
        BoomberGame.imgFadeTransition.addAction(
                Actions.sequence(
                        Actions.alpha(fromAlpha),
                        Actions.alpha(toAlpha, duration, Interpolation.pow2),
                        Actions.run(onCompleted)
                )
        );
    }

    public static void lazySetRegionsForAnimation(AnimationActor animation, String regionName) {
        if (!animation.getCurrentRegionName().equalsIgnoreCase(regionName)) {
            boolean isBeforeFlipX = animation.isFlipX();

            animation.setToRegions(regionName);
            animation.setKeyToLock(-1);

            if (isBeforeFlipX) {
                animation.callFlip(true, false);
                animation.setKeyToLock(-1);
            }
        }
    }

    public static void lazySetDirectionVelocityForBody(AnimationActor animation, BaseEntity.Direction dir, Body body, float speed) {
        if (dir != null) {
            switch (dir) {
                case UP:
                    body.setLinearVelocity(0, speed);
                    break;
                case DOWN:
                    body.setLinearVelocity(0, -speed);
                    break;
                case LEFT:
                    body.setLinearVelocity(-speed, 0);

                    if (!animation.isFlipX()) {
                        animation.callFlip(true, false);
                        animation.setKeyToLock(-1);
                    }

                    break;
                case RIGHT:
                    body.setLinearVelocity(speed, 0);

                    if (animation.isFlipX()) {
                        animation.callFlip(true, false);
                        animation.setKeyToLock(-1);
                    }

                    break;
                default:
                    body.setLinearVelocity(0, 0);
                    break;
            }
        }
    }

    public static void lazyDoWithDirection(AnimationActor animation, BaseEntity.Direction dir, Body body, float speed, String regionsIdleName, String regionsMoveName) {
        if (dir != null) {
            GameUtils.lazySetRegionsForAnimation(animation, (dir == BaseEntity.Direction.IDLE) ? regionsIdleName : regionsMoveName);
            GameUtils.lazySetDirectionVelocityForBody(animation, dir, body, speed);
        }
    }

    public static BaseEntity.Direction findoutDirectionFromVelocity(Vector2 velocity) {
        boolean isXBigger = (velocity.x > velocity.y);

        if (velocity.x == 0 && velocity.y == 0) return BaseEntity.Direction.IDLE;

        if (isXBigger) {
            return (velocity.x < 0) ? BaseEntity.Direction.LEFT : BaseEntity.Direction.RIGHT;
        } else return (velocity.y < 0) ? BaseEntity.Direction.DOWN : BaseEntity.Direction.UP;
    }

    public static void lazyDoWithDirectionForAI(AnimationActor animation, BaseEntity.Direction dir, Body body, String regionsIdleName, String regionsMoveName) {
        dir = findoutDirectionFromVelocity(body.getLinearVelocity());

        if (dir != null) {
            GameUtils.lazySetRegionsForAnimation(animation, (dir == BaseEntity.Direction.IDLE) ? regionsIdleName : regionsMoveName);
        }
    }

    public static boolean isStaticEntities(String name) {
        if (name.contains("ENEMY")) return isStaticEnemies(name);

        return isStaticBlock(name);
    }

    public static boolean isStaticBlock(String name) {
        return (name.contains("WALL") || name.contains("BRICK") || name.contains("WOOD"));
    }

    public static boolean isStaticEnemies(String name) {
        return (name.contains("FIRER"));
    }

    public static boolean isStillExistEnemiesInGame(BaseMode mode) {
        Array<Actor> actors = mode.stage.getActors();

        for (Actor actor : actors) {
            if (actor instanceof EnemyEntity && !isStaticEnemies(actor.getName())) return true;
        }

        return false;
    }

    public static Array<Vector2> generateWayPointsFromPathFound(TiledSmoothableGraphPath<FlatTiledNode> path) {
        Array<Vector2> wayPoints = new Array<>();

        for (int i = 0; i < path.getCount(); ++i) wayPoints.add(path.getNodePosition(i));

        return wayPoints;
    }
}
