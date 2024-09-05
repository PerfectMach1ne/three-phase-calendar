#!/bin/bash

gcc main.c -std='c17' $(ncursesw6-config --cflags) -c
gcc main.o -lm -lcurl -lncursesw -o main
