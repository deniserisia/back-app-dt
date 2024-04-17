FROM ubuntu:latest AS BUILD

RUN apt-get update

RUN apt-get install openjdk-18-jdk -y

COPY . .

RUN apt-get install maven -y

RUN mvn clean install

FROM openjdk:18-jdk-slim

ENTRYPOINT ["java","-jar","/app.jar"]
EXPOSE 5432
EXPOSE 8084

ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar


ENTRYPOINT ["java", "-jar", "app.jar"]
