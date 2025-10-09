package com.yoad.arkanoid.config;

import static com.yoad.arkanoid.game.Dimensions.*;

/**
 * Runtime config chosen from the main menu.
 */
public final class GameConfig {

    /** Gameplay difficulty scales. */
    public enum Difficulty {
        EASY   (0.85, -1),  // slightly slower balls, 1 row fewer
        NORMAL (1.00,  0),  // baseline
        HARD   (1.25, +1);  // faster balls, +1 row

        private final double speedScale;
        private final int rowDelta;
        Difficulty(double speedScale, int rowDelta) {
            this.speedScale = speedScale; this.rowDelta = rowDelta;
        }
        public double speedScale() { return speedScale; }
        public int rowDelta()      { return rowDelta; }
    }

    //---------- Fields ----------

    private Difficulty difficulty = Difficulty.NORMAL;
    private Theme theme = Theme.OCEAN; // default theme

    // base layout (before difficulty deltas)
    private static final int BASE_ROWS = 6;
    private static final int BASE_COLS = 12;

    //---------- Difficulty ----------

    public Difficulty getDifficulty()       { return difficulty; }
    public void setDifficulty(Difficulty d) { if (d != null) difficulty = d; }

    public void nextDifficulty() {
        difficulty = switch (difficulty) {
            case EASY   -> Difficulty.NORMAL;
            case NORMAL -> Difficulty.HARD;
            case HARD   -> Difficulty.EASY;
        };
    }

    public String difficultyLabel() {
        return switch (difficulty) {
            case EASY   -> "Easy";
            case NORMAL -> "Normal";
            case HARD   -> "Hard";
        };
    }

    // --- theme (delegates visuals to Backgrounds/ThemePalettes) ---
    public Theme theme()            { return theme; }
    public void setTheme(Theme t)   { if (t != null) theme = t; }

    /** Cycle through the 5 themes (OCEAN → SUNSET → NEON → FOREST → MONO → …). */
    public void nextTheme() {
        Theme[] all = Theme.values();
        theme = all[(theme.ordinal() + 1) % all.length];
    }

    public String themeLabel() {
        return switch (theme) {
            case OCEAN  -> "Ocean";
            case SUNSET -> "Sunset";
            case NEON   -> "Neon";
            case FOREST -> "Forest";
            case MONO   -> "Mono";
        };
    }

    // --- derived values ---
    /** Rows after difficulty delta (min 3). */
    public int rows() { return Math.max(3, BASE_ROWS + difficulty.rowDelta()); }

    /** Columns are fixed by design */
    public int cols() { return BASE_COLS; }

    /** Base ball speed (≈3 px/tick), scaled by resolution + difficulty. */
    public double ballSpeed() { return sd(3.0) * difficulty.speedScale(); }
}
