# Supermarket Concurrency Simulation

This project simulates the operation of a small supermarket with a focus on customer behavior, self-checkout machines, employee maintenance tasks, and a manager opening the store. It uses multi-threading and synchronization primitives to ensure correct and efficient interaction between all actors in the system.

## Overview

The simulation includes:
- **Customers**: Shop, pay at self-checkouts, and optionally buy coffee.
- **Self-Checkout Machines**: Serve customers, print receipts, and occasionally go out of service.
- **Employees**: Perform maintenance on out-of-service checkouts.
- **Manager**: Opens the supermarket, triggering the start of all other activities.

All entities are modeled as POSIX threads, with proper synchronization to avoid race conditions, deadlocks, and thread starvation.


## How It Works

### Flow of Execution

1. **Initialization**:
   - Threads are created for each customer, employee, self-checkout, and the manager.
   - Synchronization primitives (mutexes, condition variables, semaphores) are initialized.

2. **Store Opening**:
   - The manager thread opens the supermarket, notifying all waiting threads.

3. **Customer Behavior**:
   - Waits for the store to open.
   - Shops and searches for an available self-checkout.
   - Waits for and uses the checkout machine.
   - Optionally buys coffee if tempted.

4. **Self-Checkout Machines**:
   - Wait for a customer to start the checkout process.
   - Process scanning and payment.
   - Occasionally break down and wait for maintenance.

5. **Employees**:
   - Wait for the store to open.
   - Continuously check for out-of-service self-checkouts and repair them.

6. **Shutdown**:
   - After all customers finish, employees are cancelled.
   - Self-checkouts are gracefully turned off.

## Synchronization Strategy

- **Mutexes**: Protect access to shared data such as self-checkout state, coffee counters, and the open status of the supermarket.
- **Condition Variables**: Used for event-based waiting (e.g., waiting for supermarket to open or for a self-checkout to be available).
- **Semaphores**: Control access to limited resources (e.g., number of coffee counters, active self-checkouts).

## How to Run

### Compile

```bash
gcc -pthread -o supermarket main.c vector.c
```

### Run

```bash
./supermarket <num_customers> <num_employees> <num_selfcheckouts>
```

- `<num_customers>`: Number of customers (1–2000)
- `<num_employees>`: Number of employees (1–1000)
- `<num_selfcheckouts>`: Number of checkout machines (1–1000)

Example:

```bash
./supermarket 10 2 4
```

## Example Output

```
!! S U P E R M A R K E T   O P E N E D !!
[ OK ] Customer 0 can now shop
[ OK ] Customer 0 is looking for an available self-checkout
[ OK ] Customer 0 can now scan and pay.
...
!! S U P E R M A R K E T   C L O S E D !!
```

##Notes

- Proper locking ensures thread-safe operations.
- Assertions and debug messages are included to help identify unexpected states.
- The simulation uses randomized timings to mimic real-world delays.

## Cleanup

All resources (mutexes, condition variables, threads, vectors, etc.) are properly cleaned up at the end of the simulation.


This project was developed as part of a concurrency and systems programming assignment.