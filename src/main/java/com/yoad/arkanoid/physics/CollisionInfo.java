package com.yoad.arkanoid.physics;

import com.yoad.arkanoid.geometry.Point;

/**
 * The physics.CollisionInfo class represents information about a collision, including the collision
 * point and the object involved in the collision.
 */
public class CollisionInfo {
  //---------- Fields ----------
  private final Point collisionPoint;
  private final Collidable collidable;

  //---------- Constructor & Getters/Setters ----------

  public CollisionInfo(Point collisionPoint, Collidable collidable) {
    this.collisionPoint = collisionPoint;
    this.collidable = collidable;
  }

  public Point collisionPoint() { return collisionPoint; }
  public Collidable collisionObject() { return collidable;}
}
