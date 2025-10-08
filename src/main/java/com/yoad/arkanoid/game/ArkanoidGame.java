package com.yoad.arkanoid.game;

import biuoop.GUI;
import biuoop.DrawSurface;
import biuoop.Sleeper;

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

import java.awt.Color;


import java.awt.Image;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.File;

/**
 * The game.Game class is responsible for managing the game logic, including the creation and update of sprites,
 * handling game environment interactions, and managing the game loop for rendering and timing.
 */
public class ArkanoidGame {

    private SpriteCollection sprites;
    private World environment;
    private GUI gui;
    private DrawSurface d;
    private Sleeper sleeper;
    private Counter blockCounter;
    private Counter ballCounter;
    private BlockRemover blockRemover;
    private BallRemover ballRemover;
    private Counter score;
    private ScoreTrackingListener scoreTracker;

    /**
     * Constructs a new game.Game object. Initializes the sprite collection and game environment.
     */
    public ArkanoidGame() {
        sprites = new SpriteCollection();
        environment = new World();
        blockCounter = new Counter();
        score = new Counter();
        ballCounter = new Counter();
        blockRemover = new BlockRemover(this, blockCounter, score);
        ballRemover = new BallRemover(this, ballCounter);
        scoreTracker = new ScoreTrackingListener(score);
    }

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
     * Initializes the game by setting up the graphical user interface (GUI),
     * creating the ball and blocks (as sprites and collidables), and adding them to the game.
     * A simple grid of blocks is created for the game, and the ball is set up with an initial position and velocity.
     */
    public void initialize() {
        // Set up GUI and sleeper for frame timing
        gui = new GUI("game.Game", 800, 600);
        sleeper = new Sleeper();

        ScoreHUD scoreIndicator = new ScoreHUD(score);
        scoreIndicator.addToGame(this);

        Rectangle r = new Rectangle(357, 576, 94, 12);
        Paddle paddle = new Paddle(r, gui.getKeyboardSensor());
        paddle.addToGame(this);

        // Create and configure the sprites.Ball
        // Ball radius
        int radius = 6;

        // Ball positions (below the grid, not inside any blocks)
        Ball ball1 = new Ball(new Point(200, 400), radius, Color.WHITE, this.environment);
        ball1.setVelocity(3, -3);
        ball1.addToGame(this);

        Ball ball2 = new Ball(new Point(400, 420), radius, Color.WHITE, this.environment);
        ball2.setVelocity(-3, -3);
        ball2.addToGame(this);

        Ball ball3 = new Ball(new Point(600, 410), radius, Color.WHITE, this.environment);
        ball3.setVelocity(3, -3);
        ball3.addToGame(this);

            // Update the counter
        ballCounter.increase(3);

        // Create and add Blocks to the game (in a grid pattern)
        int blockWidth = 49;
        int blockHeight = 23;
        int rows = 6;
        int cols = 12;

        Color[] colors = {Color.MAGENTA, Color.RED, Color.YELLOW, Color.CYAN, Color.PINK, Color.GREEN};

        for (int row = 0; row < rows; row++) {
            Color currentColor = colors[row % colors.length]; // Cycle colors if needed
            for (int col = 0; col < cols; col++) {
                int x = 723 - col * 49;  // X position with margin
                int y = 150 + row * 23; // Y position with margin
                Brick block = new Brick(new Rectangle(new Point(x, y), blockWidth, blockHeight), currentColor);
                block.addToGame(this);  // Add each block to the game
                blockCounter.increase(1);
                block.addHitListener(blockRemover);
                block.addHitListener(scoreTracker);
            }
            cols--; // Shrinking the number of blocks each row
        }

        Brick block1 = new Brick(new Rectangle(new Point(0, 0), 800, 28), Color.GRAY);
        block1.addToGame(this);
        Brick block2 = new Brick(new Rectangle(new Point(0, 28), 28, 772), Color.GRAY);
        block2.addToGame(this);
        Brick block3 = new Brick(new Rectangle(new Point(772, 28), 28, 772), Color.GRAY);
        block3.addToGame(this);
        Brick block4 = new Brick(new Rectangle(new Point(28, 600), 744, 28), Color.GRAY);
        block4.addHitListener(ballRemover);
        block4.addToGame(this);

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
        this.sprites.getSprites().remove(s);
    }

    /**
     * Runs the game by starting the animation loop. This method continuously updates the game state
     * (calls timePassed on all sprites), draws the game, and sleeps for the appropriate time to maintain
     * the specified frames per second (FPS).
     */
    public void run() {
        int framesPerSecond = 60;
        int millisecondsPerFrame = 1000 / framesPerSecond;
        while (blockCounter.getValue() > 0 && ballCounter.getValue() > 0) {
            long startTime = System.currentTimeMillis(); // Timing for frame duration

            // Notify all sprites that time has passed
            this.sprites.notifyAllTimePassed();

            // Get the draw surface and render all sprites
            DrawSurface d = gui.getDrawSurface();
            d.setColor(Color.BLUE);
            d.fillRectangle(0, 0, 800, 600);

            this.sprites.drawAllOn(d);  // Draw all sprites on the surface
            gui.show(d);                // Display the surface

            // Timing control: Sleep to maintain FPS
            long frameTime = System.currentTimeMillis() - startTime;
            if (frameTime < millisecondsPerFrame) {
                sleeper.sleepFor(millisecondsPerFrame - frameTime);
            }
        }

        if (blockCounter.getValue() <= 0) {
            System.out.println("You Win!\nYour Score is " + score.getValue());
        } else {
            System.out.println("Game Over.\nYour Score is " + score.getValue());
        }
        gui.close();
    }

//    public void run() {
//        int framesPerSecond = 60;
//        int millisecondsPerFrame = 1000 / framesPerSecond;
//        Image background = null;
//
//        try {
//            BufferedImage raw = ImageIO.read(
//                    new File("C:/Users/Yoad/OneDrive/Desktop/First year/OOP/ass5/freakbob.jpg"));
//            background = raw.getScaledInstance(800, 600, Image.SCALE_SMOOTH); // ✅ Scale once
//        } catch (IOException e) {
//            System.out.println("Error loading background: " + e.getMessage());
//        }
//
//        while (blockCounter.getValue() > 0 && ballCounter.getValue() > 0) {
//            long startTime = System.currentTimeMillis(); // Timing for frame duration
//
//            // Notify all sprites that time has passed
//            this.sprites.notifyAllTimePassed();
//
////            // Get the draw surface and render all sprites
//            DrawSurface d = gui.getDrawSurface();
//            // Then later, check if background is not null
//            if (background != null) {
//                d.drawImage(0, 0, background);
//            } else {
//                d.setColor(Color.BLUE);
//                d.fillRectangle(0, 0, 800, 600);
//            }
//
//            // Draw the rest of the sprites after the background
//            this.sprites.drawAllOn(d);
//            gui.show(d);
//
//            // Timing control: Sleep to maintain FPS
//            long frameTime = System.currentTimeMillis() - startTime;
//            if (frameTime < millisecondsPerFrame) {
//                sleeper.sleepFor(millisecondsPerFrame - frameTime);
//            }
//        }
//    }
}
