package com.yoad.arkanoid.geometry;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class RectangleTest {
    @Test
    void widthHeightNonNegative() {
        Rectangle r = new Rectangle(new Point(10, 20), 30, 40);
        assertEquals(30, r.getWidth());
        assertEquals(40, r.getHeight());
        assertEquals(10, r.getUpperLeft().getX());
        assertEquals(20, r.getUpperLeft().getY());
    }

    @Test
    void containsPointsOnEdges() {
        Rectangle r = new Rectangle(new Point(0, 0), 100, 50);
        assertTrue(r.contains(new Point(0, 0)));
        assertTrue(r.contains(new Point(100, 0)));
        assertTrue(r.contains(new Point(0, 50)));
        assertTrue(r.contains(new Point(100, 50)));
    }
}
