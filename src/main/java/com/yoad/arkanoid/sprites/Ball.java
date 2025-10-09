package com.yoad.arkanoid.sprites;

import com.yoad.arkanoid.game.ArkanoidGame;
import com.yoad.arkanoid.game.World;
import com.yoad.arkanoid.geometry.Line;
import com.yoad.arkanoid.geometry.Point;
import com.yoad.arkanoid.geometry.Rectangle;
import com.yoad.arkanoid.geometry.Velocity;
import com.yoad.arkanoid.physics.CollisionInfo;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Random;

/**
 * The sprites.Ball class represents a ball with a center point, radius, and color.
 * It provides methods to access its properties and draw it on a given surface.
 */
public class Ball implements Sprite {
    // Fields
    private Point center;
    private final int radius;
    private Color fxColor;
    private Velocity velocity;
    private World environment;

    private final Deque<Point2D> trail = new ArrayDeque<>(12);
    private static final int TRAIL_LEN = 8;
    private static final double TRAIL_ALPHA = 0.18;

    /**
     * Constructs a sprites.Ball object with a specified center point, radius, and color.
     *
     * @param center the center point of the ball
     * @param r      the radius of the ball
     * @param fxColor  the color of the ball
     * @param environment the balls environment
     */
    public Ball(Point center, int r, Color fxColor, World environment) {
        this.center = center;
        this.radius = r;
        this.fxColor = (fxColor != null ? fxColor : Color.WHITE);
        this.velocity = new Velocity(0, 0);
        this.environment = environment;
    }

    /**
     * Returns the x-coordinate of the ball's center.
     *
     * @return the x-coordinate of the center
     */
    public int getX() { return (int) this.center.getX(); }

    /**
     * Returns the y-coordinate of the ball's center.
     *
     * @return the y-coordinate of the center
     */
    public int getY() { return (int) this.center.getY(); }

    /**
     * Returns the radius (size) of the ball.
     *
     * @return the radius of the ball
     */
    public int getSize() { return this.radius; }

    /**
     * returns this balls color.
     * @return color.
     */
    public Color getFxColor() { return this.fxColor; }

    /**
     * Draws the ball on the specified Surface.
     *
     * @param g the Surface on which to draw the ball
     */
    @Override
    public void draw(GraphicsContext g) {
        // (optional) trail: comment this block out if you didn't add the trail
        pushTrail();
        if (!trail.isEmpty()) {
            Point2D[] pts = trail.toArray(new Point2D[0]);
            for (int i = 0; i < pts.length; i++) {
                double t = 1.0 - (i / (double) TRAIL_LEN);
                double alpha = TRAIL_ALPHA * t;
                double r = getSize() * (0.75 + 0.25 * t);
                g.setFill(Color.color(1, 1, 1, alpha));
                g.fillOval(pts[i].getX() - r, pts[i].getY() - r, r * 2, r * 2);
            }
        }

        // actual ball
        double d = this.radius * 2.0;
        g.setFill(this.fxColor);
        g.fillOval(this.center.getX() - this.radius, this.center.getY() - this.radius, d, d);
    }

    private void pushTrail() {
        trail.addFirst(new Point2D(getX(), getY()));
        while (trail.size() > TRAIL_LEN) trail.removeLast();
    }

    /**
     * Sets the velocity of the ball.
     *
     * @param v the velocity to be assigned to the ball
     */
    public void setVelocity(Velocity v) {
        this.velocity = v; 
    }

    /**
     * Sets the velocity of the ball using dx and dy values.
     *
     * @param dx the change in x direction
     * @param dy the change in y direction
     */
    public void setVelocity(double dx, double dy) {
        this.velocity = new Velocity(dx, dy); 
    }

    /**
     * sets the balls environment.
     * @param env
     */
    public void setEnvironment(World env) {
        this.environment = env;
    }

    /**
     * Adds this sprite to the specified game.
     * The sprite will be managed by the game and be part of the game's sprite collection,
     * allowing it to be drawn and updated during the game loop.
     *
     * @param g the game to which this sprite will be added.
     *          This game will manage the sprite and include it in its sprite collection.
     */
    public void addToGame(ArkanoidGame g) { g.addSprite(this); }

    /**
     * Returns the current velocity of the ball.
     *
     * @return a new geometry.Velocity object representing the ball's velocity
     */
    public Velocity getVelocity() { return new Velocity(this.velocity.getDx(), this.velocity.getDy()); }

    /**
     * Generates a random velocity based on the balls size.
     */
    public void randomVelocity() {
        Random rand = new Random();
        int baseSpeed = 7; // Base speed for smaller balls
        int radius = this.getSize();
        int speed = Math.max(3, baseSpeed - (radius / 10));

        if (radius > 50) {
            speed = 3; // Large balls move very slowly
        }

        double angle = rand.nextDouble() * 360; // Random direction
        if (Math.abs(Math.sin(Math.toRadians(angle))) < 0.1
                ||  Math.abs(Math.cos(Math.toRadians(angle))) < 0.1) {
            angle += 10;  // Avoid pure horizontal motion
        }
        Velocity v = Velocity.fromAngleAndSpeed(angle, speed);
        this.setVelocity(v);
    }

    /**
     * Calculates and returns the trajectory of the ball based on its current position and velocity.
     * The trajectory is represented as a line from the ball's current center to the point where it would
     * move next if no collisions occur.
     *
     * @return a shapes.Line representing the ball's next movement path based on its velocity
     */
    public Line getTrajectory() {
        Point start = this.center;
        Point end = new Point(center.getX() + velocity.getDx() * 3, center.getY() + velocity.getDy() * 3);
        return new Line(start, end);
    }

    /**
     * sets the balls color.
     * @param color to this color.
     */
    public void setFxColor(Color fx) { if (fx != null) this.fxColor = fx; }

    /**
     * Moves the ball one step forward based on its current velocity.
     * The method updates the ball's position by applying its velocity to the current position.
     * The new position is computed by calling the {@link Velocity#applyToPoint(Point)} method
     * on the current position.
     *
     * @see Velocity#applyToPoint(Point)
     */
    @Override
    public void timePassed() {
        Line trajectory = this.getTrajectory();
        CollisionInfo collisionInfo = environment.getClosestCollision(trajectory);

        if (collisionInfo == null) {
            this.center = this.getVelocity().applyToPoint(this.center);
        } else {
            Point collisionPoint = collisionInfo.collisionPoint();
            double dx = this.getVelocity().getDx();
            double dy = this.getVelocity().getDy();

            // Push-out distance (increased aggressively)
            double epsilon = 6.0; // Matches paddle's teleport distance

            // Push the ball out HARD along its trajectory
            double newX = collisionPoint.getX() - epsilon * Math.signum(dx);
            double newY = collisionPoint.getY() - epsilon * Math.signum(dy);
            this.center = new Point(newX, newY);

            // Update velocity (direction only, speed unchanged)
            Velocity newVelocity = collisionInfo.collisionObject().hit(this, collisionPoint, this.getVelocity());
            this.setVelocity(newVelocity);

            // Safety: If still stuck (rare), push again
            Rectangle paddleRect = collisionInfo.collisionObject().getCollisionRectangle();
            if (paddleRect.contains(this.center)) {
                epsilon += 4.0; // Extra-hard push
                newX = collisionPoint.getX() - epsilon * Math.signum(dx);
                newY = collisionPoint.getY() - epsilon;
                this.center = new Point(newX, newY);
            }
        }
    }

    /**
     * removes the ball from the game.
     * @param g the game.
     */
    public void removeFromGame(ArkanoidGame g) {
        g.removeSprite(this);
    }
}
