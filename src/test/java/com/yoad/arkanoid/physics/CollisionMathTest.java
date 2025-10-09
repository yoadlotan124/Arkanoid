package com.yoad.arkanoid.physics;

import com.yoad.arkanoid.geometry.Line;
import com.yoad.arkanoid.geometry.Point;
import com.yoad.arkanoid.geometry.Rectangle;
import com.yoad.arkanoid.geometry.Velocity;
import com.yoad.arkanoid.sprites.Ball;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CollisionMathTest {

    @Test
    void lineIntersectsRectangleEdge() {
        Rectangle rect = new Rectangle(new Point(0, 0), 100, 50);

        // Horizontal line passing through the rectangle's left edge (x=0) at y=25
        Line l = new Line(-10, 25, 10, 25);

        // If Rectangle exposes its edges as lines, replace these with actual calls.
        assertTrue(l.isIntersecting(new Line(0, 0, 0, 50)));
    }

    @Test
    void collisionInfoHoldsContactPointAndObject() {
        Collidable dummy = new Collidable() {
            private final Rectangle r = new Rectangle(new Point(0, 0), 10, 10);

            @Override
            public Rectangle getCollisionRectangle() {
                return r;
            }

            @Override
            public Velocity hit(Ball hitter, Point collisionPoint, Velocity currentVelocity) {
                // No-op: just return the incoming velocity so test remains pure.
                return currentVelocity;
            }
        };

        CollisionInfo info = new CollisionInfo(new Point(5, 5), dummy);
        assertEquals(5.0, info.collisionPoint().getX(), 1e-9);
        assertEquals(5.0, info.collisionPoint().getY(), 1e-9);
        assertSame(dummy, info.collisionObject());
    }
}
