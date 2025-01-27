# tpc API Server

Universal Java HTTP server & API backend for every tpc frontend.
Utilizes postgres as RDBMS.

## Rebuilding the Docker image

Simply run:

#### `docker compose build`

## How to run

#### `docker compose up`

Or, alternatively, if you want to avoid containerization:

#### `./gradlew run`

I cannot guarantee for this to work, though - you might need to manually install Gradle in the project directory yourself for this.
Another caveat with the "Dockerless" setup is that you're going to have to manually install and configure the right version of a postgres instance on your machine.
