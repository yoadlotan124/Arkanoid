package com.yoad.arkanoid.events;

import com.yoad.arkanoid.game.Brick;
import com.yoad.arkanoid.audio.Sounds;
import com.yoad.arkanoid.game.ArkanoidGame;
import com.yoad.arkanoid.sprites.Ball;

import java.util.Random;

/**
 * The {@code BlockRemover} class is responsible for removing blocks from the game
 * when they are hit, and for keeping track of the number of remaining blocks using a {@link Counter}.
 * This class implements the {@link HitListener} interface and should be registered
 * as a listener to any block that should be removed upon being hit.
 */
public class BlockRemover implements HitListener {
    private ArkanoidGame game;
    private Counter remainingBlocks;
    private Counter scoreCounter;

    // Power ups spawner purposes
    private final Random rng = new Random();

    /**
     * Constructs a new BlockRemover.
     * @param game the {@link ArkanoidGame} instance from which blocks will be removed.
     * @param remainingBlocks a {@link Counter} that keeps track of remaining blocks.
     * @param scoreCounter score counter.
     */
    public BlockRemover(ArkanoidGame game, Counter remainingBlocks, Counter scoreCounter) {
        this.game = game;
        this.remainingBlocks = remainingBlocks;
        this.scoreCounter = scoreCounter;
    }

    /**
     * Called whenever a block is hit. Removes the block from the game,
     * unregisters this listener from it, and decreases the block count.
     * @param beingHit the {@link Brick} that was hit.
     * @param hitter the {@link Ball} that hit the block.
     */
    @Override
    public void hitEvent(Brick beingHit, Ball hitter) {
        hitter.setFxColor(beingHit.getColor());
        beingHit.removeFromGame(game);
        beingHit.removeHitListener(this);
        remainingBlocks.decrease(1);
        // Sound effect
        Sounds.BRICK.play();
        // ~25% drop chance
        if (rng.nextDouble() < 0.25) {
            var rect = beingHit.getCollisionRectangle();
            double cx = rect.getStartX() + rect.getWidth() / 2.0;
            double cy = rect.getStartY() + rect.getHeight() / 2.0;

            // weighted choice: 40% SIZE, 20% MULTI, 40% SPEED
            double r = rng.nextDouble();
            com.yoad.arkanoid.powerups.PowerUpType type =
                (r < 0.4) ? com.yoad.arkanoid.powerups.PowerUpType.EXPAND_PADDLE :
                (r < 0.6) ? com.yoad.arkanoid.powerups.PowerUpType.MULTI_BALL :
                            com.yoad.arkanoid.powerups.PowerUpType.PADDLE_SPEED;

            game.spawnPowerUp(cx, cy, type); // overload spawnPowerUp to accept a type
        }
    }
}
