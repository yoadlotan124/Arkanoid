package com.yoad.arkanoid.sprites;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import com.yoad.arkanoid.events.Counter;
import com.yoad.arkanoid.game.ArkanoidGame;


/**
 * A {@code ScoreIndicator} is a {@link Sprite} that displays the current score
 * at the top of the screen.
 */
public class ScoreHUD implements Sprite {
    private Counter currentScore;

    /**
     * Constructs a new ScoreIndicator.
     *
     * @param currentScore the counter that tracks the score
     */
    public ScoreHUD(Counter currentScore) {
        this.currentScore = currentScore;
    }

    /**
     * Draws the score indicator text on the given draw surface.
     *
     * @param d the DrawSurface to draw on
     */
    @Override
    public void draw(GraphicsContext g) {
        g.setFill(Color.WHITE);
        g.setFont(Font.font(18));
        g.fillText("Score: " + currentScore.getValue(), 350, 60);
    }

    /**
     * Handles time-based updates.
     * (Score display doesn't animate, so this is empty.)
     */
    @Override
    public void timePassed() {
        // Nothing needed here
    }

    /**
     * Adds this indicator to the game.
     *
     * @param g the game to add this sprite to
     */
    public void addToGame(ArkanoidGame g) {
        g.addSprite(this);
    }
}
