package com.yoad.arkanoid.physics;

import com.yoad.arkanoid.geometry.Point;

/**
 * The physics.CollisionInfo class represents information about a collision, including the collision
 * point and the object involved in the collision.
 */
public class CollisionInfo {
  private final Point collisionPoint;
  private final Collidable collidable;

  /**
   * Constructs a physics.CollisionInfo with a given collision point and collidable object.
   *
   * @param collisionPoint the point at which the collision occurs
   * @param collidable the object involved in the collision
   */
  public CollisionInfo(Point collisionPoint, Collidable collidable) {
    this.collisionPoint = collisionPoint;
    this.collidable = collidable;
  }

  /**
   * Returns the point at which the collision occurs.
   *
   * @return the collision point
   */
  public Point collisionPoint() {
    return collisionPoint;
  }

  /**
   * Returns the collidable object involved in the collision.
   *
   * @return the collidable object
   */
  public Collidable collisionObject() {
    return collidable;
  }
}
