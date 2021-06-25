package doubleh.game.boomber.actors.entities.dynamics.items;

import com.badlogic.gdx.math.Vector2;
import doubleh.game.boomber.BoomberGame;
import doubleh.game.boomber.actors.entities.dynamics.BaseItemEntity;
import doubleh.game.boomber.actors.entities.dynamics.PlayerEntity;
import doubleh.game.boomber.modes.BaseMode;

import static doubleh.game.boomber.utils.Constants.MAX_SPEED;

public class SpeedItem extends BaseItemEntity {

    public SpeedItem(BaseMode mode, Vector2 position) {
        super(mode, BoomberGame.getInstance().assets.get("img/items/lightning.png"), position, "SPEED ITEM");
    }

    @Override
    public void setOnActive(PlayerEntity player) {
        BoomberGame.getInstance().audios.playSound("s_SPEED", 1f, 1f, 0f);

        if (player != null && player.getSpeed() < MAX_SPEED) {
            player.setSpeed(player.getSpeed() + 0.5f);
            player.maxGotSpeed++;
        }

        super.setOnActive(player);
    }
}
