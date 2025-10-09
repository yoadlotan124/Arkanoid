package com.yoad.arkanoid.geometry;

/**
 * The geometry.Velocity class specifies the change in position on the x and y axes. It represents
 * the movement of an object by storing its changes in the x (dx) and y (dy) directions.
 */
public class Velocity {
  //---------- Fields ----------

  private final double dx;
  private final double dy;

  //---------- Constructor & Getters/Setters ----------

  public Velocity(double dx, double dy) {
    this.dx = dx;
    this.dy = dy;
  }

  public double getDx() { return dx; }
  public double getDy() { return dy; }

  //---------- Velocity's Logic ----------

  /**
   * Creates a new geometry.Velocity instance based on a given angle and speed.
   */
  public static Velocity fromAngleAndSpeed(double angle, double speed) {
    double angleRad = Math.toRadians(angle);
    double dx = speed * Math.cos(angleRad);
    double dy = speed * Math.sin(angleRad);
    return new Velocity(dx, dy);
  }

  /**
   * Applies this velocity to a given point, returning a new point with the updated position.
   */
  public Point applyToPoint(Point p) {
    return new Point(p.getX() + dx, p.getY() + dy);
  }
}
