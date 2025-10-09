package com.yoad.arkanoid.audio;

import javafx.scene.media.AudioClip;

public final class ClickPool {
    private final AudioClip[] clips;
    private int idx = 0;

    public ClickPool(String resourcePath, double volume, int voices) {
        if (voices < 1) voices = 1;
        clips = new AudioClip[voices];
        var url = ClickPool.class.getResource(resourcePath);
        if (url == null) {
            // null-safe: empty pool
            for (int i = 0; i < voices; i++) clips[i] = null;
            return;
        }
        String uri = url.toExternalForm();
        for (int i = 0; i < voices; i++) {
            AudioClip c = new AudioClip(uri);
            c.setVolume(Math.max(0.0, Math.min(1.0, volume)));
            // warm up (avoids first-play latency)
            c.play(0.0);
            clips[i] = c;
        }
    }

    /** Fire-and-forget; uses next voice so rapid clicks don’t cut off. */
    public void play() {
        if (clips.length == 0) return;
        idx = (idx + 1) % clips.length;
        AudioClip c = clips[idx];
        if (c != null) c.play();
    }
}
