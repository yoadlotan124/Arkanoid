package com.yoad.arkanoid.sprites;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import com.yoad.arkanoid.events.Counter;
import com.yoad.arkanoid.game.ArkanoidGame;
import com.yoad.arkanoid.ui.TextFx;
import com.yoad.arkanoid.ui.Typography;

import static com.yoad.arkanoid.game.Dimensions.*;


public class ScoreHUD implements Sprite {
    private Counter currentScore;

    public ScoreHUD(Counter currentScore) {
        this.currentScore = currentScore; 
    }

    @Override
    public void draw(GraphicsContext g) {
        // centered pill "Score: N"
        String text = "Score: " + currentScore.getValue();
        TextFx.pill(g, text, WIDTH / 2.0, sx(18), Typography.title(), Color.color(0,0,0,0.45), Color.WHITE);
    }


    @Override
    public void timePassed() {
        // Nothing needed here
    }

    public void addToGame(ArkanoidGame g) { g.addSprite(this); }
}
