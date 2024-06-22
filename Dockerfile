# Use an official OpenJDK runtime as a parent image
FROM openjdk:17-jdk-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the project files into the container
COPY . .

# Package the application outside with the long command in buildAndRunDocker.sh
#RUN ./mvnw package -DskipTests

# Copy the jar file to the docker image
COPY target/macaronsBackend-1.0.jar /app/macaronsBackend.jar

# Expose the port that the application will run on
EXPOSE 8080

# Run the jar file
ENTRYPOINT ["java", "-jar", "/app/macaronsBackend.jar"]