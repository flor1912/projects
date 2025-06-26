#include "util.h"

/******************************************************************
 * Reads from stdin and processes the string entered for an easier
 * handling of commands.
 *
 * @param char pointer to the buffer that will be processed
 *
 * @return int true/false when successfully read from the stdin
 */
int getCommand(char *buffer) 
{
  memset(buffer, 0, REQUEST_MAX_LENGTH * sizeof(char));
  if (fgets(buffer, REQUEST_MAX_LENGTH * sizeof(char), stdin) != NULL) {
    return 1;
  }
  else
    return 0;
}

/******************************************************************
 * Predefined function for testing. This is just a wrapper for testing
 * the specific results. You can extend, modify or remove our provided
 * output if you want.
 *
 * @param none
 *
 * @return none
 */
void checkResults()
{
  printf("[TREASURE KEEPER] %s\n", mmaps.mapped_region_response_->message);
  printMap();
}

/******************************************************************
 * Print game info. You can extend or modify the map printed format as 
 * you like.
 *
 * @param none
 *
 * @return none
 */
void printMap()
{
  if (mmaps.mapped_region_adventure_state_->quest_active)
  {
    for (int i = 1; i <= MAP_NR_COLUMNS*MAP_NR_ROWS; i++)
    {
      char current_char = mmaps.mapped_region_adventure_state_->map[i-1];

      printf("%c ", current_char);

      if(i % MAP_NR_COLUMNS == 0)
      {
        printf("\n");
      }
    }
  }
  printf("\n");
}

/******************************************************************
 * Wrapper function for testing the TreasureKeeper setup.
 *
 * @param none
 *
 * @return none
 */
void checkTreasureKeeper() 
{
  if (process_id == -1)
    exit(-3);
}

/******************************************************************
 * Checks the read- and write capabilities of the shared objects
 * initialized before.
 * You can extend our provided tests here if you want.
 *
 * @param none
 *
 * @return none
 */
void checkSHMrw(int fd) 
{
  if (fd == -1) 
  {
    exit(-1);
  }
}

/******************************************************************
 * Checks the read-only capabilities of the shared objects
 * initialized before.
 * You can extend our provided tests here if you want.
 *
 * @param none
 *
 * @return none
 */
void checkSHMro(int fd) 
{
  if (fd == -1) 
  {
    exit(-1);
  }
}

/******************************************************************
 * Wrapper function for testing the shared objects
 *
 * @param none
 *
 * @return none
 */
void checkSHMAdventurer() 
{
  // use checkSHMro and checkSHMrw here if you want
  checkSHMrw(fds.fd_shm_request_);
  checkSHMro(fds.fd_shm_adventure_state_);
  checkSHMro(fds.fd_shm_response_);
  checkSHMrw(fds.fd_shm_locks_);
}

/******************************************************************
 * Wrapper function for testing the shared objects
 *
 * @param none
 *
 * @return none
 */
void checkSHMTreasureKeeper() 
{
  // use checkSHMro and checkSHMrw here if you want
  checkSHMro(fds.fd_shm_request_);
  checkSHMrw(fds.fd_shm_adventure_state_);
  checkSHMrw(fds.fd_shm_response_);
  checkSHMrw(fds.fd_shm_locks_);
}

/******************************************************************
 * Predefined function for testing.
 * Wrapper function for testing the mmap capabilities.
 * You can extend our provided tests here if you want.
 *
 * @param none
 *
 * @return none
 */
void checkMMAPTreasureKeeper() 
{
  if (mmaps.mapped_region_response_ == NULL || mmaps.mapped_region_request_ == NULL || 
      mmaps.mapped_region_locks_ == NULL || mmaps.mapped_region_adventure_state_ == NULL) 
  {
    exit(-1);
  }
}

/******************************************************************
 * Predefined function for testing.
 * Wrapper function for testing the mmap capabilities.
 * You can extend our provided tests here if you want.
 *
 * @param none
 *
 * @return none
 */
void checkMMAPAdventurer() 
{
  if (mmaps.mapped_region_response_ == NULL || mmaps.mapped_region_request_ == NULL || 
      mmaps.mapped_region_locks_ == NULL || mmaps.mapped_region_adventure_state_ == NULL) 
  {
    exit(-1);
  }
}

/******************************************************************
 * Predefined function for testing. Checks if everything was cleaned
 * up properly in both processes. Add your own tests here if you want.
 *
 * @param none
 *
 * @return none
 */
void checkCleanup() 
{
}

/******************************************************************
 * Predefined function for testing.
 *
 * @param none
 *
 * @return none
 */
int checkSetup() 
{
  return 0;
}

/******************************************************************
 * Predefined function for testing.
 *
 * @param none
 *
 * @return none
 */
int checkProgress()
{
  return 0;
}

/******************************************************************
 * Predefined function for testing.
 *
 * @param none
 *
 * @return none
 */
int checkProgressTreasureKeeper()
{
  return 0;
}

/******************************************************************
 * Predefined function for testing.
 *
 * @param none
 *
 * @return none
 */
int checkProgressAdventurer()
{
  return 0;
}

/******************************************************************
 * Get a random field index on the map.
 *
 * @param none
 *
 * @return index
 */
int selectRandomField()
{
  return rand() % MAP_NR_FIELDS;
}

/******************************************************************
 * Shuffel the fields on the map. This function is part of preparing
 * the map for a new quest.
 *
 * @param map pointer to the first element of the array representing
 *            the map
 *
 * @return none
 */
void shuffleMap(char* map)
{
  for(int n = 0; n <= NUM_SHUFFLE_ROUNDS; n++)
  {
    int index_1 = selectRandomField();
    int index_2 = selectRandomField();

    char tmp = map[index_1];
    map[index_1] = map[index_2];
    map[index_2] = tmp;
  }
}

/******************************************************************
 * Update the hints on the map. This function is part of preparing
 * the map for a new quest.
 *
 * @param map Pointer to the first element of the array representing
 *            the map
 *
 * @return none
 */
void updateMapHints(char* map)
{
  for (int i = 0; i < MAP_NR_FIELDS; i++)
  {
    if (map[i] != CHAR_TRAP && map[i] != CHAR_TREASURE)
    {
      map[i] = '0' + getNumberSurroundingTraps(map, i);
    }
  }
}

/******************************************************************
 * Helper function to retrieve the number of traps surrounding the
 * specified field index.
 *
 * @param map Pointer to the first element of the array representing
 *            the map
 * @param index Index if the field
 *
 * @return number of surrounding traps
 */
int getNumberSurroundingTraps(char* map, int index)
{
  int column = index % MAP_NR_COLUMNS;
  int row = index / MAP_NR_COLUMNS;

  int number_traps = 0;

  for (int neighbor_row = row-1; neighbor_row <= row+1; neighbor_row++)
  {
    for (int neighbor_column = column-1; neighbor_column <= column+1; neighbor_column++)
    {
      int neighbor_index = neighbor_row*MAP_NR_COLUMNS+neighbor_column;
      if (isValidRowRange(neighbor_row)
        && isValidColumnRange(neighbor_column)
        && isValidMapRange(neighbor_index)
        && isTrap(map[neighbor_index]))
      {
        number_traps++;
      }
    }
  }

  return number_traps;
}

/******************************************************************
 * Helper function to check if passed index is a valid map index.
 *
 * @param index 
 *
 * @return 1 if valid, 0 otherwise
 */
int isValidMapRange(int index)
{
  return (index >= 0 && index < MAP_NR_FIELDS);
}

/******************************************************************
 * Helper function to check if passed row index is a valid map row.
 *
 * @param row
 *
 * @return 1 if valid, 0 otherwise
 */
int isValidRowRange(int row)
{
  return (row >= 0 && row < MAP_NR_ROWS);
}

/******************************************************************
 * Helper function to check if passed column index is a valid map 
 * column.
 *
 * @param column
 *
 * @return 1 if valid, 0 otherwise
 */
int isValidColumnRange(int column)
{
  return (column >= 0 && column < MAP_NR_COLUMNS);
}

/******************************************************************
 * Resets the map into initial state  i.e. all fields are unexplored.
 * This function is part of preparing the map for a new quest.
 *
 * @param map Pointer to the first element of the array representing
 *            the map
 *
 * @return none
 */
void resetQuestMap(char* map)
{
  memset(map, CHAR_UNEXPLORED, MAP_NR_FIELDS);
}

/******************************************************************
 * Check if the passed character is the trap-character.
 *
 * @param c character to check
 *
 * @return 1 if trap-character, 0 otherwise
 */
int isTrap(char c)
{
  return (c == CHAR_TRAP);
}

/******************************************************************
 * Check if the passed character is a valid number (0-9).
 *
 * @param c character to check
 *
 * @return 1 if number, 0 otherwise
 */
int isNumber(char c)
{
  return (c >= '0' && c <= '9');
}

/******************************************************************
 * Programs main function. Here for testing reasons.
 *
 * @param none
 *
 * @return none
 */
int main()
{
  #ifdef START
  return START();
  #endif
}

