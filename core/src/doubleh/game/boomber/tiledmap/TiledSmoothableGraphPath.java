package doubleh.game.boomber.tiledmap;

import com.badlogic.gdx.ai.pfa.DefaultGraphPath;
import com.badlogic.gdx.ai.pfa.SmoothableGraphPath;
import com.badlogic.gdx.math.Vector2;

public class TiledSmoothableGraphPath<N extends TiledNode<N>>
        extends DefaultGraphPath<N> implements SmoothableGraphPath<N, Vector2> {

    @Override
    public Vector2 getNodePosition(int index) {
        N node = nodes.get(index);
        return new Vector2(node.x, node.y);
    }

    @Override
    public void swapNodes(int index1, int index2) {
        nodes.set(index1, nodes.get(index2));
    }

    @Override
    public void truncatePath(int newLength) {
        nodes.truncate(newLength);
    }
}
