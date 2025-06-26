
#ifndef A2_RESOURCES_H
#define A2_RESOURCES_H

#include "pthread.h"
#include "stdint.h"
#include "stdlib.h"
#include "vector.h"
#include "semaphore.h"

// ------------------------------------------------------------------------
// STUDENT TODO:
// * You are allowed to modify this file.
// * Please note that you are only permitted to include synchronization
//   primitives as new variables!

typedef enum{
  READY,            // waiting for the customer (state upon opening)
  START,            // transition state suggesting process start
  SCAN_AND_PAY,     // customer can scan and pay
  PAYING_COMPLETED, // transition state suggesting paying completion
  OUT_OF_SERVICE,   // needs maintenance
  OFF               // supermarket closed, self-checkouts switched off
} State;

typedef struct {
  pthread_t customer_tid;
  ssize_t id;
} Customer;

typedef struct{
  pthread_t selfcheckout_tid;
  ssize_t id;
  State state;
  Customer* customer;
  pthread_mutex_t mutex; 
  pthread_cond_t cond;
  pthread_cond_t close;
} SelfCheckout;

typedef struct {
  pthread_t employee_tid;
  ssize_t id;
} Employee;

#endif //A2_RESOURCES_H
