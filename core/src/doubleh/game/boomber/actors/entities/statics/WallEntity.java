package doubleh.game.boomber.actors.entities.statics;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import doubleh.game.boomber.modes.BaseMode;

import static doubleh.game.boomber.utils.Constants.*;

/**
 * This class is wall entity in the game
 */
public class WallEntity extends StaticEntity {

    public WallEntity(BaseMode mode, Vector2 position, Vector2 bodySize, BodyDef.BodyType type, float density) {
        super(mode, position, bodySize, type, density, "WALL", "WALL", false, CATEGORY_WALL, MASK_WALL, GROUP_ALWAYS_COLLIDE, false, false);
    }
}
