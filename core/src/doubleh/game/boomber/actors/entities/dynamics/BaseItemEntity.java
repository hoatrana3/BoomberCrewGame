package doubleh.game.boomber.actors.entities.dynamics;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.RepeatAction;
import doubleh.game.boomber.modes.BaseMode;

import static doubleh.game.boomber.utils.Constants.*;

public abstract class BaseItemEntity extends DynamicEntity {
    public float existTime;

    public BaseItemEntity(BaseMode mode, Texture texture, Vector2 position, String actorName) {
        super(mode, texture, position, new Vector2(0.98f, 0.98f), BodyDef.BodyType.StaticBody, 1f, "ITEM", actorName, false, CATEGORY_ITEM, MASK_ITEM, GROUP_NOT_ACTIVED, true, true);

        existTime = 14f;

        addAction(Actions.repeat(RepeatAction.FOREVER, Actions.sequence(Actions.alpha(0), Actions.alpha(1, 1, Interpolation.pow2), Actions.alpha(0, 1, Interpolation.pow2))));
    }

    @Override
    public void update(float delta) {
        super.update(delta);

        existTime -= delta;

        if (existTime <= 0) {
            mode.entityToDelete.add(this);
        }
    }

    public void setOnActive(PlayerEntity player) {
        mode.entityToDelete.add(this);
    }
}
