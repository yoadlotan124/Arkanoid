package com.yoad.arkanoid.game;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import com.yoad.arkanoid.geometry.Point;
import com.yoad.arkanoid.geometry.Rectangle;
import com.yoad.arkanoid.geometry.Velocity;
import com.yoad.arkanoid.sprites.Ball;

import javafx.scene.paint.Color;

public class BrickHitTest {

    private static Ball dummyBall() {
        // center/velocity/env irrelevant for Brick.hit; just needs a Ball instance
        return new Ball(new Point(0,0), 6, Color.WHITE, null);
    }

    @Test
    void reflect_onTop_and_onBottom() {
        Brick b = new Brick(new Rectangle(new Point(100,100), 50, 20), Color.web("#ff8800"));

        Velocity inDown = new Velocity(2, 3); // coming from above, hitting top
        Velocity outTop = b.hit(dummyBall(), new Point(120, 100 - 0.1), inDown);
        assertEquals( 2.0, outTop.getDx(), 1e-6);
        assertEquals(-3.0, outTop.getDy(), 1e-6);

        Velocity inUp = new Velocity(-1, -4); // coming from below, hitting bottom
        Velocity outBot = b.hit(dummyBall(), new Point(130, 120 + 0.1), inUp);
        assertEquals(-1.0, outBot.getDx(), 1e-6);
        assertEquals( 4.0, outBot.getDy(), 1e-6);
    }

    @Test
    void reflect_onSides() {
        Brick b = new Brick(new Rectangle(new Point(100,100), 50, 20), Color.web("#ff8800"));

        Velocity inRight = new Velocity(5, 0.5); // hitting left side
        Velocity outLeft = b.hit(dummyBall(), new Point(100 - 0.1, 110), inRight);
        assertEquals(-5.0, outLeft.getDx(), 1e-6);
        assertEquals( 0.5, outLeft.getDy(), 1e-6);

        Velocity inLeft = new Velocity(-3, -1); // hitting right side
        Velocity outRight = b.hit(dummyBall(), new Point(150 + 0.1, 115), inLeft);
        assertEquals( 3.0, outRight.getDx(), 1e-6);
        assertEquals(-1.0, outRight.getDy(), 1e-6);
    }
}
