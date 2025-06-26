/*
 * DO NOT CHANGE THIS FILE!
 */

#include <ncurses.h>
#include <stdio.h>
#include <pthread.h>
#include <unistd.h>
#include <assert.h>
#include "string.h"
#include <time.h>

// Version to check against upstream
#define VERSION 2

// Map geometry
#define MAP_WIDTH 50
#define MAP_LENGTH 50

// Field Types
#define EMPTY 1
#define PLAYER 2
#define PAL 3
#define ENEMY_BASIC 6
#define ENEMY_ROCK 7

// ncurses constants
#define SPACE 51
#define PLAYER_COLOR 52
#define ENEMY_COLOR 53
#define PAL_COLOR 54
#define ROCK_COLOR 55

// Game settings
#define POINTS_PAL 1000

#define INVALID_TID ((pthread_t)(-1))

#ifndef SNP_TUTOR_TEST_1_H
#define SNP_TUTOR_TEST_1_H

typedef struct
{
    unsigned char x_;
    unsigned char y_;
} position;

typedef struct
{
    unsigned char pos_x_;
    unsigned char pos_y_;
    unsigned char type_;
} parameters;

typedef struct enemy_info_t
{
    struct enemy_info_t* next;
    void* return_value;
    pthread_t tid;
    int enemy_type;
    int enemy_id;
} enemy_info;

extern enemy_info enemy_infos;

extern WINDOW *game_window;
extern char game_map[MAP_LENGTH][MAP_WIDTH];

extern int lifepoints;
extern int points;
extern position player_position;
extern pthread_t player_tid;
extern pthread_t pal_tid;
extern time_t time1;
extern int is_pal_collected;

extern void *rvalue_player;
extern void *rvalue_pal;

extern int number_enemies;
extern int number_rocks;

void init_map();
void init_screen();

void drawPal(int x_pos, int y_pos);
void drawEnemy(char type, int x_pos, int y_pos);
char getMapValue(int x_pos, int y_pos);

void refreshMap();

int start_game(int argc, char** argv);
int end_game(void *rvalue, void *rvalue_pal);

void freeNodes(enemy_info* node);
enemy_info* getEnemyInfo(int enemy_type, int enemy_id);

void *playerLogic();

void *enemyRock(parameters *params);
void *enemyBasic(parameters *params);

void *placePal(parameters *params);

void init_enemies(unsigned char type, int number_of_enemy_type);

void moveAndDrawPlayer(char *direction, char *prev_direction, position *pos, char type);

char getRandPosX();

char getRandPosY();

#endif // SNP_TUTOR_TEST_1_H
