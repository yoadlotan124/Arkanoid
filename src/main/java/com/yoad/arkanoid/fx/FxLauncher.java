package com.yoad.arkanoid.fx;

import com.yoad.arkanoid.game.ArkanoidGame;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import com.yoad.arkanoid.ui.MenuButton;
import static com.yoad.arkanoid.ui.UIUtils.drawCentered;
import static com.yoad.arkanoid.game.Dimensions.*;


/** Boots JavaFX and drives the game loop via AnimationTimer. */
public class FxLauncher extends Application {

    private enum UiState { MENU, GAME }
    private UiState state = UiState.MENU;

    private Canvas canvas;
    private GraphicsContext g;
    private AnimationTimer timer;

    // Game instance (created when Play is clicked)
    private ArkanoidGame game;

    // Menu buttons
    private MenuButton btnPlay, btnQuit;
    private double mouseX = -1, mouseY = -1;

    private long lastNs = 0;

    @Override
    public void start(Stage stage) {
        canvas = new Canvas(WIDTH, HEIGHT);
        g = canvas.getGraphicsContext2D();

        Scene scene = new Scene(new StackPane(canvas), WIDTH, HEIGHT);
        stage.setTitle("Arkanoid");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();

        // Input routing: menu gets mouse; game gets keys+mouse when active
        scene.setOnMouseMoved(e -> {
            mouseX = e.getX(); mouseY = e.getY();
            if (state == UiState.MENU) updateMenuHover(mouseX, mouseY);
            else if (state == UiState.GAME && game != null) game.onMouseMoved(mouseX, mouseY);
        });
        scene.setOnMouseClicked(e -> {
            if (state == UiState.MENU) handleMenuClick(e.getX(), e.getY());
            else if (state == UiState.GAME && game != null) game.onMouseClicked(e.getX(), e.getY());
        });
        scene.setOnKeyPressed(e -> {
            if (state == UiState.GAME && game != null) game.onKeyChanged(e.getCode(), true);
        });
        scene.setOnKeyReleased(e -> {
            if (state == UiState.GAME && game != null) game.onKeyChanged(e.getCode(), false);
        });

        // Build menu buttons
        createMenuButtons();

        // Main loop
        timer = new AnimationTimer() {
            @Override public void handle(long now) {
                if (lastNs == 0) lastNs = now;
                double dt = (now - lastNs) / 1_000_000_000.0;
                lastNs = now;

                if (state == UiState.MENU) {
                    drawMenu(g);
                } else {
                    // lazy init game on first frame after switching state
                    if (game == null) {
                        game = new ArkanoidGame();
                        game.initialize();
                    }
                    game.tick(g, dt);
                    if (game.consumeReturnToMenuRequest()) {
                        // drop back to main menu
                        state = UiState.MENU;
                        game = null;                 // GC old game
                        updateMenuHover(mouseX, mouseY); // refresh hover state in menu
                        return; // draw menu next frame
                    }
                }
            }
        };
        timer.start();
    }

    // -------- MENU: layout + drawing --------

    private void createMenuButtons() {
        double panelW = sx(420);
        double panelH = sx(320);
        double panelX = (WIDTH - panelW) / 2.0;
        double panelY = (HEIGHT - panelH) / 2.0;

        double btnW = panelW - sx(60);
        double btnH = sx(54);
        double x = panelX + sx(30);
        double y0 = panelY + sx(120);
        double gap = sx(18);

        btnPlay = new MenuButton("Play", x, y0, btnW, btnH);
        btnQuit = new MenuButton("Quit", x, y0 + btnH + gap, btnW, btnH);
    }

    private void updateMenuHover(double x, double y) {
        btnPlay.hovered = btnPlay.contains(x, y);
        btnQuit.hovered = btnQuit.contains(x, y);
    }

    private void handleMenuClick(double x, double y) {
        if (btnPlay.contains(x, y)) {
            state = UiState.GAME; // game created lazily in loop
        } else if (btnQuit.contains(x, y)) {
            Platform.exit();
        }
    }

    private void drawMenu(GraphicsContext g) {
        // background gradient (matches in-game vibes)
        var grad = new javafx.scene.paint.LinearGradient(
                0, 0, 0, 1, true,
                javafx.scene.paint.CycleMethod.NO_CYCLE,
                new javafx.scene.paint.Stop(0, Color.web("#0f172a")),
                new javafx.scene.paint.Stop(1, Color.web("#1e293b"))
        );
        g.setFill(grad);
        g.fillRect(0, 0, WIDTH, HEIGHT);

        // Center panel
        double panelW = sx(420);
        double panelH = sx(320);
        double x = (WIDTH - panelW) / 2.0;
        double y = (HEIGHT - panelH) / 2.0;
        double r = sx(18);

        g.setFill(Color.color(0.12, 0.15, 0.21, 0.94));
        g.fillRoundRect(x, y, panelW, panelH, r, r);
        g.setStroke(Color.color(1, 1, 1, 0.12));
        g.setLineWidth(1.5);
        g.strokeRoundRect(x, y, panelW, panelH, r, r);

        // Title
        g.setFill(Color.WHITE);
        g.setFont(Font.font(30 * SCALE));
        drawCentered(g, "ARKANOID", WIDTH / 2.0, y + sx(60));

        // Subtitle
        g.setFont(Font.font(14 * SCALE));
        g.setFill(Color.color(1, 1, 1, 0.75));
        drawCentered(g, "JavaFX Edition by Yoad Lotan", WIDTH / 2.0, y + sx(86));

        // Buttons
        drawMenuButton(g, btnPlay, Color.web("#60a5fa"), Color.web("#3b82f6"));
        drawMenuButton(g, btnQuit, Color.web("#f97316"), Color.web("#ea580c"));

        // Footer hint
        g.setFill(Color.color(1, 1, 1, 0.6));
        g.setFont(Font.font(12 * SCALE));
        drawCentered(g, "Mouse: hover & click • ESC pauses in-game", WIDTH / 2.0, y + panelH - sx(14));
    }

    private void drawMenuButton(GraphicsContext g, MenuButton b, Color base, Color hover) {
        // Main menu uses the default styling
        b.draw(g, base, hover); // default = 20*SCALE font, sx(12) radius
    }

    public static void main(String[] args) {
        launch(args);
    }
}
