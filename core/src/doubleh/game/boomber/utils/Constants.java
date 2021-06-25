package doubleh.game.boomber.utils;

import com.badlogic.gdx.math.Vector2;

/**
 * This is place for storing all constants of game
 */
public final class Constants {
    public static int WIDTH = 1024;                             // window width
    public static int HEIGHT = 576;                             // window height
    public static String TITLE = "BOMBERCREW by @DoubleH";      // title of window
    public static final boolean IS_FULL_SCREEN = false;         // is full screen mode

    public static boolean DEBUG = false;                        // is turn on debug mode

    public static final float MAX_SPEED = 10f;              // Max speed entity can reach
    public static final int MAX_EXPLOSION_LENGTH = 7;       // Max explosion length entity can reach
    public static final int MAX_BOOM_CAPACITY = 8;          // Max boom capacity entity can reach
    public static final int MAX_LIVES = 15;

    public static final float MIN_SPEED = 5f;              // Min speed entity can reach
    public static final int MIN_EXPLOSION_LENGTH = 1;       // Min explosion length entity can reach
    public static final int MIN_BOOM_CAPACITY = 1;          // Min boom capacity entity can reach

    public static final int MAX_LEVEL = 8; // Max level of the game

    public static boolean USE_LEARP = true; // is use lerp calculation for smoother camera and movement
    public static boolean USE_BOUND = true;
    public static boolean USE_CHEAT = false;

    public static String CHARACTOR_NAME = "ninja";

    public static final float UNIT_HEIGHT = HEIGHT / 36f;   // Unit of box2d world compare to height of window
    public static final float PPM = HEIGHT / UNIT_HEIGHT;   // Pixel per meter - conversion factor between Scene2D and Box2D --> 16 units Box2D height: PPM = HEIGHT / 16 = 36

    public static final float LEARP_FACTOR = .1f; // learp factor for use learp calculation

    public static boolean FINISHED_LOADING = false;

    public static final Vector2 DEFAULT_BUTTON_SIZE = new Vector2(Constants.WIDTH / 6f, Constants.HEIGHT / 16f);

    public static boolean SOUND_ON = true;
    public static boolean MUSIC_ON = true;
    public static float MUSIC_VOLUME_RATIO = 1f;
    public static float SOUND_VOLUME_RATIO = 1f;

    public static final int Z_ON_GROUND = 0;
    public static final int Z_SUPER_LOW = 50;
    public static final int Z_VERY_LOW = 100;
    public static final int Z_LOW = 200;
    public static final int Z_NORMAL = 300;
    public static final int Z_HIGH = 500;
    public static final int Z_VERY_HIGH = 700;
    public static final int Z_SUPER_HIGH = 900;
    public static final int Z_UNIVERSE = 1000;

    public static short CATEGORY_PLAYER = 0x0001;
    public static short CATEGORY_BOOM = 0x0002;
    public static short CATEGORY_ENEMY = 0x004;
    public static short CATEGORY_EXPLOSION = 0x0008;
    public static short CATEGORY_BRICK = 0x0016;
    public static short CATEGORY_WALL = 0x0032;
    public static short CATEGORY_WOOD = 0x0064;
    public static short CATEGORY_PORTAL = 0x0128;
    public static short CATEGORY_ITEM = 0x0256;
    public static short CATEGORY_LIGHT = 0x0512;

    public static short MASK_PLAYER = (short) ~CATEGORY_PLAYER;
    public static short MASK_BOOM = (short) 0x0000;     // 0x0000: Disable collide with everything
    public static short MASK_ENEMY = (short) 0xFFFF;    // 0xFFFF: Enable collide with everything
    public static short MASK_EXPLOSION = (short) ~CATEGORY_EXPLOSION;
    public static short MASK_BRICK = (short) 0xFFFF;
    public static short MASK_WALL = (short) 0xFFFF;
    public static short MASK_WOOD = (short) 0xFFFF;
    public static short MASK_PORTAL = (short) 0xFFFF;
    public static short MASK_ITEM = (short) 0xFFFF;
    public static short MASK_LIGHT = (short) 0xFFFF;

    public static short GROUP_NEVER_COLLIDE = -1;
    public static short GROUP_NOT_ACTIVED = 0;
    public static short GROUP_ALWAYS_COLLIDE = 1;
}
