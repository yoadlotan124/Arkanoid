package com.yoad.arkanoid.fx;

import javafx.scene.paint.Color;

public final class FxColors {
    private FxColors() {}

    public static Color fromAwt(java.awt.Color c) {
        if (c == null) return Color.WHITE;
        return new Color(
            c.getRed()   / 255.0,
            c.getGreen() / 255.0,
            c.getBlue()  / 255.0,
            c.getAlpha() / 255.0
        );
    }

    public static javafx.scene.paint.Color toFx(java.awt.Color c) {
        return javafx.scene.paint.Color.rgb(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha() / 255.0);
    }
}
