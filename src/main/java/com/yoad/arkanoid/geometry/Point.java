package com.yoad.arkanoid.geometry;

/**
 * Represents a point in a 2D coordinate system.
 * Provides methods to calculate distance between points and check equality.
 */
public class Point {
    // Fields
    private static final double EPSILON = 1e-9; // Precision threshold
    private final double x;
    private final double y;

    /**
     * Constructs a shapes.Point with the specified x and y coordinates.
     *
     * @param x The x-coordinate of the point.
     * @param y The y-coordinate of the point.
     */
    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Calculates the distance between this point and another point.
     *
     * @param other The other point to which the distance is calculated.
     * @return The distance between the two points. Returns 0 if the other point is null.
     */
    public double distance(Point other) {
        // Null check
        if (other == null) {
            return 0;
        }

        return Math.sqrt((this.x - other.getX()) * (this.x - other.getX())
                + (this.y - other.getY()) * (this.y - other.getY()));
    }

    /**
     * Compares this point with another point to check if they are equal.
     *
     * @param other The other point to compare to.
     * @return {@code true} if the points have the same x and y coordinates, {@code false} otherwise.
     */
    public boolean equals(Point other) {
        if (this == other) {
            return true;  // Same reference, return true
        }
        if (other == null) {
            return false;  // Null check
        }

        return Math.abs(this.x - other.getX()) < Point.EPSILON
                && Math.abs(this.y - other.getY()) < Point.EPSILON;
    }

    /**
     * Gets the x-coordinate of this point.
     *
     * @return The x-coordinate of the point.
     */
    public double getX() {
        return this.x;
    }

    /**
     * Gets the y-coordinate of this point.
     *
     * @return The y-coordinate of the point.
     */
    public double getY() {
        return this.y;
    }
}
