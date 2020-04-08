FROM openjdk:8-jdk-alpine

WORKDIR /app

COPY ./build/libs/*.jar /app/onboard.jar

ENTRYPOINT exec java -jar onboard.jar
