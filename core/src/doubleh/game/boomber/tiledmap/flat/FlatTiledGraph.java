package doubleh.game.boomber.tiledmap.flat;

import com.badlogic.gdx.ai.pfa.Connection;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.utils.Array;
import doubleh.game.boomber.tiledmap.TiledGraph;
import doubleh.game.boomber.tiledmap.TiledNode;

public class FlatTiledGraph implements TiledGraph<FlatTiledNode> {

    private TiledMap map;

    public int width, height;

    protected Array<FlatTiledNode> nodes;

    public FlatTiledNode startNode;
    public boolean diagonal;

    public FlatTiledGraph() {
        super();
    }

    public FlatTiledGraph(TiledMap map) {
        this();

        this.map = map;

        this.width = map.getProperties().get("width", Integer.class);
        this.height = map.getProperties().get("height", Integer.class);

        this.nodes = new Array<>(width * height);
        this.diagonal = false;
        this.startNode = null;
    }

    @Override
    public void init() {

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                nodes.add(new FlatTiledNode(x, y, getTypeOfNode(x, y), 4, this));
            }
        }

        // Each node has up to 4 neighbors, therefore no diagonal movement is possible
        for (int x = 0; x < width; x++) {
            int idx = x * height;
            for (int y = 0; y < height; y++) {
                FlatTiledNode n = nodes.get(idx + y);
                addConnectionAroundNode(n, x, y);
            }
        }
    }

    @Override
    public FlatTiledNode getNode(int x, int y) {
        return nodes.get(x * height + y);
    }

    @Override
    public FlatTiledNode getNode(int index) {
        return nodes.get(index);
    }

    @Override
    public int getIndex(FlatTiledNode node) {
        return node.getIndex();
    }

    @Override
    public int getNodeCount() {
        return nodes.size;
    }

    @Override
    public Array<Connection<FlatTiledNode>> getConnections(FlatTiledNode fromNode) {
        return fromNode.getConnections();
    }

    private void addConnection(FlatTiledNode n, int xOffset, int yOffset) {
        FlatTiledNode target = getNode(n.x + xOffset, n.y + yOffset);
        if (target.type == FlatTiledNode.TILE_FLOOR)
            n.getConnections().add(new FlatTiledConnection(this, n, target));
    }

    public void addConnectionAroundNode(FlatTiledNode n, int x, int y) {
        if (x > 0) addConnection(n, -1, 0);
        if (y > 0) addConnection(n, 0, -1);
        if (x < width - 1) addConnection(n, 1, 0);
        if (y < height - 1) addConnection(n, 0, 1);
    }

    public void reformatNodeAtCoordToFloor(int x, int y) {
        FlatTiledNode n = getNode(x, y);
        FlatTiledNode nleft = getNode(x - 1, y);
        FlatTiledNode nright = getNode(x + 1, y);
        FlatTiledNode ndown = getNode(x, y - 1);
        FlatTiledNode nup = getNode(x, y + 1);

        n.type = TiledNode.TILE_FLOOR;

        addConnectionAroundNode(n, x, y);
        addConnection(nleft, 1, 0);
        addConnection(nright, -1, 0);
        addConnection(ndown, 0, 1);
        addConnection(nup, 0, -1);
    }

    private int getTypeOfNode(int x, int y) {
        TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get("tile-entities");
        TiledMapTileLayer.Cell cell = layer.getCell(x, y);

        if (cell == null) {
            TiledMapTileLayer groundLayer = (TiledMapTileLayer) map.getLayers().get("tile-ground");
            TiledMapTileLayer.Cell groundCell = groundLayer.getCell(x, y);

            if (groundCell == null) return TiledNode.TILE_EMPTY;
            else return TiledNode.TILE_FLOOR;
        }

        MapProperties properties = cell.getTile().getProperties();

        if (
                properties.containsKey("WALL")
                        || properties.containsKey("BRICK")
                        || properties.containsKey("WOOD")
                        || properties.containsKey("FIRER_RIGHT_ENEMY")
                        || properties.containsKey("FIRER_LEFT_ENEMY")
        ) return TiledNode.TILE_BLOCK;


        return TiledNode.TILE_FLOOR;
    }

    public void dispose() {
        nodes.clear();
    }
}
