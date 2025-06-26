#include "Adventurer.h"

/******************************************************************
 * Main routine that contains the game loop. After the initial setup, 
 * requests are made and published to the TreasureKeeper. Stops if exit 
 * command is recognized. Initialize everything needed in here. Checks in 
 * this function must not be relocated.
 * Do NOT use any synchronization mechanisms outside of the game loop.
 *
 * @param none
 *
 * @return predefined exit codes
 */
int startAdventurer()
{
  char buffer[REQUEST_MAX_LENGTH];
  initSharedMemoriesAdventurer();
  checkSHMAdventurer();
  initMmapingsAdventurer();
  checkMMAPAdventurer();
  initLocks();
  initProcess();
  checkTreasureKeeper();

  // TODO Student START
  // init additional variables here if needed

  // TODO Student END

  do
  {
    // TODO Student START
    // use the provided function getCommand()
    // exit the loop if no command (EOF) is received or the command is 
    // 'exit' (exit still has to be handled by the TreasureKeeper)

    sem_wait(&(mmaps.mapped_region_locks_->sem));

    getCommand(buffer);
      
    if (feof(stdin)) {
      strcpy(mmaps.mapped_region_request_->message, "exit\n");
    }
    else
    {
      strcpy(mmaps.mapped_region_request_->message, buffer);
    }
    sem_post(&(mmaps.mapped_region_locks_->sem_2));
    sem_wait(&(mmaps.mapped_region_locks_->sem_3));
    sem_post(&(mmaps.mapped_region_locks_->sem_4));
    if (strcmp(mmaps.mapped_region_request_->message, "exit\n") == 0)
    {
      break;
    }

    // TODO Student END

    checkResults();

  } while (strcmp(buffer, CMD_EXIT) != 0);

  closeMmapingsAdventurer();
  checkCleanup();

  return 0;
}

/******************************************************************
 *  This function starts and initializes the TreasureKeeper process.
 *  Use the predefined variable process_id for the pid of the newly created
 *  process. You can ignore process_id in the TreasureKeeper process.
 *
 *  @param none
 *
 *  @return none
 */
void initProcess()
{
  if(checkSetup()) 
  {
    return;
  }

  // TODO Student START
  // start the TreasureKeeper process and load the right executable
  process_id = fork();
  char *args[] = {"./TreasureKeeper", NULL};
  if (process_id < 0) 
  {
    perror("Fork failed");
    exit(EXIT_FAILURE);
  } else if (process_id == 0)
  {
  // Child process
    if (execv("./TreasureKeeper", args) == -1) 
    {
      perror("Exec failed");
      exit(EXIT_FAILURE);
    }
    } else 
    {    
    // Parent process
    }
  
  // TODO Student END
}

/******************************************************************
 * Initializes your shared objects of the Adventurer and resizes them.
 * Make sure you only give the permissions the Adventurer needs. But please 
 * use MODERW, for compatibility with the testsystem.
 *
 * @param none
 *
 * @return none
 */
void initSharedMemoriesAdventurer()
{
    // TODO Student START
  fds.fd_shm_request_ = shm_open(SHM_NAME_REQUEST, FLAGS_SHM_READWRITE, MODERW);
  ftruncate(fds.fd_shm_request_, sizeof(shmrequest));

  fds.fd_shm_adventure_state_ = shm_open(SHM_NAME_ADVENTURE_STATE, FLAGS_SHM_READONLY, MODERW);
  ftruncate(fds.fd_shm_adventure_state_, sizeof(shmadventurestate));

  fds.fd_shm_response_ = shm_open(SHM_NAME_RESPONSE, FLAGS_SHM_READONLY, MODERW);
  ftruncate(fds.fd_shm_response_, sizeof(shmresponse));

  fds.fd_shm_locks_ = shm_open(SHM_NAME_LOCKS, FLAGS_SHM_READWRITE, MODERW);
  ftruncate(fds.fd_shm_locks_, sizeof(shmlocks));

    // TODO Student END
}

/******************************************************************
 * Maps the shared objects to the virtual memory space of the Adventurer
 * Don't do anything else in this function.
 *
 * @param none
 *
 * @return none
 */
void initMmapingsAdventurer()
{
  if (checkProgressAdventurer())
  {
    return;
  }

  // TODO Student START
  mmaps.mapped_region_request_ = mmap(NULL, sizeof(shmrequest), PROT_READ | PROT_WRITE, MAP_SHARED, fds.fd_shm_request_, 0);
  mmaps.mapped_region_adventure_state_ = mmap(NULL, sizeof(shmadventurestate), PROT_READ , MAP_SHARED, fds.fd_shm_adventure_state_, 0);
  mmaps.mapped_region_response_ = mmap(NULL, sizeof(shmresponse), PROT_READ , MAP_SHARED, fds.fd_shm_response_, 0);
  mmaps.mapped_region_locks_ = mmap(NULL, sizeof(shmlocks), PROT_READ | PROT_WRITE, MAP_SHARED, fds.fd_shm_locks_, 0);


  // TODO Student END
}

/******************************************************************
 * Initializes the locks of the shared object
 *
 * @param none
 *
 * @return none
 */
void initLocks()
{
  // TODO Student START
  if (sem_init(&(mmaps.mapped_region_locks_->sem), 1, 0) == -1) {
        perror("Error initializing semaphore");
        exit(EXIT_FAILURE);
    }

  if (sem_init(&(mmaps.mapped_region_locks_->sem_2), 1, 0) == -1) {
        perror("Error initializing semaphore");
        exit(EXIT_FAILURE);
    }  

  if (sem_init(&(mmaps.mapped_region_locks_->sem_3), 1, 0) == -1) {
        perror("Error initializing semaphore");
        exit(EXIT_FAILURE);
    }

  if (sem_init(&(mmaps.mapped_region_locks_->sem_4), 1, 1) == -1) {
      perror("Error initializing semaphore");
      exit(EXIT_FAILURE);
  }     


  // TODO Student END
}

/******************************************************************
 * Removes all mappings and shared objects as seen from the Adventurer
 * This part is an essential function for closing this application
 * accordingly without leaving artifacts on your system!
 *
 * @param none
 *
 * @return none
 */
void closeMmapingsAdventurer()
{
  if(checkProgress()) 
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


  if (shm_unlink(SHM_NAME_REQUEST) == -1) {
        perror("shm_unlink");
    }

  if (shm_unlink(SHM_NAME_ADVENTURE_STATE) == -1) {
        perror("shm_unlink");
    }

  if (shm_unlink(SHM_NAME_RESPONSE) == -1) {
        perror("shm_unlink");
    }

  if (shm_unlink(SHM_NAME_LOCKS) == -1) {
        perror("shm_unlink");
    }  


  // TODO Student END
}
