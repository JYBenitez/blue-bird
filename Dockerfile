# FROM docker/whalesay:latest
# LABEL Name=bluebird Version=0.0.1
# RUN apt-get -y update && apt-get install -y fortunes
# CMD ["sh", "-c", "/usr/games/fortune -a | cowsay"]
# Dockerfile
FROM openjdk:17-jdk-slim
ARG JAR_FILE=target/microblog.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
