FROM gradle:8.12-jdk21

RUN apk update

WORKDIR /src
COPY ./app/src/* ./app/build.gradle.kts .
# copy update_local_ip.sh and run it

EXPOSE 8057