package doubleh.game.boomber.utils;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

/**
 * This class is for building object of box2d
 */
public final class B2dBuilder {

    /**
     * Create box2d body to a world
     *
     * @param world world to create
     * @param x     x position
     * @param y     y position
     * @param type  type of body
     * @return body created
     */
    public static Body createBody(World world, float x, float y, BodyDef.BodyType type, boolean canRotate) {
        Body rBody;

        BodyDef bDef = new BodyDef();
        bDef.type = type;
        bDef.position.set(x, y);
        bDef.fixedRotation = !canRotate;

        rBody = world.createBody(bDef);

        return rBody;
    }

    /**
     * Create box2d box fixture for a body
     *
     * @param body      body to create
     * @param w         wight of fixture
     * @param h         height of fixture
     * @param density   density of fixture
     * @param userData  userData of fixture
     * @param useFilter is use filter collision
     * @param cBits     category bits
     * @param mBits     mask bits
     * @param gIndex    group index
     * @param isSensor  is creating a sensor fixture
     * @return fixture created
     */
    public static Fixture createBoxFixture(Body body, float w, float h, float density, String userData, boolean useFilter, short cBits, short mBits, short gIndex, boolean isSensor) {
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(w / 2, h / 2);

        Fixture rFixture = body.createFixture(shape, density);

        shape.dispose();

        if (useFilter) rFixture.setFilterData(createFilterData(cBits, mBits, gIndex));
        rFixture.setUserData(userData);
        rFixture.setSensor(isSensor);

        return rFixture;
    }

    /**
     * Create box2d triangle fixture for a body
     *
     * @param body      body to create
     * @param w         wight of fixture
     * @param h         height of fixture
     * @param density   density of fixture
     * @param userData  userData of fixture
     * @param useFilter is use filter collision
     * @param cBits     category bits
     * @param mBits     mask bits
     * @param gIndex    group index
     * @param isSensor  is creating a sensor fixture
     * @return fixture created
     */
    public static Fixture createTriangleFixture(Body body, float w, float h, float density, String userData, boolean useFilter, short cBits, short mBits, short gIndex, boolean isSensor) {
        Vector2[] vertices = new Vector2[3];

        vertices[0] = new Vector2(-w / 2, -h / 2);
        vertices[1] = new Vector2(w / 2, -h / 2);
        vertices[2] = new Vector2(0, h / 2);

        PolygonShape shape = new PolygonShape();
        shape.set(vertices);

        Fixture rFixture = body.createFixture(shape, density);
        shape.dispose();

        if (useFilter) rFixture.setFilterData(createFilterData(cBits, mBits, gIndex));
        rFixture.setUserData(userData);
        rFixture.setSensor(isSensor);

        return rFixture;
    }

    /**
     * Create box2d circle fixture for a body
     *
     * @param body      body to create
     * @param radius    radius of fixture
     * @param density   density of fixture
     * @param userData  userData of fixture
     * @param useFilter is use filter collision
     * @param cBits     category bits
     * @param mBits     mask bits
     * @param gIndex    group index
     * @param isSensor  is creating a sensor fixture
     * @return fixture created
     */
    public static Fixture createCircleFixture(Body body, float radius, float density, String userData, boolean useFilter, short cBits, short mBits, short gIndex, boolean isSensor) {
        Shape shape = new CircleShape();
        shape.setRadius(radius);

        Fixture rFixture = body.createFixture(shape, density);
        shape.dispose();

        if (useFilter) rFixture.setFilterData(createFilterData(cBits, mBits, gIndex));
        rFixture.setUserData(userData);
        rFixture.setSensor(isSensor);

        return rFixture;
    }

    /**
     * Create box2d chainshape fixture for a body
     *
     * @param body      body to create
     * @param vertices  vertices of chainshape
     * @param density   density of fixture
     * @param userData  userData of fixture
     * @param useFilter is use filter collision
     * @param cBits     category bits
     * @param mBits     mask bits
     * @param gIndex    group index
     * @param isSensor  is creating a sensor fixture
     * @return fixture created
     */
    public static Fixture createChainShapeFixture(Body body, Vector2[] vertices, float density, String userData, boolean useFilter, short cBits, short mBits, short gIndex, boolean isSensor) {
        ChainShape shape = new ChainShape();

        shape.createChain(vertices);

        Fixture rFixture = body.createFixture(shape, density);
        shape.dispose();

        if (useFilter) rFixture.setFilterData(createFilterData(cBits, mBits, gIndex));
        rFixture.setUserData(userData);
        rFixture.setSensor(isSensor);

        return rFixture;
    }

    /**
     * Create filter collision data
     *
     * @param cBits  category bits - let the defination to fixture which use this filter
     * @param mBits  mask bits - define the collision mask with this filter
     * @param gIndex group index
     *               this < 0  and same group: wont collide no matter what
     *               this > 0  and same group: will collide no matter what
     *               default : collide follow category bits and mask bits
     * @return filter created
     */
    public static Filter createFilterData(short cBits, short mBits, short gIndex) {
        Filter filter = new Filter();

        filter.categoryBits = cBits;
        filter.maskBits = mBits;
        filter.groupIndex = gIndex;

        return filter;
    }
}
