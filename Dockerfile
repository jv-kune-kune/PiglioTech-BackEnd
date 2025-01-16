# Use an OpenJDK 21 base image
FROM eclipse-temurin:21-jdk-alpine

# Set the working directory
WORKDIR /app

# Copy the built JAR file into the container
COPY target/PiglioTech-0.0.1-SNAPSHOT.jar app.jar

# Expose the application port (default for Spring Boot is 8080)
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
