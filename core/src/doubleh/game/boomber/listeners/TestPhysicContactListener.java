package doubleh.game.boomber.listeners;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;

/**
 * This class for PhysicTestScreen for testing contact in that screen only
 */
public class TestPhysicContactListener implements ContactListener {

    @Override
    public void beginContact(Contact contact) {

    }

    @Override
    public void endContact(Contact contact) {

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

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
}
