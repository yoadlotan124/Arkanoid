package com.yoad.arkanoid.sprites;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import com.yoad.arkanoid.events.Counter;
import com.yoad.arkanoid.game.ArkanoidGame;
import com.yoad.arkanoid.ui.TextFx;
import com.yoad.arkanoid.ui.Typography;

import static com.yoad.arkanoid.game.Dimensions.*;


/**
 * A {@code ScoreIndicator} is a {@link Sprite} that displays the current score
 * at the top of the screen.
 */
public class ScoreHUD implements Sprite {

    //Fields
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
     * @param g the Surface to draw on
     */
    @Override
    public void draw(GraphicsContext g) {
        // centered pill "Score: N"
        String text = "Score: " + currentScore.getValue();
        TextFx.pill(g, text, WIDTH / 2.0, sx(18), Typography.title(), Color.color(0,0,0,0.45), Color.WHITE);
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
    public void addToGame(ArkanoidGame g) { g.addSprite(this); }
}
