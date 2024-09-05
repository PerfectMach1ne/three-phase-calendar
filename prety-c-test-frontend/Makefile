.PHONY = all default clean move
CC = gcc
LDLIBS = -lcurl -lncursesw 

C17FLAG = -std='c17'

SRCS := $(wildcard *.c)
BINS := $(SRCS:%.c=%)
NCURSES_CFLAGS := $(shell ncursesw6-config --cflags)

all: default ${BINS} move clean 

default:
	@echo "Pretty C frontend!"
	@echo "We don't need no JS, CSS, HTML, or even PostMan to test the API."
	@echo "Welcome to Doing Things Void Style."

%: %.o
	@echo "Compiling binaries..."
	${CC} $< ${LDLIBS} -o $@   

%.o: %.c
	@echo "Creating objects..."
	${CC} -c ${C17FLAG} $(NCURSES_CFLAGS) -DNCURSES_WIDECHAR $< 

move:
	@echo "Moving binaries to bin folder..."
	mv -t ./bin ${BINS}

clean:
	@echo "Cleaning up..."
	rm -rv *.o ${BINS}
