package com.yoad.arkanoid.sprites;

import javafx.scene.canvas.GraphicsContext;

/**
 * The {@code sprites.Sprite} interface defines the common behavior for any object that can be drawn
 * on the screen and updated over time.
 */
public interface Sprite {

  /**
   * Draws the sprite on the given {@link GraphicsContext}.
   *
   * @param g the {@link GraphicsContext} on which the sprite will be drawn. This object is used to
   *     display graphical elements.
   */
  void draw(GraphicsContext g);

  /**
   * Notifies the sprite that a certain amount of time has passed. This method can be used to update
   * the sprite's state or position based on time.
   */
  void timePassed();
}
