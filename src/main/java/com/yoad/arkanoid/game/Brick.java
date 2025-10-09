package com.yoad.arkanoid.game;
import com.yoad.arkanoid.sprites.Ball;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import com.yoad.arkanoid.audio.Sounds;
import com.yoad.arkanoid.events.HitListener;
import com.yoad.arkanoid.events.HitNotifier;
import com.yoad.arkanoid.geometry.Point;
import com.yoad.arkanoid.geometry.Rectangle;
import com.yoad.arkanoid.geometry.Velocity;
import com.yoad.arkanoid.physics.Collidable;
import com.yoad.arkanoid.sprites.Sprite;
import com.yoad.arkanoid.fx.FxColors;

/**
 * represents a class for our game blocks that will be rectangles.
 */
public class Brick implements Collidable, Sprite, HitNotifier {
    //Fields
    private final Rectangle rect;
    private final Color color;
    private List<HitListener> hitListeners;

    private long flashUntilNs = 0L;

    /**
     * constructor for the class.
     * @param rect represents our block
     * @param color color to be set
     */
    public Brick(Rectangle rect, Color color) {
        this.rect = rect;
        this.color = color;
        this.hitListeners = new ArrayList<>();
    }

    /**
     * notifies all listeners.
     * @param hitter the ball.
     */
    private void notifyHit(Ball hitter) {
        List<HitListener> listeners = new ArrayList<>(this.hitListeners); // copy the list
        for (HitListener hl : listeners) {
            hl.hitEvent(this, hitter);
        }
    }

    /**
     * Draws the block.
     * The block is filled with its specified color and positioned according to its rectangle.
     *
     * @param g the Surface on which the block will be drawn
     */
    @Override
    public void draw(javafx.scene.canvas.GraphicsContext g) {
        var r = this.getCollisionRectangle();
        double x = r.getStartX(), y = r.getStartY(), w = r.getWidth(), h = r.getHeight();

        // Corner radius (nice & rounded)
        double rad = Math.min(w, h) * 0.32;

        // Convert AWT -> FX
        javafx.scene.paint.Color base = javafx.scene.paint.Color.rgb(
            color.getRed(), color.getGreen(), color.getBlue()
        );

        // Optional: don't flash the gray walls
        boolean isWall = (color.equals(java.awt.Color.GRAY));

        // Hit flash — brighten briefly
        double bright = (!isWall && System.nanoTime() < flashUntilNs) ? 1.25 : 1.0;
        javafx.scene.paint.Color body = javafx.scene.paint.Color.color(
            clamp01(base.getRed()   * bright),
            clamp01(base.getGreen() * bright),
            clamp01(base.getBlue()  * bright)
        );

        // Fill
        g.setFill(body);
        g.fillRoundRect(x, y, w, h, rad, rad);

        // Soft top-left highlight
        g.setStroke(body.brighter().brighter());
        g.setLineWidth(1.2);
        g.strokeLine(x + 3, y + 3, x + w * 0.65, y + 3);
        g.strokeLine(x + 3, y + 3, x + 3,        y + h * 0.65);

        // Subtle border
        g.setStroke(javafx.scene.paint.Color.color(0, 0, 0, 0.35));
        g.setLineWidth(1.0);
        g.strokeRoundRect(x + 0.5, y + 0.5, w - 1, h - 1, rad, rad);
    }

    /**
     * @return the rectangle of this block
     */
    @Override
    public Rectangle getCollisionRectangle() {
        return this.rect;
    }

    /**
     * Notify the object that we collided with at collisionPoint with a given velocity.
     * @param collisionPoint the point of collision.
     * @param currentVelocity our current velocity.
     * @return the new geometry.Velocity after colliding.
     */
    @Override
    public Velocity hit(Ball hitter, Point collisionPoint, Velocity currentVelocity) {

        flashUntilNs = System.nanoTime() + (long) (120e6);

        double dx = currentVelocity.getDx();
        double dy = currentVelocity.getDy();

        if (!ballColorMatch(hitter)) {
            this.notifyHit(hitter);
        }

        // Get rectangle boundaries with a small buffer to catch edge cases
        double left = rect.getStartX() - 0.6;
        double right = rect.getStartX() + rect.getWidth() + 0.6;
        double top = rect.getStartY() - 0.6;
        double bottom = rect.getStartY() + rect.getHeight() + 0.6;

        // Determine which side was hit (with priority to corners)
        boolean hitLeft = Math.abs(collisionPoint.getX() - left) < 0.8;
        boolean hitRight = Math.abs(collisionPoint.getX() - right) < 0.8;
        boolean hitTop = Math.abs(collisionPoint.getY() - top) < 0.8;
        boolean hitBottom = Math.abs(collisionPoint.getY() - bottom) < 0.8;

        // Corner collision (reverse both velocities)
        if ((hitLeft || hitRight) && (hitTop || hitBottom)) {
            return new Velocity(-dx, -dy);
        } else if (hitLeft || hitRight) {
            return new Velocity(-dx, dy);
        } else if (hitTop || hitBottom) {
            return new Velocity(dx, -dy);
        }

        // Emergency fallback - if somehow no edge detected but we're in collision
        // This prevents sticking by forcing a bounce
        if (rect.contains(collisionPoint)) {
            return new Velocity(-dx, -dy); // Reverse both directions aggressively
        }

        return currentVelocity;
    }

    /**
     * Adds this sprite to the specified game and registers it as a collidable object.
     * The sprite will be managed by the game and be part of the game's sprite collection for rendering,
     * and it will also be included in the game's collidable collection for collision detection.
     *
     * @param g the game to which this sprite will be added.
     *          This game will manage the sprite by adding it to both the sprite collection
     *          (for drawing and updating) and the collidable collection (for collision detection).
     */
    public void addToGame(ArkanoidGame g) {
        g.addSprite(this);
        g.addCollidable(this);
    }

    /**
     * not done yet.
     */
    public void timePassed() {
    }

    /**
     * removes this from the game.
     * @param g the game.
     */
    public void removeFromGame(ArkanoidGame g) {
        g.removeCollidable(this);
        g.removeSprite(this);
    }

    /**
     * compares block and balls color.
     * @param ball game ball.
     * @return true if same color, false otherwise.
     */
    public boolean ballColorMatch(Ball ball) {
        return this.color.equals(ball.getColor());
    }

    @Override
    public void addHitListener(HitListener hl) {
        this.hitListeners.add(hl);
    }

    @Override
    public void removeHitListener(HitListener hl) {
        this.hitListeners.remove(hl);
    }

    /**
     * returns the blocks color.
     * @return color.
     */
    public Color getColor() {
        return color;
    }

    // helper
    private static double clamp01(double v) { return v < 0 ? 0 : (v > 1 ? 1 : v); }
}
