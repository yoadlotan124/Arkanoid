package com.yoad.arkanoid.audio;

public final class Sounds {
    private Sounds() {}

    public static final Sound BRICK   = new Sound("/audio/hit.mp3",      0.17);
    public static final Sound POWERUP = new Sound("/audio/powerup.mp3",  0.12, 0.03);
    public static final Sound LOSE    = new Sound("/audio/lose.mp3",     0.3);
}
