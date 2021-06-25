package doubleh.game.boomber.utils;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import doubleh.game.boomber.BoomberGame;
import doubleh.game.boomber.actors.entities.BaseEntity;
import doubleh.game.boomber.actors.entities.dynamics.BaseItemEntity;
import doubleh.game.boomber.actors.entities.dynamics.ExplosionEntity;
import doubleh.game.boomber.actors.entities.dynamics.items.BoomItem;
import doubleh.game.boomber.actors.entities.dynamics.items.ExplosionItem;
import doubleh.game.boomber.actors.entities.dynamics.items.HealthItem;
import doubleh.game.boomber.actors.entities.dynamics.items.SpeedItem;
import doubleh.game.boomber.modes.BaseMode;

public final class GameFactory {

    /**
     * Generate random position of portal in a layer map
     *
     * @param layer input layer
     * @return position generated
     */
    public static Vector2 generatePortalPosition(TiledMapTileLayer layer) {
        int width = layer.getWidth();
        int height = layer.getHeight();

        int posX;
        int posY;

        Vector2 lastPos = new Vector2(-1, -1);

        while (lastPos.x == -1 || lastPos.y == -1) {
            posX = MathUtils.random(0, width);
            posY = MathUtils.random(0, height);

            for (int i = 0; i < posX; ++i)
                for (int j = 0; j < posY; ++j) {
                    TiledMapTileLayer.Cell cell = layer.getCell(i, j);

                    try {
                        if (cell.getTile().getProperties().containsKey("BRICK") || cell.getTile().getProperties().containsKey("WOOD")) {
                            lastPos.x = i;
                            lastPos.y = j;
                        }
                    } catch (NullPointerException npe) {
                        // TODO: Catch null pointer cell (which was tileset for put enemies)
                    }
                }
        }

        return lastPos;
    }

    /**
     * Generate random item at position
     *
     * @param mode     mode to contain item
     * @param position position of item
     * @return created item
     */
    public static BaseItemEntity generateRandomItem(BaseMode mode, Vector2 position) {
        int type = MathUtils.random(15);

        BaseItemEntity item;

        switch (type) {
            case 0:
                item = new BoomItem(mode, position);
                break;
            case 5:
                item = new ExplosionItem(mode, position);
                break;
            case 10:
                item = new SpeedItem(mode, position);
                break;
            case 15:
                item = new HealthItem(mode, position);
                break;
            default:
                item = null;
        }

        return item;
    }

    /**
     * Fast build and add an explosion into mode
     *
     * @param mode         mode current playing
     * @param pos          position of explosion
     * @param radius       radius of explosion
     * @param actorName    actor name of explosion
     * @param atlasPath    atlas path of explosion
     * @param timeToEffect time for explosion effect
     */
    public static void buildAndAddNewExplosion(BaseMode mode, Vector2 pos, float radius, String actorName, String atlasPath, float timeToEffect) {
        ExplosionEntity newExplosion = new ExplosionEntity(mode, null, pos, new Vector2(radius, radius), BodyDef.BodyType.DynamicBody, 1f, true, actorName, atlasPath, timeToEffect);
        mode.addNewEntity(newExplosion);
    }

    /**
     * Create explosion between two point
     *
     * @param mode            mode current playing
     * @param actorName       actor name for explosion
     * @param atlasPath       atlas path for explosin
     * @param boxSize         box size for explosion
     * @param from            start point
     * @param to              end point
     * @param explosionRadius explosion radius
     * @param timeToEffect    time to give explosion effect
     */
    public static void createExplosionBetweenTwoPoints(BaseMode mode, String actorName, String atlasPath, Vector2 boxSize, Vector2 from, Vector2 to, float explosionRadius, float timeToEffect) {
        boolean followVertical = (from.x == to.x);

        // If follow verical use X to compare and loop, if not use Y
        for (int i = 0; i <= ((followVertical) ? Math.abs(from.y - to.y) : Math.abs(from.x - to.x)); ++i) {
            Vector2 explosionPos = (followVertical) ?
                    new Vector2(from.x, (from.y > to.y) ? (from.y - (boxSize.y * i)) : (from.y + (boxSize.y * i)))
                    : new Vector2((from.x > to.x) ? (from.x - (boxSize.x * i)) : (from.x + (boxSize.x * i)), from.y);

            explosionPos = new Vector2((float) Math.floor(explosionPos.x), (float) Math.floor(explosionPos.y));

            Body bodyInPos = GameUtils.getBodyAtCoord(mode.world, (int) explosionPos.x, (int) explosionPos.y);
            BaseEntity entity = null;

            if (bodyInPos != null) entity = mode.mapEntites.get(bodyInPos);

            if (entity != null) {
                if (entity.getName().equals("WALL")) break;
                else if (entity.getName().equals("BRICK") || entity.getName().equals("WOOD")) {
                    buildAndAddNewExplosion(mode, explosionPos, explosionRadius, actorName, atlasPath, timeToEffect);

                    break;
                }
            }

            buildAndAddNewExplosion(mode, explosionPos, explosionRadius, actorName, atlasPath, timeToEffect);
        }

    }

    /**
     * Create square explosion from a point to far
     *
     * @param mode         mode current playing
     * @param pos          center point
     * @param radius       radius of explosion
     * @param squareMaxFar square far level
     * @param actorName    actor name of explosion
     * @param atlasPath    atlas path of explosion
     * @param timeToEffect explosion time effect
     */
    public static void createSquareExpandExplosion(BaseMode mode, Vector2 pos, float radius, int squareMaxFar, String actorName, String atlasPath, float timeToEffect) {
        for (int i = squareMaxFar; i >= -squareMaxFar; --i) {
            Vector2 from = new Vector2(pos.x + i, pos.y - squareMaxFar);
            Vector2 to = new Vector2(pos.x + i, pos.y + squareMaxFar + 1);

            createExplosionBetweenTwoPoints(mode, actorName, atlasPath, new Vector2(radius, radius), from, to, radius, timeToEffect);
        }
    }

    public static void createFourDirectionSingleLineExplosion(BaseMode mode, Vector2 center, float radius, String actorName, String atlasPath, float timeToEffect, float explosionLength) {
        createExplosionBetweenTwoPoints(mode, actorName, atlasPath, new Vector2(1f, 1f), new Vector2(center.x, center.y), new Vector2(center.x - explosionLength, center.y), radius, timeToEffect); // Left
        createExplosionBetweenTwoPoints(mode, actorName, atlasPath, new Vector2(1f, 1f), new Vector2(center.x + 1, center.y), new Vector2(center.x + explosionLength, center.y), radius, timeToEffect); // Right
        createExplosionBetweenTwoPoints(mode, actorName, atlasPath, new Vector2(1f, 1f), new Vector2(center.x, center.y + 1), new Vector2(center.x, center.y + explosionLength), radius, timeToEffect); // Up
        createExplosionBetweenTwoPoints(mode, actorName, atlasPath, new Vector2(1f, 1f), new Vector2(center.x, center.y - 1), new Vector2(center.x, center.y - explosionLength), radius, timeToEffect); // Down
    }

    public static void createFourDirectionDoubleLineExplosion(BaseMode mode, Vector2 center, float radius, String actorName, String atlasPath, float timeToEffect, float explosionLength) {
        createExplosionBetweenTwoPoints(mode, actorName, atlasPath, new Vector2(1f, 1f), new Vector2(center.x, center.y - 1), new Vector2(center.x - explosionLength, center.y - 1), radius, timeToEffect); // Left
        createExplosionBetweenTwoPoints(mode, actorName, atlasPath, new Vector2(1f, 1f), new Vector2(center.x, center.y - 1), new Vector2(center.x + explosionLength + 1, center.y - 1), radius, timeToEffect); // Right
        createExplosionBetweenTwoPoints(mode, actorName, atlasPath, new Vector2(1f, 1f), new Vector2(center.x - 1, center.y), new Vector2(center.x - 1, center.y + explosionLength + 1), radius, timeToEffect); // Up
        createExplosionBetweenTwoPoints(mode, actorName, atlasPath, new Vector2(1f, 1f), new Vector2(center.x - 1, center.y), new Vector2(center.x - 1, center.y - explosionLength), radius, timeToEffect); // Down

        createExplosionBetweenTwoPoints(mode, actorName, atlasPath, new Vector2(1f, 1f), new Vector2(center.x, center.y + 1), new Vector2(center.x - explosionLength, center.y + 1), radius, timeToEffect); // Left
        createExplosionBetweenTwoPoints(mode, actorName, atlasPath, new Vector2(1f, 1f), new Vector2(center.x, center.y + 1), new Vector2(center.x + explosionLength + 1, center.y + 1), radius, timeToEffect); // Right
        createExplosionBetweenTwoPoints(mode, actorName, atlasPath, new Vector2(1f, 1f), new Vector2(center.x + 1, center.y), new Vector2(center.x + 1, center.y + explosionLength + 1), radius, timeToEffect); // Up
        createExplosionBetweenTwoPoints(mode, actorName, atlasPath, new Vector2(1f, 1f), new Vector2(center.x + 1, center.y), new Vector2(center.x + 1, center.y - explosionLength), radius, timeToEffect); // Down
    }

    public static TextButton createTextButton(String text, String styleName, Color textColor, Vector2 size, Vector2 position, EventListener event) {
        TextButton button = new TextButton(text, BoomberGame.skin, styleName);
        button.getStyle().fontColor.set(textColor);
        button.setSize(size.x, size.y);
        button.setPosition(position.x, position.y);
        button.addListener(event);

        return button;
    }
}
