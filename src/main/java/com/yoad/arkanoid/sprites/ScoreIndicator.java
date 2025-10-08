package com.yoad.arkanoid.sprites;

import biuoop.DrawSurface;
import com.yoad.arkanoid.notifiers.Counter;
import com.yoad.arkanoid.game.Game;

import java.awt.Color;

/**
 * A {@code ScoreIndicator} is a {@link Sprite} that displays the current score
 * at the top of the screen.
 */
public class ScoreIndicator implements Sprite {
    private Counter currentScore;

    /**
     * Constructs a new ScoreIndicator.
     *
     * @param currentScore the counter that tracks the score
     */
    public ScoreIndicator(Counter currentScore) {
        this.currentScore = currentScore;
    }

    /**
     * Draws the score indicator text on the given draw surface.
     *
     * @param d the DrawSurface to draw on
     */
    @Override
    public void drawOn(DrawSurface d) {
        d.setColor(Color.WHITE);
        d.drawText(350, 60, "Score: " + currentScore.getValue(), 18);
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
    public void addToGame(Game g) {
        g.addSprite(this);
    }
}
