# Stage 1: Build the application using Maven
FROM maven:3.9.4-eclipse-temurin-21 AS build
WORKDIR /app

# Copy pom.xml and download dependencies
COPY pom.xml ./
RUN mvn dependency:go-offline

# Copy the source code
COPY src ./src

# Build the application, skipping tests for production
RUN mvn clean package -DskipTests

# Stage 2: Run the application using a lightweight JDK image
FROM eclipse-temurin:21-jdk-jammy
WORKDIR /app

# Copy the built JAR from the build stage
COPY --from=build /app/target/*.jar app.jar

# Copy the Prod properties file
COPY src/main/resources/application-prod.properties /app/config/application-prod.properties

# Expose port 8080
EXPOSE 8080

# Add a health check to monitor the service
HEALTHCHECK --interval=30s --timeout=5s --start-period=10s --retries=3 \
  CMD ["curl", "-f", "http://localhost:8080/actuator/health"]

# Run the Spring Boot application with the Neon properties
ENTRYPOINT ["java", "-jar", "app.jar", "--spring.config.location=file:/app/config/application-prod.properties"]
