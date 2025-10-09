package com.yoad.arkanoid.audio;

import javafx.scene.media.AudioClip;

public final class Sound {
    private final AudioClip clip;
    private final double minGapNs; // rate-limit window
    private long lastPlayNs = 0L;

    public Sound(String resourcePath, double volume) {
        this(resourcePath, volume, 0.0);
    }

    public Sound(String resourcePath, double volume, double minGapSeconds) {
        AudioClip c = null;
        try {
            var url = Sound.class.getResource(resourcePath);
            if (url != null) {
                c = new AudioClip(url.toExternalForm());
                c.setVolume(Math.max(0.0, Math.min(1.0, volume)));
            }
        } catch (Exception ignored) {}
        this.clip = c;
        this.minGapNs = Math.max(0.0, minGapSeconds) * 1_000_000_000.0;
        // warm-up (avoid first-play delay)
        if (this.clip != null) this.clip.play(0.0); // inaudible pre-load
    }

    /** Play if not rate-limited. */
    public void play() {
        if (clip == null) return;
        long now = System.nanoTime();
        if (now - lastPlayNs >= (long)minGapNs) {
            clip.play();
            lastPlayNs = now;
        }
    }

    /** Always restart from 0 (use for short UI clicks). */
    public void playRestart() {
        if (clip == null) return;
        clip.stop();
        clip.play();
        lastPlayNs = System.nanoTime();
    }
}
