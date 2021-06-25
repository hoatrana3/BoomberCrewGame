package doubleh.game.boomber.actors.entities.dynamics.items;

import com.badlogic.gdx.math.Vector2;
import doubleh.game.boomber.BoomberGame;
import doubleh.game.boomber.actors.entities.dynamics.BaseItemEntity;
import doubleh.game.boomber.actors.entities.dynamics.PlayerEntity;
import doubleh.game.boomber.modes.BaseMode;

import static doubleh.game.boomber.utils.Constants.MAX_BOOM_CAPACITY;

public class BoomItem extends BaseItemEntity {

    public BoomItem(BaseMode mode, Vector2 position) {
        super(mode, BoomberGame.getInstance().assets.get("img/items/bomb.png"), position, "BOOM ITEM");
    }

    @Override
    public void setOnActive(PlayerEntity player) {
        BoomberGame.getInstance().audios.playSound("s_BOOMCAPACITY", 1f, 1f, 0f);

        if (player != null && player.getBoomCapacity() < MAX_BOOM_CAPACITY) {
            player.setBoomCapacity(player.getBoomCapacity() + 1);
            player.maxGotCapacity++;
        }

        super.setOnActive(player);
    }
}
