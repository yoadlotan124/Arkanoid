package com.yoad.arkanoid.events;

import com.yoad.arkanoid.game.Block;
import com.yoad.arkanoid.game.Game;
import com.yoad.arkanoid.sprites.Ball;

/**
 * The {@code BallRemover} class is responsible for removing balls from the game when they hit
 * certain blocks (e.g., the bottom death-region block), and for keeping track of the remaining
 * number of balls using a {@link Counter}. This is useful for handling game-over conditions or
 * respawning logic.
 */
public class BallRemover implements HitListener {
  private Game game;
  private Counter remainingBalls;

  /**
   * Constructs a new BallRemover.
   *
   * @param game the {@link Game} instance from which balls will be removed.
   * @param remainingBalls a {@link Counter} tracking how many balls remain in play.
   */
  public BallRemover(Game game, Counter remainingBalls) {
    this.game = game;
    this.remainingBalls = remainingBalls;
  }

  /**
   * This method is called whenever a {@link Block} is hit. It removes the hitting {@link Ball} from
   * the game and updates the ball counter.
   *
   * @param beingHit the block that was hit (usually the "death" block).
   * @param hitter the ball that hit the block and should be removed.
   */
  @Override
  public void hitEvent(Block beingHit, Ball hitter) {
    hitter.removeFromGame(game);
    remainingBalls.decrease(1);
  }
}
