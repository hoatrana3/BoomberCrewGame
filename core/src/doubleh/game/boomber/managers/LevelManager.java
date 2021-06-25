package doubleh.game.boomber.managers;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Vector2;
import doubleh.game.boomber.tiledmap.flat.FlatTiledGraph;

public class LevelManager {
    public BaseLevel currentLevel;

    public LevelManager(int initLevel) {
        changeLevel(initLevel);
    }

    public void changeLevel(int index) {
        switch (index) {
            case 1:
                currentLevel = new LevelOne();
                break;
            case 2:
                currentLevel = new LevelTwo();
                break;
            case 3:
                currentLevel = new LevelThree();
                break;
            case 4:
                currentLevel = new LevelFour();
                break;
            case 5:
                currentLevel = new LevelFive();
                break;
            case 6:
                currentLevel = new LevelSix();
                break;
            case 7:
                currentLevel = new LevelSeven();
                break;
            case 8:
                currentLevel = new LevelEight();
                break;
            default:
                break;
        }
    }

    public abstract class BaseLevel {
        public Vector2 playerStartPos;
        public int timeOut;
        public float nightBold;
        public boolean inNight;
        public FlatTiledGraph graphMap;

        public int
                lvlWidth,
                lvlHeight,
                lvlTileWidth,
                lvlTileHeight,
                lvlPixelWidth,
                lvlPixelHeight;

        public TiledMap tiledMap;

        private BaseLevel(String pathToLevelTile, Vector2 playerStartPos, int timeOut, boolean inNight, float nightBold) {
            this.playerStartPos = playerStartPos;
            this.timeOut = timeOut;
            this.nightBold = nightBold;
            this.inNight = inNight;

            load(pathToLevelTile);
        }

        private void load(String pathToLevelTile) {
            TmxMapLoader.Parameters params = new TmxMapLoader.Parameters();
            params.textureMinFilter = Texture.TextureFilter.Nearest;
            params.textureMagFilter = Texture.TextureFilter.Nearest;
            params.generateMipMaps = true;

            tiledMap = new TmxMapLoader().load(pathToLevelTile, params);

            lvlWidth = tiledMap.getProperties().get("width", Integer.class);
            lvlHeight = tiledMap.getProperties().get("height", Integer.class);
            lvlTileWidth = tiledMap.getProperties().get("tilewidth", Integer.class);
            lvlTileHeight = tiledMap.getProperties().get("tileheight", Integer.class);
            lvlPixelWidth = lvlWidth * lvlTileWidth;
            lvlPixelHeight = lvlHeight * lvlTileHeight;

            if (graphMap != null) {
                graphMap.dispose();
                graphMap = null;
            }

            graphMap = new FlatTiledGraph(tiledMap);
            graphMap.init();
        }
    }

    private class LevelOne extends BaseLevel {

        private LevelOne() {
            super("map/level1/Level1.tmx", new Vector2(1, 1), 150, false, 1.0f);
        }
    }

    private class LevelTwo extends BaseLevel {

        private LevelTwo() {
            super("map/level2/Level2.tmx", new Vector2(1, 19), 300, true, 0.2f);
        }
    }

    private class LevelThree extends BaseLevel {

        private LevelThree() {
            super("map/level3/Level3.tmx", new Vector2(1, 7), 500, false, 1.0f);
        }
    }

    private class LevelFour extends BaseLevel {

        private LevelFour() {
            super("map/level4/Level4.tmx", new Vector2(15, 28), 325, false, 1.0f);
        }
    }

    private class LevelFive extends BaseLevel {

        private LevelFive() {
            super("map/level5/Level5.tmx", new Vector2(15, 3), 300, true, 0.15f);
        }
    }

    private class LevelSix extends BaseLevel {

        private LevelSix() {
            super("map/level6/Level6.tmx", new Vector2(1, 19), 250, true, 0.1f);
        }
    }

    private class LevelSeven extends BaseLevel {

        private LevelSeven() {
            super("map/level7/Level7.tmx", new Vector2(1, 29), 200, false, 1.0f);
        }
    }

    private class LevelEight extends BaseLevel {

        private LevelEight() {
            super("map/level8/Level8.tmx", new Vector2(9, 28), 500, true, 0.75f);
        }
    }
}
