# Arkanoid (Java)

A modern, object-oriented Java recreation of the classic **Arkanoid / Breakout** arcade game.  
Originally built for a university OOP course; now being refactored for cleaner architecture, accurate physics, and future features.

## 🎮 Features
- Paddle + ball gameplay with brick destruction
- Deterministic timing (fixed-timestep update loop)
- Precise collision handling (ball vs walls, paddle, bricks)
- Clean OOP design (`Game`, `Ball`, `Paddle`, `Brick`, `Physics`, `Renderer`)
- Easy to extend (levels, power-ups, HUD, sounds)

## 🧠 Tech Highlights
- Pure Java — no external game engine
- Collision via AABB + reflection (moving circle vs rectangles)
- Separation of concerns: game loop, physics, rendering, input
- Build scripts included (`build.xml`), or compile with `javac`

## 🚀 Run locally

### Option A — Compile/Run with `javac` (no IDE)
```bash
git clone https://github.com/yoadlotan124/Arkanoid.git
cd Arkanoid
# If using the provided BIU jar for current GUI (temporary):
# Windows (PowerShell/cmd):
javac -cp lib/biuoop-1.4.jar -d out src/**/*.java
java -cp "out;lib/biuoop-1.4.jar" your.main.package.Main

# macOS/Linux:
javac -cp lib/biuoop-1.4.jar -d out $(find src -name "*.java")
java -cp "out:lib/biuoop-1.4.jar" your.main.package.Main
