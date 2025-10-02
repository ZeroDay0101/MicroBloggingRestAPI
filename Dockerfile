# ====== BUILD STAGE ======
FROM maven:3.9.3-eclipse-temurin-17 AS build

WORKDIR /app

# Copy only the Maven files first to use caching
COPY pom.xml .

# Download dependencies (cached unless pom.xml changes)
RUN mvn dependency:resolve

# Copy the source code
COPY src ./src

# Build the JAR (skip tests to speed up build)
RUN mvn package -DskipTests

# ====== RUN STAGE ======
FROM openjdk:17-jdk-slim


WORKDIR /app

# Copy the JAR from the build stage
COPY --from=build /app/target/*.jar app.jar

# Expose the port
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]