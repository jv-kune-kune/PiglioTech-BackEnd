# Use Maven to build the application (compatible with Java 21)
FROM maven:3.9.4-eclipse-temurin-21 AS build
WORKDIR /app

# Copy project files to the container
COPY pom.xml ./
COPY src ./src

# Build the application and skip tests for production
RUN mvn clean package -DskipTests

# Use a lightweight JDK image to run the application
FROM eclipse-temurin:21-jdk-jammy
WORKDIR /app

# Copy the built JAR file from the build stage
COPY --from=build /app/target/*.jar app.jar

# Copy the Neon properties file to the container
COPY src/main/resources/application-neon.properties /app/config/application-neon.properties

# Expose the application port
EXPOSE 8080

# Add a health check to monitor the service
HEALTHCHECK --interval=30s --timeout=5s --start-period=10s --retries=3 CMD curl -f http://localhost:8080/actuator/health || exit 1

# Run the Spring Boot application with the Neon properties
ENTRYPOINT ["java", "-jar", "app.jar", "--spring.config.location=file:/app/config/application-neon.properties"]
