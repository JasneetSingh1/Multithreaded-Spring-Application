# Use an official OpenJDK runtime as a base image
FROM openjdk:17-jdk-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the compiled Spring Boot JAR to the working directory
COPY target/D387_sample_code-0.0.2-SNAPSHOT.jar .

# Expose the port that Spring Boot listens on
EXPOSE 8080

# Set the command to run your Spring Boot application
CMD ["java", "-jar", "D387_sample_code-0.0.2-SNAPSHOT.jar"]
