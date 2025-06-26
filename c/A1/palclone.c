/*
 * Implement the missing functionality in this file. DO NOT CHANGE ANY OTHER FILE!
 * Add your code only inside the given TODO borders
 */

#include "palclone.h"
#include <stdlib.h>

WINDOW *game_window;
char game_map[MAP_LENGTH][MAP_WIDTH];

int number_enemies = 7;
int number_rocks = 5;

pthread_t player_tid;
pthread_t pal_tid;

enemy_info enemy_infos;

void *rvalue_player = 0;
void *rvalue_pal;

time_t time1;

position player_position = {MAP_WIDTH / 2, MAP_LENGTH - 2};
position pal_position = {-1, -1};

int lifepoints = 100;
int points = 0;

int is_pal_collected = 0;

enemy_info* getEnemyInfo(int enemy_type, int enemy_id)
{
    enemy_info * current_node = &enemy_infos;
    while(current_node != NULL)
    {
    	if (current_node->enemy_type == enemy_type && current_node->enemy_id == enemy_id)
        {
            return current_node;
        }

        current_node = current_node->next;
    }

    return NULL;
}

void *playerLogic()
{
    game_map[player_position.y_][player_position.x_] = (char)PLAYER;

    char direction = 'l';
    char prev_direction = 'l';

    int c;
    keypad(stdscr, TRUE);
    noecho();
    timeout(1);
    while (true)
    {
        c = getch();
        switch (c)
        {
            case 's':
                direction = 'd';
                break;
            case 'w':
                direction = 'u';
                break;
            case 'a':
                direction = 'l';
                break;
            case 'd':
                direction = 'r';
                break;
            default:
                break;
        }
        if (c == 'q')
        {
            lifepoints = 0;
            continue;
        }

        usleep(100000);
 

        if(getMapValue(player_position.x_, player_position.y_) == PAL){

            points = points + POINTS_PAL;
            is_pal_collected = 1;
            pthread_cancel(pal_tid);
        }

        moveAndDrawPlayer(&direction, &prev_direction, &player_position, PLAYER);
    }
}

void *enemyRock(parameters *params)
{
    if (params->type_ != ENEMY_ROCK)
        return (void *)-1;

    while (lifepoints > 0)
    {
        usleep(300000);
        drawEnemy(ENEMY_ROCK, params->pos_x_, params->pos_y_);
        drawEnemy(ENEMY_ROCK, params->pos_x_ + 1, params->pos_y_);
        drawEnemy(ENEMY_ROCK, params->pos_x_, params->pos_y_ + 1);
        drawEnemy(ENEMY_ROCK, params->pos_x_ + 1,params->pos_y_ + 1);
    }

    free(params);


    return (void *)999;
}

void *enemyBasic(parameters *params)
{
    if (params->type_ != ENEMY_BASIC)
        return (void *)-1;


    position enemy_pos = {params->pos_x_, params->pos_y_};
    drawEnemy(ENEMY_BASIC, params->pos_x_, params->pos_y_);

    free(params);


    int nr_direction = 0;
    char prev_direction = 'l';

    while (lifepoints > 0)
    {
        usleep(300000);

        if (rand() % 5 == 0)
        {
            nr_direction = rand() % 4;
        }
        char direction;
        switch (nr_direction)
        {
            case 0:
                direction = 'l';
                moveAndDrawPlayer(&direction, &prev_direction, &enemy_pos, (char) ENEMY_BASIC);
                break;
            case 1:
                direction = 'r';
                moveAndDrawPlayer(&direction, &prev_direction, &enemy_pos, (char) ENEMY_BASIC);
                break;
            case 2:
                direction = 'u';
                moveAndDrawPlayer(&direction, &prev_direction, &enemy_pos, (char) ENEMY_BASIC);
                break;
            case 3:
                direction = 'd';
                moveAndDrawPlayer(&direction, &prev_direction, &enemy_pos, (char) ENEMY_BASIC);
                break;
            default:
                break;
        }
    }
    return (void *)998;
}

void init_enemies(unsigned char type, int number_of_enemy_type)
{
    pthread_attr_t enemy_attr;
    pthread_attr_init(&enemy_attr);

    if(type == ENEMY_BASIC)
    {
    	for(int i = 0; i < number_enemies; i++)
    	{
    		parameters *enm = malloc(sizeof(parameters));
    		enm->pos_x_ = getRandPosX();
    		enm->pos_y_ = getRandPosY();
    		enm->type_ = ENEMY_BASIC;
    		enemy_info *temp = getEnemyInfo(ENEMY_BASIC, i);
    		pthread_create(&temp->tid, &enemy_attr, (void*)enemyBasic, enm);

    	}
    }else if(type == ENEMY_ROCK)
    {
    	for(int i = 0; i < number_rocks; i++)
    	{
    		parameters *mne = malloc(sizeof(parameters));
    		mne->pos_x_ = getRandPosX();
    		mne->pos_y_ = getRandPosY();
    		mne->type_ = ENEMY_ROCK;
    		enemy_info *temp = getEnemyInfo(ENEMY_ROCK, i);
    		pthread_create(&temp->tid, &enemy_attr, (void*)enemyRock, mne);
    	}
    }


    pthread_attr_destroy(&enemy_attr);
}

void *placePal(parameters *params)
{
    if (params->type_ != PAL)
        return (void *)-1;


    pthread_setcancelstate(PTHREAD_CANCEL_ENABLE, NULL);
    pthread_setcanceltype(PTHREAD_CANCEL_ASYNCHRONOUS, NULL);

    unsigned char position_x;
    unsigned char position_y;

    position_x = params->pos_x_;
    position_y = params->pos_y_;

    
    free(params);

    while (1)
    {
        drawPal(position_x, position_y);

        // Bonus helper
        pal_position.x_ = position_x;
        pal_position.y_ = position_y;
    }
}

int start_game(int argc, char* argv[])
{
    init_screen();
    srand((unsigned int)time(&time1));


    enemy_info* current_node = &enemy_infos;
    memset(current_node, 0, sizeof(enemy_info));

    int temp = 0;

    for(int i = 0; i < number_enemies + number_rocks; i++)
    {
        if(i > 0)
        {
            enemy_info* new_enemy = (enemy_info*)malloc(sizeof(enemy_info));
    	    if(new_enemy == NULL)
    	    {
                fprintf(stderr, "Failed to allocate memory\n");
                exit(EXIT_FAILURE);
            }
            if(i < number_enemies)
            {
                new_enemy->next = NULL;
                new_enemy->return_value = NULL;
      	        new_enemy->enemy_type = ENEMY_BASIC;
                new_enemy->enemy_id = i;
            }
            else
            {
      	        temp = i - number_enemies;
                new_enemy->next = NULL;
                new_enemy->return_value = NULL;
      	        new_enemy->enemy_type = ENEMY_ROCK;
                new_enemy->enemy_id = temp;
            }
            current_node->next = new_enemy;
                
            current_node = new_enemy;
            
        }
        if(i == 0){
            current_node->enemy_type = ENEMY_BASIC;
            current_node->enemy_id = i;
        }

    }

    init_map();
     
    pthread_create(&player_tid, NULL, playerLogic, NULL);

    init_enemies(ENEMY_BASIC, number_enemies);
    init_enemies(ENEMY_ROCK, number_rocks);

    refreshMap();

    parameters *paraPal = malloc(sizeof(parameters));
    paraPal->pos_x_ = getRandPosX();
    paraPal->pos_y_ = getRandPosY();
    paraPal->type_ = PAL;
    pthread_create(&pal_tid, NULL, (void*) placePal, paraPal);
    
   
    while (lifepoints > 0)
    {
    
        usleep(10000);
        
        if (is_pal_collected == 1)
        {            
            pthread_join(pal_tid, &rvalue_pal);

            parameters *paraPal = malloc(sizeof(parameters));
            paraPal->pos_x_ = getRandPosX();
            paraPal->pos_y_ = getRandPosY();
            paraPal->type_ = PAL;
            pthread_create(&pal_tid, NULL, (void*) placePal, paraPal);


            is_pal_collected = FALSE;

        }

        refreshMap();
    }

    pthread_cancel(pal_tid);
    pthread_join(pal_tid, &rvalue_pal);

    pthread_cancel(player_tid);
    pthread_join(player_tid, &rvalue_player);


    current_node = &enemy_infos;
    while (current_node != NULL) {
        pthread_join(current_node->tid, &current_node->return_value);
        current_node = current_node->next;
    }

    return end_game((void **)rvalue_player, (void *)rvalue_pal);
}