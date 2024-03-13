FROM openjdk:17-alpine

ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} /app.jar

# dockerize 도구 설치
RUN apk add --no-cache dockerize

EXPOSE 8080

# dockerize를 사용하여 MariaDB와 Redis가 준비될 때까지 기다리기.
ENTRYPOINT ["dockerize", "-wait", "tcp://db:3306", "-wait", "tcp://redis:6379", "java", "-jar", "-Dspring.profiles.active=prod", "/app.jar"]
