package com.yoad.arkanoid.ui;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import static com.yoad.arkanoid.game.Dimensions.*;
import static com.yoad.arkanoid.ui.UIUtils.*;

public class MenuButton {
    public double x, y, w, h;
    public String label;
    public boolean hovered = false;

    public MenuButton(String label, double x, double y, double w, double h) {
        this.label = label; this.x = x; this.y = y; this.w = w; this.h = h;
    }

    public boolean contains(double px, double py) {
        return px >= x && px <= x + w && py >= y && py <= y + h;
    }

    /** Default styled draw */
    public void draw(GraphicsContext g, Color base, Color hover) {
        draw(g, base, hover, 20 * SCALE, sx(12));
    }

    /** Customizable draw */
    public void draw(GraphicsContext g, Color base, Color hover, double fontPx, double cornerRadius) {
        Color fill = hovered ? hover : base;
        g.setFill(fill);
        fillRoundRect(g, x, y, w, h, cornerRadius);
        g.setStroke(Color.color(1, 1, 1, 0.18));
        g.setLineWidth(1.0);
        strokeRoundRect(g, x, y, w, h, cornerRadius);

        g.setFill(Color.WHITE);
        g.setFont(Font.font(fontPx));
        drawCentered(g, label, x + w / 2.0, y + h / 2.0 + sx(7));
    }
}
