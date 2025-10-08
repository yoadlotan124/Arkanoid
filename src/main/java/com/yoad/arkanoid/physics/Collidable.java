package com.yoad.arkanoid.physics;
import com.yoad.arkanoid.sprites.Ball;

import com.yoad.arkanoid.geometry.Point;
import com.yoad.arkanoid.geometry.Rectangle;
import com.yoad.arkanoid.geometry.Velocity;

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
