package doubleh.game.boomber.tiledmap.flat;

import com.badlogic.gdx.utils.Array;
import doubleh.game.boomber.tiledmap.TiledNode;

public class FlatTiledNode extends TiledNode<FlatTiledNode> {

    private FlatTiledGraph graphHolder;

    public FlatTiledNode(int x, int y, int type, int connectionCapacity, FlatTiledGraph graph) {
        super(x, y, type, new Array<>(connectionCapacity));

        this.graphHolder = graph;
    }

    @Override
    public int getIndex() {
        return x * graphHolder.height + y;
    }
}
