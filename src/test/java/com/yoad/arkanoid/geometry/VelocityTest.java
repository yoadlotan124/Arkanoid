package com.yoad.arkanoid.geometry;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class VelocityTest {
    @Test
    void applyToPointMovesCorrectly() {
        Velocity v = new Velocity(3, -2);
        Point start = new Point(10, 10);
        Point after = v.applyToPoint(start);
        assertEquals(13, after.getX(), 1e-9);
        assertEquals(8, after.getY(), 1e-9);
    }

    @Test
    void fromAngleAndSpeed() {
        Velocity v = Velocity.fromAngleAndSpeed(0, 5);
        assertEquals(5, v.getDx(), 1e-9);
        assertEquals(0, v.getDy(), 1e-9);
    }
}
