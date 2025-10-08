# Arkanoid (Java)

A modern, object-oriented Java recreation of the classic **Arkanoid / Breakout** arcade game.  
Built as part of my university OOP course and later refactored for clean architecture, physics accuracy, and gameplay polish.

## 🎮 Features
- 🧱 **Fully functional gameplay** — control a paddle to bounce the ball and destroy bricks.
- ⚙️ **Physics engine** — realistic ball reflection, angle-based control from paddle hits.
- 💥 **Collision system** — handles multiple bricks, edges, and corner cases with precision.
- 🧩 **Object-Oriented Design** — clean class hierarchy for entities, game state, and levels.
- ⏱️ **Smooth frame timing** — consistent movement using a fixed update rate.
- 🌈 **Expandable architecture** — ready for power-ups, multiple levels, or scoring systems.

## 🧠 Technical Highlights
- Implemented in **pure Java**, no external game engines.
- Designed around **composition and inheritance** for clean extensibility.
- Collision handled via **axis-aligned bounding boxes (AABB)** and reflection vectors.
- Clear separation of concerns:
  - `Game` — main loop and state management  
  - `Ball`, `Paddle`, `Brick` — independent entities  
  - `Physics` — movement and collision logic  
  - `Renderer` — handles display and refresh  

## 🚀 Future Improvements
- Power-ups (multi-ball, expand paddle, slow motion)
- Level loader (JSON or text-based)
- Particle and sound effects
- Score tracking and HUD

## 🖥️ How to Run
1. Clone this repository  
   ```bash
   git clone https://github.com/<yoadlotan124>/Arkanoid.git
