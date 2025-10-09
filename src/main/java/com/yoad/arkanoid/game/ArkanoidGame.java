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

import com.yoad.arkanoid.powerups.PowerUp;
import com.yoad.arkanoid.powerups.PowerUpType;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.yoad.arkanoid.ui.MenuButton;
import static com.yoad.arkanoid.game.Dimensions.*;

/**
 * The game.ArkanoidGame class is responsible for managing the game logic, including the creation and update of sprites,
 * handling game environment interactions, and managing the game loop for rendering and timing.
 */
public class ArkanoidGame {

    private SpriteCollection sprites = new SpriteCollection();
    private World environment = new World();
    private final GameConfig config;

    // Primary controllable entities
    private Paddle paddle;

    // Save original size and speed for reverting
    private int basePaddleWidth;
    private int basePaddleSpeed;

    // Input (fed from JavaFX Scene events)
    private volatile boolean keyLeft  = false;
    private volatile boolean keyRight = false;
    private volatile boolean keySpace = false; // kept for future use
    private volatile boolean keyEsc   = false; // kept for future use
    private volatile boolean keyEnter = false; // kept for future/menu use

    // Pause state
    private boolean paused = false;

    // Pause menu buttons
    private MenuButton btnResume, btnRestart, btnLobby;
    private boolean returnToMenuRequested = false;
    private double mouseX = -1, mouseY = -1; // for hover

    // Game accounting
    private Counter blockCounter = new Counter();
    private Counter ballCounter  = new Counter();
    private Counter score        = new Counter();

    private BlockRemover blockRemover = new BlockRemover(this, blockCounter, score);
    private BallRemover  ballRemover  = new BallRemover(this, ballCounter);
    private ScoreTrackingListener scoreTracker = new ScoreTrackingListener(score);

    // power-ups
    private final List<PowerUp> powerUps = new ArrayList<>();
    private final Map<PowerUpType, Long> effectExpiryNs = new EnumMap<>(PowerUpType.class);
    private final Random rng = new Random();

    // End state
    private boolean finished = false;
    private String endMessage = "";

    // constructor for game config (difficulty etc...)
    public ArkanoidGame() {
        this(new GameConfig());
    }
    public ArkanoidGame(GameConfig config) {
        this.config = config;
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

        btnResume  = new MenuButton("Resume",          x, y0,                  btnW, btnH);
        btnRestart = new MenuButton("Restart",         x, y0 + btnH + gap,     btnW, btnH);
        btnLobby   = new MenuButton("Return to Lobby", x, y0 + 2*(btnH+gap),   btnW, btnH);
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
        basePaddleWidth = paddle.getCollisionRectangle().getWidth();
        basePaddleSpeed = paddle.getSpeed();

        // Balls
        double s = config.ballSpeed();

        Ball ball = new Ball(new Point(sx(200), sx(400)), sx(6), java.awt.Color.WHITE, this.environment);
        ball.setVelocity( s, -s);
        ball.addToGame(this);

        ballCounter.increase(1);

        // Bricks (grid)
        int blockWidth  = sx(49);
        int blockHeight = sx(23);
        int rows = config.rows();
        int cols = config.cols();

        java.awt.Color[] colors = config.getTheme().palette();

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
        btnResume.hovered  = btnResume.contains(x, y);
        btnRestart.hovered = btnRestart.contains(x, y);
        btnLobby.hovered   = btnLobby.contains(x, y);
    }


    public void onMouseClicked(double x, double y) {
        if (!paused) return;
        if (btnResume.contains(x, y))  { paused = false; return; }
        if (btnRestart.contains(x, y)) { restart(); paused = false; return; }
        if (btnLobby.contains(x, y))   { returnToMenuRequested = true; paused = false; return; }
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

            // update falling power-ups
            Iterator<PowerUp> it = powerUps.iterator();
            while (it.hasNext()) {
                PowerUp p = it.next();
                p.update(dt);
                // off-screen bottom → drop
                if (p.y() > HEIGHT) { it.remove(); continue; }
                // caught by paddle?
                if (intersects(p, paddle.getCollisionRectangle())) {
                    applyPowerUp(p.type);
                    it.remove();
                }
            }

            // expire timed effects
            long now = System.nanoTime();
            Long exp = effectExpiryNs.get(PowerUpType.EXPAND_PADDLE);
            if (exp != null && now >= exp) {
                setPaddleWidth(basePaddleWidth);          // revert
                effectExpiryNs.remove(PowerUpType.EXPAND_PADDLE);
            }
            // speed expiry
            Long expS = effectExpiryNs.get(PowerUpType.PADDLE_SPEED);
            if (expS != null && now >= expS) {
                paddle.setSpeed(basePaddleSpeed);
                effectExpiryNs.remove(PowerUpType.PADDLE_SPEED);
            }
        }

        // DRAW
        drawBackground(g);
        sprites.drawAll(g);

        // draw power-ups on top
        for (PowerUp p : powerUps) {
            p.draw(g);
        }

        // draw timers (always visible, even when paused)
        if (paused) {
            drawPauseOverlay(g);      // your existing pause panel
            drawPowerupTimers(g);     // draw on top of overlay
        } else {
            drawPowerupTimers(g);
        }
    }

    /**
     * Background creator function
     * @param g graphic content on which to draw the background
     */
    private void drawBackground(GraphicsContext g) {
        var top = config.getTheme().bgTop();
        var bot = config.getTheme().bgBottom();
        var grad = new javafx.scene.paint.LinearGradient(
            0, 0, 0, 1, true,
            javafx.scene.paint.CycleMethod.NO_CYCLE,
            new javafx.scene.paint.Stop(0, top),
            new javafx.scene.paint.Stop(1, bot)
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
    public void restart() {
        // clear runtime flags
        paused = false;
        finished = false;
        endMessage = null;

        // clear power-ups and timers
        powerUps.clear();
        effectExpiryNs.clear();

        // rebuild core containers
        sprites = new SpriteCollection();
        environment = new World();

        // reset counters + listeners
        blockCounter = new Counter();
        ballCounter  = new Counter();
        score        = new Counter();
        blockRemover = new BlockRemover(this, blockCounter, score);
        ballRemover  = new BallRemover(this, ballCounter);
        scoreTracker = new ScoreTrackingListener(score);

        // re-create level entities (HUD, paddle, balls, bricks…)
        initialize(); // this should add fresh HUD/paddle/balls/blocks
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
        drawButton(g, btnLobby,    Color.web("#f97316"), Color.web("#ea580c")); // orange
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

    private java.util.List<Ball> getBalls() {
        java.util.ArrayList<Ball> list = new java.util.ArrayList<>();
        for (Sprite s : this.sprites.getSprites()) {
            if (s instanceof Ball b) list.add(b);
        }
        return list;
    }


    // ------ Helpers for Graphics ------
    private static double textWidth(GraphicsContext g, String s) {
    Text t = new Text(s);
    t.setFont(g.getFont());
    return t.getLayoutBounds().getWidth();
    }

    private void drawButton(GraphicsContext g, MenuButton b, Color base, Color hover) {
    // Pause menu uses slightly smaller font + radius than main menu
    b.draw(g, base, hover, 18 * SCALE, sx(10));
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

    // ------ Helpers for Menu purposes ------

    /**
     * returns to the main menu once requested
     * @return true or false depending on if pressed
     */
    public boolean consumeReturnToMenuRequest() {
        boolean r = returnToMenuRequested;
        returnToMenuRequested = false;
        return r;
    }

    // ------ Helpers for PowerUps ------

    public void spawnPowerUp(double centerX, double centerY) {
        int half = sx(12);
        powerUps.add(new PowerUp(PowerUpType.EXPAND_PADDLE, centerX - half, centerY - half));
    }

    public void spawnPowerUp(double centerX, double centerY, PowerUpType type) {
        int half = sx(12);
        powerUps.add(new PowerUp(type, centerX - half, centerY - half));
    }

    private boolean intersects(PowerUp p, com.yoad.arkanoid.geometry.Rectangle r) {
        int rx = r.getStartX(), ry = r.getStartY(), rw = r.getWidth(), rh = r.getHeight();
        double px = p.x(), py = p.y(); int pw = p.w(), ph = p.h();
        return px < rx + rw && px + pw > rx && py < ry + rh && py + ph > ry;
    }

    private void applyPowerUp(PowerUpType type) {
    switch (type) {
        case EXPAND_PADDLE -> {
            setPaddleWidth((int)Math.round(basePaddleWidth * 1.5));
            effectExpiryNs.put(PowerUpType.EXPAND_PADDLE, System.nanoTime() + (long)(12e9)); // 12s
        }
        case PADDLE_SPEED -> {
            paddle.setSpeed((int)Math.round(basePaddleSpeed * 1.5));
            effectExpiryNs.put(PowerUpType.PADDLE_SPEED, System.nanoTime() + (long)(10e9)); // 10s
        }
        case MULTI_BALL -> {
            var balls = getBalls();
            int cap = 8; // total balls max
            int canAdd = Math.max(0, cap - balls.size());
            if (canAdd <= 0) return;

            double s = config.ballSpeed(); // same as your first ball
            int spawned = 0;

            for (Ball b : balls) {
                if (spawned >= canAdd) break;

                // decide vertical sign: keep current up/down, or force up (-1.0)
                double signY = (b.getVelocity().getDy() < 0 ? -1.0 : 1.0);

                // spawn up to 2 clones per source ball: one to the right, one to the left
                int clonesForThis = Math.min(2, canAdd - spawned);
                for (int i = 0; i < clonesForThis; i++) {
                    double dx = (i == 0 ? +s : -s);
                    double dy = signY * s;

                    // nudge start so it doesn’t overlap the source ball
                    double offset = b.getSize() + sx(2);
                    double ox = (i == 0 ? +offset : -offset);
                    double oy = -offset;

                    Ball nb = new Ball(
                        new Point(b.getX() + ox, b.getY() + oy),
                        b.getSize(),
                        b.getColor(),
                        this.environment
                    );
                    nb.setVelocity(dx, dy);     // EXACTLY like your initial spawn (±s, -s)
                    nb.addToGame(this);

                    ballCounter.increase(1);
                    spawned++;
                    if (spawned >= canAdd) break;
                }
            }
        }
    }
}


    private void setPaddleWidth(int newWidth) {
        // delegate to Paddle
        paddle.scaleWidthTo(newWidth);
    }

    /** Draw active power-up timers (top-left). */
    private void drawPowerupTimers(GraphicsContext g) {
        if (effectExpiryNs.isEmpty()) return;

        double x = sx(12), y = sx(12);
        double w = sx(180), h = sx(28), r = sx(10);
        long now = System.nanoTime();

        for (var entry : effectExpiryNs.entrySet()) {
            var type = entry.getKey();
            long expNs = entry.getValue();
            double remaining = Math.max(0.0, (expNs - now) / 1_000_000_000.0);

            // style per effect
            Color base, border; String label;
            switch (type) {
                case EXPAND_PADDLE -> {
                    base = Color.web("#22c55e"); border = Color.web("#16a34a"); label = "Expand";
                }
                case PADDLE_SPEED -> {
                    base = Color.web("#f59e0b"); border = Color.web("#d97706"); label = "Speed";
                }
                default -> {
                    base = Color.web("#64748b"); border = Color.web("#475569"); label = type.name();
                }
            }

            g.setFill(base.deriveColor(0, 1, 1, 0.92));
            fillRoundRect(g, x, y, w, h, r);
            g.setStroke(border);
            g.setLineWidth(1.0);
            strokeRoundRect(g, x, y, w, h, r);

            g.setFill(Color.WHITE);
            g.setFont(Font.font(14 * SCALE));
            String text = label + "  " + String.format("%.1fs", remaining);
            g.fillText(text, x + sx(12), y + h / 2.0 + sx(5));

            y += h + sx(8); // stack vertically
        }
    }
}