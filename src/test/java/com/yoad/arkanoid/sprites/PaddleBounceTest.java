package com.yoad.arkanoid.sprites;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import com.yoad.arkanoid.geometry.Point;
import com.yoad.arkanoid.geometry.Rectangle;
import com.yoad.arkanoid.geometry.Velocity;

public class PaddleBounceTest {
    private static final double EPS = 1e-3;

    @Test
    void regions_matchExpectedDxPattern_and_haveVerticalComponent() {
        // Paddle from x=100..200, top edge at y=300
        Paddle p = new Paddle(new Rectangle(100, 300, 100, 12));
        // Incoming straight down (speed = 5)
        Velocity incoming = new Velocity(0, 5);
        double speed = Math.hypot(incoming.getDx(), incoming.getDy());

        double leftX   = 100 +  5;   // far-left
        double midLx   = 100 + 25;   // left-mid
        double centerX = 100 + 50;   // center
        double midRx   = 100 + 75;   // right-mid
        double rightX  = 100 + 95;   // far-right
        double topY    = 300;

        Velocity vL  = p.hit(null, new Point(leftX,   topY - 0.1), incoming); // 300°
        Velocity vLM = p.hit(null, new Point(midLx,   topY - 0.1), incoming); // 330°
        Velocity vC  = p.hit(null, new Point(centerX, topY - 0.1), incoming); // vertical (dx≈0)
        Velocity vRM = p.hit(null, new Point(midRx,   topY - 0.1), incoming); // 30°
        Velocity vR  = p.hit(null, new Point(rightX,  topY - 0.1), incoming); // 60°

        // All must have vertical component
        assertNotEquals(0.0, vL.getDy(),  EPS);
        assertNotEquals(0.0, vLM.getDy(), EPS);
        assertNotEquals(0.0, vC.getDy(),  EPS);
        assertNotEquals(0.0, vRM.getDy(), EPS);
        assertNotEquals(0.0, vR.getDy(),  EPS);

        // Expected dx magnitudes, given speed=5:
        // cos(300°)=0.5, cos(330°)=0.866, center≈0, cos(30°)=0.866, cos(60°)=0.5
        assertEquals(0.5   * speed, Math.abs(vL.getDx()),  0.6);  // allow tolerance
        assertEquals(0.866 * speed, Math.abs(vLM.getDx()), 0.6);
        assertEquals(0.0,            Math.abs(vC.getDx()),  1.0);
        assertEquals(0.866 * speed, Math.abs(vRM.getDx()), 0.6);
        assertEquals(0.5   * speed, Math.abs(vR.getDx()),  0.6);

        // Speed roughly preserved
        for (Velocity v : new Velocity[]{vL,vLM,vC,vRM,vR}) {
            double outSpeed = Math.hypot(v.getDx(), v.getDy());
            assertEquals(speed, outSpeed, 1.0);
        }
    }
}
