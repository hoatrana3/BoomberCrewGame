package doubleh.game.boomber.actors.entities.dynamics;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import doubleh.game.boomber.actors.entities.BaseEntity;
import doubleh.game.boomber.modes.BaseMode;
import doubleh.game.boomber.utils.GameUtils;

import static doubleh.game.boomber.utils.Constants.*;

/**
 * This class is enemy entity in the game
 */
public abstract class EnemyEntity extends DynamicEntity {
    protected Action act;
    protected int lives;

    protected boolean isTouchingPlayer;
    protected boolean detectedPlayer;

    protected PlayerEntity target;

    public EnemyEntity(BaseMode mode, Texture texture, Vector2 position, Vector2 bodySize, BodyDef.BodyType type, float density, String actorName, boolean isSensor) {
        super(mode, texture, position, bodySize, type, density, "ENEMY", actorName, false, CATEGORY_ENEMY, MASK_ENEMY, GROUP_NOT_ACTIVED, isSensor, true);

        isTouchingPlayer = false;
        detectedPlayer = false;
    }

    public void effectOnPlayer(PlayerEntity player) {
        if (GameUtils.isCirclerOverlapCircle(body.getPosition(), boxSize.x / 2, player.getBody().getPosition(), player.getBoxSize().x / 2))
            isTouchingPlayer = true;
    }

    @Override
    public void update(float delta) {
        super.update(delta);

        if (animation != null) {
            animation.setPosition((body.getPosition().x + animation.getOffset().x - boxSize.x / 2) * PPM, (body.getPosition().y + animation.getOffset().y - boxSize.y / 2) * PPM);
            animation.act(delta);
        }

        if (exist) {
            updateWhenExist(delta);
        }
        else {
            updateWhenNotExist(delta);
        }
    }

    public int getLives() {
        return lives;
    }

    public void setLives(int lives) {
        this.lives = lives;
    }

    public boolean isTouchingPlayer() {
        return isTouchingPlayer;
    }

    public void setTouchingPlayer(boolean touchingPlayer) {
        isTouchingPlayer = touchingPlayer;
    }

    public boolean isDetectedPlayer() {
        return detectedPlayer;
    }

    /**
     * Change detection for boss
     *
     * @param isDetected whether detected player
     * @param player     player to aim
     */
    public void setDetectedPlayer(boolean isDetected, PlayerEntity player) {
        detectedPlayer = isDetected;
        target = player;
    }

    @Override
    public void beAttacked(boolean force, BaseEntity attacker) {
        super.beAttacked(force, attacker);

        if (force || attacker.getName().equalsIgnoreCase("PLAYER EXPLOSION")) {
            if (lives - 1 > 0) lives--;
            else {
                if (getName().contains("BOSS")) {
                    GameUtils.lazySetRegionsForAnimation(this.animation, "die");
                    this.animation.setPlayMode(Animation.PlayMode.NORMAL);
                    this.animation.setOffset(new Vector2(this.animation.getOffset().x - 0.25f, this.animation.getOffset().y - 0.4f));

                    animationDie(30, 3f);
                } else {
                    this.animation.setKeyToLock(0);

                    animationDie(10, 1f);
                }

                this.body.setLinearVelocity(0, 0);

                for (Fixture fixture : body.getFixtureList()) fixture.setSensor(true);

                exist = false;
            }
        }
    }

    private void animationDie(float offsetY, float duration) {
        this.animation.addAction(Actions.sequence(
                Actions.parallel(
                        Actions.fadeOut(duration, Interpolation.pow2),
                        Actions.moveTo(this.animation.getX(), this.animation.getY() + offsetY, duration, Interpolation.pow2)
                ),
                Actions.run(() -> mode.entityToDelete.add(this))
                )
        );
    }

    protected abstract void updateWhenExist(float delta);
    protected abstract void updateWhenNotExist(float delta);
}
