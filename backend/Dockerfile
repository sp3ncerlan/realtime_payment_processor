# Backend multi-stage Dockerfile - builds the Spring Boot jar and creates a minimal runtime image

# Build stage (uses system maven in image)
FROM maven:3.9.3-eclipse-temurin-17 as builder
WORKDIR /workspace

# Copy pom and download dependencies first (speeds up repeated builds)
COPY pom.xml ./
RUN mvn -B -DskipTests dependency:go-offline

# Copy source and build the application
COPY . ./
RUN mvn -B -DskipTests package

# Runtime image
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app
# Copy the built jar from the builder stage. Use a wildcard to match the actual artifact name.
COPY --from=builder /workspace/target/*.jar ./app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar"]
