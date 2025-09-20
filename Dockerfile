# ----------- Stage 1: Build the application -----------
FROM maven:3.8.8-eclipse-temurin-21 AS build

WORKDIR /app

# Copy POM and source code
COPY pom.xml .
COPY src ./src

# Build the fat JAR
RUN mvn clean package -DskipTests

# ----------- Stage 2: Minimal runtime image -----------
FROM eclipse-temurin:21-jdk-alpine

WORKDIR /app

# Copy the built JAR from the build stage
COPY --from=build /app/target/Employee-Management-0.0.1-SNAPSHOT.jar app.jar

# Expose application port
EXPOSE 8089

# Run-time environment variables for DB credentials (can override at runtime)
ENV DB_URL=jdbc:mysql://host.docker.internal:3306/employee_management
ENV DB_USERNAME=root
ENV DB_PASSWORD=root

# Start the application
ENTRYPOINT ["java","-jar","app.jar"]
