package doubleh.game.boomber.tiledmap;

import com.badlogic.gdx.ai.pfa.indexed.IndexedGraph;

public interface TiledGraph<N extends TiledNode<N>> extends IndexedGraph<N> {
    void init();

    N getNode(int x, int y);

    N getNode(int index);
}
