package com.yoad.arkanoid.ui;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.text.Text;

/**
 * Class for modularity of menu buttons
 */
public final class UIUtils {
    private UIUtils() {}

    /** Center text horizontally around cx. */
    public static void drawCentered(GraphicsContext g, String text, double cx, double cy) {
        Text t = new Text(text);
        t.setFont(g.getFont());
        double w = t.getLayoutBounds().getWidth();
        g.fillText(text, cx - w / 2.0, cy);
    }

    public static void fillRoundRect(GraphicsContext g, double x, double y, double w, double h, double r) {
        g.fillRoundRect(x, y, w, h, r, r);
    }

    public static void strokeRoundRect(GraphicsContext g, double x, double y, double w, double h, double r) {
        g.strokeRoundRect(x, y, w, h, r, r);
    }
}
