package com.yoad.arkanoid.geometry;

/**
 * Represents a line segment in a 2D plane, defined by two points: a start point and an end point.
 * Provides methods to calculate length, check for intersection, and determine other properties of
 * the line.
 */
public class Line {
  // Fields
  private static final double EPSILON = 1e-9; // Precision threshold
  private final Point start;
  private final Point end;
  private final double length;

  /**
   * Constructs a shapes.Line given two Points that represent the start and end of the line.
   *
   * @param start The start point of the line.
   * @param end The end point of the line.
   */
  public Line(Point start, Point end) {
    this.start = start;
    this.end = end;
    this.length = start.distance(end);
  }

  /**
   * Constructs a shapes.Line given four coordinates (x1, y1) for the start point and (x2, y2) for
   * the end point.
   *
   * @param x1 The x-coordinate of the start point.
   * @param y1 The y-coordinate of the start point.
   * @param x2 The x-coordinate of the end point.
   * @param y2 The y-coordinate of the end point.
   */
  public Line(double x1, double y1, double x2, double y2) {
    this(new Point(x1, y1), new Point(x2, y2));
  }

  /**
   * Returns the length of the line.
   *
   * @return The length of the line segment.
   */
  public double getLength() {
    return this.length;
  }

  /**
   * Returns the midpoint of the line.
   *
   * @return The point representing the middle of the line.
   */
  public Point getMiddle() {
    return new Point(
        (this.start.getX() + this.end.getX()) / 2.0, (this.start.getY() + this.end.getY()) / 2.0);
  }

  /**
   * Returns the start point of the line.
   *
   * @return The start point of the line.
   */
  public Point getStart() {
    return new Point(this.start.getX(), this.start.getY());
  }

  /**
   * Returns the end point of the line.
   *
   * @return The end point of the line.
   */
  public Point getEnd() {
    return new Point(this.end.getX(), this.end.getY());
  }

  /**
   * Checks if the point p lies on the line segment.
   *
   * @param p The point to check
   * @return {@code true} if q lies on the segment, {@code false} otherwise.
   */
  public boolean onSegment(Point p) {
    return p.getX() >= Math.min(this.start.getX(), this.end.getX()) - EPSILON
        && p.getX() <= Math.max(this.start.getX(), this.end.getX()) + EPSILON
        && p.getY() >= Math.min(this.start.getY(), this.end.getY()) - EPSILON
        && p.getY() <= Math.max(this.start.getY(), this.end.getY()) + EPSILON;
  }

  /**
   * Determines the orientation of the ordered triplet (p, q, r).
   *
   * @param p The first point.
   * @param q The second point.
   * @param r The third point.
   * @return 0 if collinear, 1 if clockwise, 2 if counterclockwise.
   */
  private int orientation(Point p, Point q, Point r) {
    double val =
        (q.getY() - p.getY()) * (r.getX() - q.getX())
            - (q.getX() - p.getX()) * (r.getY() - q.getY());
    return Math.abs(val) < EPSILON ? 0 : (val > 0 ? 1 : 2);
  }

  /**
   * Checks if this line intersects with another line.
   *
   * @param other The other line to check for intersection.
   * @return {@code true} if the lines intersect, {@code false} otherwise.
   */
  public boolean isIntersecting(Line other) {
    int o1 = orientation(this.start, this.end, other.start);
    int o2 = orientation(this.start, this.end, other.end);
    int o3 = orientation(other.start, other.end, this.start);
    int o4 = orientation(other.start, other.end, this.end);

    // General case: If orientations differ, lines intersect
    if (o1 != o2 && o3 != o4) {
      return true;
    }

    // Collinear cases: check if one of the points lies on the segment
    if (o1 == 0 && this.onSegment(other.start)) {
      return true;
    }
    if (o2 == 0 && this.onSegment(other.end)) {
      return true;
    }
    if (o3 == 0 && other.onSegment(this.start)) {
      return true;
    }
    if (o4 == 0 && other.onSegment(this.end)) {
      return true;
    }

    return false; // No intersection
  }

  /**
   * Checks if this line intersects with two other lines.
   *
   * @param other1 The first line to check for intersection.
   * @param other2 The second line to check for intersection.
   * @return {@code true} if the lines intersect, {@code false} otherwise.
   */
  public boolean isIntersecting(Line other1, Line other2) {
    int o1 = orientation(other1.start, other1.end, other2.start);
    int o2 = orientation(other1.start, other1.end, other2.end);
    int o3 = orientation(other2.start, other2.end, other1.start);
    int o4 = orientation(other2.start, other2.end, other1.end);

    // General case: If orientations differ, lines intersect
    if (o1 != o2 && o3 != o4) {
      return true;
    }

    // Collinear cases: check if one of the points lies on the segment
    if (o1 == 0 && other1.onSegment(other2.start)) {
      return true;
    }
    if (o2 == 0 && other1.onSegment(other2.end)) {
      return true;
    }
    if (o3 == 0 && other2.onSegment(other1.start)) {
      return true;
    }
    if (o4 == 0 && other2.onSegment(other1.end)) {
      return true;
    }

    return false; // No intersection
  }

  /**
   * Returns the intersection point of this line and another line if they intersect.
   *
   * @param other The other line to check for intersection.
   * @return The intersection point if the lines intersect, {@code null} otherwise.
   */
  public Point intersectionWith(Line other) {
    // No intersection
    if (!isIntersecting(other)) {
      return null;
    }

    // Check if lines are exactly the same
    if (this.equals(other)) {
      return null;
    }

    // Check if the intersection is the end-points
    if (this.start.equals(other.start) && !this.end.equals(other.end)) {
      return this.start;
    }
    if (this.start.equals(other.end) && !this.end.equals(other.start)) {
      return this.start;
    }
    if (this.end.equals(other.start) && !this.start.equals(other.end)) {
      return this.end;
    }
    if (this.end.equals(other.end) && !this.start.equals(other.start)) {
      return this.end;
    }

    // Calculate intersection point
    double a1 = this.end.getY() - this.start.getY();
    double b1 = this.start.getX() - this.end.getX();
    double c1 = a1 * this.start.getX() + b1 * this.start.getY();

    double a2 = other.end.getY() - other.start.getY();
    double b2 = other.start.getX() - other.end.getX();
    double c2 = a2 * other.start.getX() + b2 * other.start.getY();

    // Calculate determinant
    double det = a1 * b2 - a2 * b1;
    if (det == 0) {
      // Lines are parallel or collinear, check if they overlap
      if (this.onSegment(other.start)
          || this.onSegment(other.end)
          || other.onSegment(this.start)
          || other.onSegment(this.end)) {
        // If lines overlap or touch, return any valid intersection point
        // You can return any of the overlapping points, for example:
        if (this.onSegment(other.start)) {
          return other.start; // Intersection point is one of the endpoints
        }
        if (this.onSegment(other.end)) {
          return other.end; // Return another valid intersection point
        }
        // If lines overlap completely, return any point from either segment
        return this
            .start; // Or you can choose to return other.start or any other point on the segments
      }
      return null;
    }

    // Compute the intersection point
    double x = (b2 * c1 - b1 * c2) / det;
    double y = (a1 * c2 - a2 * c1) / det;

    // Check if the point is on the segments
    if (this.onSegment(new Point(x, y)) && other.onSegment(new Point(x, y))) {
      return new Point(x, y);
    }
    return null;
  }

  /**
   * Compares this line with another line for equality.
   *
   * @param other The other line to compare to.
   * @return {@code true} if the lines are equal, {@code false} otherwise.
   */
  public boolean equals(Line other) {
    if (other == null) {
      return false;
    }

    // Uses the points "equals" function
    return (this.start.equals(other.start) && this.end.equals(other.end))
        || (this.start.equals(other.end) && this.end.equals(other.start));
  }

  /**
   * returns the closest intersection point with the start of the line.
   *
   * @param rect the rectangle to compare with.
   * @return the closest point of intersection with the rectangle.
   */
  public Point closestIntersectionToStartOfLine(Rectangle rect) {
    java.util.List<Point> interList = rect.intersectionPoints(this);
    if (interList.isEmpty()) {
      return null;
    }
    Point closestPoint = interList.get(0);
    for (Point p : interList) {
      if (start.distance(p) < start.distance(closestPoint)) {
        closestPoint = p;
      }
    }
    return closestPoint;
  }
}
