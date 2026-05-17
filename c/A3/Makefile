PROJECT_ROOT = $(dir $(abspath $(lastword $(MAKEFILE_LIST))))

CC :=gcc
CFLAGS := -Werror -Wall
LDFLAGS := -lrt -lpthread -g
OBJS  := Adventurer TreasureKeeper

all: Adventurer TreasureKeeper

Adventurer: Adventurer.c
	$(CC) $(CFLAGS) Adventurer.c util.c -o Adventurer $(LDFLAGS) -DSTART=startAdventurer

TreasureKeeper: TreasureKeeper.c
	$(CC) $(CFLAGS) TreasureKeeper.c util.c -o TreasureKeeper $(LDFLAGS) -DSTART=startTreasureKeeper

run:
	./Adventurer

clean:
	rm -fr ASS4 $(OBJS)