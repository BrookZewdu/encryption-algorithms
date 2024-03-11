FROM maven:3.6.0-jdk-11-slim AS build
COPY src /home/app/src
COPY pom.xml ./
RUN mvn -f ./pom.xml clean package

# Package stage
FROM openjdk:11-jre-slim
COPY --from=build ./target/*.jar /app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]