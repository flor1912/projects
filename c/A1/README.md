# PALCLONE

**PALCLONE** is a terminal-based, ncurses-powered game written in C. It features real-time interaction between the player, enemies, and collectibles (referred to as "PALs") within a 2D grid-based world.

---

## Game Overview

- Move your player (`P`) across the screen.
- Avoid enemies (`E` and `R`) while collecting the PAL (`P` in magenta).
- Survive as long as possible to score points.
- The game ends when the player loses all life points (i.e., touches an enemy or leaves the map bounds).

---

## Features

- Real-time game loop using pthreads
- Colorful terminal graphics via `ncurses`
- Multiple enemy types (basic enemies, rock enemies)
- Player and PAL positioning
- Screen edge wrapping for enemies
- Point tracking system
- Game over screen with final score

---

## Build Instructions

Ensure you have `gcc`, `make`, `pthread`, and `ncursesw` installed.

### Linux
