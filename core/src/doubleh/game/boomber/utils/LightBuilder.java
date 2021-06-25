package doubleh.game.boomber.utils;

import box2dLight.ConeLight;
import box2dLight.PointLight;
import box2dLight.RayHandler;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.physics.box2d.Body;

import static doubleh.game.boomber.utils.Constants.PPM;

public final class LightBuilder {

    //////////// POINT LIGHT ////////////

    /**
     * Create point light attach to a body in box2d
     *
     * @param rayHandler ray handler the light
     * @param body       body to attach
     * @param c          color of light
     * @param dist       distance spread from point to around
     * @return the point light created
     */
    public static PointLight createPointLight(RayHandler rayHandler, Body body, Color c, int ray, float dist, float softLength) {
        PointLight pl = new PointLight(rayHandler, ray, c, dist, 0, 0);
        pl.setSoftnessLength(softLength);
        pl.attachToBody(body);
        pl.setXray(false);
        return pl;
    }

    /**
     * Create point light attach to a position in box2d
     *
     * @param rayHandler ray handler the light
     * @param x          x position
     * @param y          y position
     * @param c          color of light
     * @param dist       distance spread from point to around
     * @return the point light created
     */
    public static PointLight createPointLight(RayHandler rayHandler, float x, float y, Color c, int ray, float dist, float softLength) {
        PointLight pl = new PointLight(rayHandler, ray, c, dist, x * PPM, y * PPM);
        pl.setSoftnessLength(softLength);
        pl.setXray(false);
        return pl;
    }

    //////////// CONE LIGHT ////////////

    /**
     * Create cone light attach to a body in box2d
     *
     * @param rayHandler ray handler the light
     * @param body       body to attach
     * @param c          color of light
     * @param dist       distance spread from point to around
     * @param dir        direction (angle) of the cone light
     * @param cone       the cone spread
     * @return the point light created
     */
    public static ConeLight createConeLight(RayHandler rayHandler, Body body, Color c, float dist, float dir, float cone, float softLength) {
        ConeLight cl = new ConeLight(rayHandler, 120, c, dist, 0, 0, dir, cone);
        cl.setSoftnessLength(softLength);
        cl.attachToBody(body);
        cl.setXray(false);
        return cl;
    }

    /**
     * Create cone light attach to a body in box2d
     *
     * @param rayHandler ray handler the light
     * @param x          x position
     * @param y          y position
     * @param c          color of light
     * @param dist       distance spread from point to around
     * @param dir        direction (angle) of the cone light
     * @param cone       the cone spread
     * @return the point light created
     */
    public static ConeLight createConeLight(RayHandler rayHandler, float x, float y, Color c, float dist, float dir, float cone, float softLength) {
        ConeLight cl = new ConeLight(rayHandler, 120, c, dist, x * PPM, y * PPM, dir, cone);
        cl.setSoftnessLength(softLength);
        cl.setXray(false);
        return cl;
    }
}
