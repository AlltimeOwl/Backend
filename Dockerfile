FROM openjdk:17-alpine

ARG JAR_FILE=build/libs/*.jar

COPY ${JAR_FILE} /app.jar
COPY AuthKey_32NZF755K3.p8 /app/resources/apple/
COPY key-store.p12 /app/resources/ssl/

EXPOSE 8080

ENTRYPOINT ["java","-jar", "-Dspring.profiles.active=prod", "/app.jar"]
