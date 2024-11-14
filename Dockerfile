# Use the official OpenJDK 21 image as the base
FROM eclipse-temurin:21-jdk

# Copy the built JAR file directly into the container
COPY target/*.jar app.jar

# Expose the default Spring Boot port
EXPOSE 8080

# Run the Spring Boot application
ENTRYPOINT ["java", "-jar", "/app.jar"]
