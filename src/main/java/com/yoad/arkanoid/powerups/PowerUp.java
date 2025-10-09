package com.yoad.arkanoid.powerups;

import com.yoad.arkanoid.sprites.Sprite;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import static com.yoad.arkanoid.game.Dimensions.*;

/** A falling pickup the paddle can catch. */
public final class PowerUp implements Sprite {
    public final PowerUpType type;
    private double x, y;               // top-left
    private final int w, h;            // size

    public PowerUp(PowerUpType type, double x, double y) {
        this.type = type;
        this.x = x; this.y = y;
        this.w = sx(24);
        this.h = sx(24);
    }

    /** Move down by pixels per second, scaled by dt. */
    public void update(double dt) {
        y += sd(140.0) * dt; // fall speed ~140px/s scaled
    }

    @Override public void timePassed() { /* we drive with dt in game loop */ }

    @Override
    public void draw(GraphicsContext g) {
        double r = sx(12);
        // base pill
        switch (type) {
            case EXPAND_PADDLE -> {
                g.setFill(javafx.scene.paint.Color.web("#22c55e")); // green
                g.fillRoundRect(x, y, w, h, r, r);
                g.setStroke(javafx.scene.paint.Color.color(0, 0, 0, 0.25));
                g.strokeRoundRect(x, y, w, h, r, r);

                // "SIZE" label
                g.setFill(javafx.scene.paint.Color.WHITE);
                g.setFont(javafx.scene.text.Font.font(11 * com.yoad.arkanoid.game.Dimensions.SCALE));
                // center text roughly
                g.fillText("SIZE", x + w * 0.06, y + h * 0.65);
            }
            case MULTI_BALL -> {
                g.setFill(javafx.scene.paint.Color.web("#60a5fa")); // blue
                g.fillRoundRect(x, y, w, h, r, r);
                g.setStroke(javafx.scene.paint.Color.color(0, 0, 0, 0.25));
                g.strokeRoundRect(x, y, w, h, r, r);

                // plus icon
                g.setStroke(javafx.scene.paint.Color.WHITE);
                g.setLineWidth(2);
                g.strokeLine(x + w*0.5, y + h*0.28, x + w*0.5, y + h*0.72);
                g.strokeLine(x + w*0.28, y + h*0.5, x + w*0.72, y + h*0.5);
            }
            case PADDLE_SPEED -> {
                g.setFill(javafx.scene.paint.Color.web("#f59e0b")); // amber
                g.fillRoundRect(x, y, w, h, r, r);
                g.setStroke(javafx.scene.paint.Color.color(0, 0, 0, 0.25));
                g.strokeRoundRect(x, y, w, h, r, r);

                // lightning bolt (simple zig-zag)
                g.setStroke(javafx.scene.paint.Color.WHITE);
                g.setLineWidth(2);
                double x1 = x + w*0.35, y1 = y + h*0.22;
                double x2 = x + w*0.75, y2 = y + h*0.50;
                double x3 = x + w*0.2, y3 = y + h*0.46;
                double x4 = x + w*0.62, y4 = y + h*0.80;
                g.strokeLine(x1, y1, x2, y2);
                g.strokeLine(x2, y2, x3, y3);
                g.strokeLine(x3, y3, x4, y4);
            }
        }
    }

    public double x() { return x; }
    public double y() { return y; }
    public int w() { return w; }
    public int h() { return h; }
}
