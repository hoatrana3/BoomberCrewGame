package doubleh.game.boomber.actors.entities.dynamics;

import box2dLight.PointLight;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.utils.Timer;
import doubleh.game.boomber.BoomberGame;
import doubleh.game.boomber.actors.others.AnimationActor;
import doubleh.game.boomber.modes.BaseMode;
import doubleh.game.boomber.utils.LightBuilder;

import static doubleh.game.boomber.utils.Constants.*;

public class ExplosionEntity extends DynamicEntity {
    private float timeExplodeEffect;
    public PointLight light;

    private long idSoundWhenLongerTimeOut;
    private float currentSoundVolume;

    public ExplosionEntity(BaseMode mode, Texture texture, Vector2 position, Vector2 bodySize, BodyDef.BodyType type, float density, boolean isSensor, String actorName, String atlasPath, float timeToEffect) {
        super(mode, texture, position, bodySize, type, density, "EXPLOSION", actorName, false, CATEGORY_EXPLOSION, MASK_EXPLOSION, GROUP_ALWAYS_COLLIDE, isSensor, true);

        this.timeExplodeEffect = timeToEffect;

        this.animation = new AnimationActor(mode, position, new Vector2(.5f, 0.65f), bodySize.x * PPM / 2, atlasPath, "", 10f, 0f, Animation.PlayMode.LOOP, actorName);

        light = LightBuilder.createPointLight(mode.rayHandler, body, new Color(1f, .5f, .5f, 0.8f), 90, (mode.mapLevel.currentLevel.inNight) ? 1.25f : 0f, (mode.mapLevel.currentLevel.inNight) ? 7f : 0f);
        light.setXray(true);

        idSoundWhenLongerTimeOut = Long.MIN_VALUE;
        currentSoundVolume = 1f;
    }

    @Override
    public void update(float delta) {
        timeExplodeEffect -= delta;

        if (timeExplodeEffect <= 0.5f) light.setDistance(light.getDistance() - 0.02f);
        animation.act(delta);

        if (timeExplodeEffect <= 0) {
            light.remove();
            mode.deleteEntity(this);
        }

        final Sound sound = BoomberGame.getInstance().audios.get("s_FIRE", Sound.class);

        if (timeExplodeEffect > 0.5f && idSoundWhenLongerTimeOut == Long.MIN_VALUE) {
            idSoundWhenLongerTimeOut = sound.play();

            Timer.schedule(new Timer.Task() {
                @Override
                public void run() {
                    sound.stop(idSoundWhenLongerTimeOut);
                }
            }, timeExplodeEffect);
        } else if (idSoundWhenLongerTimeOut != Long.MIN_VALUE) {
            currentSoundVolume = MathUtils.lerp(currentSoundVolume, 0, 0.0025f);
            sound.setVolume(idSoundWhenLongerTimeOut, currentSoundVolume);
        }
    }

    public float getTimeExplodeEffect() {
        return timeExplodeEffect;
    }

    public void setTimeExplodeEffect(float timeExplodeEffect) {
        this.timeExplodeEffect = timeExplodeEffect;
    }
}
