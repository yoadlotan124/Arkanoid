package com.yoad.arkanoid;

import com.yoad.arkanoid.game.Game; // change if your Game is in .core

public class App {
    public static void main(String[] args) {
        Game game = new Game();
        game.initialize();
        game.run();
    }
}
