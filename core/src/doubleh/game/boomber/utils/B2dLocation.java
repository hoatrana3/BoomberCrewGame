package doubleh.game.boomber.utils;

import com.badlogic.gdx.ai.utils.Location;
import com.badlogic.gdx.math.Vector2;

public class B2dLocation implements Location<Vector2> {

    private Vector2 position;
    private float orientation;

    public B2dLocation() {
        this.position = new Vector2();
        this.orientation = 0;
    }

    @Override
    public Vector2 getPosition() {
        return position;
    }

    @Override
    public float getOrientation() {
        return orientation;
    }

    @Override
    public void setOrientation(float orientation) {
        this.orientation = orientation;
    }

    @Override
    public Location<Vector2> newLocation() {
        return new B2dLocation();
    }

    @Override
    public float vectorToAngle(Vector2 vector) {
        return B2dSteeringUtils.vectorToAngle(vector);
    }

    @Override
    public Vector2 angleToVector(Vector2 outVector, float angle) {
        return B2dSteeringUtils.angleToVector(outVector, angle);
    }

}
