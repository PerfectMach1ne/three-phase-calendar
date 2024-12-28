# Dockerfile todolist:
# Import the right image
# Build
# Move to minimal image
# Expose port 8057
# LATER_0: handle .properties secrets better (sops integration?)
# LATER_1: Docker Compose
FROM gradle:8.12-jdk21

RUN apk update

WORKDIR /src
COPY ./app/src/* ./app/build.gradle.kts .
# copy update_local_ip.sh and run it

EXPOSE 8057
