FROM openjdk:21
WORKDIR /
RUN ./mvnw clean package -Pproduction
ADD ./target/my-app-1.0-SNAPSHOT.jar app.jar
CMD java -jar -Dspring.profiles.active=prod app.jar