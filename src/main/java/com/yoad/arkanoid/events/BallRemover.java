package com.yoad.arkanoid.events;

import com.yoad.arkanoid.game.Brick;
import com.yoad.arkanoid.game.ArkanoidGame;
import com.yoad.arkanoid.sprites.Ball;

/**
 * The {@code BallRemover} class is responsible for removing balls from the game
 * when they hit certain blocks (e.g., the bottom death-region block),
 * and for keeping track of the remaining number of balls using a {@link Counter}.
 * This is useful for handling game-over conditions or respawning logic.
 */
public class BallRemover implements HitListener {
    //---------- Fields ----------

    private ArkanoidGame game;
    private Counter remainingBalls;

    //---------- Constructor & Getters/Setters ----------

    public BallRemover(ArkanoidGame game, Counter remainingBalls) {
        this.game = game;
        this.remainingBalls = remainingBalls;
    }

    //---------- BallRemover's Logic ----------

    @Override
    public void hitEvent(Brick beingHit, Ball hitter) {
        hitter.removeFromGame(game);
        remainingBalls.decrease(1);
    }
}
