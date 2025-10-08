# Arkanoid (Java 17 + JavaFX + Gradle)

[![Java CI](https://github.com/yoadlotan124/Arkanoid/actions/workflows/ci.yml/badge.svg)](https://github.com/yoadlotan124/Arkanoid/actions/workflows/ci.yml)

A modern, object-oriented Java recreation of the classic **Arkanoid / Breakout** arcade game.  
Originally built for a university OOP course; now refactored to **JavaFX** with cleaner architecture, improved physics, and a future-ready design.

---

## 🎮 Features
- Paddle + ball gameplay with brick destruction  
- Fixed-timestep style update (driven by JavaFX `AnimationTimer`)  
- Precise collisions (ball ↔ walls, paddle, bricks)  
- Event/listener system for scoring and block removal  
- Modular OOP design (`game`, `sprites`, `geometry`, `physics`, `events`)  
- Clean Gradle build + GitHub Actions CI

---

## 🧠 Tech Highlights
- **Java 17 (LTS)** — no external game engine  
- **JavaFX 17** for rendering & input (via OpenJFX Gradle plugin)  
- **Gradle (Kotlin DSL)** with wrapper checked in  
- **JUnit 5** tests

---

## 🚀 Run & Build

Clone and run with Gradle:

```bash
git clone https://github.com/yoadlotan124/Arkanoid.git
cd Arkanoid
