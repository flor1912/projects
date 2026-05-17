# Adventurer & TreasureKeeper IPC Quest Game

This systems programming project simulates a text-based interactive quest game between two processes—**Adventurer** and **TreasureKeeper**—using inter-process communication (IPC) mechanisms like shared memory, semaphores, and memory mappings.

---

## Overview

- **Adventurer**: Accepts user input and sends commands (like `start quest`, `explore`, `mark`, `exit`) to the TreasureKeeper.
- **TreasureKeeper**: Processes the command and manages the quest logic, sending back results.
- The game operates on a 3x4 map grid and mimics Minesweeper-like mechanics.

---

## Key Concepts & Technologies

- **IPC Mechanisms**:
  - `shm_open`, `mmap`, `sem_init`, and POSIX shared memory regions
- **Synchronization**:
  - `sem_t` semaphores (`sem`, `sem_2`, `sem_3`, `sem_4`) are used for coordination between processes
- **Memory Regions**:
  - Shared structures: `shmrequest`, `shmadventurestate`, `shmresponse`, `shmlocks`
- **Game Logic**:
  - Traps, treasures, explored/marked/unexplored cells, quest state
  - Commands are parsed and interpreted securely


## Game Flow

1. **Startup**:
   - `Adventurer` initializes shared memory and forks `TreasureKeeper`.
   - Memory mappings are created for request/response/adventure state/locks.

2. **Interaction Loop**:
   - `Adventurer` reads commands from user input (`getCommand`) and writes them into shared memory.
   - Synchronization via semaphores ensures mutual agreement and timely responses.

3. **Game Mechanics**:
   - `TreasureKeeper` maintains a hidden map and responds with hints.
   - Responses include trap detection, quest progress, and success/failure messages.
   - All outputs are shown to the user with updated map visuals.

4. **Termination**:
   - Typing `exit` terminates both processes cleanly and unmaps shared resources.


## Supported Commands

| Command | Description |
|---------|-------------|
| `start quest` | Initializes a new quest. |
| `explore N` | Explore field `N` (1–12). |
| `mark N` | Mark field `N` as suspicious. |
| `exit` | Quit the game. |

---

## Cleanup

All shared memory regions and semaphores are unmapped and unlinked at program termination to prevent resource leaks.

---

## Notes

- Semaphores are used to coordinate message flow and avoid race conditions.
- Shared memory objects are given appropriate read/write permissions.
- Memory mapping and unmapping must follow strict lifecycle rules.

---

## Sample Output

```
[TREASURE KEEPER] Welcome, adventurer! Start your first quest, once you're ready.
start quest
* * * *
* * * *
* * * *
explore 5
[TREASURE KEEPER] No treasure found, search on.
```

---

Educational use only — Developed for a systems programming assignment.