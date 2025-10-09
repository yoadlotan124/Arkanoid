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

public class Ball implements Sprite {
    //---------- Fields ----------

    private Point center;
    private final int radius;
    private Color color;
    private Velocity velocity;
    private World environment;

    private final Deque<Point2D> trail = new ArrayDeque<>(12);
    private static final int TRAIL_LEN = 8;
    private static final double TRAIL_ALPHA = 0.18;

    //---------- Constructor & Getters/Setters ----------

    public Ball(Point center, int r, Color color, World environment) {
        this.center = center;
        this.radius = r;
        this.color = (color != null ? color : Color.WHITE);
        this.velocity = new Velocity(0, 0);
        this.environment = environment;
    }

    public int getX() { return (int) this.center.getX(); }
    public int getY() { return (int) this.center.getY(); }
    public int getSize() { return this.radius; }
    public Color getColor() { return this.color; }
    public Velocity getVelocity() { return new Velocity(this.velocity.getDx(), this.velocity.getDy()); }

    public void setVelocity(Velocity v) { this.velocity = v; }
    public void setVelocity(double dx, double dy) { this.velocity = new Velocity(dx, dy); }
    public void setEnvironment(World env) { this.environment = env; }
    public void setColor(Color color) { if (color != null) this.color = color; }

    public void addToGame(ArkanoidGame g) { g.addSprite(this); }
    public void removeFromGame(ArkanoidGame g) { g.removeSprite(this); }

    //---------- Ball Logic ----------

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
     */
    public Line getTrajectory() {
        Point start = this.center;
        Point end = new Point(center.getX() + velocity.getDx() * 3, center.getY() + velocity.getDy() * 3);
        return new Line(start, end);
    }

    /**
     * Moves the ball one step forward based on its current velocity.
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

            // Update velocity (direction)
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

    //---------- Graphics ----------

    @Override
    public void draw(GraphicsContext g) {
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
        g.setFill(this.color);
        g.fillOval(this.center.getX() - this.radius, this.center.getY() - this.radius, d, d);
    }

    private void pushTrail() {
        trail.addFirst(new Point2D(getX(), getY()));
        while (trail.size() > TRAIL_LEN) trail.removeLast();
    }
}
