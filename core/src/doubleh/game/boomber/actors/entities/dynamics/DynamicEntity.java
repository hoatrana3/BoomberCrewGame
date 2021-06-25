package doubleh.game.boomber.actors.entities.dynamics;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import doubleh.game.boomber.actors.entities.BaseEntity;
import doubleh.game.boomber.modes.BaseMode;

import static doubleh.game.boomber.utils.B2dBuilder.*;
import static doubleh.game.boomber.utils.Constants.PPM;

public abstract class DynamicEntity extends BaseEntity {

    public DynamicEntity(BaseMode mode, Texture texture, Vector2 position, Vector2 bodySize, BodyDef.BodyType type, float density, String userData, String actorName, boolean useFilter, short cBits, short mBits, short gIndex, boolean isSensor, boolean isCircle) {
        super(mode, density, bodySize);

        this.texture = texture;

        body = createBody(mode.world, position.x + bodySize.x / 2f, position.y + bodySize.y / 2f, type, false);
        if (isCircle)
            createCircleFixture(body, bodySize.x / 2, density, userData, useFilter, cBits, mBits, gIndex, isSensor);
        else
            createBoxFixture(body, bodySize.x, bodySize.y, density, userData, useFilter, cBits, mBits, gIndex, isSensor);

        setSize(bodySize.x * PPM, bodySize.y * PPM);
        setPosition(position.x * PPM, position.y * PPM);

        setName(actorName);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);

        setPosition((body.getPosition().x - boxSize.x / 2) * PPM, (body.getPosition().y - boxSize.y / 2) * PPM);

        if (texture != null) {
            Color temp = batch.getColor();

            batch.setColor(temp.r, temp.g, temp.b, getColor().a * parentAlpha);
            super.draw(batch, parentAlpha);
            batch.draw(texture, getX(), getY(), getOriginX() + getWidth() / 2, getOriginY() + getHeight() / 2, getWidth(), getHeight(), getScaleX(), getScaleY(), body.getAngle() * MathUtils.radiansToDegrees, 0, 0, texture.getWidth(), texture.getHeight(), false, false);

            batch.setColor(temp);
        } else {
            if (animation != null) {
                animation.draw(batch, parentAlpha);
            }
        }
    }
}
