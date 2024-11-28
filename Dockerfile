# Use a stable OpenJDK base image
FROM azul/zulu-openjdk-alpine:21-jre

# Set the working directory
WORKDIR /app

# Copy the application JAR to the image
ADD target/kafka-0.0.1-SNAPSHOT.jar /app/kafka-0.0.1-SNAPSHOT.jar

# Add the OpenTelemetry agent from its existing location in the project directory
ADD opentelemetry-javaagent.jar /app/opentelemetry-javaagent.jar

# Expose the application port
EXPOSE 8080

# Set the default entry point to run the application
ENTRYPOINT ["java", "-javaagent:/app/opentelemetry-javaagent.jar", "-jar", "/app/kafka-0.0.1-SNAPSHOT.jar"]
