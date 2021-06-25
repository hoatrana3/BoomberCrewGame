package doubleh.game.boomber.utils;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.PolylineMapObject;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import doubleh.game.boomber.actors.entities.BaseEntity;
import doubleh.game.boomber.actors.entities.dynamics.enemies.boss.BossRed;
import doubleh.game.boomber.actors.entities.dynamics.enemies.boss.BossViolet;
import doubleh.game.boomber.actors.entities.dynamics.enemies.normal.BatEnemy;
import doubleh.game.boomber.actors.entities.dynamics.enemies.normal.BoomEnemy;
import doubleh.game.boomber.actors.entities.dynamics.enemies.normal.FirerEnemy;
import doubleh.game.boomber.actors.entities.dynamics.enemies.normal.MovingAroundEnemy;
import doubleh.game.boomber.actors.entities.statics.BrickEntity;
import doubleh.game.boomber.actors.entities.statics.WallEntity;
import doubleh.game.boomber.actors.entities.statics.WoodEntity;
import doubleh.game.boomber.modes.BaseMode;

import static doubleh.game.boomber.utils.Constants.PPM;

public final class TiledUtils {

    /**
     * Get list of entities in layer
     *
     * @param mode  mode contains entites
     * @param layer layer to get entites
     * @return the list of entites created
     */
    public static void createEntitiesFromTileLayer(BaseMode mode, TiledMapTileLayer layer) {
        if (layer == null || mode == null) return;

        for (int x = 0; x < layer.getWidth(); ++x)
            for (int y = 0; y < layer.getHeight(); ++y) {
                BaseEntity entity = createEntityFromCoordinateInLayer(mode, layer, x, y);

                if (entity != null) {
                    mode.addNewEntity(entity);
                }
            }
    }

    /**
     * Get a entities created from cell[x][y] from a layer
     *
     * @param mode  mode contains entity
     * @param layer layer to get cell
     * @param x     x pos in tile map
     * @param y     y pos in tile map
     * @return entity created
     */
    public static BaseEntity createEntityFromCoordinateInLayer(BaseMode mode, TiledMapTileLayer layer, int x, int y) {
        if (layer == null || mode == null) return null;

        BaseEntity entity = null;
        TiledMapTileLayer.Cell cell = layer.getCell(x, y);

        try {
            if (cell != null) {
                if (cell.getTile().getProperties().containsKey("BRICK")) {
                    entity = new BrickEntity(mode, new Vector2(x, y), new Vector2(1f, 1f), BodyDef.BodyType.StaticBody, 10f);
                }

                if (cell.getTile().getProperties().containsKey("WALL")) {
                    entity = new WallEntity(mode, new Vector2(x, y), new Vector2(1f, 1f), BodyDef.BodyType.StaticBody, 10f);
                }

                if (cell.getTile().getProperties().containsKey("WOOD")) {
                    entity = new WoodEntity(mode, new Vector2(x, y), new Vector2(1f, 1f), BodyDef.BodyType.StaticBody, 10f);
                }

                if (cell.getTile().getProperties().containsKey("MVA_ENEMY")) {
                    entity = new MovingAroundEnemy(mode, null, new Vector2(x, y), new Vector2(0.98f, 0.98f), 1f);
                    TiledUtils.removeCellAtCoordInTileMap(mode, (int) entity.getBody().getPosition().x, (int) entity.getBody().getPosition().y);
                }

                if (cell.getTile().getProperties().containsKey("FIRER_RIGHT_ENEMY")) {
                    entity = new FirerEnemy(mode, null, new Vector2(x, y), new Vector2(0.98f, 0.98f), 1f, false);
                    TiledUtils.removeCellAtCoordInTileMap(mode, (int) entity.getBody().getPosition().x, (int) entity.getBody().getPosition().y);
                }

                if (cell.getTile().getProperties().containsKey("FIRER_LEFT_ENEMY")) {
                    entity = new FirerEnemy(mode, null, new Vector2(x, y), new Vector2(0.98f, 0.98f), 1f, true);
                    TiledUtils.removeCellAtCoordInTileMap(mode, (int) entity.getBody().getPosition().x, (int) entity.getBody().getPosition().y);
                }

                if (cell.getTile().getProperties().containsKey("BOOM_ENEMY")) {
                    entity = new BoomEnemy(mode, null, new Vector2(x, y), new Vector2(0.98f, 0.98f), 1f);
                    TiledUtils.removeCellAtCoordInTileMap(mode, (int) entity.getBody().getPosition().x, (int) entity.getBody().getPosition().y);
                }

                if (cell.getTile().getProperties().containsKey("BAT_ENEMY")) {
                    entity = new BatEnemy(mode, null, new Vector2(x, y), new Vector2(0.98f, 0.98f), 1f);
                    TiledUtils.removeCellAtCoordInTileMap(mode, (int) entity.getBody().getPosition().x, (int) entity.getBody().getPosition().y);
                }

                if (cell.getTile().getProperties().containsKey("VIOLET_BOSS")) {
                    entity = new BossViolet(mode, null, new Vector2(x, y));
                    TiledUtils.removeCellAtCoordInTileMap(mode, (int) entity.getBody().getPosition().x, (int) entity.getBody().getPosition().y);
                }

                if (cell.getTile().getProperties().containsKey("RED_BOSS")) {
                    entity = new BossRed(mode, null, new Vector2(x, y));
                    TiledUtils.removeCellAtCoordInTileMap(mode, (int) entity.getBody().getPosition().x, (int) entity.getBody().getPosition().y);
                }
            }
        } catch (NullPointerException npe) {
            // TODO: cell properties null
        }

        return entity;
    }

    /**
     * Parse tiled objects layer into box2d wolrd
     *
     * @param world   box2d wolrd
     * @param objects list objects of tiled object layer
     */
    public static void parseTiledObjectLayer(World world, MapObjects objects) {
        for (MapObject object : objects) {
            if (object instanceof PolylineMapObject) {
                BodyDef def = new BodyDef();
                def.fixedRotation = true;
                def.type = BodyDef.BodyType.StaticBody;

                Body body = world.createBody(def);

                Shape shape = createShapeFromPolyline((PolylineMapObject) object);

                body.createFixture(shape, 1.0f);
                body.setUserData("TiledMapObject");

                shape.dispose();
            }
        }
    }

    /**
     * Create shape from polylines object in tiled map
     *
     * @param object polylinemap object
     * @return chain shape created
     */
    private static ChainShape createShapeFromPolyline(PolylineMapObject object) {
        float[] vertices = object.getPolyline().getTransformedVertices();
        Vector2[] converter = new Vector2[vertices.length / 2];

        for (int i = 0; i < converter.length; ++i)
            converter[i] = new Vector2(vertices[i * 2] / PPM, vertices[i * 2 + 1] / PPM);

        ChainShape rShape = new ChainShape();
        rShape.createChain(converter);

        return rShape;
    }

    /**
     * Remove in tilemap a tile at position
     *
     * @param mode mode to do with
     * @param x    x position
     * @param y    y position
     */
    public static void removeCellAtCoordInTileMap(BaseMode mode, int x, int y) {
        if (mode == null || x < 0 || y < 0 || x > mode.width || y > mode.height)
            return;

        TiledMapTileLayer entityLayer = (TiledMapTileLayer) mode.tiledMap.getLayers().get("tile-entities");

        entityLayer.getCell(x, y).setTile(null);
    }
}
