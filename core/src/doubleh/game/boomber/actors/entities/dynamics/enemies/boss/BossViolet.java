package doubleh.game.boomber.actors.entities.dynamics.enemies.boss;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import doubleh.game.boomber.BoomberGame;
import doubleh.game.boomber.modes.BaseMode;
import doubleh.game.boomber.utils.Constants;
import doubleh.game.boomber.utils.GameFactory;

import static doubleh.game.boomber.utils.Constants.PPM;

public class BossViolet extends BaseBoss {

    private float darkTargetBold;

    private enum Skill {
        EXPLOSION_AROUND, EXPLOSION_FOURTH, DARK_CHANGING, NONE;

        public static Skill fromInteger(int index) {
            switch (index) {
                case 0:
                    return EXPLOSION_AROUND;
                case 1:
                    return EXPLOSION_FOURTH;
                case 2:
                    return DARK_CHANGING;
                default:
                    return null;
            }
        }

        public static Skill randomSkill() {
            return Skill.fromInteger(MathUtils.random(2));
        }
    }

    public BossViolet(BaseMode mode, Texture texture, Vector2 position) {
        super(mode, texture, position, new Vector2(1.94f, 1.94f),
                new Vector2(1f, 1.4f), 1.94f * 2f,
                Constants.MIN_SPEED - 2.5f, 1.94f * PPM * 1.75f,
                "BOSS VIOLET", "img/enemy/boss/violet/boss-violet.atlas", 40);

        darkTargetBold = mode.mapLevel.currentLevel.nightBold;
    }

    @Override
    public void update(float delta) {
        super.update(delta);

        if (mode.mapLevel.currentLevel.nightBold != darkTargetBold)
            makeSkyDarkToValue(darkTargetBold, (darkTargetBold > mode.mapLevel.currentLevel.nightBold) ? 0.0005f : 0.05f);
    }

    @Override
    protected boolean checkTargetInEffect() {
        return target.isVioletEffect();
    }

    @Override
    protected void setTargetInEffect(boolean isIn) {
        target.setVioletEffect(isIn);
    }

    @Override
    protected void actWithSkill() {
        switch (Skill.randomSkill()) {
            case DARK_CHANGING:
                darkTargetBold = MathUtils.random(0.75f);
                BoomberGame.getInstance().audios.playSound("s_BVIOLET_DARK", 1, 1, 0);
                break;

            case EXPLOSION_AROUND:
                GameFactory.createSquareExpandExplosion(mode, new Vector2((int) this.body.getPosition().x, (int) this.body.getPosition().y), 0.95f, 3, "BOSS VIOLET EXPLOSION", "img/explosion/explosion_boss_violet.atlas", 5f);
                BoomberGame.getInstance().audios.playSound("s_BVIOLET_SQUARE", 1, 1, 0);
                break;

            case EXPLOSION_FOURTH:
            default:
                GameFactory.createFourDirectionDoubleLineExplosion(mode, new Vector2((int) this.body.getPosition().x, (int) this.body.getPosition().y), 0.95f, "BOSS VIOLET EXPLOSION", "img/explosion/explosion_boss_violet.atlas", 5f, 5f);
                BoomberGame.getInstance().audios.playSound("s_BVIOLET_DOUBLE", 1, 1, 0);
                break;
        }
    }

    @Override
    protected void doRandomWhenNormalMove() {
        if (MathUtils.random(50) == 1) {
            GameFactory.buildAndAddNewExplosion(mode, new Vector2((int) this.body.getPosition().x, (int) (this.body.getPosition().y - this.boxSize.y / 2)), 0.9f, "BOSS VIOLET EXPLOSION", "img/explosion/explosion_boss_violet.atlas", 2f);
        }
    }

    private void makeSkyDarkToValue(float targetDark, float step) {
        mode.mapLevel.currentLevel.inNight = true;
        mode.mapLevel.currentLevel.nightBold = MathUtils.lerp(mode.mapLevel.currentLevel.nightBold, targetDark, step);
        mode.rayHandler.setAmbientLight(mode.mapLevel.currentLevel.nightBold);
    }

    @Override
    public void updateWhenNotExist(float delta) {
        super.updateWhenNotExist(delta);

        darkTargetBold = 0.75f;
    }
}
