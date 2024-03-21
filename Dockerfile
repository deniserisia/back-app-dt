FROM ubuntu:latest AS BUILD

RUN apt-get update && \
    apt-get install -y openjdk-11-jdk maven

WORKDIR /app

COPY . .

FROM openjdk:11-jdk-slim

EXPOSE 8087

COPY --from=BUILD /app/target/back-app-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT ["java", "-Dspring.config.location=file:/app/application-dev.properties", "-jar", "app.jar"]
