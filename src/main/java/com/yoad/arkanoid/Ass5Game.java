package com.yoad.arkanoid;

import com.yoad.arkanoid.game.Game;
/**
 * The Ass5Game class is the entry point for running the game.
 * It creates a Game instance, initializes it, and starts the game loop.
 */
public class Ass5Game {

    /**
     * Main method that launches the game.
     *
     * @param args command-line arguments (not used).
     */
    public static void main(String[] args) {
        Game game = new Game();
        game.initialize();
        game.run();
    }
}
