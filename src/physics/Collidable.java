package physics;
import sprites.Ball;

import geometry.Point;
import geometry.Rectangle;
import geometry.Velocity;

/**
 * represents an interface for classes that use some sort of collisions.
 */
public interface Collidable {

    /**
     * @return the rectangle that collided with.
     */
    Rectangle getCollisionRectangle();

    /**
     * Notify the object that we collided with at collisionPoint with a given velocity.
     * @param collisionPoint the point of collision.
     * @param currentVelocity our current velocity.
     * @param hitter the ball.
     * @return the new geometry.Velocity after colliding.
     */
    Velocity hit(Ball hitter, Point collisionPoint, Velocity currentVelocity);
}
