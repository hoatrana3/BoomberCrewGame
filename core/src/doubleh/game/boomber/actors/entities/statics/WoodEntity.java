package doubleh.game.boomber.actors.entities.statics;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import doubleh.game.boomber.actors.entities.BaseEntity;
import doubleh.game.boomber.actors.entities.dynamics.ExplosionEntity;
import doubleh.game.boomber.modes.BaseMode;
import doubleh.game.boomber.utils.TiledUtils;

import static doubleh.game.boomber.utils.Constants.*;

public class WoodEntity extends StaticEntity {

    public WoodEntity(BaseMode mode, Vector2 position, Vector2 bodySize, BodyDef.BodyType type, float density) {
        super(mode, position, bodySize, type, density, "WOOD", "WOOD", false, CATEGORY_WOOD, MASK_WOOD, GROUP_ALWAYS_COLLIDE, false, false);
    }

    @Override
    public void beAttacked(boolean force, BaseEntity attacker) {
        if (attacker.getName().contains("EXPLOSION")) {
            mode.entityToDelete.add(this);
            TiledUtils.removeCellAtCoordInTileMap(mode, (int) body.getPosition().x, (int) body.getPosition().y);

            ((ExplosionEntity) attacker).setTimeExplodeEffect(5f);
            if (mode.mapLevel.currentLevel.inNight)
                ((ExplosionEntity) attacker).light.setDistance(2f);
        }
    }
}
