#!/bin/bash

gcc -c -std='c17' main.c -lcurl
gcc main.o -lm -lcurl -o main
