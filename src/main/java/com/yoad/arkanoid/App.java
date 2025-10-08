package com.yoad.arkanoid;

import com.yoad.arkanoid.game.ArkanoidGame;

public class App {
    public static void main(String[] args) {
        ArkanoidGame game = new ArkanoidGame();
        game.initialize();
        game.run();
    }
}
