# 멀티스테이지 빌드 - 빌드 단계
FROM eclipse-temurin:25-jdk-alpine AS builder

WORKDIR /app

# Gradle 관련 파일 복사 (캐시 활용)
COPY gradlew .
COPY gradle gradle
COPY build.gradle.kts .
COPY settings.gradle.kts .
COPY gradle.properties .

# 공통 라이브러리 및 서비스 build.gradle.kts 복사
COPY common-lib/build.gradle.kts common-lib/
COPY user-service/build.gradle.kts user-service/

# 의존성 다운로드 (캐시 레이어)
RUN chmod +x gradlew && ./gradlew dependencies --no-daemon || true

# 소스 코드 복사
COPY . .

# user-service 빌드
RUN ./gradlew :user-service:bootJar --no-daemon

# 실행 단계
FROM eclipse-temurin:25-jre-alpine

WORKDIR /app

# 빌드된 JAR 복사
COPY --from=builder /app/user-service/build/libs/*.jar app.jar

# 보안: non-root 사용자로 실행
RUN addgroup -g 1000 spring && adduser -u 1000 -G spring -s /bin/sh -D spring
USER spring:spring

# 헬스체크
HEALTHCHECK --interval=30s --timeout=3s --start-period=60s --retries=3 \
    CMD wget --quiet --tries=1 --spider http://localhost:8080/actuator/health || exit 1

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
