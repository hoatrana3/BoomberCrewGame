package doubleh.game.boomber.actors.entities.dynamics;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import doubleh.game.boomber.BoomberGame;
import doubleh.game.boomber.actors.entities.BaseEntity;
import doubleh.game.boomber.actors.others.AnimationActor;
import doubleh.game.boomber.modes.BaseMode;
import doubleh.game.boomber.utils.GameFactory;

import static doubleh.game.boomber.utils.Constants.*;

public class BoomEntity extends DynamicEntity {
    private float timeToExplode;
    private float explosionLength;
    private boolean exploded;
    private PlayerEntity player;

    public BoomEntity(BaseMode mode, Texture texture, Vector2 position, Vector2 bodySize, BodyDef.BodyType type, float density, String actorName, boolean isSensor) {
        super(mode, texture, position, bodySize, type, density, "BOOM", actorName, false, CATEGORY_BOOM, MASK_BOOM, GROUP_NEVER_COLLIDE, isSensor, true);

        exploded = false;

        this.animation = new AnimationActor(mode, position, new Vector2(.5f, .5f), bodySize.x * PPM, "img/boom/boom.atlas", "", 10f, 0f, Animation.PlayMode.NORMAL, actorName);

        timeToExplode = 2;
    }

    public void setExplosionLength(float explosionLength) {
        this.explosionLength = explosionLength;
    }

    @Override
    public void update(float delta) {
        timeToExplode -= delta;

        if (timeToExplode <= 0) {
            animation.act(delta);

            if (!exploded) {
                if (animation.getKeyFrameIndex() == 4) {
                    exploded = true;
                    createExplosion();
                }
            }

            if (animation.isFinished()) {
                if (player.getBoomCapacity() + 1 <= player.maxGotCapacity) {
                    player.setBoomCapacity(player.getBoomCapacity() + 1);
                }

                for (int i = 0; i < body.getFixtureList().size; ++i) {
                    body.getFixtureList().get(i).setSensor(false);
                }

                mode.deleteEntity(this);
            }
        }
    }

    public void setTimeToExplode(float timeToExplode) {
        this.timeToExplode = timeToExplode;
    }

    /**
     * Create explosion 4 directions when boom explode
     */
    public void createExplosion() {
        Vector2 boomPos = body.getPosition();

        BoomberGame.getInstance().audios.playSound("s_BOOM", 1.0f, 1.0f, 0.0f);
        GameFactory.createFourDirectionSingleLineExplosion(mode, boomPos, 0.9f, "PLAYER EXPLOSION", "img/explosion/explosion.atlas", 0.5f, explosionLength);
    }

    public void setPlayer(PlayerEntity player) {
        this.player = player;
    }

    @Override
    public void beAttacked(boolean force, BaseEntity attacker) {
        if (attacker.getName().contains("EXPLOSION")) {
            timeToExplode = -1;
        }
    }
}
