#pragma once
#ifndef ASS4_ADVENTURER_H
#define ASS4_ADVENTURER_H

#include "util.h"

/*
 * Initialization of global variables
 */

filedescriptors fds = {-1, -1, -1, -1};
mappings mmaps = {NULL, NULL, NULL, NULL};
pid_t process_id = -1;

/*
 * Various headers for used functions in Adventurer.c
 */

int startAdventurer();
void initProcess();
void initSharedMemoriesAdventurer();
void initMmapingsAdventurer();
void initLocks();
int getCommand(char *buffer);
void closeMmapingsAdventurer();

#endif //ASS4_ADVENTURER_H
