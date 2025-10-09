package com.yoad.arkanoid.ui;

import javafx.scene.text.Font;

import static com.yoad.arkanoid.game.Dimensions.SCALE;

public final class Typography {
    private Typography() {}

    // Scaled font sizes (px @ 800x600 baseline, scaled by SCALE)
    public static Font display() { return Font.font(36 * SCALE); }  // big title
    public static Font headline(){ return Font.font(24 * SCALE); }  // section headings
    public static Font title()   { return Font.font(20 * SCALE); }  // HUD titles
    public static Font body()    { return Font.font(16 * SCALE); }  // normal text
    public static Font small()   { return Font.font(12 * SCALE); }  // notes
}