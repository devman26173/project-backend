# base image
FROM maven:3.9.9-eclipse-temurin-17 AS builder
WORKDIR /app

# dependency
COPY pom.xml ./
RUN --mount=type=cache,target=/root/.m2 mvn -q -DskipTests dependency:go-offline

# Build
COPY src src
RUN --mount=type=cache,target=/root/.m2 mvn -q -DskipTests clean package

# runtime image
FROM eclipse-temurin:17-jre
WORKDIR /app

# Non-root user(For security)
RUN useradd -create-home -shell /bin/bash appuser

# Copy jar to runtime
COPY --from=builder /app/target/*.jar /app/app.jar

# use switch
RUN chown -R spring:spring /app
USER appuser

# port
EXPOSE 8080
