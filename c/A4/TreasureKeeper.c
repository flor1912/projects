#include "TreasureKeeper.h"

#define INITIAL_MAP "###TT0000000"

char map_solution[12];

char *messages[] = {
    "Your quest awaits. May fortune favor your journey!\n",       // 0
    "Patience, adventurer. The quest has not yet started.\n",     // 1
    "No treasure found, search on.\n",                            // 2
    "Treasure discovered! Well done, adventurer!\n",              // 3
    "That's a trap! Watch your step next time!\n",                // 4
    "Can't explore this.\n",                                      // 5
    "Marked. That will help you to remember.\n",                  // 6
    "Can't mark this.\n",                                         // 7
    "I don't understand...\n",                                    // 8
    "Until the next adventure awaits!\n"};                        // 9

// TODO Student START
// you can add your own global variables here if needed
// (do not use the same names in Adventurer and TreasureKeeper because of testing reasons)

// TODO Student END

/******************************************************************
 * Main routine that contains the game loop. After the initial setup, 
 * requests are handled and answers are published to the Adventurer.
 * Stops when the exit command is received. Initialize everything needed 
 * in here. Checks in this function must not be relocated.
 * Do NOT use any synchronization mechanisms outside of the game loop.
 * 
 * @param none
 *
 * @return predefined exit codes
 */
int startTreasureKeeper()
{
  initSharedMemoriesTreasureKeeper();
  checkSHMTreasureKeeper();
  initMmapingsTreasureKeeper();
  checkMMAPTreasureKeeper();

  memcpy(map_solution, INITIAL_MAP, MAP_NR_COLUMNS*MAP_NR_ROWS);

  printf("[TREASURE KEEPER] Welcome, adventurer! Start your first quest, once you're ready.\n");
  do
  {
    // TODO Student START
   sem_wait(&(mmaps.mapped_region_locks_->sem_4));
    sem_post(&(mmaps.mapped_region_locks_->sem));
    sem_wait(&(mmaps.mapped_region_locks_->sem_2));
    
    strcpy(mmaps.mapped_region_response_->message,  cmdHandler(mmaps.mapped_region_request_));
    if (strcmp(mmaps.mapped_region_request_->message, "exit\n") == 0)
    {
      printf("[TREASURE KEEPER] %s\n", mmaps.mapped_region_response_->message);
      sem_post(&(mmaps.mapped_region_locks_->sem_3));
      break;
    }
    sem_post(&(mmaps.mapped_region_locks_->sem_3));
    // TODO Student END

  } while (strcmp(mmaps.mapped_region_request_->message, "exit\n") != 0);

  closeMmapingsTreasureKeeper();
  checkCleanup();

  return 0;

}

/******************************************************************
 * Initializes the TreasureKeepers shared objects and resizes them. Make 
 * sure to only give the permissions the TreasureKeeper needs. But please 
 * use MODERW, for compatibility with the testsystem.
 *
 * @param none
 *
 * @return none
 */
void initSharedMemoriesTreasureKeeper()
{
  // TODO Student START
  fds.fd_shm_request_ = shm_open(SHM_NAME_REQUEST, FLAGS_SHM_READONLY, MODERW);
  ftruncate(fds.fd_shm_request_, sizeof(shmrequest));

  fds.fd_shm_adventure_state_ = shm_open(SHM_NAME_ADVENTURE_STATE, FLAGS_SHM_READWRITE, MODERW);
  ftruncate(fds.fd_shm_adventure_state_, sizeof(shmadventurestate));

  fds.fd_shm_response_ = shm_open(SHM_NAME_RESPONSE, FLAGS_SHM_READWRITE, MODERW);
  ftruncate(fds.fd_shm_response_, sizeof(shmresponse));

  fds.fd_shm_locks_ = shm_open(SHM_NAME_LOCKS, FLAGS_SHM_READWRITE, MODERW);
  ftruncate(fds.fd_shm_locks_, sizeof(shmlocks));
  // TODO Student END
}

/******************************************************************
 * Maps the shared objects to the virtual memory space of the TreasureKeeper.
 * Don't do anything else in this function.
 *
 * @param none
 *
 * @return none
 */
void initMmapingsTreasureKeeper()
{
  if (checkProgressTreasureKeeper())
  {
    return;
  }

  // TODO Student START
  mmaps.mapped_region_request_ = mmap(NULL, sizeof(shmrequest), PROT_READ , MAP_SHARED, fds.fd_shm_request_, 0);
  mmaps.mapped_region_adventure_state_ = mmap(NULL, sizeof(shmadventurestate), PROT_READ| PROT_WRITE , MAP_SHARED, fds.fd_shm_adventure_state_, 0);
  mmaps.mapped_region_response_ = mmap(NULL, sizeof(shmresponse), PROT_READ | PROT_WRITE , MAP_SHARED, fds.fd_shm_response_, 0);
  mmaps.mapped_region_locks_ = mmap(NULL, sizeof(shmlocks), PROT_READ | PROT_WRITE, MAP_SHARED, fds.fd_shm_locks_, 0);


  // TODO Student END
}

/******************************************************************
 * Removes all mappings and shared objects as seen from the TreasureKeeper.
 * This part is an essential function for closing this application
 * accordingly without leaving artifacts on your system!
 *
 * @param none
 *
 * @return none
 */
void closeMmapingsTreasureKeeper()
{
  if (checkProgressTreasureKeeper())
  {
    return;
  }

  // TODO Student START
  if (munmap(mmaps.mapped_region_request_, sizeof(shmrequest)) == -1) {
      perror("munmap request");
  }
  if (munmap(mmaps.mapped_region_adventure_state_, sizeof(shmadventurestate)) == -1) {
      perror("munmap game state");
  }
  if (munmap(mmaps.mapped_region_response_, sizeof(shmresponse)) == -1) {
      perror("munmap response");
  }
  if (munmap(mmaps.mapped_region_locks_, sizeof(shmlocks)) == -1) {
      perror("munmap locks");
  }

  if (close(fds.fd_shm_request_) == -1) {
        perror("close");
    }

  if (close(fds.fd_shm_adventure_state_) == -1) {
      perror("close");
    }

  if (close(fds.fd_shm_response_) == -1) {
      perror("close");
    }

  if (close(fds.fd_shm_locks_) == -1) {
      perror("close");
    } 

  // TODO Student END
}

/******************************************************************
 * Handle the incoming requests and return the appropiate answer. 
 * Feel free to use the provided functions:
 * prepareQuest(), exploreField(), isQuestWon(), isQuestLost(), markField()
 *
 * @param shmrequest the pointer to the request object
 *
 * @return char* the answer to the request
 */
char *cmdHandler(shmrequest *request)
{
  // TODO Student START
  // respond to the incoming questions

  if (isCommandStartQuest(request->message))
  {
    mmaps.mapped_region_adventure_state_->quest_active = 1;
    prepareQuest(mmaps.mapped_region_adventure_state_->map);
    return messages[0]; 
  }
  else if (isCommandExplore(request->message))
  {
    if(mmaps.mapped_region_adventure_state_->quest_active == 1)
    {
      int temp = isCommandExplore(request->message) - 1;
      //printf("temp %d\n", temp);
        if (exploreField(mmaps.mapped_region_adventure_state_->map, temp))
        {
          if(isQuestWon(temp))
          {
            mmaps.mapped_region_adventure_state_->quest_active = 0;
            return messages[3];
          }
          else if(isQuestLost(temp))
          {
            mmaps.mapped_region_adventure_state_->quest_active = 0;
            return messages[4];
          }
          else
          {
            return messages[2];
          }
        }else
        {
          return messages[5];
        }
    }
    else
    {
      return messages[1];
    }
  }
  else if (isCommandMark(request->message))
  {
    if(mmaps.mapped_region_adventure_state_->quest_active == 1){
      int num = isCommandMark(request->message) - 1;
      if(markField(mmaps.mapped_region_adventure_state_->map, num))
      {
        return messages[6];
      }
      else
      {
        return messages[7];
      }
    }
    else
    {
      return messages[1];
    }  
  }
  else if (isCommandExit(request->message))
  {
    mmaps.mapped_region_adventure_state_->quest_active = 0;
    return messages[9];
  }

  // TODO Student END

  return messages[8];
}


/******************************************************************
 * Prepares the TreasureKeeper-internal map solution as well as the
 * public map for the new quest. The initial map solution is shuffled
 * and the map hints are updated depending on the number of surrounding
 * traps for each empty field of the solution.
 * On the public quest map, each field is reset to unexplored.
 *
 * @param map Pointer to the first element of the array representing
 *            the quest map
 *
 * @return none
 */
void prepareQuest(char* map)
{
  memcpy(map_solution, INITIAL_MAP, MAP_NR_FIELDS);
  shuffleMap(map_solution);
  updateMapHints(map_solution);
  
  resetQuestMap(map);
}

/******************************************************************
 * Attempts to explore the given map index. Checks if field is valid
 * target to explore (e.g. not already explored).
 *
 * @param map Pointer to the first element of the array representing
 *            the quest map
 * @param index Index of the field to explore
 *
 * @return 1 if success, otherwise 0
 */
int exploreField(char* map, int index)
{
  if (map[index] == CHAR_UNEXPLORED || map[index] == CHAR_MARKED)
  {
    map[index] = map_solution[index];
    return 1;
  }
  return 0;
}

/******************************************************************
 * Attempts to explore the given map index. Checks if field is valid
 * target to mark (e.g. not already explored).
 *
 * @param map Pointer to the first element of the array representing
 *            the quest map
 * @param index Index of the field to explore
 *
 * @return 1 if success, otherwise 0
 */
int markField(char* map, int index)
{
  if (map[index] == CHAR_UNEXPLORED || map[index] == CHAR_MARKED)
  {
    map[index] = CHAR_MARKED;
    return 1;
  }
  return 0;
}

/******************************************************************
 * Check if exploration of the given map index will lead to quest win.
 * This is the case if the field contains a treasure.
 *
 * @param map Pointer to the first element of the array representing
 *            the quest map
 * @param index Index of the field to explore
 *
 * @return 1 if won, otherwise 0
 */
int isQuestWon(int index)
{
  return (map_solution[index] == CHAR_TREASURE);
}

/******************************************************************
 * Check if exploration of the given map index will lead to quest loss.
 * This is the case if the field contains a trap.
 *
 * @param map Pointer to the first element of the array representing
 *            the quest map
 * @param index Index of the field to explore
 *
 * @return 1 if lost, otherwise 0
 */
int isQuestLost(int index)
{
  return (map_solution[index] == CHAR_TRAP);
}

/******************************************************************
 * Check if command is a valid start quest command.
 *
 * @param message pointer to the message
 *
 * @return 1 if valid, otherwise 0
 */
int isCommandStartQuest(char *message)
{
  return (strncmp(message, CMD_START, strlen(CMD_START)) == 0);
}

/******************************************************************
 * Get the passed index from a valid reveal command in case it is a valid number. Note 
 * that this function only returns indices inside the map bounds and can therefore
 * also be used to check the correctness of the reveal command. The reveal command itself
 * is not checked so additional checks are necessary.
 *
 * @param message pointer to the command
 * @param plain_command
 *
 * @return index if valid, otherwise 0
 */
int getRequestFieldIndex(char *message, char *plain_command)
{
  if(strlen(message) == (strlen(plain_command) + 2) 
      && isNumber(message[strlen(plain_command)]))
  {
    return message[strlen(plain_command)] - '0';
  }
  // two character
  else if(strlen(message) == (strlen(plain_command) + 3)
    && isNumber(message[strlen(plain_command)])
    && isNumber(message[strlen(plain_command) + 1]))
  {
    int field_index = (message[strlen(plain_command)] - '0') * 10 + (message[strlen(plain_command) + 1] - '0');
    return (field_index >= 1 && field_index <= 12) ? field_index : 0;
  }

  return 0;
}

/******************************************************************
 * Check if command is a valid explore command (keyword explore + space + 
 * number between 1-12)
 *
 * @param message pointer to the message
 *
 * @return field index if valid, otherwise 0
 */
int isCommandExplore(char *message)
{
  if (strncmp(message, CMD_EXPLORE, strlen(CMD_EXPLORE)) == 0)
    return getRequestFieldIndex(message, CMD_EXPLORE);
  else
    return 0;
}

/******************************************************************
 * Check if command is a valid mark command (keyword mark + space + 
 * number between 1-12)
 *
 * @param message pointer to the message
 *
 * @return field index if valid, otherwise 0
 */
int isCommandMark(char *message)
{
  if (strncmp(message, CMD_MARK, strlen(CMD_MARK)) == 0)
    return getRequestFieldIndex(message, CMD_MARK);
  else
    return 0;
}


/******************************************************************
 * Check if command is a valid exit command.
 *
 * @param message pointer to the message
 *
 * @return field index if valid, otherwise 0
 */
int isCommandExit(char *message)
{
  return (strncmp(message, CMD_EXIT, strlen(CMD_EXIT)) == 0);
}

