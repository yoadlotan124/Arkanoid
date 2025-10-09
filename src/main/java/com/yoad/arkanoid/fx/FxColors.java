package com.yoad.arkanoid.fx;

import javafx.scene.paint.Color;

/** JavaFX color helpers (no AWT). */
public final class FxColors {
    private FxColors() {}

    /** From 0–255 RGB(A). */
    public static Color rgba(int r, int g, int b, int a) {
        return Color.rgb(
            clamp255(r), clamp255(g), clamp255(b),
            clamp01(a / 255.0)
        );
    }
    public static Color rgb(int r, int g, int b) {
        return rgba(r, g, b, 255);
    }

    /** From 0–1 doubles (already normalized). */
    public static Color rgba(double r, double g, double b, double a) {
        return new Color(clamp01(r), clamp01(g), clamp01(b), clamp01(a));
    }

    /** From hex strings: "#RRGGBB" or "#RRGGBBAA" (case-insensitive). */
    public static Color hex(String hex) {
        return Color.web(hex);
    }

    /** From packed ARGB int (0xAARRGGBB). */
    public static Color fromArgbInt(int argb) {
        int a = (argb >>> 24) & 0xFF;
        int r = (argb >>> 16) & 0xFF;
        int g = (argb >>>  8) & 0xFF;
        int b = (argb       ) & 0xFF;
        return rgba(r, g, b, a);
    }

    private static int clamp255(int v) {
        return (v < 0) ? 0 : (v > 255 ? 255 : v);
    }
    private static double clamp01(double v) {
        if (v < 0.0) return 0.0;
        if (v > 1.0) return 1.0;
        return v;
    }
}
