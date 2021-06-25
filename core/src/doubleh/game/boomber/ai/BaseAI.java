package doubleh.game.boomber.ai;

import doubleh.game.boomber.actors.entities.BaseEntity;

public abstract class BaseAI {
    protected BaseEntity entity;

    public BaseAI(BaseEntity entity) {
        this.entity = entity;
    }

    public abstract void update(float delta);

    public BaseEntity getEntity() {
        return entity;
    }

    public void setEntity(BaseEntity entity) {
        this.entity = entity;
    }

    @Override
    public boolean equals(Object obj) {
        return (obj.getClass() == this.getClass());
    }
}
