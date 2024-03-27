FROM openjdk:17-jdk-alpine

ARG JAR_FILE=build/libs/*.jar

COPY ${JAR_FILE} /app.jar

# COPY src/main/resources/apple/AuthKey_32NZF755K3.p8 /app/src/main/resources/apple/AuthKey_32NZF755K3.p8

# WORKDIR /app

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=prod", "-Djava.net.preferIPv4Stack=true", "-Djava.net.preferIPv4Addresses=true", "/app.jar"]
