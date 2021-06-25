package doubleh.game.boomber.modes;

import box2dLight.RayHandler;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.Sort;
import com.badlogic.gdx.utils.viewport.FitViewport;
import doubleh.game.boomber.BoomberGame;
import doubleh.game.boomber.actors.entities.BaseEntity;
import doubleh.game.boomber.actors.entities.dynamics.PlayerEntity;
import doubleh.game.boomber.actors.entities.statics.PortalEntity;
import doubleh.game.boomber.listeners.GameContactListener;
import doubleh.game.boomber.managers.LevelManager;
import doubleh.game.boomber.tiledmap.flat.FlatTiledGraph;
import doubleh.game.boomber.utils.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static doubleh.game.boomber.utils.Constants.*;

public abstract class BaseMode {

    public boolean isPaused;

    public TiledMap tiledMap;
    private OrthogonalTiledMapRenderer tiledMapRenderer;

    public Stage stage;
    public World world;

    public PlayerEntity player;
    public PortalEntity portal;

    public Vector2 portalPos;
    public int currentLevelIndex;

    public Map<Body, BaseEntity> mapEntites;
    public ArrayList<Fixture> fixtureToTurnOffSensor;
    public ArrayList<BaseEntity> entityToDelete;

    protected Box2DDebugRenderer b2dr;
    protected OrthographicCamera physicCamera;
    protected OrthographicCamera scene2dCamera;

    public RayHandler rayHandler;

    public boolean won, lost;

    public int timeCountdown;
    public float eslapedTime;

    public LevelManager mapLevel;

    public int width, height;

    public BaseMode(int initCurrentLevelIndex) {
        stage = new Stage(new FitViewport(Constants.WIDTH, Constants.HEIGHT));

        currentLevelIndex = initCurrentLevelIndex;
        load();
    }

    public void load() {
        isPaused = true;

        mapLevel = new LevelManager(currentLevelIndex);
        eslapedTime = 0;

        won = lost = false;

        timeCountdown = mapLevel.currentLevel.timeOut;

        fixtureToTurnOffSensor = new ArrayList<>();
        entityToDelete = new ArrayList<>();

        Gdx.input.setInputProcessor(stage);

        scene2dCamera = new OrthographicCamera();
        scene2dCamera.setToOrtho(false, Constants.WIDTH, Constants.HEIGHT);

        physicCamera = new OrthographicCamera();
        physicCamera.setToOrtho(false, stage.getCamera().viewportWidth / PPM, stage.getCamera().viewportHeight / PPM);

        world = new World(new Vector2(0, 0), false);
        world.setContactListener(new GameContactListener(this));

        b2dr = new Box2DDebugRenderer();

        rayHandler = new RayHandler(world);
        rayHandler.setAmbientLight((mapLevel.currentLevel.inNight) ? mapLevel.currentLevel.nightBold : 1.0f);

        this.mapEntites = new HashMap<>();

        this.tiledMap = mapLevel.currentLevel.tiledMap;
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap, PPM / 36f);

        this.width = mapLevel.currentLevel.lvlWidth;
        this.height = mapLevel.currentLevel.lvlHeight;

        TiledUtils.parseTiledObjectLayer(world, tiledMap.getLayers().get("collision-layer").getObjects());

        TiledUtils.createEntitiesFromTileLayer(this, (TiledMapTileLayer) tiledMap.getLayers().get("tile-entities"));

        new Thread(() -> {
            portalPos = GameFactory.generatePortalPosition((TiledMapTileLayer) tiledMap.getLayers().get("tile-entities"));

            portal = new PortalEntity(this, portalPos, new Vector2(1f, 1f), BodyDef.BodyType.StaticBody, 100f);
            addNewEntity(portal);

            portal.setVisible(false);
        }).start();

        if (player == null)
            player = new PlayerEntity(this, null, mapLevel.currentLevel.playerStartPos, new Vector2(0.98f, 0.98f), BodyDef.BodyType.DynamicBody, 10f, "PLAYER", false);
        else {
            player = recollectDataPlayer();
        }

        addNewEntity(player);

        GameUtils.lazySetupFadeImageTransition(new Vector2(-Constants.WIDTH * 100, -Constants.HEIGHT * 100), new Vector2(Constants.WIDTH * 10000, Constants.HEIGHT * 10000),
                1, 0, 1,
                () -> isPaused = false);

        stage.addActor(BoomberGame.imgFadeTransition);
    }

    public void reload() {
        try {
            dispose();
            load();
        } catch (NullPointerException npe) {
            // TODO: Failed to reload
        }
    }

    public void handleInput() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.D)) {
            DEBUG = !DEBUG;
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.Q)) {
            isPaused = true;

            GameUtils.lazySetupFadeImageTransition(new Vector2(-Constants.WIDTH * 100, -Constants.HEIGHT * 100), new Vector2(Constants.WIDTH * 10000, Constants.HEIGHT * 10000),
                    0, 1, 1,
                    () -> {
                        BoomberGame.getInstance().setScreen(BoomberGame.getInstance().mainMenuScreen);
                    });
        }
    }

    private void objectsUpdate() {
        for (Fixture fixture : fixtureToTurnOffSensor) {
            fixture.setSensor(false);
        }

        fixtureToTurnOffSensor.clear();
        fixtureToTurnOffSensor = new ArrayList<>();

        for (BaseEntity entity : entityToDelete) {
            deleteEntity(entity);
        }

        entityToDelete.clear();
        entityToDelete = new ArrayList<>();
    }

    private void cameraUpdate() {
        if (!Constants.USE_LEARP) {
            CameraStylist.lockOnTarget(stage.getCamera(), new Vector2(player.getX(), player.getY()));
            CameraStylist.lockOnTarget(scene2dCamera, new Vector2(player.getX(), player.getY()));
        } else {
            CameraStylist.learpOnTarget(stage.getCamera(), new Vector2(player.getX(), player.getY()));
            CameraStylist.learpOnTarget(scene2dCamera, new Vector2(player.getX(), player.getY()));
        }

        if (USE_BOUND)
            CameraStylist.boundary(scene2dCamera, scene2dCamera.viewportWidth / 2, scene2dCamera.viewportHeight / 2, mapLevel.currentLevel.lvlPixelWidth - scene2dCamera.viewportWidth, mapLevel.currentLevel.lvlPixelHeight - scene2dCamera.viewportHeight);

        stage.getCamera().position.set(scene2dCamera.position);

        CameraStylist.lockOnTarget(physicCamera, new Vector2(stage.getCamera().position.x / PPM, stage.getCamera().position.y / PPM));
    }

    public void update(float delta) {
        if (!isPaused) {
            handleInput();
            world.step(delta, 6, 2);
            objectsUpdate();
        }

        cameraUpdate();
        stage.act(delta);

        rayHandler.setCombinedMatrix(physicCamera);
        rayHandler.update();

        try {
            for (int i = 0; i < stage.getActors().size; ++i) {
                BaseEntity entity = (BaseEntity) stage.getActors().get(i);

                entity.update(delta);
            }
        } catch (Exception e) {
            // TODO: Actors were deleted, null pointer
        }

        eslapedTime += delta;

        if (eslapedTime > 1.0f) {
            eslapedTime = 0;

            timeCountdown -= 1;
        }

        if (lost) doWhenLost();

        if (!isPaused) callSortEntities();
    }

    public void callSortEntities() {
        new Thread(() -> {
            try {
                Sort.instance().sort(stage.getActors(), new GameUtils.ActorComparator());
            } catch (Exception e) {
                // TODO: Sort failed with exception: Comparision fail
            }
        }).start();
    }

    public void renderInformation() {
        BoomberGame.getInstance().font.setColor(Color.WHITE);
    }

    public void setGLClearColor() {
        Gdx.gl.glClearColor(1f, 1f, 1f, 1f);
    }

    public void render(float delta) {
        setGLClearColor();
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        update(delta);

        tiledMapRenderer.setView(scene2dCamera);
        tiledMapRenderer.render();

        stage.draw();

        if (DEBUG) {
            b2dr.render(world, physicCamera.combined);
        }

        rayHandler.render();

        renderInformation();
    }

    /**
     * Add an entity
     *
     * @param entity entity to add
     */
    public void addNewEntity(BaseEntity entity) {
        stage.addActor(entity);
        if (!mapEntites.containsKey(entity.getBody())) mapEntites.put(entity.getBody(), entity);

        int zIndex;

        switch (entity.getName()) {
            case "PLAYER":
                zIndex = Z_VERY_HIGH;
                break;

            case "PLAYER EXPLOSION":
                zIndex = Z_NORMAL;
                break;

            case "BOOM":
                zIndex = Z_LOW;
                break;

            case "SPEED ITEM":
            case "HEALTH ITEM":
            case "BOOM ITEM":
            case "EXPLOSION ITEM":
                zIndex = Z_VERY_LOW;
                break;

            case "WOOD":
            case "BRICK":
            case "WALL":
                zIndex = Z_SUPER_LOW;
                break;

            case "PORTAL":
                zIndex = Z_ON_GROUND;
                break;

            case "MVA ENEMY":
            case "BOOM ENEMY":
            case "FIRER ENEMY":
                zIndex = Z_HIGH;
                break;

            case "BOSS VIOLET":
            case "BOSS RED":
                zIndex = Z_SUPER_HIGH;
                break;

            case "BAT ENEMY":
                zIndex = Z_UNIVERSE;
                break;

            default:
                zIndex = Z_NORMAL;
                break;
        }

        entity.setZIndex(zIndex);
        entity.setRealZIndex(zIndex);
    }

    /**
     * Delete an entity
     *
     * @param entity entity to delete
     */
    public void deleteEntity(BaseEntity entity) {
        try {
            entity.doBeforeDie();

            stage.getActors().removeValue(entity, false);
            mapEntites.remove(entity.getBody());

            entity.detach();
        } catch (NullPointerException e) {
            // TODO: Unexpectation???
        }
    }

    public void dispose() {
        tiledMapRenderer.dispose();
        tiledMap.dispose();

        b2dr.dispose();
        rayHandler.dispose();

        world.dispose();
        stage.clear();
    }

    private PlayerEntity recollectDataPlayer() {
        PlayerEntity newPlayer = new PlayerEntity(this, null, mapLevel.currentLevel.playerStartPos, new Vector2(0.98f, 0.98f), BodyDef.BodyType.DynamicBody, 10f, "PLAYER", false);

        newPlayer.maxGotCapacity = player.maxGotCapacity;
        newPlayer.maxGotLength = player.maxGotLength;
        newPlayer.maxGotSpeed = player.maxGotSpeed;

        newPlayer.setBoomCapacity(player.maxGotCapacity);
        newPlayer.setBoomExplosionLength(player.maxGotLength);
        newPlayer.setSpeed(player.maxGotSpeed);
        newPlayer.setLives(player.getLives());

        return newPlayer;
    }

    public void doWhenWon() {
    }

    public void doWhenLost() {
        if (mapLevel.currentLevel.nightBold > 0.05f) {
            mapLevel.currentLevel.inNight = true;
            mapLevel.currentLevel.nightBold = MathUtils.lerp(mapLevel.currentLevel.nightBold, 0f, 0.05f);
            rayHandler.setAmbientLight(mapLevel.currentLevel.nightBold);
        } else {
            changeToGameOverScreen();
        }
    }

    public FlatTiledGraph getCurrentGraph() {
        return mapLevel.currentLevel.graphMap;
    }

    public void changeToGameOverScreen() {
        isPaused = true;

        GameUtils.lazySetupFadeImageTransition(new Vector2(-Constants.WIDTH * 100, -Constants.HEIGHT * 100), new Vector2(Constants.WIDTH * 10000, Constants.HEIGHT * 10000),
                0, 1, 1,
                () -> {
                    BoomberGame.getInstance().setScreen(BoomberGame.getInstance().gameOverScreen);
                });
    }
}
