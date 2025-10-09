package com.yoad.arkanoid.game;

import javafx.scene.paint.Color;
import static com.yoad.arkanoid.game.Dimensions.*;

/** Runtime config chosen from the main menu. */
public class GameConfig {

    public enum Difficulty {
        EASY (0.85, -1),   // slightly slower balls, 1 row fewer
        NORMAL(1.00,  0),  // baseline
        HARD (1.25, +1);   // faster balls, +1 row

        private final double speedScale;
        private final int rowDelta;
        Difficulty(double speedScale, int rowDelta) {
            this.speedScale = speedScale; this.rowDelta = rowDelta;
        }
        public double speedScale() { return speedScale; }
        public int rowDelta() { return rowDelta; }
    }

    public enum Theme {
        CLASSIC(
            Color.web("#0f172a"), Color.web("#1e293b"), // bg top, bottom
            new java.awt.Color[]{ java.awt.Color.MAGENTA, java.awt.Color.RED, java.awt.Color.YELLOW,
                                  java.awt.Color.CYAN,    java.awt.Color.PINK, java.awt.Color.GREEN }
        ),
        OCEAN(
            Color.web("#0ea5e9"), Color.web("#0369a1"),
            new java.awt.Color[]{ new java.awt.Color( 56,189,248), new java.awt.Color( 59,130,246),
                                   new java.awt.Color( 99,102,241), new java.awt.Color( 14,165,233),
                                   new java.awt.Color( 20,184,166), new java.awt.Color(125,211,252) }
        ),
        SUNSET(
            Color.web("#f97316"), Color.web("#7c2d12"),
            new java.awt.Color[]{ new java.awt.Color(249,115, 22), new java.awt.Color(244, 63, 94),
                                   new java.awt.Color(234, 88, 12), new java.awt.Color(251,146, 60),
                                   new java.awt.Color(234,179,  8), new java.awt.Color(147, 51, 12) }
        ),
        NEON(
            Color.web("#111827"), Color.web("#000000"),
            new java.awt.Color[]{ new java.awt.Color( 16,185,129), new java.awt.Color(  5,150,105),
                                   new java.awt.Color(139, 92,246), new java.awt.Color( 59,130,246),
                                   new java.awt.Color(  6,182,212), new java.awt.Color(236, 72,153) }
        );

        private final Color bgTop, bgBottom;
        private final java.awt.Color[] palette;
        Theme(Color bgTop, Color bgBottom, java.awt.Color[] palette) {
            this.bgTop = bgTop; this.bgBottom = bgBottom; this.palette = palette;
        }
        public Color bgTop()    { return bgTop; }
        public Color bgBottom() { return bgBottom; }
        public java.awt.Color[] palette() { return palette; }
    }

    // chosen values (default)
    private Difficulty difficulty = Difficulty.NORMAL;
    private Theme theme = Theme.CLASSIC;

    // base layout (before difficulty deltas)
    private final int baseRows = 6;
    private final int baseCols = 12;

    public Difficulty getDifficulty()           { return difficulty; }
    public void setDifficulty(Difficulty d)     { this.difficulty = d; }
    public Theme getTheme()                     { return theme; }
    public void setTheme(Theme t)               { this.theme = t; }

    public void nextDifficulty() {
        this.difficulty = switch (difficulty) {
            case EASY    -> Difficulty.NORMAL;
            case NORMAL  -> Difficulty.HARD;
            case HARD    -> Difficulty.EASY;
        };
    }
    public void nextTheme() {
        this.theme = switch (theme) {
            case CLASSIC -> Theme.OCEAN;
            case OCEAN   -> Theme.SUNSET;
            case SUNSET  -> Theme.NEON;
            case NEON    -> Theme.CLASSIC;
        };
    }

    // derived values
    public int rows() { return Math.max(3, baseRows + difficulty.rowDelta()); }
    public int cols() { return baseCols; }

    /** Base ball speed ≈ 3px/tick scaled by difficulty & resolution scale. */
    public double ballSpeed() { return sd(3.0) * difficulty.speedScale(); }
}
