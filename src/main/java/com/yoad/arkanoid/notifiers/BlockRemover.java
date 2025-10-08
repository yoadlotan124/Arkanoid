package com.yoad.arkanoid.notifiers;

import com.yoad.arkanoid.game.Block;
import com.yoad.arkanoid.game.Game;
import com.yoad.arkanoid.sprites.Ball;

/**
 * The {@code BlockRemover} class is responsible for removing blocks from the game
 * when they are hit, and for keeping track of the number of remaining blocks using a {@link Counter}.
 * This class implements the {@link HitListener} interface and should be registered
 * as a listener to any block that should be removed upon being hit.
 */
public class BlockRemover implements HitListener {
    private Game game;
    private Counter remainingBlocks;
    private Counter scoreCounter;

    /**
     * Constructs a new BlockRemover.
     * @param game the {@link Game} instance from which blocks will be removed.
     * @param remainingBlocks a {@link Counter} that keeps track of remaining blocks.
     * @param scoreCounter score counter.
     */
    public BlockRemover(Game game, Counter remainingBlocks, Counter scoreCounter) {
        this.game = game;
        this.remainingBlocks = remainingBlocks;
        this.scoreCounter = scoreCounter;
    }

    /**
     * Called whenever a block is hit. Removes the block from the game,
     * unregisters this listener from it, and decreases the block count.
     * @param beingHit the {@link Block} that was hit.
     * @param hitter the {@link Ball} that hit the block.
     */
    @Override
    public void hitEvent(Block beingHit, Ball hitter) {
        hitter.setColor(beingHit.getColor());
        beingHit.removeFromGame(game);
        beingHit.removeHitListener(this);
        remainingBlocks.decrease(1);
        if (remainingBlocks.getValue() == 0) {
            scoreCounter.increase(100); // Bonus for clearing all blocks
        }
    }
}
