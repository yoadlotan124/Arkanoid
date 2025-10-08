package com.yoad.arkanoid.geometry;

import java.util.ArrayList;
import java.util.List;

/** Represents a rectangular area defined by its top-left corner, width, and height. */
public class Rectangle {
  private final int width, height;
  private Point upperLeft;

  /**
   * Constructs a rectangle with the specified position and dimensions.
   *
   * @param startX The x-coordinate of the top-left corner of the rectangle.
   * @param startY The y-coordinate of the top-left corner of the rectangle.
   * @param width The width of the rectangle.
   * @param height The height of the rectangle.
   */
  public Rectangle(int startX, int startY, int width, int height) {
    this.upperLeft = new Point(startX, startY);
    this.width = width;
    this.height = height;
  }

  /**
   * Constructs a rectangle with the specified position and dimensions.
   *
   * @param upperLeft The shapes.Point of the top-left corner of the rectangle.
   * @param width The width of the rectangle.
   * @param height The height of the rectangle.
   */
  public Rectangle(Point upperLeft, int width, int height) {
    this.upperLeft = new Point(upperLeft.getX(), upperLeft.getY());
    this.width = width;
    this.height = height;
  }

  /**
   * Checks if a given point is inside the rectangle.
   *
   * @param p The point to check.
   * @return {@code true} if the point (including its radius) is completely inside the rectangle,
   *     {@code false} otherwise.
   */
  public boolean contains(Point p) {
    double buffer = 0.1; // Small tolerance to avoid edge cases
    return (p.getX() >= this.getStartX() - buffer
        && p.getX() <= this.getStartX() + width + buffer
        && p.getY() >= this.getStartY() - buffer
        && p.getY() <= this.getStartY() + height + buffer);
  }

  /**
   * Creates a list of intersection points with a given line (possibly empty).
   *
   * @param line the line to check intersections with the rectangle
   * @return list of intersection points (no nulls)
   */
  public List<Point> intersectionPoints(Line line) {
    List<Point> interList = new ArrayList<>();

    Point lowerLeft = new Point(this.getStartX(), this.getStartY() + this.getHeight());
    Point upperRight = new Point(this.getStartX() + this.getWidth(), this.getStartY());
    Point lowerRight =
        new Point(this.getStartX() + this.getWidth(), this.getStartY() + this.getHeight());
    Point upperLeft = new Point(this.getStartX(), this.getStartY());

    Line[] edges = {
      new Line(upperLeft, lowerLeft),
      new Line(lowerLeft, lowerRight),
      new Line(lowerRight, upperRight),
      new Line(upperRight, upperLeft)
    };

    for (Line edge : edges) {
      Point intersection = line.intersectionWith(edge);
      if (intersection != null) {
        interList.add(intersection);
      }
    }

    return interList;
  }

  /**
   * Gets the x-coordinate of the top-left corner of the rectangle.
   *
   * @return The x-coordinate of the top-left corner.
   */
  public int getStartX() {
    return (int) this.upperLeft.getX();
  }

  /**
   * Gets the y-coordinate of the top-left corner of the rectangle.
   *
   * @return The y-coordinate of the top-left corner.
   */
  public int getStartY() {
    return (int) this.upperLeft.getY();
  }

  /**
   * Gets the width of the rectangle.
   *
   * @return The width of the rectangle.
   */
  public int getWidth() {
    return this.width;
  }

  /**
   * Gets the height of the rectangle.
   *
   * @return The height of the rectangle.
   */
  public int getHeight() {
    return this.height;
  }

  /**
   * @return the upper left point of the shapes.Rectangle
   */
  public Point getUpperLeft() {
    return this.upperLeft;
  }

  /**
   * moves the rectangle accordingly.
   *
   * @param direction
   */
  public void move(int direction) {
    // Update the x-coordinate based on direction (positive for right, negative for left)
    this.upperLeft = new Point(this.upperLeft.getX() + direction, this.upperLeft.getY());
  }
}
