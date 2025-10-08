package com.yoad.arkanoid.events;

import com.yoad.arkanoid.game.Brick;
import com.yoad.arkanoid.sprites.Ball;

/**
 * A {@code ScoreTrackingListener} is a {@link HitListener} that increases the score
 * whenever a block is hit.
 *
 * <p>Each time a hit occurs, this listener increases the score by 5 points.
 * It is typically used to track the player's score during gameplay.</p>
 */
public class ScoreTrackingListener implements HitListener {
    private Counter currentScore;

    /**
     * Constructs a ScoreTrackingListener with a reference to the score counter.
     *
     * @param scoreCounter the counter that keeps track of the current score
     */
    public ScoreTrackingListener(Counter scoreCounter) {
        this.currentScore = scoreCounter;
    }

    /**
     * Called when a hit occurs. Increases the score by 5.
     *
     * @param beingHit the block that was hit
     * @param hitter the ball that hit the block
     */
    @Override
    public void hitEvent(Brick beingHit, Ball hitter) {
        currentScore.increase(5);
    }
}
