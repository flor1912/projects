#include <stdio.h>
#include <stdbool.h>
#include <stdlib.h>
#include <time.h>
#include <unistd.h>

#include "definitions.h"

// ------------------------------------------------------------------------
// STUDENT TODO: define synchronization primitives here

pthread_mutex_t mutexOpen = PTHREAD_MUTEX_INITIALIZER;
pthread_cond_t condOpen = PTHREAD_COND_INITIALIZER;

pthread_cond_t condClose = PTHREAD_COND_INITIALIZER;

pthread_mutex_t extra_mutex = PTHREAD_MUTEX_INITIALIZER;

pthread_mutex_t free_selfcheckouts_mutex = PTHREAD_MUTEX_INITIALIZER;
pthread_cond_t free_selfcheckouts_cond = PTHREAD_COND_INITIALIZER;

pthread_mutex_t coffee_mutex = PTHREAD_MUTEX_INITIALIZER;

sem_t semaphore1;
sem_t semaphore4;
sem_t semaphore2;
sem_t semaphore5;
sem_t semaphore6;
sem_t semaphore;
sem_t sem_coffee;

// ------------------------------------------------------------------------

// ------------------------------------------------------------------------
/* STUDENT TODO:
 * Ensure that shared resources are locked correctly to prevent data races!
 * Implement reasonable synchronization strategies to avoid thread starvation!
 */

void takeACoffeeToGo(Customer* customer) 
{
  int i = 0;

  printf("[ OK ] Customer %zd is waiting in line for coffee\n", customer->id);
  sem_wait(&sem_coffee);
  pthread_mutex_lock(&coffee_mutex);
  
  while (coffee_counters[i] != NULL) 
  {
    i++;
  }

  coffee_counters[i] = customer;
  pthread_mutex_unlock(&coffee_mutex);
  
  buyCoffee();

  pthread_mutex_lock(&coffee_mutex);
  coffee_counters[i] = NULL;
  pthread_mutex_unlock(&coffee_mutex);
  sem_post(&sem_coffee);

  printf("[ OK ] Customer %zd can finally go home now! ;D\n", customer->id);
}

// ------------------------------------------------------------------------
/* STUDENT TODO:
 * Ensure that potential shared resources are locked correctly to prevent data races!
 */

vector_iterator findFreeSelfCheckout()  
{
  vector_iterator it = vector_begin(&free_selfcheckouts);
  vector_iterator it_end = vector_end(&free_selfcheckouts);

  for (; it != it_end; ++it) 
  {
    SelfCheckout* checkout = (SelfCheckout*)*it;
    pthread_mutex_lock(&checkout->mutex);
    if (checkout->state != OUT_OF_SERVICE)
    {
      pthread_mutex_unlock(&checkout->mutex);
      return it;
    }
    pthread_mutex_unlock(&checkout->mutex);
  }
  return NULL;
}

// ------------------------------------------------------------------------
/* STUDENT TODO:
 * Ensure that potential shared resources are locked correctly to prevent data races!
 */
vector_iterator findSelfCheckoutToBeMaintained()  
{ 
  vector_iterator it = vector_begin(&free_selfcheckouts);
  vector_iterator it_end = vector_end(&free_selfcheckouts);

  for (; it != it_end; ++it) 
  {
    SelfCheckout* checkout = (SelfCheckout*)*it;
    pthread_mutex_lock(&checkout->mutex);
    if (checkout->state == OUT_OF_SERVICE)
    {
      pthread_mutex_unlock(&checkout->mutex);
      return it;
    }
    pthread_mutex_unlock(&checkout->mutex);
  }
  
  return NULL;
}

// ------------------------------------------------------------------------
/* STUDENT TODO:
 * Ensure that existing shared resources are locked correctly to prevent data races!
 * Implement reasonable synchronization strategies to ensure that every customer finds an available self-checkout!
 * Verify that the self-checkout/customer interaction aligns with the assignment description and follow-up comments!
 */

void goToTheSelfCheckout(Customer* customer)  
{
  
  sem_wait(&semaphore4);
  pthread_mutex_lock(&free_selfcheckouts_mutex);
 
  printf("[ OK ] Customer %zd is looking for an available self-checkout\n", customer->id);

  // 1) Find an available self-checkout station in the self-checkout lane
  
  vector_iterator self_checkout_it = findFreeSelfCheckout();


  SelfCheckout* self_checkout = (SelfCheckout*)*self_checkout_it;

  if(self_checkout->customer) 
  {
    printf("[OOPS] This is a super- not a wildmarket! Please, wait for your turn @Customer %zd \n", customer->id);
  }

  
  self_checkout->customer = customer;

  vector_erase(&free_selfcheckouts, self_checkout_it);

  pthread_mutex_unlock(&free_selfcheckouts_mutex);

  assert(self_checkout->state == READY && "What is going on?!"); // consider using asserts for debugging ;)
  
  // 2) Initiate the self-checkout process (state = START)
  pthread_mutex_lock(&self_checkout->mutex);
  self_checkout->state = START;
  pthread_cond_signal(&self_checkout->cond); 


  while (self_checkout->state != SCAN_AND_PAY) {
       pthread_cond_wait(&self_checkout->cond, &self_checkout->mutex); 
    } 
  
  if(self_checkout->state == SCAN_AND_PAY)
  {
    printf("[ OK ] Customer %zd can now scan and pay.\n", customer->id);
    scanAndPay();

    // 4) Complete paying (state = PAYING_COMPLETED) to proceed
    self_checkout->state = PAYING_COMPLETED;
    pthread_cond_signal(&self_checkout->cond);
    pthread_mutex_unlock(&self_checkout->mutex);
  }
  
  //sem_wait(&semaphore5);
  pthread_mutex_lock(&self_checkout->mutex);
   while ((self_checkout->customer == customer || self_checkout->state != READY)) {
       pthread_cond_wait(&self_checkout->cond, &self_checkout->mutex);
    } 
  // 5) Wait for the receipt ==> state = READY + customer reassignment happened 
  if(self_checkout->customer == customer || self_checkout->state != READY)  
  {
    printf("[OOPS] Heeey customer %zd! Please, wait for the receipt!\n", customer->id);
  } 
  
  // [ !! ] Be cautious of potential state logic issues introduced by the chosen locking strategy
  printf("[ OK ] Customer %zd received the receipt and can go!\n", customer->id);
  pthread_mutex_unlock(&self_checkout->mutex);
  sem_post(&semaphore6);


}

// ------------------------------------------------------------------------
/* STUDENT TODO:
 * Ensure that potential shared resources are locked correctly to prevent data races!
 * Implement synchronization strategy to prevent customers from starting shopping before the supermarket opens!
 */
void customer(Customer* customer) 
{
  pthread_mutex_lock(&mutexOpen);
  while(supermarket_opened != true){
    pthread_cond_wait(&condOpen, &mutexOpen);
  }
  pthread_mutex_unlock(&mutexOpen);

  if(!supermarket_opened)  
  {
    printf("[OOPS] Customer %zd is trying to enter even though the supermarket is closed...\n", customer->id);
  }

  // 1) Customer arrives and starts shopping
  
  printf("[ OK ] Customer %zd can now shop\n", customer->id);

  goToTheSelfCheckout(customer);

  printf("[ OK ] Customer %zd is done shopping and would go home now...\n", customer->id);
  
  // 2) Eventually buy a coffee-to-go at the supermarket's coffee shop
  if(coffeeSmellsPhantastic())
  {
    printf("[ OK ] ... if the coffee didn't smell soo good...\n");
    takeACoffeeToGo(customer);
  }

}

// ------------------------------------------------------------------------
/* STUDENT TODO:
 * Ensure that potential shared resources are locked correctly to prevent data races!
 * Implement synchronization strategy to prevent employees from starting shopping before the supermarket opens!
 * Ensure that your locking approach does not introduce any problems upon cancellation!
 */
void employee(Employee* employee)
{

  pthread_mutex_lock(&mutexOpen);
  while(supermarket_opened != true){
    pthread_cond_wait(&condOpen, &mutexOpen);
  }
  pthread_mutex_unlock(&mutexOpen);

  if(!supermarket_opened)
  {
    printf("[OOPS] Employee %zd is working even though the supermarket is closed!\n", employee->id);
  }

  while (1) 
  {
    
    // 1) Check the self-checkout machines in the self-rythmus (no notification needed)
    pthread_mutex_lock(&extra_mutex);
    vector_iterator self_checkout_it = findSelfCheckoutToBeMaintained();
    if (!self_checkout_it) 
    {
      printf("[ OK ] Employee %zd has nothing to do...\n", employee->id);
      pthread_mutex_unlock(&extra_mutex);
      coffeeTime();
      
      continue;
    }
   
    //sem_wait(&semaphore);
    SelfCheckout* self_checkout = (SelfCheckout*)*self_checkout_it;

    //pthread_mutex_lock(&self_checkout->mutex);

    vector_erase(&free_selfcheckouts, self_checkout_it);
    pthread_mutex_unlock(&extra_mutex);

    //pthread_mutex_unlock(&self_checkout->mutex);
    // 2) Perform maintenance on an out-of-service self-checkout (==> state = READY)
    printf("[ OK ] Employee %zd is performing maintenance on self-checkout %zd.\n", employee->id, self_checkout->id); 
    
    maintenance();
    
    pthread_mutex_lock(&self_checkout->mutex);
    self_checkout->state = READY;
    //assert(self_checkout->state == READY && "What is going on?!");
    // 3) Ensure the self-checkout is again available for active usage
    vector_push_back(&free_selfcheckouts, self_checkout);
    //sem_post(&semaphore2);
    pthread_cond_signal(&self_checkout->close);
    //sem_post(&semaphore); 
    pthread_mutex_unlock(&self_checkout->mutex);

  }
}

// ------------------------------------------------------------------------
/* STUDENT TODO:
 * Ensure that existing shared resources are locked correctly to prevent data races!
 * Implement synchronization startegy to prevent self-checkout machines from starting operation before the supermarket opens!
 * Verify that the self-checkout/customer interaction aligns with the assignment description and follow-up comments!
 */

void selfCheckout(SelfCheckout* self_checkout) 
{
  pthread_mutex_lock(&mutexOpen);
  while(supermarket_opened != true){
    pthread_cond_wait(&condOpen, &mutexOpen);
  }
  pthread_mutex_unlock(&mutexOpen);

  
  if(!supermarket_opened)
  {
    printf("[OOPS] This business has no future! Self-checkout %zd is operational even though the supermarket is closed.\n", self_checkout->id);
  }

  while(true)
  {
    
    printf("[ OK ] Self-Checkout %zd: Welcome!! Please press Start!!\n", self_checkout->id);
    //sem_post(&semaphore5);
    pthread_mutex_lock(&self_checkout->mutex);
    while (self_checkout->state != START && self_checkout->state != OFF) {
       pthread_cond_wait(&self_checkout->cond, &self_checkout->mutex); 
    }  
    
    // 1) Wait for the start action (==> customer assignment + state = START) or supermarket closing (state = OFF)
    if(self_checkout->state == OFF)
    {
      pthread_mutex_unlock(&self_checkout->mutex);
      break;
    }
    if(self_checkout->state != START)
    {
      printf("[OOPS] Self-Checkout %zd may need maintenance! Proceeding without waiting a new customer.\n", self_checkout->id);
    }

    printf("[ OK ] Self-Checkout %zd: Please scan your products!\n", self_checkout->id);
 
    self_checkout->state = SCAN_AND_PAY;
    pthread_cond_signal(&self_checkout->cond);
    pthread_mutex_unlock(&self_checkout->mutex);

    pthread_mutex_lock(&self_checkout->mutex);
    while (self_checkout->state != PAYING_COMPLETED) {
       pthread_cond_wait(&self_checkout->cond, &self_checkout->mutex); // Wait until payment is completed
    }
    // 3) Wait for the customer to scan purchased products and complete payment
    if(self_checkout->state != PAYING_COMPLETED)
    {
      printf("[OOPS] Self-Checkout %zd: Waiting for payment...\n", self_checkout->id);
    }
    pthread_mutex_unlock(&self_checkout->mutex);

    printReceiptAndFinish();

    // 3) Print the receipt and complete the procedure (switch back to READY state).
    pthread_mutex_lock(&self_checkout->mutex);
    self_checkout->customer = NULL;
    self_checkout->state = READY;

    //pthread_cond_signal(&self_checkout->cond);
    vector_push_back(&free_selfcheckouts, self_checkout);

    printf("[ OK ] Self-Checkout %zd: Thank you! Bye!\n", self_checkout->id);

    // //4) Unfortunately, some self-checkouts may be out of service from time to time.
    if(outOfService())  
    {
      
      self_checkout->state = OUT_OF_SERVICE; 
      printf("[ OK ] Self-Checkout %zd: Ohh, no! I am out of service :(\n", self_checkout->id);
       
      while (self_checkout->state != READY) {
       pthread_cond_wait(&self_checkout->close, &self_checkout->mutex);
    }
      //pthread_cond_wait(&condClose, &self_checkout->mutex);
      pthread_cond_signal(&self_checkout->cond);
 
      pthread_mutex_unlock(&self_checkout->mutex);
      sem_wait(&semaphore6);
      sem_post(&semaphore4);

      continue;
    } 
    self_checkout->state = READY;
    pthread_cond_signal(&self_checkout->cond);
    pthread_mutex_unlock(&self_checkout->mutex);
    sem_wait(&semaphore6);
    sem_post(&semaphore4);
   
  }
   
}

// ------------------------------------------------------------------------
/* STUDENT TODO:
 * Ensure that existing shared resources are locked correctly to prevent data races!
 * Implement synchronization startegy for the manager to notify all other actors about the supermarket opening!
 */

void manager_open() 
{
  // 1) open the supermarket
  pthread_mutex_lock(&mutexOpen);

  printf("!! S U P E R M A R K E T   O P E N E D !!\n");

  supermarket_opened = true;
  
  pthread_cond_broadcast(&condOpen);
  pthread_mutex_unlock(&mutexOpen); 
}

// ------------------------------------------------------------------------
/* STUDENT TODO:
 * Ensure that potential shared resources are locked correctly to prevent data races!
 * Initialize and destroy mutexes, condition variables, and semaphores as necessary!
 * --> but feel free to do so in other parts of the codebase as well
 */
int main(int argc, char* argv[]) 
{
  srand(time(NULL));

  ssize_t num_customers;
  ssize_t num_employees;
  ssize_t num_selfcheckouts;

  handleArguments(argc, argv, &num_customers, &num_employees, &num_selfcheckouts);

  Customer* customers = malloc(num_customers * sizeof(Customer));
  Employee* employees = malloc(num_employees * sizeof(Employee));
  SelfCheckout** selfcheckouts = malloc(num_selfcheckouts * sizeof(SelfCheckout*));
 

  Employee* manager = malloc(sizeof(Employee));
  
  if (!customers || !employees || !selfcheckouts || !manager) 
  {
    free(customers);
    free(employees);
    fprintf(stderr, "Could not allocate memory!\n");
    exit(ERROR);
  }

  pthread_mutex_init(&mutexOpen, NULL);
  pthread_cond_init(&condOpen, NULL);


  if (sem_init(&semaphore1, 0, 0) != 0) {
        perror("Semaphore initialization failed");
        exit(EXIT_FAILURE);
    }
     if (sem_init(&semaphore4, 0, num_selfcheckouts) != 0) {
        perror("Semaphore initialization failed");
        exit(EXIT_FAILURE);
    }    

    if (sem_init(&semaphore2, 0, 1) != 0) {
        perror("Semaphore initialization failed");
        exit(EXIT_FAILURE);
    } 

   if (sem_init(&semaphore5, 0, 0) != 0) {
        perror("Semaphore initialization failed");
        exit(EXIT_FAILURE);
    }  
     if (sem_init(&semaphore6, 0, 0) != 0) {
        perror("Semaphore initialization failed");
        exit(EXIT_FAILURE);
    } 

    if (sem_init(&semaphore, 0, 1) != 0) {
        perror("Semaphore initialization failed");
        exit(EXIT_FAILURE);
    } 

     if (sem_init(&sem_coffee, 0, NUMBER_COFFEE_COUNTERS) != 0) {
        perror("Semaphore initialization failed");
        exit(EXIT_FAILURE);
    }          
  
  // ~ create the threads cast 
  createCustomers(customers, num_customers, (void*(*)(void*))customer);
  createEmployees(employees, num_employees, (void*(*)(void*))employee);
  createSelfCheckouts(selfcheckouts, num_selfcheckouts, (void*(*)(void*))selfCheckout);
   for (ssize_t i = 0; i < num_selfcheckouts; i++) {
        pthread_mutex_init(&selfcheckouts[i]->mutex, NULL);
        pthread_cond_init(&selfcheckouts[i]->cond, NULL);
    }

  createManager(manager, (void*(*)(void*))manager_open);
  
  // ~ join the manager and customers
  pthread_join(manager->employee_tid, NULL);
  //printf("ne manager\n");
  

  for (ssize_t i = 0; i < num_customers; i++) 
  { 
    pthread_join(customers[i].customer_tid, NULL);
  }

  // ~ cancel and join employees
  for (ssize_t i = 0; i < num_employees; i++) 
  {

    pthread_cancel(employees[i].employee_tid);
    pthread_join(employees[i].employee_tid, NULL);

  }

  // ~ finally join the self-checkout machines, !! take care of the breaking condition !!
  for (ssize_t i = 0; i < num_selfcheckouts; i++) 
  {
    pthread_mutex_lock(&selfcheckouts[i]->mutex);
    selfcheckouts[i]->state = OFF;
    pthread_cond_signal(&selfcheckouts[i]->cond);
    pthread_mutex_unlock(&selfcheckouts[i]->mutex);
    
    pthread_join(selfcheckouts[i]->selfcheckout_tid, NULL);
  }

  freeResources(customers, employees, selfcheckouts); 

  printf("!! S U P E R M A R K E T   C L O S E D !!\n");

  return 0;
}