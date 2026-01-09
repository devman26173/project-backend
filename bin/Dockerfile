# 1. Java 17 베이스
FROM eclipse-temurin:17-jdk-jammy

# 2. JAR 파일 복사
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar

# 3. 컨테이너 실행
ENTRYPOINT ["java","-jar","/app.jar","--spring.profiles.active=prod"]
