package doubleh.game.boomber.actors.entities.dynamics.items;

import com.badlogic.gdx.math.Vector2;
import doubleh.game.boomber.BoomberGame;
import doubleh.game.boomber.actors.entities.dynamics.BaseItemEntity;
import doubleh.game.boomber.actors.entities.dynamics.PlayerEntity;
import doubleh.game.boomber.modes.BaseMode;

import static doubleh.game.boomber.utils.Constants.MAX_LIVES;
import static doubleh.game.boomber.utils.Constants.MAX_SPEED;

public class HealthItem extends BaseItemEntity {

    public HealthItem(BaseMode mode, Vector2 position) {
        super(mode, BoomberGame.getInstance().assets.get("img/items/hospital.png"), position, "HEALTH ITEM");
    }

    @Override
    public void setOnActive(PlayerEntity player) {
        BoomberGame.getInstance().audios.playSound("s_HEALTH", 1f, 1f, 0f);

        if (player != null && player.getLives() < MAX_LIVES) {
            player.setLives(player.getLives() + 1);
        }

        super.setOnActive(player);
    }
}
