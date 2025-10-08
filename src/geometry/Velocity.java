package geometry;

/**
 * The geometry.Velocity class specifies the change in position on the x and y axes.
 * It represents the movement of an object by storing its changes in the x (dx) and y (dy) directions.
 */
public class Velocity {
    private final double dx;
    private final double dy;

    /**
     * Constructs a geometry.Velocity object with the specified changes in x and y directions.
     *
     * @param dx the change in the x-direction
     * @param dy the change in the y-direction
     */
    public Velocity(double dx, double dy) {
        this.dx = dx;
        this.dy = dy;
    }

    /**
     * Creates a new geometry.Velocity instance based on a given angle and speed.
     *
     * @param angle The angle in degrees, measured counterclockwise from the positive x-axis.
     * @param speed The magnitude of the velocity.
     * @return A geometry.Velocity object representing the motion in Cartesian coordinates.
     */
    public static Velocity fromAngleAndSpeed(double angle, double speed) {
        double angleRad = Math.toRadians(angle);
        double dx = speed * Math.cos(angleRad);
        double dy = speed * Math.sin(angleRad);
        return new Velocity(dx, dy);
    }

    /**
     * Applies this velocity to a given point, returning a new point with the updated position.
     *
     * @param p the original point
     * @return a new shapes.Point with updated coordinates (x + dx, y + dy)
     */
    public Point applyToPoint(Point p) {
        return new Point(p.getX() + dx, p.getY() + dy);
    }

    /**
     * Returns the change in the x-direction (dx).
     *
     * @return the value of dx
     */
    public double getDx() {
        return dx;
    }

    /**
     * Returns the change in the y-direction (dy).
     *
     * @return the value of dy
     */
    public double getDy() {
        return dy;
    }
}