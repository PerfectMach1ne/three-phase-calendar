# Dockerfile todolist:
# Move to minimal image
# LATER_0: handle .properties secrets better (sops integration?)
# LATER_1: Docker Compose

# alpine:30.20.3 = https://github.com/alpinelinux/docker-alpine/blob/7d63673353bd39d92ba42f6effcc199aeebd45ee/x86_64/Dockerfile
# eclipse-temurin:21-jdk-alpine = https://github.com/adoptium/containers/blob/d7a5038edcd8ab08b0babaeae09d0c097453a023/21/jdk/alpine/Dockerfile
# gradle:8.12-jdk21-alpine = https://github.com/keeganwitt/docker-gradle/blob/master/jdk21-alpine/Dockerfile 
FROM eclipse-temurin:21-jdk-alpine

RUN sed -i 's/^root:/starr4ever:/' /etc/passwd && \
    sed -i 's/^root:/starr4ever:/' /etc/shadow
WORKDIR /src

COPY ./gradle ./gradle
ADD ./gradlew ./settings.gradle.kts .
COPY ./app/src ./app/src
COPY ./tpc_docker_test.properties ./app/src/main/resources/tpc_testing.properties
COPY ./app/build.gradle.kts ./app
COPY --chmod=0755 ./toys/docker_ip.sh .

ENTRYPOINT ["./docker_ip.sh"]
# CMD ["./gradlew", "run", "--debug"]
# FROM eclipse-temurin:21-jdk-alpine
# copy update_local_ip.sh and run it

USER starr4ever
EXPOSE 8057
