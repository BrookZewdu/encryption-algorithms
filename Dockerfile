FROM maven:3.6.0-jdk-11-slim AS build
COPY src ./
COPY pom.xml ./
RUN ./mvnw clean package -Pproduction

# Package stage
FROM openjdk:11-jre-slim
COPY --from=build ./target/*.jar /app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]