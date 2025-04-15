# 1. JDK 베이스 이미지
FROM openjdk:17-jdk-alpine

# 2. JAR 복사
COPY build/libs/from-unknown-0.0.1-SNAPSHOT.jar app.jar

# 3. 실행
ENTRYPOINT ["java", "-jar", "/app.jar"]
