package com.yoad.arkanoid.sprites;

import javafx.scene.canvas.GraphicsContext;

import java.util.ArrayList;
import java.util.List;

/**
 * {@code sprites.SpriteCollection} is a class that manages a collection of {@link Sprite} objects.
 * It allows adding new sprites to the collection, updating all sprites with the passage of time,
 * and drawing all sprites onto a given {@link GraphicsContext}.
 */
public class SpriteCollection {

  // Fields
  private final List<Sprite> sprites = new ArrayList<>();

  /**
   * Adds a sprite to the collection.
   *
   * @param s the {@link Sprite} to be added to the collection. This sprite will be managed by the
   *     collection for updates and drawing.
   */
  public void addSprite(Sprite s) {
    this.sprites.add(s);
  }

  /**
   * Removes a sprite from the collection.
   *
   * @param s the {@link Sprite} to be removed from the collection.
   */
  public void removeSprite(Sprite s) {
     sprites.remove(s); 
  }

  /**
   * returns the sprites.
   *
   * @return sprite list.
   */
  public List<Sprite> getSprites() {
    return this.sprites;
  }

  /**
   * Notifies all sprites in the collection that time has passed. Calls the {@link
   * Sprite#timePassed()} method for each sprite, allowing them to update their state or position
   * based on the passage of time.
   */
  public void notifyAllTimePassed() {
    List<Sprite> spriteCopy = new ArrayList<>(this.sprites); // Make a copy
    for (Sprite s : spriteCopy) {
      s.timePassed();
    }
  }

  /**
   * Draws all sprites in the collection on the given {@link GraphicsContext}. Calls the {@link
   * Sprite#draw(GraphicsContext)} method for each sprite to render them.
   *
   * @param g the {@link GraphicsContext} on which the sprites will be drawn. This surface is where the
   *     graphical elements will be rendered.
   */
  public void drawAll(GraphicsContext g) {
        for (Sprite s : new ArrayList<>(sprites)) {
            s.draw(g);
        }
    }
}
