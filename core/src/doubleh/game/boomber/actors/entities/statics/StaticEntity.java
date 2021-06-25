package doubleh.game.boomber.actors.entities.statics;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import doubleh.game.boomber.actors.entities.BaseEntity;
import doubleh.game.boomber.modes.BaseMode;

import static doubleh.game.boomber.utils.B2dBuilder.*;
import static doubleh.game.boomber.utils.Constants.PPM;

public abstract class StaticEntity extends BaseEntity {

    public StaticEntity(BaseMode mode, Vector2 position, Vector2 bodySize, BodyDef.BodyType type, float density, String userData, String actorName, boolean useFilter, short cBits, short mBits, short gIndex, boolean isSensor, boolean isCircle) {
        super(mode, density, bodySize);

        body = createBody(mode.world, position.x + bodySize.x / 2f, position.y + bodySize.y / 2f, type, false);
        if (isCircle)
            createCircleFixture(body, bodySize.x / 2, density, userData, useFilter, cBits, mBits, gIndex, isSensor);
        else
            createBoxFixture(body, bodySize.x, bodySize.y, density, userData, useFilter, cBits, mBits, gIndex, isSensor);

        setSize(bodySize.x * PPM, bodySize.y * PPM);
        setPosition(position.x * PPM, position.y * PPM);

        setName(actorName);

        mode.mapEntites.put(this.body, this);
    }
}
