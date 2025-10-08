package com.yoad.arkanoid;

import com.yoad.arkanoid.game.Game;

public class App {
    public static void main(String[] args) {
        Game game = new Game();
        game.initialize();
        game.run();
    }
}
