package com.yoad.arkanoid.geometry;

/**
 * Represents a point in a 2D coordinate system. Provides methods to calculate distance between
 * points and check equality.
 */
public class Point {
  //---------- Fields ----------

  private static final double EPSILON = 1e-9; // Precision threshold
  private final double x;
  private final double y;

  //---------- Constructor & Getters/Setters ----------

  public Point(double x, double y) {
    this.x = x;
    this.y = y;
  }

  public double getX() { return this.x; }
  public double getY() { return this.y; }

  //---------- Point's Logic ----------

  /**
   * Calculates the distance between this point and another point.
   */
  public double distance(Point other) {
    // Null check
    if (other == null) {
      return 0;
    }

    return Math.sqrt(
        (this.x - other.getX()) * (this.x - other.getX())
            + (this.y - other.getY()) * (this.y - other.getY()));
  }

  /**
   * Compares this point with another point to check if they are equal.
   */
  public boolean equals(Point other) {
    if (this == other) {
      return true; // Same reference, return true
    }
    if (other == null) {
      return false; // Null check
    }

    return Math.abs(this.x - other.getX()) < Point.EPSILON
        && Math.abs(this.y - other.getY()) < Point.EPSILON;
  }
}
