# Arkanoid (Java 17 + Gradle)

[![Java CI](https://github.com/yoadlotan124/Arkanoid/actions/workflows/ci.yml/badge.svg)](https://github.com/yoadlotan124/Arkanoid/actions/workflows/ci.yml)


A modern, object-oriented Java recreation of the classic **Arkanoid / Breakout** arcade game.  
Originally built for a university OOP course, now refactored with a cleaner architecture, improved physics, and future-ready design.

---

## 🎮 Features
- Paddle + ball gameplay with brick destruction  
- Deterministic fixed-timestep update loop  
- Precise collisions (ball ↔ walls, paddle, bricks)  
- Event/listener system for scoring and block removal  
- Modular OOP design (Game, Ball, Paddle, Brick, Physics, Renderer)  
- Easy to extend with levels, power-ups, sounds, and HUD

---

## 🧠 Tech Highlights
- **Pure Java 17** — no external game engine  
- **BIUOOP 1.4** for simple rendering & input  
- **Gradle (Kotlin DSL)** build system with wrapper checked-in  
- **JUnit 5** for unit testing  
- **Package separation:** `geometry`, `physics`, `sprites`, `game`, `notifiers`

---

## 🚀 Run & Build

Clone and run with Gradle (no manual `javac` or Ant needed):

```bash
git clone https://github.com/yoadlotan124/Arkanoid.git
cd Arkanoid

# build & run
./gradlew clean run
