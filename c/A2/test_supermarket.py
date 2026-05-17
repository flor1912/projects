#!/usr/bin/env python3
"""
test_supermarket.py — automated test suite for Supermarket Concurrency Simulation
"""

import subprocess
import os
import sys
import time
import shutil
import argparse

GREEN  = "\033[92m"
RED    = "\033[91m"
YELLOW = "\033[93m"
CYAN   = "\033[96m"
BOLD   = "\033[1m"
RESET  = "\033[0m"


def header(title):
    print(f"\n{BOLD}{CYAN}{'─' * 60}{RESET}")
    print(f"{BOLD}{CYAN}  {title}{RESET}")
    print(f"{BOLD}{CYAN}{'─' * 60}{RESET}\n")


def result(name, passed, detail="", duration=0):
    status = f"{GREEN}PASS{RESET}" if passed else f"{RED}FAIL{RESET}"
    ms = f"  {YELLOW}({duration*1000:.0f}ms){RESET}" if duration else ""
    print(f"  [{status}]  {name}{ms}")
    if not passed and detail:
        for line in detail.strip().splitlines()[:6]:
            print(f"           {RED}{line[:200]}{RESET}")


# ── 1. Compilation via CMake ──────────────────────────────────────────────────

def compile_project(project_dir):
    build_dir = os.path.join(project_dir, "build_test")
    os.makedirs(build_dir, exist_ok=True)
    t = time.monotonic()
    config = subprocess.run(
        ["cmake", ".."],
        cwd=build_dir,
        capture_output=True,
        text=True,
        timeout=30,
    )
    if config.returncode != 0:
        return False, "", build_dir, time.monotonic() - t, config.stderr

    build = subprocess.run(
        ["cmake", "--build", "."],
        cwd=build_dir,
        capture_output=True,
        text=True,
        timeout=60,
    )
    duration = time.monotonic() - t

    binary = os.path.join(build_dir, "supermarket")
    if build.returncode != 0 or not os.path.exists(binary):
        return False, "", build_dir, duration, build.stderr

    return True, binary, build_dir, duration, ""


# ── 2. Argument validation tests ──────────────────────────────────────────────

def test_no_arguments(binary):
    t = time.monotonic()
    proc = subprocess.run(
        [binary],
        capture_output=True, text=True, timeout=5
    )
    dur = time.monotonic() - t
    crashed = proc.returncode < 0
    detail = f"crashed with signal {-proc.returncode}" if crashed else ""
    return not crashed, detail, dur


def test_too_many_arguments(binary):
    t = time.monotonic()
    proc = subprocess.run(
        [binary, "10", "2", "4", "extra", "args"],
        capture_output=True, text=True, timeout=5
    )
    dur = time.monotonic() - t
    crashed = proc.returncode < 0
    detail = f"crashed with signal {-proc.returncode}" if crashed else ""
    return not crashed, detail, dur


def test_zero_customers(binary):
    t = time.monotonic()
    try:
        proc = subprocess.run(
            [binary, "0", "2", "4"],
            capture_output=True, text=True, timeout=5
        )
        dur = time.monotonic() - t
        crashed = proc.returncode < 0
        detail = f"crashed with signal {-proc.returncode}" if crashed else ""
        return not crashed, detail, dur
    except subprocess.TimeoutExpired:
        return False, "timed out — program hung on 0 customers", 5.0


def test_negative_arguments(binary):
    t = time.monotonic()
    try:
        proc = subprocess.run(
            [binary, "-1", "2", "4"],
            capture_output=True, text=True, timeout=5
        )
        dur = time.monotonic() - t
        crashed = proc.returncode < 0
        detail = f"crashed with signal {-proc.returncode}" if crashed else ""
        return not crashed, detail, dur
    except subprocess.TimeoutExpired:
        return False, "timed out on negative input", 5.0


# ── 3. Runtime behaviour tests ────────────────────────────────────────────────

def test_normal_run(binary, customers=10, employees=2, checkouts=4, timeout=15):
    t = time.monotonic()
    try:
        proc = subprocess.run(
            [binary, str(customers), str(employees), str(checkouts)],
            capture_output=True, text=True, timeout=timeout
        )
        dur = time.monotonic() - t
    except subprocess.TimeoutExpired:
        return False, f"timed out after {timeout}s — possible deadlock", timeout

    failures = []

    if proc.returncode != 0:
        failures.append(f"exit code {proc.returncode} (expected 0)")
    if "O P E N E D" not in proc.stdout.upper():
        failures.append("stdout missing: SUPERMARKET OPENED message")
    if "C L O S E D" not in proc.stdout.upper():
        failures.append("stdout missing: SUPERMARKET CLOSED message")

    detail = "\n".join(failures)
    return len(failures) == 0, detail, dur


def test_large_run(binary, timeout=100):
    t = time.monotonic()
    try:
        proc = subprocess.run(
            [binary, "50", "10", "20"],
            capture_output=True, text=True, timeout=timeout
        )
        dur = time.monotonic() - t
    except subprocess.TimeoutExpired:
        return False, f"timed out after {timeout}s with 50 customers — possible deadlock", timeout

    failures = []
    if proc.returncode != 0:
        failures.append(f"exit code {proc.returncode} (expected 0)")
    if "C L O S E D" not in proc.stdout.upper():
        failures.append("SUPERMARKET CLOSED never printed — simulation may have hung")

    return len(failures) == 0, "\n".join(failures), dur


def test_minimal_run(binary, timeout=10):
    t = time.monotonic()
    try:
        proc = subprocess.run(
            [binary, "1", "1", "1"],
            capture_output=True, text=True, timeout=timeout
        )
        dur = time.monotonic() - t
    except subprocess.TimeoutExpired:
        return False, "timed out on minimal run (1 1 1)", timeout

    passed = proc.returncode == 0 and "C L O S E D" in proc.stdout.upper()
    detail = "" if passed else f"exit {proc.returncode}, stdout:\n{proc.stdout[:300]}"
    return passed, detail, dur


# ── 4. ThreadSanitizer ────────────────────────────────────────────────────────

def test_thread_sanitizer(project_dir):
    tsan_dir = os.path.join(project_dir, "build_tsan")
    os.makedirs(tsan_dir, exist_ok=True)

    c_files = [
        os.path.join(project_dir, f)
        for f in os.listdir(project_dir)
        if f.endswith(".c")
    ]
    if not c_files:
        return False, "No .c files found for TSan compilation."

    t = time.monotonic()
    compile_proc = subprocess.run(
        ["gcc", "-fsanitize=thread", "-g", "-pthread", "-Wall",
         "-o", os.path.join(tsan_dir, "supermarket_tsan")] + c_files,
        capture_output=True, text=True, timeout=30,
    )
    if compile_proc.returncode != 0:
        return False, f"TSan compilation failed:\n{compile_proc.stderr}", time.monotonic() - t

    binary = os.path.join(tsan_dir, "supermarket_tsan")
    try:
        proc = subprocess.run(
            [binary, "10", "2", "4"],
            capture_output=True, text=True, timeout=20,
        )
    except subprocess.TimeoutExpired:
        return False, "TSan run timed out", time.monotonic() - t

    dur = time.monotonic() - t
    combined = proc.stdout + proc.stderr

    if "WARNING: ThreadSanitizer" in combined or "DATA RACE" in combined:
        race_lines = [
            l for l in combined.splitlines()
            if any(k in l for k in ["WARNING", "RACE", "Read", "Write", "Thread"])
        ]
        return False, "Data race detected:\n" + "\n".join(race_lines[:10]), dur

    return True, "", dur


def main():
    parser = argparse.ArgumentParser(description="Supermarket simulation test suite")
    parser.add_argument("--src", default=".", help="Path to project directory")
    parser.add_argument("--skip-tsan",     action="store_true")
    args = parser.parse_args()

    project_dir = os.path.abspath(args.src)
    header("Supermarket Concurrency Simulation — test suite")
    print(f"  Project: {project_dir}\n")

    passed_count = 0
    total_count  = 0
    build_dir    = None

    try:
        ok, binary, build_dir, dur, detail = compile_project(project_dir)
        result("Compilation (CMake)", ok, detail, dur)
        total_count += 1
        if ok:
            passed_count += 1
        else:
            print(f"\n{RED}  Compilation failed — skipping all other tests.{RESET}\n")
            sys.exit(1)

        header("Argument validation tests")

        for name, fn in [
            ("No arguments — no crash",       lambda: test_no_arguments(binary)),
            ("Too many arguments — no crash",  lambda: test_too_many_arguments(binary)),
            ("Zero customers — no hang/crash", lambda: test_zero_customers(binary)),
            ("Negative input — no hang/crash", lambda: test_negative_arguments(binary)),
        ]:
            ok, detail, dur = fn()
            result(name, ok, detail, dur)
            total_count  += 1
            passed_count += 1 if ok else 0

        header("Runtime behaviour tests")

        for name, fn in [
            ("Minimal run  (1 customer, 1 employee, 1 checkout)",
             lambda: test_minimal_run(binary)),
            ("Normal run   (10 customers, 2 employees, 4 checkouts)",
             lambda: test_normal_run(binary, 10, 2, 4)),
            ("Large run    (50 customers, 10 employees, 20 checkouts)",
             lambda: test_large_run(binary)),
        ]:
            ok, detail, dur = fn()
            result(name, ok, detail, dur)
            total_count  += 1
            passed_count += 1 if ok else 0

        header("Concurrency & memory safety tests")

        if not args.skip_tsan:
            ok, detail, dur = test_thread_sanitizer(project_dir)
            result("Thread safety — no data races (TSan)", ok, detail, dur)
            total_count  += 1
            passed_count += 1 if ok else 0
        else:
            print(f"  {YELLOW}[SKIP]{RESET}  ThreadSanitizer (--skip-tsan)")

    finally:
        for d in ["build_test", "build_tsan"]:
            path = os.path.join(project_dir, d)
            if os.path.exists(path):
                shutil.rmtree(path, ignore_errors=True)

    colour = GREEN if passed_count == total_count else RED
    print(f"\n{BOLD}{'─' * 60}{RESET}")
    print(f"{BOLD}  Result: {colour}{passed_count}/{total_count} tests passed{RESET}")
    print(f"{BOLD}{'─' * 60}{RESET}\n")

    sys.exit(0 if passed_count == total_count else 1)


if __name__ == "__main__":
    main()
