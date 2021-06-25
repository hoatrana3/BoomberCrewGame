package doubleh.game.boomber.actors.entities.dynamics.items;

import com.badlogic.gdx.math.Vector2;
import doubleh.game.boomber.BoomberGame;
import doubleh.game.boomber.actors.entities.dynamics.BaseItemEntity;
import doubleh.game.boomber.actors.entities.dynamics.PlayerEntity;
import doubleh.game.boomber.modes.BaseMode;

import static doubleh.game.boomber.utils.Constants.MAX_EXPLOSION_LENGTH;

public class ExplosionItem extends BaseItemEntity {

    public ExplosionItem(BaseMode mode, Vector2 position) {
        super(mode, BoomberGame.getInstance().assets.get("img/items/fire.png"), position, "EXPLOSION ITEM");
    }

    @Override
    public void setOnActive(PlayerEntity player) {
        BoomberGame.getInstance().audios.playSound("s_BOOMLENGTH", 1f, 1f, 0f);

        if (player != null && player.getBoomExplosionLength() < MAX_EXPLOSION_LENGTH) {
            player.setBoomExplosionLength((int) (player.getBoomExplosionLength() + 1f));
            player.maxGotLength++;
        }

        super.setOnActive(player);
    }
}
