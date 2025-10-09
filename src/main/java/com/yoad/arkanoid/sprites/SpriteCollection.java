package com.yoad.arkanoid.sprites;

import javafx.scene.canvas.GraphicsContext;

import java.util.ArrayList;
import java.util.List;

public class SpriteCollection {
  private final List<Sprite> sprites = new ArrayList<>();

  public void addSprite(Sprite s) { this.sprites.add(s); }

  public void removeSprite(Sprite s) { sprites.remove(s); }

  public List<Sprite> getSprites() { return this.sprites; }

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

  public void drawAll(GraphicsContext g) {
        for (Sprite s : new ArrayList<>(sprites)) {
          s.draw(g); 
        }
    }
}
