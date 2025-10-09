package com.yoad.arkanoid.events;

import com.yoad.arkanoid.game.Brick;
import com.yoad.arkanoid.sprites.Ball;

/**
 * A {@code ScoreTrackingListener} is a {@link HitListener} that increases the score
 * whenever a block is hit.
 */
public class ScoreTrackingListener implements HitListener {
    //---------- Fields ----------

    private Counter currentScore;

    //---------- Constructor & Getters/Setters ----------

    public ScoreTrackingListener(Counter scoreCounter) {
        this.currentScore = scoreCounter;
    }

    //---------- Logic ----------

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
