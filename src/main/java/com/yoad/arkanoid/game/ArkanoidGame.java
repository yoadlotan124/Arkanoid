package com.yoad.arkanoid.game;

import com.yoad.arkanoid.events.BallRemover;
import com.yoad.arkanoid.events.BlockRemover;
import com.yoad.arkanoid.events.Counter;
import com.yoad.arkanoid.events.ScoreTrackingListener;
import com.yoad.arkanoid.geometry.Point;
import com.yoad.arkanoid.geometry.Rectangle;
import com.yoad.arkanoid.physics.Collidable;
import com.yoad.arkanoid.sprites.Ball;
import com.yoad.arkanoid.sprites.Paddle;
import com.yoad.arkanoid.sprites.ScoreHUD;
import com.yoad.arkanoid.sprites.Sprite;
import com.yoad.arkanoid.sprites.SpriteCollection;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.input.KeyCode;

import java.util.Objects;

import static com.yoad.arkanoid.game.Dimensions.*;

/**
 * The game.ArkanoidGame class is responsible for managing the game logic, including the creation and update of sprites,
 * handling game environment interactions, and managing the game loop for rendering and timing.
 */
public class ArkanoidGame {

    private final SpriteCollection sprites = new SpriteCollection();
    private final World environment = new World();

    // Primary controllable entities
    private Paddle paddle;

    // Input (fed from JavaFX Scene events)
    private volatile boolean keyLeft  = false;
    private volatile boolean keyRight = false;
    private volatile boolean keySpace = false; // kept for future use
    private volatile boolean keyEsc   = false; // kept for future use
    private volatile boolean keyEnter = false; // kept for future/menu use

    // Game accounting
    private final Counter blockCounter = new Counter();
    private final Counter ballCounter  = new Counter();
    private final Counter score        = new Counter();

    private final BlockRemover blockRemover = new BlockRemover(this, blockCounter, score);
    private final BallRemover  ballRemover  = new BallRemover(this, ballCounter);
    private final ScoreTrackingListener scoreTracker = new ScoreTrackingListener(score);

    // End state
    private boolean finished = false;
    private String endMessage = "";

    /**
     * Initializes the game by setting up the graphical user interface (GUI),
     * creating the ball and blocks (as sprites and collidables), and adding them to the game.
     * A simple grid of blocks is created for the game, and the ball is set up with an initial position and velocity.
     */
    public void initialize() {
        // HUD
        new ScoreHUD(score).addToGame(this);

        // Paddle
        Rectangle r = new Rectangle(sx(357), sx(576), sx(94), sx(12));
        paddle = new Paddle(r);
        paddle.addToGame(this);

        // Balls
        int radius = sx(6);
        Ball ball1 = new Ball(new Point(sx(200), sx(400)), radius, java.awt.Color.WHITE, this.environment);
        ball1.setVelocity(sd(3), sd(-3));
        ball1.addToGame(this);

        Ball ball2 = new Ball(new Point(sx(400), sx(420)), radius, java.awt.Color.WHITE, this.environment);
        ball2.setVelocity(sd(-3), sd(-3));
        ball2.addToGame(this);

        Ball ball3 = new Ball(new Point(sx(600), sx(410)), radius, java.awt.Color.WHITE, this.environment);
        ball3.setVelocity(sd(3), sd(-3));
        ball3.addToGame(this);

        ballCounter.increase(3);

        // Bricks (grid)
        int blockWidth = sx(49);
        int blockHeight = sx(23);
        int rows = 6;
        int cols = 12;

        java.awt.Color[] colors = {
            java.awt.Color.MAGENTA, java.awt.Color.RED, java.awt.Color.YELLOW,
            java.awt.Color.CYAN,    java.awt.Color.PINK, java.awt.Color.GREEN
        };

        for (int row = 0; row < rows; row++) {
            java.awt.Color currentColor = colors[row % colors.length];
            for (int col = 0; col < cols; col++) {
                int x = sx(723) - col * sx(49);
                int y = sx(150) + row * sx(23);
                Brick block = new Brick(new Rectangle(new Point(x, y), blockWidth, blockHeight), currentColor);
                block.addToGame(this);
                blockCounter.increase(1);
                block.addHitListener(blockRemover);
                block.addHitListener(scoreTracker);
            }
            cols--; // shrinking row
        }

        // Walls (top/left/right) + bottom (death)
        Brick topWall = new Brick(new Rectangle(new Point(0, 0), WIDTH, sx(28)), java.awt.Color.GRAY);
        topWall.addToGame(this);
        Brick leftWall = new Brick(new Rectangle(new Point(0, sx(28)), sx(28), HEIGHT - sx(28)), java.awt.Color.GRAY);
        leftWall.addToGame(this);
        Brick rightWall = new Brick(new Rectangle(new Point(WIDTH - sx(28), sx(28)), sx(28), HEIGHT - sx(28)), java.awt.Color.GRAY);
        rightWall.addToGame(this);
        Brick bottomWall = new Brick(new Rectangle(new Point(sx(28), HEIGHT), WIDTH - sx(56), sx(28)), java.awt.Color.GRAY);
        bottomWall.addHitListener(ballRemover);
        bottomWall.addToGame(this);
    }

    // ---------------- Input from JavaFX ----------------

    /** Called by FxLauncher: scene.setOnKeyPressed/Released → onKeyChanged(code, down) */
    public void onKeyChanged(KeyCode code, boolean down) {
        if (code == null) return;
        switch (code) {
            case LEFT, A   -> keyLeft  = down;
            case RIGHT, D  -> keyRight = down;
            case SPACE     -> keySpace = down;
            case ESCAPE    -> keyEsc   = down;
            case ENTER     -> keyEnter = down;
            default -> { /* ignore other keys */ }
        }
    }

    // ---------------- Per-frame update & render ----------------

    /**
     * Called every frame by the FX AnimationTimer.
     * @param g  GraphicsContext for drawing this frame
     * @param dt Seconds since last frame (you can use it to scale movement if you wish)
     */
    public void tick(GraphicsContext g, double dt) {
        if (finished) {
            drawBackground(g);
            drawEndOverlay(g);
            return;
        }

        // Feed input to the paddle before updating sprites
        if (paddle != null) {
            paddle.setInput(keyLeft, keyRight);
        }

        // UPDATE
        sprites.notifyAllTimePassed(); // your existing per-tick updates

        // Check end conditions
        if (blockCounter.getValue() <= 0) {
            finished = true;
            endMessage = "You Win!\nScore: " + score.getValue();
        } else if (ballCounter.getValue() <= 0) {
            finished = true;
            endMessage = "Game Over\nScore: " + score.getValue();
        }

        // DRAW
        drawBackground(g);
        sprites.drawAll(g);

        // (Optional) overlay debug text or HUD extensions here
    }

    private void drawBackground(GraphicsContext g) {
        g.setFill(Color.BLUE);
        g.fillRect(0, 0, WIDTH, HEIGHT);
    }

    private void drawEndOverlay(GraphicsContext g) {
        g.setFill(Color.color(0, 0, 0, 0.55));
        g.fillRect(0, 0, WIDTH, HEIGHT);

        g.setFill(Color.WHITE);
        g.setFont(Font.font(28));
        String msg = Objects.requireNonNullElse(endMessage, "");
        double y = HEIGHT / 2.0 - 10;
        for (String line : msg.split("\\n")) {
            double w = textWidth(g, line);
            g.fillText(line, (WIDTH - w) / 2.0, y);
            y += 36;
        }
        g.setFont(Font.font(sx(18)));
        g.fillText("Press ENTER to restart", WIDTH / 2.0 - 110, y + 10);

        // Simple restart on ENTER (re-init world)
        if (keyEnter) {
            restart();
        }
    }

    private void restart() {
        // Clear collections
        sprites.getSprites().clear();
        environment.getCollidables().clear();
        finished = false;
        endMessage = "";

        // Reset counters
        blockCounter.decrease(blockCounter.getValue());
        ballCounter.decrease(ballCounter.getValue());
        score.decrease(score.getValue());

        // Rebuild world
        initialize();
    }

    // ---------------- Game interface (used by listeners & sprites) ----------------

    /**
     * Adds a collidable object to the game environment.
     *
     * @param c the collidable object to add
     */
    public void addCollidable(Collidable c) {
        environment.addCollidable(c);
    }

    /**
     * Adds a sprite to the game. Sprites are drawn and updated during the game loop.
     *
     * @param s the sprite to add
     */
    public void addSprite(Sprite s) {
        sprites.addSprite(s);
    }

    /**
     * removes block from the list.
     * @param c removes c
     */
    public void removeCollidable(Collidable c) {
        this.environment.getCollidables().remove(c);
    }

    /**
     * removes sprite from the list.
     * @param s removes s
     */
    public void removeSprite(Sprite s) {
        this.sprites.removeSprite(s);
    }

    // ---------------- Helpers for FxLauncher ----------------

    public boolean isFinished() { 
        return finished; 
    }

    public String  getEndMessage() { 
        return endMessage; 
    }

    public Counter getScoreCounter() {
         return score; 
    }

    public Counter getBlockCounter() { 
        return blockCounter; 
    }

    public Counter getBallCounter() { 
        return ballCounter; 
    }

    private static double textWidth(GraphicsContext g, String s) {
    Text t = new Text(s);
    t.setFont(g.getFont());
    return t.getLayoutBounds().getWidth();
}
}
