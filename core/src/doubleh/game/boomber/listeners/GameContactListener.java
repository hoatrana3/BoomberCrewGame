package doubleh.game.boomber.listeners;

import com.badlogic.gdx.physics.box2d.*;
import doubleh.game.boomber.actors.entities.dynamics.*;
import doubleh.game.boomber.actors.entities.statics.BrickEntity;
import doubleh.game.boomber.actors.entities.statics.WoodEntity;
import doubleh.game.boomber.modes.BaseMode;
import doubleh.game.boomber.modes.LevelMode;
import doubleh.game.boomber.utils.GameUtils;

/**
 * This class for the real game
 */
public class GameContactListener implements ContactListener {
    BaseMode mode;

    public GameContactListener(BaseMode mode) {
        this.mode = mode;
    }

    @Override
    public void beginContact(Contact contact) {
        if (areCollided(contact, "EXPLOSION", "PLAYER")) {
            PlayerEntity player = (PlayerEntity) mode.mapEntites.get(getFixtureHaveSameUserData(contact, "PLAYER").getBody());
            ExplosionEntity explosion = (ExplosionEntity) mode.mapEntites.get(getFixtureHaveSameUserData(contact, "EXPLOSION").getBody());

            if (player != null && explosion != null && player.isExist()) player.beAttacked(false, explosion);
        }

        if (areCollided(contact, "ENEMY", "PLAYER")) {
            PlayerEntity player = (PlayerEntity) mode.mapEntites.get(getFixtureHaveSameUserData(contact, "PLAYER").getBody());
            EnemyEntity enemy = (EnemyEntity) mode.mapEntites.get(getFixtureHaveSameUserData(contact, "ENEMY").getBody());

            if (player != null && enemy != null && player.isExist() && enemy.isExist()) player.beAttacked(false, enemy);
        }

        if (areCollided(contact, "EXPLOSION", "BRICK")) {
            BrickEntity brick = (BrickEntity) mode.mapEntites.get(getFixtureHaveSameUserData(contact, "BRICK").getBody());
            ExplosionEntity explosion = (ExplosionEntity) mode.mapEntites.get(getFixtureHaveSameUserData(contact, "EXPLOSION").getBody());

            if (brick != null && explosion != null)
                if (GameUtils.isTwoEntitiesInSameCell(explosion, brick)) {
                    brick.beAttacked(false, explosion);
                }
        }

        if (areCollided(contact, "EXPLOSION", "WOOD")) {
            WoodEntity wood = (WoodEntity) mode.mapEntites.get(getFixtureHaveSameUserData(contact, "WOOD").getBody());
            ExplosionEntity explosion = (ExplosionEntity) mode.mapEntites.get(getFixtureHaveSameUserData(contact, "EXPLOSION").getBody());

            if (wood != null && explosion != null)
                if (GameUtils.isTwoEntitiesInSameCell(explosion, wood)) {
                    wood.beAttacked(false, explosion);
                }
        }

        if (areCollided(contact, "EXPLOSION", "BOOM")) {
            BoomEntity boom = (BoomEntity) mode.mapEntites.get(getFixtureHaveSameUserData(contact, "BOOM").getBody());
            ExplosionEntity explosion = (ExplosionEntity) mode.mapEntites.get(getFixtureHaveSameUserData(contact, "EXPLOSION").getBody());

            if (boom != null && explosion != null) {
                boom.beAttacked(false, explosion);
            }
        }

        if (areCollided(contact, "PLAYER", "PORTAL")) {
            if (mode instanceof LevelMode && mode.won) {
                mode.doWhenWon();
            }
        }

        if (areCollided(contact, "PLAYER", "ITEM")) {
            PlayerEntity player = (PlayerEntity) mode.mapEntites.get(getFixtureHaveSameUserData(contact, "PLAYER").getBody());
            BaseItemEntity item = (BaseItemEntity) mode.mapEntites.get(getFixtureHaveSameUserData(contact, "ITEM").getBody());

            if (item != null && player != null && player.isExist())
                item.setOnActive(player);
        }

        if (areCollided(contact, "EXPLOSION", "ENEMY")) {
            ExplosionEntity explosion = (ExplosionEntity) mode.mapEntites.get(getFixtureHaveSameUserData(contact, "EXPLOSION").getBody());
            EnemyEntity enemy = (EnemyEntity) mode.mapEntites.get(getFixtureHaveSameUserData(contact, "ENEMY").getBody());

            if (enemy != null && explosion != null && enemy.isExist()) enemy.beAttacked(false, explosion);
        }

        if (areCollided(contact, "PLAYER", "ENEMY SENSOR")) {
            EnemyEntity enemy = (EnemyEntity) mode.mapEntites.get(getFixtureHaveSameUserData(contact, "ENEMY SENSOR").getBody());
            PlayerEntity player = (PlayerEntity) mode.mapEntites.get(getFixtureHaveSameUserData(contact, "PLAYER").getBody());

            if (player != null && enemy != null && player.isExist()) enemy.setDetectedPlayer(true, player);
        }
    }

    @Override
    public void endContact(Contact contact) {
        if (areCollided(contact, "PLAYER", "BOOM")) {
            mode.fixtureToTurnOffSensor.add(getFixtureHaveSameUserData(contact, "BOOM"));
        }

        if (areCollided(contact, "ENEMY", "PLAYER")) {
            EnemyEntity enemy = (EnemyEntity) mode.mapEntites.get(getFixtureHaveSameUserData(contact, "ENEMY").getBody());

            if (enemy != null) enemy.setTouchingPlayer(false);
        }
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {
        if (areCollided(contact, "ENEMY", "ENEMY")) {
            EnemyEntity e1 = (EnemyEntity) mode.mapEntites.get(contact.getFixtureA().getBody());
            EnemyEntity e2 = (EnemyEntity) mode.mapEntites.get(contact.getFixtureB().getBody());

            if (!GameUtils.isStaticEnemies(e1.getName()) && !GameUtils.isStaticEnemies(e2.getName()))
                contact.setEnabled(false);
        }

        if (areCollided(contact, "PLAYER", "BOOM") && getFixtureHaveSameUserData(contact, "BOOM").isSensor()) {
            contact.setEnabled(false);
        }

        if (areCollided(contact, "PLAYER", "PORTAL")) {
            if (mode instanceof LevelMode && mode.won) {
                contact.setEnabled(false);
            }
        }

        if (areCollided(contact, "BOOM", "BOOM")) {
            contact.setEnabled(false);
        }

        if (areCollided(contact, "EXPLOSION", "EXPLOSION")) {
            contact.setEnabled(false);
        }

        if (areCollided(contact, "ENEMY", "PLAYER")) {
            EnemyEntity enemy = (EnemyEntity) mode.mapEntites.get(getFixtureHaveSameUserData(contact, "ENEMY").getBody());

            if (enemy != null) {
                enemy.getBody().setLinearVelocity(0, 0);
                if (enemy.getName().contains("BOSS")) contact.setEnabled(false);
            }
        }
    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {
    }

    /**
     * Lazy function to check if two userData are colliding in contact
     *
     * @param contact contact to check
     * @param userA   userData for body A
     * @param userB   userData for body B
     * @return is right in cotact?
     */
    private boolean areCollided(Contact contact, Object userA, Object userB) {
        boolean res = false;

        try {
            res = (contact.getFixtureA().getUserData().equals(userA) && contact.getFixtureB().getUserData().equals(userB))
                    || (contact.getFixtureA().getUserData().equals(userB) && contact.getFixtureB().getUserData().equals(userA));
        } catch (NullPointerException e) {
            // TODO: Null fixture
        }

        return res;
    }

    /**
     * Get fixture having input userData in contact
     *
     * @param contact  contact to check
     * @param userData userData to check
     * @return fixture found
     */
    private Fixture getFixtureHaveSameUserData(Contact contact, Object userData) {
        if (contact.getFixtureA().getUserData().equals(userData)) return contact.getFixtureA();
        else return contact.getFixtureB();
    }
}
