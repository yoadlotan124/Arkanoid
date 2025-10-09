package com.yoad.arkanoid.ui;

import com.yoad.arkanoid.config.Theme;
import javafx.scene.paint.Color;

public final class ThemePalettes {
  private ThemePalettes() {}

  public static Color[] palette(Theme t) {
    return switch (t) {
      case OCEAN -> new Color[]{
        c("#60a5fa"), c("#38bdf8"), c("#22d3ee"),
        c("#a78bfa"), c("#7dd3fc"), c("#93c5fd")
      };
      case SUNSET -> new Color[]{
        c("#fca5a5"), c("#fdba74"), c("#fbbf24"),
        c("#f59e0b"), c("#fb7185"), c("#f97316")
      };
      case NEON -> new Color[]{
        c("#22d3ee"), c("#a78bfa"), c("#f472b6"),
        c("#34d399"), c("#fbbf24"), c("#60a5fa")
      };
      case FOREST -> new Color[]{
        c("#4ade80"), c("#22c55e"), c("#10b981"),
        c("#84cc16"), c("#a3e635"), c("#16a34a")
      };
      case MONO -> new Color[]{
        c("#e5e7eb"), c("#d1d5db"), c("#9ca3af"),
        c("#6b7280"), c("#4b5563"), c("#374151")
      };
    };
  }

  private static Color c(String hex) { return Color.web(hex); }
}
