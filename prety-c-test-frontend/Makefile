.PHONY = all default clean

CC = gcc
LINKERFLAG = -lm
CURLLIBS = -lcurl
C17FLAG = -std='c17'

SRCS := $(wildcard *c)
BINS := $(SRCS:%.c=%)

all: default $(BINS) move

default:
	@echo "Pretty C frontend!"
	@echo "We don't need no JS, CSS, HTML, or even PostMan to test the API."
	@echo "Welcome to Doing Things Void Style."

%: %.o
	@echo "Compiling binaries..."
	$(CC) $< -o $(LINKERFLAG) $(CURLLIBS) $@ 

%.o: %.c
	@echo "Creating objects..."
	$(CC) -c $(C17FLAG) $< $(CURLLIBS)

move:
	@echo "Moving binaries to bin folder..."
	mv -t ./bin $(BINS)

clean:
	@echo "Cleaning up..."
	rm -rvi *.o $(BINS)