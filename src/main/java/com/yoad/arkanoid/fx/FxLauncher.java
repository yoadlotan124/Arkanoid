package com.yoad.arkanoid.fx;

import javafx.application.Application;
import javafx.animation.AnimationTimer;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import com.yoad.arkanoid.game.ArkanoidGame;

import static com.yoad.arkanoid.game.Dimensions.WIDTH;
import static com.yoad.arkanoid.game.Dimensions.HEIGHT;

/** Boots JavaFX and drives the game loop via AnimationTimer. */
public class FxLauncher extends Application {

    @Override
    public void start(Stage stage) {
        Canvas canvas = new Canvas(WIDTH, HEIGHT);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        Scene scene = new Scene(new StackPane(canvas), WIDTH, HEIGHT);

        stage.setTitle("Arkanoid");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();

        ArkanoidGame game = new ArkanoidGame();   // your existing class
        game.initialize();                        // keep your current init

        // Hook up keyboard (minimal: left/right/space/esc/enter)
        scene.setOnKeyPressed(e -> game.onKeyChanged(e.getCode(), true));
        scene.setOnKeyReleased(e -> game.onKeyChanged(e.getCode(), false));

        // FX game loop: call the game's per-frame function
        AnimationTimer timer = new AnimationTimer() {
            private long last = 0;
            @Override public void handle(long now) {
                if (last == 0) last = now;
                double dt = (now - last) / 1_000_000_000.0; // seconds
                last = now;

                game.tick(gc, dt);   // let the game update+render one frame
            }
        };
        timer.start();
    }

    /** Helper to launch from a plain main() */
    public static void launchApp(String[] args) { launch(args); }
}
