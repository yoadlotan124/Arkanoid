package com.yoad.arkanoid.game;

import com.yoad.arkanoid.geometry.Line;
import com.yoad.arkanoid.geometry.Point;
import com.yoad.arkanoid.geometry.Rectangle;
import com.yoad.arkanoid.physics.Collidable;
import com.yoad.arkanoid.physics.CollisionInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Environment of the game, meaning the rectangles to collide with and such.
 */
public class World {
    // Fields
    private final Rectangle screen;
    private final List<Collidable> collidables;

    /**
     * Constructor for the game environment.
     */
    public World() {
        screen = new Rectangle(28, 28, 772, 572);
        collidables = new ArrayList<>();
    }

    /**
     * @return screen.
     */
    public Rectangle getScreen() {
        return screen;
    }

    /**
     * @return collidables list.
     */
    public List<Collidable> getCollidables() {
        return collidables;
    }

    /**
     * Adds the given collidable to the environment.
     *
     * @param c the collidable object to add
     */
    public void addCollidable(Collidable c) {
        collidables.add(c);
    }

    /**
     * Assume an object moving from line.start() to line.end().
     * If this object will not collide with any of the collidables
     * in this collection, returns null. Else, returns the information
     * about the closest collision that is going to occur.
     *
     * @param trajectory the path of the moving object
     * @return information about the closest collision, or null if none
     */
    public CollisionInfo getClosestCollision(Line trajectory) {
        Point closestPoint = null;
        Collidable closestCollidable = null;

        for (Collidable collidable : collidables) {
            Rectangle collisionRectangle = collidable.getCollisionRectangle();
            Point p = trajectory.closestIntersectionToStartOfLine(collisionRectangle);

            // Ensure that p is not null before proceeding with distance comparison
            if (p != null) {
                // If closestPoint is null (first valid collision) or if p is closer than the previous closest
                if (closestPoint == null || trajectory.getStart().distance(p)
                        < trajectory.getStart().distance(closestPoint)) {
                    closestPoint = p;
                    closestCollidable = collidable;
                }
            }
        }

        // Return null if no collision is found, otherwise return the closest collision information
        return closestPoint == null ? null : new CollisionInfo(closestPoint, closestCollidable);
    }
}
