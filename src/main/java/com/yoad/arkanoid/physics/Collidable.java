package com.yoad.arkanoid.physics;

import com.yoad.arkanoid.geometry.Point;
import com.yoad.arkanoid.geometry.Rectangle;
import com.yoad.arkanoid.geometry.Velocity;
import com.yoad.arkanoid.sprites.Ball;

/**
 * represents an interface for classes that use some sort of collisions.
 */
public interface Collidable {

  //---------- Interface's Methods ----------

  Rectangle getCollisionRectangle();

  /**
   * Notify the object that we collided with at collisionPoint with a given velocity.
   */
  Velocity hit(Ball hitter, Point collisionPoint, Velocity currentVelocity);
}
