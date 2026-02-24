# Start from Maven
FROM maven:3.9.9-eclipse-temurin-17 AS builder
WORKDIR /app

# Dependency
COPY pom.xml ./
RUN mvn -q -DskipTests dependency:go-offline

# src
COPY src ./src
RUN mvn -q -DskipTests clean package

# runtime
FROM eclipse-temurin:17-jre
WORKDIR /app

# Non-root user
RUN useradd -m -s /bin/bash appuser

# copy jar
COPY --from=builder /app/target/app.jar /app/app.jar

# chown
RUN chown -R appuser:appuser /app
USER appuser

EXPOSE 8080

ENV TZ=Asia/Seoul \
    SPRING_PROFILES_ACTIVE=prod \
    SERVER_PORT=8080

ENTRYPOINT ["java","-jar","/app/app.jar"]