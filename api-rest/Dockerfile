# Use a minimal JVM image
FROM eclipse-temurin:17-jre

# Set working directory
WORKDIR /app

# Copy built jar from build stage or host
ARG JAR_FILE=target/code-cracker-api-rest-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar

# Expose default port
EXPOSE 8124

# Allow port to be overridden with SERVER_PORT env variable
ENV SERVER_PORT=8124

# Run application
ENTRYPOINT ["java","-jar","/app/app.jar"]
