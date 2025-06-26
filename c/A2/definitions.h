/*
 * !! DO NOT MODIFY THIS FILE !!
 */

#ifndef A2_DEFINITIONS_H
#define A2_DEFINITIONS_H

#include <stdlib.h>
#include <stdint.h>
#include <pthread.h>
#include <string.h>
#include <assert.h>

#include "resources.h"
#include "vector.h"

#define ERROR -1

#define LOWER_LIMIT 1
#define UPPER_LIMIT_CUSTOMERS 2000
#define UPPER_LIMIT_EMPLOYEES 1000
#define UPPER_LIMIT_SELFCHECKOUTS 1000

#define NUMBER_COFFEE_COUNTERS 2

vector free_selfcheckouts;
bool supermarket_opened = false;
bool checkout_active = false;
Customer* coffee_counters[NUMBER_COFFEE_COUNTERS] = {};

// ------------------------------------------------------------------------------------
void checkAllowedRange(ssize_t value, ssize_t upper_limit, char* name)
{
  if (value < LOWER_LIMIT || value > upper_limit)
  {
    fprintf(stderr, "%s allowed range: [%d,%zd].\n", name, LOWER_LIMIT, upper_limit);
    exit(ERROR);
  }
}

// ------------------------------------------------------------------------------------
void handleArguments(int argc, char* argv[], ssize_t* num_customers, ssize_t* num_employees, ssize_t* num_selfcheckouts)
{
  if (argc != 4) 
  {
    fprintf(stderr, "Usage: %s <num_customers> <num_employees> <num_selfcheckouts>\n", argv[0]);
    exit(ERROR);
  }

  // check the validity of parameters
  *num_customers = atoi(argv[1]);
  *num_employees = atoi(argv[2]);
  *num_selfcheckouts = atoi(argv[3]);

  checkAllowedRange(*num_customers, UPPER_LIMIT_CUSTOMERS, "num_customers");
  checkAllowedRange(*num_employees, UPPER_LIMIT_EMPLOYEES, "num_employees");
  checkAllowedRange(*num_selfcheckouts, UPPER_LIMIT_SELFCHECKOUTS, "num_selfcheckouts");
}

// ------------------------------------------------------------------------------------
void createCustomers(Customer* customers, ssize_t num_customers, void* start_routine)
{
  for (ssize_t i = 0; i < num_customers; i++) 
  {
    customers[i].id = i;
    assert(!pthread_create(&customers[i].customer_tid, NULL, start_routine, (void*)&customers[i]));
  }
}

// ------------------------------------------------------------------------------------
void createEmployees(Employee* employees, ssize_t num_employees, void* start_routine)
{
  for (ssize_t i = 0; i < num_employees; i++) 
  {
    employees[i].id = i;
    assert(!pthread_create(&employees[i].employee_tid, NULL, start_routine, (void*)&employees[i]));
  }
}

// ------------------------------------------------------------------------------------
void createManager(Employee* manager, void* start_routine)
{
  manager->id = -1;
  assert(!pthread_create(&manager->employee_tid, NULL, start_routine, NULL));
}

// ------------------------------------------------------------------------------------
void createSelfCheckouts(SelfCheckout** checkouts, ssize_t num_checkouts, void* start_routine)
{
  vector_init(&free_selfcheckouts);

  for (ssize_t i = 0; i < num_checkouts; i++) 
  {
    SelfCheckout* self_checkout = malloc(sizeof(SelfCheckout));

    if(!self_checkout)
    {
      free(self_checkout);
      fprintf(stderr, "Could not allocate memory!\n");
      exit(-1);
    }

    self_checkout->id = i;
    self_checkout->state = READY;
    self_checkout->customer = NULL;
    vector_push_back(&free_selfcheckouts, self_checkout);
    
    // create self-checkout threads
    checkouts[i] = self_checkout;
    assert(!pthread_create(&self_checkout->selfcheckout_tid, NULL, start_routine, (void*)self_checkout));   
  }
}

// ------------------------------------------------------------------------------------
void freeResources(Customer* customers, Employee* employees, SelfCheckout** checkouts)
{
  vector_iterator it = vector_begin(&free_selfcheckouts);
  while (it != vector_end(&free_selfcheckouts)) 
  {
    free(*it);
    vector_erase(&free_selfcheckouts, it);
  }
  
  vector_destroy(&free_selfcheckouts);

  free(customers);
  free(employees);
  free(checkouts);
}

// ------------------------------------------------------------------------
void shop() 
{
  ssize_t microsec_to_sleep = 1000 + rand() % (20 * 1000);
  nanosleep((const struct timespec[]){{0, 1000L * microsec_to_sleep}}, NULL);
}

// ------------------------------------------------------------------------
void scanAndPay() 
{
  ssize_t microsec_to_sleep = 1000 + rand() % (20 * 1000);
  nanosleep((const struct timespec[]){{0, 1000L * microsec_to_sleep}}, NULL);
}

// ------------------------------------------------------------------------
void maintenance() 
{
  ssize_t microsec_to_sleep = 1000 + rand() % (20 * 1000);
  nanosleep((const struct timespec[]){{0, 1000L * microsec_to_sleep}}, NULL);
}

// ------------------------------------------------------------------------
void coffeeTime() 
{
  ssize_t microsec_to_sleep = 1000 + rand() % (20 * 1000);
  nanosleep((const struct timespec[]){{0, 1000L * microsec_to_sleep}}, NULL);
}

// ------------------------------------------------------------------------
void buyCoffee() 
{
  ssize_t microsec_to_sleep = 1000 + rand() % (20 * 1000);
  nanosleep((const struct timespec[]){{0, 1000L * microsec_to_sleep}}, NULL);
}

// ------------------------------------------------------------------------
void printReceiptAndFinish() 
{
  ssize_t microsec_to_sleep = 1000 + rand() % (20 * 1000);
  nanosleep((const struct timespec[]){{0, 1000L * microsec_to_sleep}}, NULL);
}

// ------------------------------------------------------------------------
ssize_t outOfService() 
{
  return rand() % 2;
}

// ------------------------------------------------------------------------
ssize_t coffeeSmellsPhantastic() 
{
  return rand() % 2;
}

#endif //A2_DEFINITIONS_H