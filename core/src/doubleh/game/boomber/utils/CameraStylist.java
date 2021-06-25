package doubleh.game.boomber.utils;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import static doubleh.game.boomber.utils.Constants.LEARP_FACTOR;

public final class CameraStylist {

    /**
     * Look a camera to a target
     *
     * @param camera camera to lock
     * @param target target to lock
     */
    public static void lockOnTarget(Camera camera, Vector2 target) {
        Vector3 camOldPos = camera.position;

        camOldPos.x = target.x;
        camOldPos.y = target.y;

        camera.position.set(camOldPos);
        camera.update();
    }

    /**
     * Learp a camera to a target
     *
     * @param camera camera to learp
     * @param target target to learp
     */
    public static void learpOnTarget(Camera camera, Vector2 target) {
        Vector3 camOldPos = camera.position;

        camOldPos.x = MathUtils.lerp(camOldPos.x, target.x, LEARP_FACTOR);
        camOldPos.y = MathUtils.lerp(camOldPos.y, target.y, LEARP_FACTOR);

        camera.position.set(camOldPos);
        camera.update();
    }

    /**
     * Look a camera to point between two targets
     *
     * @param camera  camera to lock
     * @param targetA targetA to calculate
     * @param targetB targetB to calculate
     */
    public static void lockAverageBetweenTargets(Camera camera, Vector2 targetA, Vector2 targetB) {
        Vector3 camOldPos = camera.position;

        camOldPos.x = (targetA.x + targetB.x) / 2;
        camOldPos.y = (targetA.y + targetB.y) / 2;

        camera.position.set(camOldPos);
        camera.update();
    }

    /**
     * Learp a camera to point between two targets
     *
     * @param camera  camera to lock
     * @param targetA targetA to calculate
     * @param targetB targetB to calculate
     */
    public static void learpAverageBetweenTargets(Camera camera, Vector2 targetA, Vector2 targetB) {
        Vector3 camOldPos = camera.position;

        camOldPos.x = MathUtils.lerp(camOldPos.x, (targetA.x + targetB.x) / 2, Constants.LEARP_FACTOR);
        camOldPos.y = MathUtils.lerp(camOldPos.y, (targetA.y + targetB.y) / 2, Constants.LEARP_FACTOR);

        camera.position.set(camOldPos);
        camera.update();
    }

    /**
     * Boundary camera
     *
     * @param camera camera to bound
     * @param startX startX position of bound
     * @param startY startYposition of bound
     * @param width  width of bound
     * @param height height of bound
     */
    public static void boundary(Camera camera, float startX, float startY, float width, float height) {
        Vector3 pos = camera.position;

        if (pos.x < startX) pos.x = startX;
        if (pos.y < startY) pos.y = startY;
        if (pos.x > startX + width) pos.x = startX + width;
        if (pos.y > startY + height) pos.y = startY + height;

        if (width < 0) pos.x -= width / 2;
        if (height < 0) pos.y -= height / 2;

        CameraStylist.learpOnTarget(camera, new Vector2(pos.x, pos.y));
    }
}
