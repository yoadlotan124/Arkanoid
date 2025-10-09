package com.yoad.arkanoid.game;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import com.yoad.arkanoid.geometry.Line;
import com.yoad.arkanoid.geometry.Point;
import com.yoad.arkanoid.geometry.Rectangle;
import com.yoad.arkanoid.physics.CollisionInfo;

import javafx.scene.paint.Color;

public class WorldClosestCollisionTest {

    @Test
    void choosesNearestHitAlongTrajectory() {
        World world = new World();

        // two bricks centered along y=0 corridor; first at x≈100, second at x≈300
        Brick first  = new Brick(new Rectangle(new Point(100, -10), 40, 20), Color.RED);
        Brick second = new Brick(new Rectangle(new Point(300, -10), 40, 20), Color.BLUE);

        world.addCollidable(first);
        world.addCollidable(second);

        Line path = new Line(new Point(0, 0), new Point(1000, 0)); // moving +X across y=0

        CollisionInfo info = world.getClosestCollision(path);
        assertNotNull(info, "Expected a collision");
        assertSame(first, info.collisionObject(), "Nearest brick should be chosen");
        assertTrue(info.collisionPoint().getX() >= 100.0 && info.collisionPoint().getX() <= 140.0);
    }
}
