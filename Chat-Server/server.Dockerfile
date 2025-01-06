FROM maven:3.9.4-eclipse-temurin-21 AS build
WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jre

# Set the working directory
WORKDIR /app

# Expose the port that your Spring Boot app runs on
EXPOSE 8880

# Copy the JAR file from the build stage
COPY --from=build /app/target/*.jar app.jar

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
