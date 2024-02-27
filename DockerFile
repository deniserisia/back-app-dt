FROM ubuntu:latest AS BUILD

RUN apt-get update
RUN apt-get install openjdk-11-jdk -y
COPY . .

RUN apt-get install maven -y
RUN mvn clean install

FROM openjdk:11-jdk-slim

EXPOSE 8084

COPY --from=build /target/back-app-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]

