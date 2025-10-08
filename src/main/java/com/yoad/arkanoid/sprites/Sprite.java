package com.yoad.arkanoid.sprites;

import biuoop.DrawSurface;

/**
 * The {@code sprites.Sprite} interface defines the common behavior for any object
 * that can be drawn on the screen and updated over time.
 */
public interface Sprite {

    /**
     * Draws the sprite on the given {@link DrawSurface}.
     *
     * @param d the {@link DrawSurface} on which the sprite will be drawn.
     *          This object is used to display graphical elements.
     */
    void drawOn(DrawSurface d);

    /**
     * Notifies the sprite that a certain amount of time has passed.
     * This method can be used to update the sprite's state or position
     * based on time.
     */
    void timePassed();
}
