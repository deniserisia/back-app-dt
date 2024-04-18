FROM ubuntu:latest AS BUILD

RUN apt-get update && \
    apt-get install -y openjdk-18-jdk maven

WORKDIR /app

COPY . .

RUN mvn clean compile install -DskipTests=true

FROM openjdk:18-jdk-slim

WORKDIR /app

COPY --from=BUILD /app/target/back-app-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8084

ENTRYPOINT ["java", "-jar", "app.jar"]
