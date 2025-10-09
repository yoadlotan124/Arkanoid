package com.yoad.arkanoid.game;

/**
 * Easy way to scale up or down the whole games dimensions.
 */
public final class Dimensions {
    //---------- Fields ----------

    private Dimensions() {}

    // Base logical size the game was authored at
    public static final int BASE_W = 800;
    public static final int BASE_H = 600;

    // Target size
    public static final int WIDTH  = 1000;
    public static final int HEIGHT = 750;

    // Global scale factor
    public static final double SCALE = WIDTH / (double) BASE_W; // == 1.25

    //---------- Helper methods ----------

    public static int sx(int v) { return (int)Math.round(v * SCALE); }
    public static double sd(double v) { return v * SCALE; }
}
