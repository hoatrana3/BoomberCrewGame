package doubleh.game.boomber.tiledmap.flat;

import com.badlogic.gdx.ai.pfa.DefaultConnection;

public class FlatTiledConnection extends DefaultConnection<FlatTiledNode> {

    private static final float NON_DIAGONAL_COST = (float) Math.sqrt(2);

    private FlatTiledGraph worldMap;

    public FlatTiledConnection(FlatTiledGraph worldMap, FlatTiledNode fromNode, FlatTiledNode toNode) {
        super(fromNode, toNode);
        this.worldMap = worldMap;
    }

    @Override
    public float getCost() {
        if (worldMap.diagonal) return 1;
        return getToNode().x != worldMap.startNode.x && getToNode().y != worldMap.startNode.y ? NON_DIAGONAL_COST : 1;
    }
}
