package doubleh.game.boomber.ai;

import com.badlogic.gdx.ai.pfa.indexed.IndexedAStarPathFinder;
import com.badlogic.gdx.math.Vector2;
import doubleh.game.boomber.actors.entities.BaseEntity;
import doubleh.game.boomber.tiledmap.TiledManhattanDistance;
import doubleh.game.boomber.tiledmap.TiledSmoothableGraphPath;
import doubleh.game.boomber.tiledmap.flat.FlatTiledGraph;
import doubleh.game.boomber.tiledmap.flat.FlatTiledNode;

public class AIAStarPathFinding extends BaseAI {

    private BaseEntity target;

    private TiledManhattanDistance<FlatTiledNode> heuristic;

    private TiledSmoothableGraphPath<FlatTiledNode> path;
    private IndexedAStarPathFinder<FlatTiledNode> pathFinder;

    private int endTileX;
    private int endTileY;
    private int startTileX;
    private int startTileY;

    public boolean pathFound;
    public boolean pathChanged;

    private FlatTiledGraph graphMap;

    public AIAStarPathFinding(BaseEntity entity) {
        super(entity);

        graphMap = entity.mode.getCurrentGraph();

        path = new TiledSmoothableGraphPath<>();
        pathFinder = new IndexedAStarPathFinder<>(graphMap, true);
        heuristic = new TiledManhattanDistance<>();

        pathFound = false;
        pathChanged = false;
    }

    private boolean updatePointOfPath() {
        boolean isChanged = false;

        Vector2 entityPos = new Vector2((int) entity.getBody().getPosition().x, (int) entity.getBody().getPosition().y);
        Vector2 targetPos = new Vector2((int) target.getBody().getPosition().x, (int) target.getBody().getPosition().y);

        if ((int) entityPos.x != startTileX
                || (int) entityPos.y != startTileY) {
            startTileX = (int) entityPos.x;
            startTileY = (int) entityPos.y;

            isChanged = true;
        }

        if ((int) targetPos.x != endTileX
                || (int) targetPos.y != endTileY) {
            endTileX = (int) targetPos.x;
            endTileY = (int) targetPos.y;

            isChanged = true;
        }

        return isChanged;
    }

    //need avoid bomb
    @Override
    public synchronized void update(float delta) {
        if (entity != null && target != null) {
            pathChanged = updatePointOfPath();

            if (pathChanged) {
                pathFound = false;

                FlatTiledNode startNode = graphMap.getNode(startTileX, startTileY);
                FlatTiledNode endNode = graphMap.getNode(endTileX, endTileY);

                if (endNode.type == FlatTiledNode.TILE_FLOOR) {
                    path.clear();
                    graphMap.startNode = startNode;
                    pathFinder.searchNodePath(startNode, endNode, heuristic, path);
                    if (pathFinder.metrics != null) {
                        int nodeCount = path.getCount();

                        if (nodeCount <= 0) pathFound = false;
                        else {
                            pathFound = true;
                        }
                    } else pathFound = false;
                }
            }
        }
    }

    public BaseEntity getTarget() {
        return target;
    }

    public void setTarget(BaseEntity target) {
        this.target = target;
    }

    public TiledSmoothableGraphPath<FlatTiledNode> getPath() {
        return path;
    }

    public void setPath(TiledSmoothableGraphPath<FlatTiledNode> path) {
        this.path = path;
    }
}
