FROM ubuntu:latest AS BUILD

RUN apt-get update

RUN apt-get install openjdk-18-jdk -y

RUN apt-get install maven -y

RUN mvn clean

RUN mvn compile

RUN mvn install

FROM openjdk:18-jdk-slim

COPY . .

EXPOSE 8084

COPY --from=BUILD /app/target/back-app-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]