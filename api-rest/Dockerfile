# Build the Spring Boot application
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /workspace

# copy pom files first for layer caching
COPY pom.xml ./
COPY core/pom.xml core/pom.xml
COPY api-rest/pom.xml api-rest/pom.xml

# copy sources
COPY core/src core/src
COPY api-rest/src api-rest/src

RUN mvn -pl api-rest -am package -DskipTests

# Runtime image
FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /workspace/api-rest/target/code-cracker-api-rest-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8124
ENV SERVER_PORT=8124
ENTRYPOINT ["java","-jar","/app/app.jar"]
