package com.yoad.arkanoid.sprites;

import biuoop.DrawSurface;
import java.util.ArrayList;
import java.util.List;

/**
 * {@code sprites.SpriteCollection} is a class that manages a collection of {@link Sprite} objects.
 * It allows adding new sprites to the collection, updating all sprites with the passage of time,
 * and drawing all sprites onto a given {@link DrawSurface}.
 */
public class SpriteCollection {

  // Fields
  private List<Sprite> sprites;

  /**
   * Constructs an empty {@code sprites.SpriteCollection}. Initializes the list to store {@link
   * Sprite} objects.
   */
  public SpriteCollection() {
    this.sprites = new ArrayList<>();
  }

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
   * Draws all sprites in the collection on the given {@link DrawSurface}. Calls the {@link
   * Sprite#drawOn(DrawSurface)} method for each sprite to render them.
   *
   * @param d the {@link DrawSurface} on which the sprites will be drawn. This surface is where the
   *     graphical elements will be rendered.
   */
  public void drawAllOn(DrawSurface d) {
    for (Sprite s : this.sprites) {
      s.drawOn(d);
    }
  }
}
