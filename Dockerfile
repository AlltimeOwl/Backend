FROM openjdk:17-alpine

ARG JAR_FILE=build/libs/*.jar

COPY ${JAR_FILE} /app.jar
#COPY src/main/resources/keystore.p12 /keystore.p12
WORKDIR /app

COPY src/main/resources/apple/AuthKey_32NZF755K3.p8 /app/src/main/resources/apple/AuthKey_32NZF755K3.p8

EXPOSE 8080

ENTRYPOINT ["java","-jar", "-Dspring.profiles.active=prod", "/app.jar"]
