#ifndef ASS4_UTIL_H
#define ASS4_UTIL_H

#include <stdio.h>
#include <errno.h>
#include <stddef.h>
#include <stdlib.h>
#include <pthread.h>
#include <limits.h>
#include <semaphore.h>
#include <linux/filter.h>
#include <linux/audit.h>
#include <sys/ptrace.h>
#include <sys/types.h>
#include <sys/prctl.h>
#include <sys/mman.h>
#include <sys/stat.h>
#include <unistd.h>
#include <fcntl.h>
#include <string.h>
#include <sys/wait.h>

/*
 * Use those defines for the name required
 * to open shared objects
 */

#define SHM_NAME_REQUEST "/request"
#define SHM_NAME_RESPONSE "/response"
#define SHM_NAME_LOCKS "/locks"
#define SHM_NAME_ADVENTURE_STATE "/adventurestate"

/*
 * Use those defines for checking the commands
 */

#define CMD_START "start quest\n"
#define CMD_EXPLORE "explore "
#define CMD_MARK "mark "
#define CMD_EXIT "exit\n"

/*
 * Other game defines. Use whenever possible.
 */

#define RESPONSE_MAX_LENGTH 64
#define REQUEST_MAX_LENGTH 32
#define MAP_NR_ROWS 3
#define MAP_NR_COLUMNS 4
#define MAP_NR_FIELDS (MAP_NR_ROWS*MAP_NR_COLUMNS)

#define CHAR_UNEXPLORED '*'
#define CHAR_TRAP '#'
#define CHAR_TREASURE 'T'
#define CHAR_MARKED 'F'

#define NUM_SHUFFLE_ROUNDS 20

/*
 * Can be used for testing
 */

#define TEST_STR "DEAD"
#define LENGTH_TEST_STR 0x4

/*
 * Important and useful predefined values
 * for shared objects
 */

#define FLAGS_SHM_READONLY O_RDONLY | O_CREAT
#define FLAGS_SHM_READWRITE O_RDWR | O_CREAT
#define MODERW S_IRUSR | S_IWUSR

/*
 *  Error codes you can use for testing.
 */

#define ERROR_SHM_ADVENTURER -1
#define ERROR_MMAP_ADVENTURER -3
#define ERROR_RES_ADVENTURER -5
#define ERROR_UNMAP_CLOSE_ADVENTURER -7

#define ERROR_SHM_TREASUREKEEPER -2
#define ERROR_MMAP_TREASUREKEEPER -4
#define ERROR_REQ_TREASUREKEEPER -6
#define ERROR_UNMAP_CLOSE_TREASUREKEEPER -8


/*
 * file descriptors of your shared objects
 */
typedef struct
{
  int fd_shm_request_;
  int fd_shm_adventure_state_;
  int fd_shm_response_;
  int fd_shm_locks_;
} filedescriptors;

/*
 * shared objects for your locks / semaphores / cond variables (you don't necessarily need them all)
 */
typedef struct
{
  // TODO Student START
  sem_t sem;
  sem_t sem_2;
  sem_t sem_3;
  sem_t sem_4;  
  // TODO Student END
} shmlocks;

/*
 * the response struct
 * can be extended if wanted
 */
typedef struct
{
  char message[RESPONSE_MAX_LENGTH];
} shmresponse;

/*
 * the adventure state struct
 * can be extended if wanted
 */
typedef struct
{
  char map[12];
  int quest_active;
} shmadventurestate;

/*
 * the request struct
 * can be extended if wanted
 */
typedef struct
{
  char message[REQUEST_MAX_LENGTH];
} shmrequest;

/*
 *the according address pointing to the mapped region
 */
typedef struct
{
  shmrequest *mapped_region_request_;
  shmadventurestate *mapped_region_adventure_state_;
  shmresponse *mapped_region_response_;
  shmlocks *mapped_region_locks_;
} mappings;

/*
 * global variables.You MUST USE them!!!!
 */
extern filedescriptors fds;
extern pid_t process_id;
extern mappings mmaps;

/*
 * check functions you should not move in the code (you can implement your own functionality in util.c)
 */
void removeAllSHM();
void checkResults();
int checkSetup();
void checkSHMTreasureKeeper();
void checkMMAPTreasureKeeper();
void checkSHMAdventurer();
void checkMMAPAdventurer();
void checkTreasureKeeper();
void checkCleanup();

int checkProgress();
int checkProgressAdventurer();
int checkProgressTreasureKeeper();

void printMap();

extern int startAdventurer();
extern int startTreasureKeeper();

int selectRandomField();
void shuffleMap(char* map);
void updateMapHints(char* map);
int getNumberSurroundingTraps(char* map, int index);
int isValidMapRange(int index);
int isValidRowRange(int row);
int isValidColumnRange(int column);
void resetQuestMap(char* map);

int isTrap(char c);
int isNumber(char c);

/*
 * Useful functions to use
 */

int getCommand(char *buffer);

#endif //ASS4_UTIL_H
