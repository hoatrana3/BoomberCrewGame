package doubleh.game.boomber.actors.entities.statics;

import box2dLight.PointLight;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import doubleh.game.boomber.actors.others.AnimationActor;
import doubleh.game.boomber.modes.BaseMode;
import doubleh.game.boomber.utils.LightBuilder;

import static doubleh.game.boomber.utils.Constants.*;

public class PortalEntity extends StaticEntity {

    public PointLight light;

    public PortalEntity(BaseMode mode, Vector2 position, Vector2 bodySize, BodyDef.BodyType type, float density) {
        super(mode, position, bodySize, type, density, "PORTAL", "PORTAL", false, CATEGORY_PORTAL, MASK_PORTAL, GROUP_NOT_ACTIVED, false, true);

        this.animation = new AnimationActor(mode, position, new Vector2(.5f, 0.5f), bodySize.x * PPM, "img/portal/portal.atlas", "", 10f, 0f, Animation.PlayMode.LOOP_PINGPONG, "PORTAL");

        light = LightBuilder.createPointLight(mode.rayHandler, body, new Color(.2f, .6f, .9f, .8f), 100, 0, (mode.mapLevel.currentLevel.inNight) ? 2.5f : 0.5f);
    }

    @Override
    public void update(float delta) {
        super.update(delta);

        animation.act(delta);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);

        animation.draw(batch, parentAlpha);
    }

    @Override
    public void detach() {
        super.detach();

        light.remove();
    }
}
