package com.yoad.arkanoid.events;

import com.yoad.arkanoid.game.Brick;
import com.yoad.arkanoid.sprites.Ball;

/**
 * The HitListener interface should be implemented by any class that wants to be notified when a
 * {@link Brick} is hit by a {@link Ball}. Classes that implement this interface can register
 * themselves with a Brick to receive hit events via the {@code hitEvent} method.
 */
public interface HitListener {

  /**
   * This method is called whenever the {@code beingHit} block is hit.
   *
   * @param beingHit the {@link Brick} that was hit.
   * @param hitter the {@link Ball} that hit the block.
   */
  void hitEvent(Brick beingHit, Ball hitter);
}
