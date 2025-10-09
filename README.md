# Arkanoid (Java 17 + JavaFX + Gradle)

[![Java CI](https://github.com/yoadlotan124/Arkanoid/actions/workflows/ci.yml/badge.svg)](https://github.com/yoadlotan124/Arkanoid/actions/workflows/ci.yml)

A modern, object-oriented Java recreation of the classic Arkanoid / Breakout arcade game. Originally built for a university OOP course; now refactored to JavaFX with cleaner architecture, improved physics, and a future-ready design.

---

## 🎮 Features
- Polished paddle & ball gameplay with brick destruction
- JavaFX AnimationTimer update loop (smooth 60 FPS feel)
- Precise collisions (ball ↔ walls, paddle, bricks)
- Event/listener system for scoring & block removal
- Difficulty selector + visual themes
- Modular OOP design (game, sprites, geometry, physics, events)
- Gradle build + GitHub Actions CI

---

## 🧠 Tech Highlights
- **Java 17 (LTS)** — no external game engine  
- **JavaFX 21** for rendering & input (via OpenJFX Gradle plugin)  
- **Gradle (Kotlin DSL)** with wrapper checked in  
- **JUnit 5** tests

---

## Requirements
- Java 17+ installed (java -version should show 17 or higher)
- No global Gradle needed (wrapper included)

---

## 🚀 Run & Build

Clone and run with Gradle:

```bash
1. Clone the repo:
git clone https://github.com/yoadlotan124/Arkanoid.git
cd Arkanoid

Run (dev)
./gradlew run        # macOS/Linux
gradlew run          # Windows

Build (tests + compile)
./gradlew clean build        # macOS/Linux
gradlew clean build          # Windows

Run tests
./gradlew test               # macOS/Linux
gradlew test                 # Windows

Create a distributable app
./gradlew installDist        # macOS/Linux
gradlew installDist          # Windows
```
---

## Ship a Runnable App (no installer)
Option A — Local distribution folder (recommended):
macOS/Linux:
./gradlew installDist

Windows:
gradlew installDist

Run it:
Windows: build\install\Arkanoid\bin\Arkanoid.bat
macOS/Linux: ./build/install/Arkanoid/bin/Arkanoid

Option B — Zip it:
macOS/Linux:
./gradlew distZip

Windows:
gradlew distZip

This produces build/distributions/Arkanoid.zip. Unzip, then run the script in bin/.

Note: JavaFX includes platform-native bits; build the zip on the same OS you plan to run for the smoothest experience.

---

## Controls:
- Mouse: Menu navigation (hover + click)

- In-game:
    Left / Right: Move paddle
    Esc: Pause menu (resume / return to lobby)

-HUD shows score and power-up timers.

---

Tests

Focused on geometry (e.g., Velocity.fromAngleAndSpeed, Rectangle.contains) and paddle bounce behavior (five-region mapping).
Run with:
./gradlew test (or: gradlew test on Windows)
