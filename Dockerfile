# 빌드 스테이지
FROM openjdk:17-alpine AS build

WORKDIR /workspace

# Gradle Wrapper, 설정 파일 및 소스 코드 복사
COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .
COPY src src

# gradlew에 실행 권한 부여하기
RUN chmod +x gradlew

# 빌드 수행
RUN ./gradlew bootJar -x test

# 런타임 스테이지
FROM openjdk:17-alpine

# 빌드 스테이지에서 생성된 JAR 파일을 복사
COPY --from=build /workspace/build/libs/*.jar app.jar

# 애플리케이션 실행
ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=prod", "/app.jar"]