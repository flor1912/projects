# PALCLONE

**PALCLONE** is a terminal-based arcade game written in C, powered by `ncurses`. Steer your ever-moving player through a 2D grid, collect PALs to score points, and dodge enemies — all in real time.

---

## Gameplay

The player is **always moving** — there is no pause or stop. When the game starts, press `W` (or `↑`) to bring the player up into the visible play area.

From there, steer continuously to collect **PALs** (shown in magenta as `P`) and rack up points. But stay sharp:

- Touching **any enemy** (`E` or `R`) kills you **instantly**
- Leaving the **map bounds** also ends the game
- The more PALs you collect before dying, the higher your final score

---

## Controls

| Key         | Action     |
|-------------|------------|
| `↑` / `W`   | Move up    |
| `↓` / `S`   | Move down  |
| `←` / `A`   | Move left  |
| `→` / `D`   | Move right |
| `Q`         | Quit       |

---

## Features

- **Always-on movement** — the player never stops; steer to survive
- **PAL collection** — collect magenta `P` tokens to earn points
- **Instant death** — any contact with an enemy ends the game immediately
- **Multiple enemy types** — basic enemies (`E`) and rock enemies (`R`)
- **Enemy wrapping** — enemies loop around screen edges
- **Real-time game loop** via `pthreads`
- **Colorful terminal graphics** via `ncursesw`
- **Game over screen** with final score display

---

## Build Instructions

### Requirements

- `gcc`
- `make`
- `libpthread`
- `libncursesw`

### Linux

```bash
sudo apt install libncursesw5-dev   # Debian/Ubuntu
# or
sudo dnf install ncurses-devel      # Fedora/RHEL

make
./palclone
```

---

## Project Structure

```
palclone/
├── src/
│   ├── main.c        # Entry point and game loop
│   ├── player.c      # Player movement and state
│   ├── enemy.c       # Enemy logic and wrapping
│   ├── pal.c         # PAL spawning and collection
│   └── render.c      # ncurses rendering
├── include/
│   └── *.h
└── Makefile
```

