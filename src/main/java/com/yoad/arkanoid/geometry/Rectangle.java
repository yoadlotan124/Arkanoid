package com.yoad.arkanoid.geometry;

import java.util.ArrayList;
import java.util.List;

public class Rectangle {
  //---------- Fields ----------

  private final int width, height;
  private Point upperLeft;

  //---------- Constructor & Getters/Setters ----------

  public Rectangle(int startX, int startY, int width, int height) {
    this.upperLeft = new Point(startX, startY);
    this.width = width;
    this.height = height;
  }
  public Rectangle(Point upperLeft, int width, int height) {
    this.upperLeft = new Point(upperLeft.getX(), upperLeft.getY());
    this.width = width;
    this.height = height;
  }

  public int getStartX() { return (int) this.upperLeft.getX(); }
  public int getStartY() { return (int) this.upperLeft.getY(); }
  public int getWidth() { return this.width; }
  public int getHeight() { return this.height; }
  public Point getUpperLeft() { return this.upperLeft; }

  //---------- Rectangle's Logic ----------

  /**
   * Checks if a given point is inside the rectangle.
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

  public void move(int direction) {
    // Update the x-coordinate based on direction (positive for right, negative for left)
    this.upperLeft = new Point(this.upperLeft.getX() + direction, this.upperLeft.getY());
  }
}
