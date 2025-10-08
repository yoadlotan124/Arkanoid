package com.yoad.arkanoid.geometry;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class LineIntersectionTest {

  @Test
  void intersectAtCenter() {
    Line a = new Line(0, 0, 10, 10);
    Line b = new Line(0, 10, 10, 0);
    Point p = a.intersectionWith(b);
    assertNotNull(p);
    assertEquals(5.0, p.getX(), 1e-9);
    assertEquals(5.0, p.getY(), 1e-9);
  }

  @Test
  void parallelNoIntersection() {
    Line a = new Line(0, 0, 10, 0);
    Line b = new Line(0, 1, 10, 1);
    assertNull(a.intersectionWith(b));
  }

  @Test
  void collinearOverlapHandled() {
    Line a = new Line(0, 0, 10, 0);
    Line b = new Line(5, 0, 15, 0);
    // depends on your API; assertTrue(a.isIntersecting(b)) if provided
    assertTrue(a.isIntersecting(b));
  }
}
