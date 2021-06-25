package doubleh.game.boomber.actors.others;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import doubleh.game.boomber.BoomberGame;
import doubleh.game.boomber.modes.BaseMode;

import static doubleh.game.boomber.utils.Constants.PPM;

public class AnimationActor extends Actor {
    private BaseMode mode;

    private TextureAtlas atlas;
    private Animation<TextureAtlas.AtlasRegion> animation;
    private float eslapedTime;

    private Vector2 offset;

    private Vector2 scale;
    private float rotation;

    private int keyToLock;

    private boolean isFlipX, isFlipY;
    private String currentRegionName;

    private boolean isUsingInScene2d;

    public AnimationActor(BaseMode mode, Vector2 position, Vector2 offsetPos, float widthTarget, String pathToAtlas, String regionName, float speedLoad, float rotation, Animation.PlayMode playMode, String actorName) {
        this.mode = mode;
        this.rotation = rotation;
        this.offset = offsetPos;
        this.isFlipX = this.isFlipY = false;
        this.currentRegionName = regionName;

        isUsingInScene2d = false;

        atlas = BoomberGame.getInstance().assets.get(pathToAtlas);

        if (regionName.isEmpty())
            animation = new Animation<>(1f / speedLoad, atlas.getRegions());
        else {
            animation = new Animation<>(1f / speedLoad, atlas.findRegions(regionName));
        }

        animation.setPlayMode(playMode);

        setPosition((position.x + offsetPos.x) * PPM, (position.y + offsetPos.y) * PPM);
        setName(actorName);

        this.scale = new Vector2(widthTarget / animation.getKeyFrame(0).getRegionWidth(), widthTarget / animation.getKeyFrame(0).getRegionWidth());

        eslapedTime = 0;
        keyToLock = -1;
    }

    public AnimationActor(BaseMode mode, Vector2 position, Vector2 offsetPos, float widthTarget, String pathToAtlas, String regionName, float speedLoad, float rotation, Animation.PlayMode playMode, String actorName, boolean isUsingInScene2d) {
        this(mode, position, offsetPos, widthTarget, pathToAtlas, regionName, speedLoad, rotation, playMode, actorName);

        this.isUsingInScene2d = isUsingInScene2d;
        setPosition(position.x + offsetPos.x, position.y + offsetPos.y);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        if ((keyToLock != getKeyFrameIndex()) || keyToLock == -1) eslapedTime += delta;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        Color temp = batch.getColor();

        batch.setColor(temp.r, temp.g, temp.b, getColor().a * parentAlpha);

        TextureAtlas.AtlasRegion region = animation.getKeyFrame(eslapedTime);
        setSize(region.getRegionWidth() * scale.x, region.getRegionHeight() * scale.y);

        region.getTexture().setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);

        if (isUsingInScene2d) {
            batch.draw(region, getX(), getY(), 0, 0, region.getRegionWidth(), region.getRegionHeight(), scale.x, scale.y, rotation);
        } else {
            Vector2 posDraw = new Vector2(getX() - region.getRegionWidth() / 2f, getY() - region.getRegionHeight() / 2f);

            batch.draw(region, posDraw.x, posDraw.y, region.getRegionWidth() / 2f, region.getRegionHeight() / 2f, region.getRegionWidth(), region.getRegionHeight(), scale.x, scale.y, rotation);
        }

        setBounds(getX(), getY(), getWidth(), getHeight());

        batch.setColor(temp);
    }

    public boolean isFinished() {
        return ((animation.getPlayMode() == Animation.PlayMode.NORMAL)
                || animation.getPlayMode() == Animation.PlayMode.REVERSED) && animation.isAnimationFinished(eslapedTime);
    }

    public int getKeyFrameIndex() {
        return animation.getKeyFrameIndex(eslapedTime);
    }

    public void setToRegions(String name) {
        this.isFlipX = this.isFlipY = false;
        this.currentRegionName = name;

        Animation.PlayMode mode = animation.getPlayMode();
        float frameDuration = animation.getFrameDuration();

        animation = new Animation<>(frameDuration, atlas.findRegions(name));
        animation.setPlayMode(mode);
        eslapedTime = 0;
    }

    public void setKeyToLock(int keyToLock) {
        this.keyToLock = keyToLock;
    }

    public void setOffset(Vector2 offset) {
        this.offset = offset;
    }

    public Vector2 getOffset() {
        return offset;
    }

    public void callFlip(boolean flipX, boolean flipY) {
        if (flipX) this.isFlipX = !this.isFlipX;
        if (flipY) this.isFlipY = !this.isFlipY;

        for (TextureAtlas.AtlasRegion region : animation.getKeyFrames()) {
            region.flip(flipX, flipY);
        }
    }

    public boolean isFlipX() {
        return isFlipX;
    }

    public boolean isFlipY() {
        return isFlipY;
    }

    public void setFlipX(boolean flipX) {
        isFlipX = flipX;
    }

    public void setFlipY(boolean flipY) {
        isFlipY = flipY;
    }

    public int getKeyToLock() {
        return keyToLock;
    }

    public String getCurrentRegionName() {
        return currentRegionName;
    }

    public void setCurrentRegionName(String currentRegionName) {
        this.currentRegionName = currentRegionName;
    }

    public Animation.PlayMode getPlayMode() {
        return animation.getPlayMode();
    }

    public void setPlayMode(Animation.PlayMode playMode) {
        this.animation.setPlayMode(playMode);
    }

    public boolean isUsingInScene2d() {
        return isUsingInScene2d;
    }

    public void setUsingInScene2d(boolean usingInScene2d) {
        isUsingInScene2d = usingInScene2d;
    }
}
