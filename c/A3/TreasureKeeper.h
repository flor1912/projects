#pragma once
#ifndef ASS4_TREASUREKEEPER_H
#define ASS4_TREASUREKEEPER_H

#include "util.h"

/*
 * Initialization of global variables
 */

filedescriptors fds = { -1, -1, -1, -1};
mappings mmaps = {NULL, NULL, NULL, NULL};
pid_t process_id = -1;

/*
 * Various headers for used functions in TreasureKeeper.c
 */

int startTreasureKeeper();
void initSharedMemoriesTreasureKeeper();
void initMmapingsTreasureKeeper();
void closeMmapingsTreasureKeeper();
char* cmdHandler(shmrequest* request);

void prepareQuest(char* map);
int exploreField(char* map, int index);
int markField(char* map, int index);

int isQuestWon(int index);
int isQuestLost(int index);

int isCommandStartQuest(char *message);
int getRequestFieldIndex(char *message, char *command);
int isCommandExplore(char *message);
int isCommandMark(char *message);
int isCommandExit(char *message);

#endif //ASS4_TREASUREKEEPER_H
