package com.yoad.arkanoid.ui;

import com.yoad.arkanoid.config.Theme;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.*;

import static com.yoad.arkanoid.game.Dimensions.*;

public final class Backgrounds {
  private Backgrounds() {}

  public static void drawGameBackground(GraphicsContext g, Theme theme) {
    double w = WIDTH, h = HEIGHT;

    // themed vertical gradient
    LinearGradient base = switch (theme) {
      case OCEAN -> lg("#0ea5e9", "#1e40af");   // sky -> indigo
      case SUNSET -> lg("#fb923c", "#7c2d12");  // orange -> deep brown
      case NEON -> lg("#06b6d4", "#7c3aed");    // cyan -> violet
      case FOREST -> lg("#22c55e", "#065f46");  // green-> deep teal
      case MONO -> lg("#9ca3af", "#111827");    // gray -> near black
    };

    g.setFill(base);
    g.fillRect(0, 0, w, h);

    // soft vignette overlay (slightly stronger in MONO)
    double edgeAlpha = (theme == Theme.MONO) ? 0.42 : 0.35;
    RadialGradient vignette = new RadialGradient(
        0, 0, w/2.0, h/2.0, Math.max(w, h)*0.55,
        false, CycleMethod.NO_CYCLE,
        new Stop(0.70, Color.color(0,0,0,0.00)),
        new Stop(1.00, Color.color(0,0,0,edgeAlpha))
    );
    g.setFill(vignette);
    g.fillRect(0, 0, w, h);
  }

  private static LinearGradient lg(String topHex, String botHex) {
    return new LinearGradient(
        0, 0, 0, 1, true, CycleMethod.NO_CYCLE,
        new Stop(0.0, Color.web(topHex)),
        new Stop(1.0, Color.web(botHex))
    );
  }
}
