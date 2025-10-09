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
import javafx.scene.control.MenuButton;
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

    // Pause state
    private boolean paused = false;

    // Pause menu buttons
    private MenuButton btnResume, btnRestart, btnQuit;
    private double mouseX = -1, mouseY = -1; // for hover

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

    
    //Button helper class
    private static final class MenuButton {
        double x, y, w, h;
        String label;
        boolean hovered = false;

        MenuButton(String label, double x, double y, double w, double h) {
            this.label = label; this.x = x; this.y = y; this.w = w; this.h = h;
        }

        boolean contains(double px, double py) {
            return px >= x && px <= x + w && py >= y && py <= y + h;
        }
    }

    /**
     * creates pause buttons during menu pullup
     */
    private void createPauseButtons() {
        double panelW = sx(360);
        double panelH = sx(260);
        double panelX = (WIDTH - panelW) / 2.0;
        double panelY = (HEIGHT - panelH) / 2.0;

        double btnW = panelW - sx(60);
        double btnH = sx(44);
        double x = panelX + sx(30);
        double y0 = panelY + sx(70);
        double gap = sx(14);

        btnResume = new MenuButton("Resume", x, y0, btnW, btnH);
        btnRestart = new MenuButton("Restart", x, y0 + btnH + gap, btnW, btnH);
        btnQuit = new MenuButton("Quit", x, y0 + 2*(btnH+gap),btnW, btnH);
    }

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

        createPauseButtons();
    }

    // ---------------- Input from JavaFX ----------------

    /** Called by FxLauncher: scene.setOnKeyPressed/Released → onKeyChanged(code, down) */
    public void onKeyChanged(KeyCode code, boolean down) {
        if (code == null) return;
        switch (code) {
            case LEFT, A -> keyLeft = down;
            case RIGHT, D -> keyRight = down;
            case SPACE -> keySpace = down;
            case ENTER -> keyEnter = down;
            case ESCAPE -> { if (down) paused = !paused; }
            default -> { /* ignore other keys */ }
        }
    }

    public void onMouseMoved(double x, double y) {
        mouseX = x; mouseY = y;
        if (!paused) return;
        // Update hover states
        btnResume.hovered = btnResume.contains(x, y);
        btnRestart.hovered = btnRestart.contains(x, y);
        btnQuit.hovered = btnQuit.contains(x, y);
    }

    public void onMouseClicked(double x, double y) {
        if (!paused) return;
        if (btnResume.contains(x, y)) { paused = false; return; }
        if (btnRestart.contains(x, y)) { restart(); paused = false; return; }
        if (btnQuit.contains(x, y)) { finished = true; endMessage = "Quit\nScore: " + score.getValue(); paused = false; }
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

        // feed input to paddle each frame
        if (paddle != null) {
            // when paused, disable movement
            paddle.setInput(!paused && keyLeft, !paused && keyRight);
        }

        // UPDATE only if not paused
        if (!paused) {
            sprites.notifyAllTimePassed();
            if (blockCounter.getValue() <= 0) {
                finished = true;
                endMessage = "You Win!\nScore: " + score.getValue();
            } else if (ballCounter.getValue() <= 0) {
                finished = true;
                endMessage = "Game Over\nScore: " + score.getValue();
            }
        }

        // DRAW
        drawBackground(g);
        sprites.drawAll(g);

        // overlay if paused
        if (paused) drawPauseOverlay(g);
    }

    /**
     * Background creator function
     * @param g graphic content on which to draw the background
     */
    private void drawBackground(GraphicsContext g) {
        // simple vertical gradient
        var grad = new javafx.scene.paint.LinearGradient(
            0, 0, 0, 1, true,
            javafx.scene.paint.CycleMethod.NO_CYCLE,
            new javafx.scene.paint.Stop(0, Color.web("#0f172a")), // slate-900
            new javafx.scene.paint.Stop(1, Color.web("#1e293b"))  // slate-800
        );
        g.setFill(grad);
        g.fillRect(0, 0, WIDTH, HEIGHT);
    }

    /**
     * End of game overlay creator
     * @param g graphic content on which to draw the overlay
     */
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

    /**
     * End of game - loss/win, optional restart
     */
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

    /**
     * Pause overlay creation function
     * @param g graphics content on which to draw the pause overlay
     */
    private void drawPauseOverlay(GraphicsContext g) {
        // dim the world
        g.setFill(Color.color(0, 0, 0, 0.45));
        g.fillRect(0, 0, WIDTH, HEIGHT);

        double panelW = sx(360);
        double panelH = sx(260);
        double x = (WIDTH - panelW) / 2.0;
        double y = (HEIGHT - panelH) / 2.0;
        double r = sx(16);

        // panel
        g.setFill(Color.color(0.13, 0.16, 0.22, 0.92)); // bluish glass
        fillRoundRect(g, x, y, panelW, panelH, r);
        g.setStroke(Color.color(1, 1, 1, 0.12));
        g.setLineWidth(1.5);
        strokeRoundRect(g, x, y, panelW, panelH, r);

        // title
        g.setFill(Color.WHITE);
        g.setFont(Font.font(22 * SCALE));
        drawCentered(g, "Paused", WIDTH / 2.0, y + sx(40));

        // buttons
        drawButton(g, btnResume,  Color.web("#22c55e"), Color.web("#16a34a")); // green
        drawButton(g, btnRestart, Color.web("#60a5fa"), Color.web("#3b82f6")); // blue
        drawButton(g, btnQuit,    Color.web("#f97316"), Color.web("#ea580c")); // orange
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

    private void drawButton(GraphicsContext g, MenuButton b, Color base, Color hover) {
        double r = sx(10);
        Color fill = b.hovered ? hover : base;
        g.setFill(fill);
        fillRoundRect(g, b.x, b.y, b.w, b.h, r);
        g.setStroke(Color.color(1, 1, 1, 0.15));
        g.setLineWidth(1.0);
        strokeRoundRect(g, b.x, b.y, b.w, b.h, r);

        g.setFill(Color.WHITE);
        g.setFont(Font.font(18 * SCALE));
        drawCentered(g, b.label, b.x + b.w / 2.0, b.y + b.h / 2.0 + sx(6));
    }

    private void fillRoundRect(GraphicsContext g, double x, double y, double w, double h, double arc) {
        g.fillRoundRect(x, y, w, h, arc, arc);
    }
    private void strokeRoundRect(GraphicsContext g, double x, double y, double w, double h, double arc) {
        g.strokeRoundRect(x, y, w, h, arc, arc);
    }

    private void drawCentered(GraphicsContext g, String text, double cx, double cy) {
        var t = new javafx.scene.text.Text(text);
        t.setFont(g.getFont());
        double w = t.getLayoutBounds().getWidth();
        g.fillText(text, cx - w / 2.0, cy);
    }

}
