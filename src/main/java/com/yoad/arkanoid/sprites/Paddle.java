package com.yoad.arkanoid.sprites;

import com.yoad.arkanoid.game.ArkanoidGame;
import com.yoad.arkanoid.geometry.Point;
import com.yoad.arkanoid.geometry.Rectangle;
import com.yoad.arkanoid.geometry.Velocity;
import com.yoad.arkanoid.physics.Collidable;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import static com.yoad.arkanoid.game.Dimensions.*;

/**
 * The sprites.Paddle class represents a paddle in the game.
 * It implements both the sprites.Sprite and physics.Collidable interfaces.
 * The paddle can move left and right based on keyboard input,
 * and it can interact with the ball when a collision occurs.
 * The paddle is a rectangular object, and its behavior is dependent on the region where the ball hits it.
 */
public class Paddle implements Sprite, Collidable {

    private Rectangle rectangle;

    // Input flags (fed each frame by ArkanoidGame)
    private boolean leftPressed = false;
    private boolean rightPressed = false;

    // Movement tuning
    private static final int SPEED = sx(6);;

    /**
     * Constructs a new sprites.Paddle object with a specified rectangle.
     * @param rectangle The rectangle that represents the paddle's shape and size.
     * @param keyboard
     */
    public Paddle(Rectangle rectangle) { this.rectangle = rectangle; }

    /** Let the game feed input state each frame (e.g., from JavaFX key handlers). */
    public void setInput(boolean left, boolean right) {
        this.leftPressed = left;
        this.rightPressed = right;
    }

    /**
     * Moves the paddle to the left by 5 units.
     */
    public void moveLeft() {
        rectangle.move(-SPEED);
        if (rectangle.getStartX() <= -sx(50)) {
            rectangle.move(sx(862)); // 862 * 1.5 preserves prior wrap behavior
        }
    }

    /**
     * Moves the paddle to the right by 5 units.
     */
    public void moveRight() {
        rectangle.move(SPEED);
        if (rectangle.getStartX() >= sx(760)) {
            rectangle.move(-sx(862));
        }
    }

    /**
     * This method is called when time has passed. It checks for key presses
     * and moves the paddle left or right accordingly.
     */
    @Override
    public void timePassed() {
        if (leftPressed)  moveLeft();
        if (rightPressed) moveRight();
    }

    /**
     * Draws the paddle on the given draw surface.
     * @param surface The surface to draw on.
     */
    @Override
    public void draw(GraphicsContext g) {
        Rectangle r = this.getCollisionRectangle();
        g.setFill(Color.WHITE);
        g.fillRect(r.getStartX(), r.getStartY(), r.getWidth(), r.getHeight());
        g.setStroke(Color.BLACK);
        g.strokeRect(r.getStartX() - 1, r.getStartY() - 1, r.getWidth() + 1, r.getHeight() + 2);
    }

    /**
     * Returns the rectangle that defines the collision boundaries of the paddle.
     * @return The rectangle representing the paddle's collision boundaries.
     */
    @Override
    public Rectangle getCollisionRectangle() { return rectangle; }

    /**
     * Handles the collision of the ball with the paddle. The ball's velocity changes
     * based on the region of the paddle where the collision occurs.
     * The paddle is divided into five regions, and the ball's velocity is altered based
     * on which region it hits. The velocity of the ball is reflected or modified accordingly.
     * @param collisionPoint The point where the ball collided with the paddle.
     * @param currentVelocity The current velocity of the ball before the collision.
     * @return The new velocity of the ball after the collision.
     */
    @Override
    public Velocity hit(Ball hitter, Point collisionPoint, Velocity currentVelocity) {
        double currentDx = currentVelocity.getDx();
        double currentDy = currentVelocity.getDy();
        double speed = Math.sqrt(currentDx * currentDx + currentDy * currentDy);

        int paddleWidth   =               rectangle.getWidth();
        int paddleLeftX   =              rectangle.getStartX();
        int paddleTopY   =               rectangle.getStartY();
        int paddleBottomY = paddleTopY + rectangle.getHeight();

        // If collision is from BELOW the paddle (should not happen normally)
        if (collisionPoint.getY() > paddleBottomY) {
            return new Velocity(currentDx, Math.abs(currentDy)); // Force downward bounce
        }

        // If collision is from ABOVE (normal case)
        if (collisionPoint.getY() < paddleTopY) {
            // Divide paddle into 5 regions for better edge control
            double regionSize = paddleWidth / 5.0;

            if (collisionPoint.getX() <= paddleLeftX + regionSize) {
                return Velocity.fromAngleAndSpeed(300, speed); // Far left: sharp angle
            } else if (collisionPoint.getX() <= paddleLeftX + 2 * regionSize) {
                return Velocity.fromAngleAndSpeed(330, speed); // Left-middle: moderate angle
            } else if (collisionPoint.getX() <= paddleLeftX + 3 * regionSize) {
                return new Velocity(currentDx, -currentDy); // Center: straight up
            } else if (collisionPoint.getX() <= paddleLeftX + 4 * regionSize) {
                return Velocity.fromAngleAndSpeed(30, speed); // Right-middle: moderate angle
            } else {
                return Velocity.fromAngleAndSpeed(60, speed); // Far right: sharp angle
            }
        }

        // If collision is from the SIDE (e.g., paddle moving into the ball)
        if ((collisionPoint.getX() <= paddleLeftX || collisionPoint.getX() >= paddleLeftX + paddleWidth)
                && (collisionPoint.getY() >= paddleTopY && collisionPoint.getY() <= paddleBottomY)) {
            return new Velocity(-currentDx, currentDy); // True side hit
        }

        // Default: bounce upward (shouldn't normally reach here)
        return new Velocity(currentDx, -currentDy);
    }

    /**
     * Adds this paddle to the given game by adding it to both the sprite collection
     * and the collidable collection.
     * @param g The game to add this paddle to.
     */
    public void addToGame(ArkanoidGame g) {
        g.addSprite(this    );
        g.addCollidable(this);
    }
}
