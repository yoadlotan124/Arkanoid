package com.yoad.arkanoid.ui;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import static com.yoad.arkanoid.game.Dimensions.sx;

public final class TextFx {
    private TextFx() {}

    // --- helpers to measure ---
    private static double textWidth(GraphicsContext g, String s) {
        Text t = new Text(s);
        t.setFont(g.getFont());
        return t.getLayoutBounds().getWidth();
    }

    private static double fontHeight(GraphicsContext g) {
        return g.getFont().getSize();
    }

    /** Centered text with a soft shadow. */
    public static void centered(GraphicsContext g, String s, double cx, double cy, Font font, Color fill) {
        g.setFont(font);

        DropShadow shadow = new DropShadow();
        shadow.setRadius(sx(6));
        shadow.setOffsetX(0);
        shadow.setOffsetY(sx(2));
        shadow.setColor(Color.color(0, 0, 0, 0.55));

        double w = textWidth(g, s);
        double h = fontHeight(g);

        g.setEffect(shadow);
        g.setFill(fill);
        g.fillText(s, cx - w * 0.5, cy + h * 0.35);
        g.setEffect(null);
    }

    /** Left-aligned text with a light outline. */
    public static void outlined(GraphicsContext g, String s, double x, double y, Font font, Color fill) {
        g.setFont(font);
        g.setFill(Color.color(0,0,0,0.35));
        g.fillText(s, x+0.7, y+0.7);
        g.fillText(s, x-0.7, y+0.7);
        g.fillText(s, x+0.7, y-0.7);
        g.fillText(s, x-0.7, y-0.7);
        g.setFill(fill);
        g.fillText(s, x, y);
    }

    /** Rounded pill label used by HUD. */
    public static void pill(GraphicsContext g, String s, double cx, double cy, Font font, Color bg, Color fg) {
        g.setFont(font);
        double padX = sx(10), padY = sx(6);
        double w = textWidth(g, s);
        double h = fontHeight(g);

        double bw = w + padX * 2;
        double bh = h + padY * 2;
        double x = cx - bw * 0.5;
        double y = cy - bh * 0.5;

        // shadow
        g.setFill(Color.color(0,0,0,0.25));
        g.fillRoundRect(x, y + sx(2), bw, bh, sx(16), sx(16));

        // body
        g.setFill(bg);
        g.fillRoundRect(x, y, bw, bh, sx(16), sx(16));

        // text
        g.setFill(fg);
        g.fillText(s, cx - w * 0.5, cy + h * 0.35);
    }
}