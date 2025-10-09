package com.yoad.arkanoid.geometry;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class VelocityTest {
    private static final double EPS = 1e-6;

    @Test
    void speed_isPreserved_forVariousAngles() {
        double[] angles = {0, 30, 60, 90, 120, 180, 225, 270, 330};
        for (double a : angles) {
            Velocity v = Velocity.fromAngleAndSpeed(a, 7.5);
            double speed = Math.hypot(v.getDx(), v.getDy());
            assertEquals(7.5, speed, 1e-6, "Speed not preserved for angle " + a);
        }
    }

    @Test
    void ninetyDegreeSteps_arePerpendicular() {
        Velocity v0 = Velocity.fromAngleAndSpeed(0, 10);
        Velocity v90 = Velocity.fromAngleAndSpeed(90, 10);
        double dot = v0.getDx() * v90.getDx() + v0.getDy() * v90.getDy();
        assertEquals(0.0, dot, 1e-6, "Vectors at 90° should be perpendicular");
    }

    @Test
    void applyToPoint_translatesCorrectly() {
        Point p = new Point(10, 20);
        Velocity v = new Velocity(3, -4);
        Point p2 = v.applyToPoint(p);
        assertEquals(13.0, p2.getX(), EPS);
        assertEquals(16.0, p2.getY(), EPS);
    }
}
