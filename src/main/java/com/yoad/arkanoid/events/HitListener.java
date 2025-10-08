package com.yoad.arkanoid.notifiers;

import com.yoad.arkanoid.game.Block;
import com.yoad.arkanoid.sprites.Ball;

/**
 * The HitListener interface should be implemented by any class that wants to be notified when a
 * {@link Block} is hit by a {@link Ball}. Classes that implement this interface can register
 * themselves with a Block to receive hit events via the {@code hitEvent} method.
 */
public interface HitListener {

  /**
   * This method is called whenever the {@code beingHit} block is hit.
   *
   * @param beingHit the {@link Block} that was hit.
   * @param hitter the {@link Ball} that hit the block.
   */
  void hitEvent(Block beingHit, Ball hitter);
}
